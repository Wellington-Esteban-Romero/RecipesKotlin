package com.taste.recipes.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.taste.recipes.R
import com.taste.recipes.adapters.RecipeAdapter
import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.databinding.ActivityRecipeBinding
import com.taste.recipes.services.RecipeService
import com.taste.recipes.utils.RetrofitProvider
import com.taste.recipes.utils.SessionManager
import com.taste.recipes.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private lateinit var recipeItem:List<RecipeItemResponse>
    private lateinit var recipeService: RecipeService
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var nameCountry:String

    companion object {
        const val EXTRA_RECIPE_TAG_ID = "RECIPE_TAG_ID"
        lateinit var session: SessionManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        getSupportActionBarRecipes()
    }

    private fun init () {

        recipeService = RetrofitProvider.getRetrofit()
        session = SessionManager(applicationContext)

        val id = intent.getStringExtra(EXTRA_RECIPE_TAG_ID).orEmpty()
        println(id +" - " + Utils.getTag(id.toInt()))

        nameCountry = Utils.getTag(id.toInt())

        getRecipe(Utils.getTag(id.toInt()))

        recipeAdapter = RecipeAdapter() { recipeItem ->
            onItemSelect(recipeItem)
        }

        binding.rvRecipe.apply {
            layoutManager = LinearLayoutManager(this@ListRecipe)
            adapter = recipeAdapter
            hasFixedSize()
        }

    }

    override fun onResume() {
        super.onResume()
        recipeAdapter.notifyDataSetChanged()
    }

    private fun getRecipe (name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = recipeService.findRecipeByCountry(name)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.i("Recipes Names", responseBody.toString())

                    runOnUiThread {
                        recipeAdapter.loadRecipes(responseBody.recipes)
                    }
                }
            }
        }
    }

    private fun onItemSelect(recipeItemResponse:RecipeItemResponse) {
        val intent = Intent(this, DetailsRecipe::class.java)
        intent.putExtra(DetailsRecipe.EXTRA_RECIPE_ID, recipeItemResponse.id)

        var id = recipeItemResponse.id

        if (!session.isFavorite(id))
            session.saveHoroscope(id, SessionManager.DES_ACTIVE)

        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_recipe_menu, menu)
        val searchItem: MenuItem = menu?.findItem(R.id.actionSearchRecipes)!!

        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false;
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                searchByName(newText.orEmpty())
                return false
            }
        })
        return true
    }

    private fun searchByName (name: String) {
        //binding.progressBar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            if (name != null && name != "") {
                val response = recipeService.findRecipeByName(name)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.i("Superheroes", responseBody.toString())
                        runOnUiThread {
                            recipeAdapter.updateRecipes(responseBody.recipes)
                            //binding.progressBar.isVisible = false
                        }
                    }
                }
            }
        }

        /*if (recipeTags.isEmpty()) {
        list_horoscope.visibility = View.GONE
        msg_empty.visibility = View.VISIBLE
        } else {
            list_horoscope.visibility = View.VISIBLE
            msg_empty.visibility = View.GONE
            horoscopeAdapter.filterHoroscope(horoscopeList)
        }*/
    }

    private fun getSupportActionBarRecipes () {
        var supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Recipes $nameCountry"
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}