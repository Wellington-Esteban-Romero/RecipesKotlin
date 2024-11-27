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
    private lateinit var recipeItems:List<RecipeItemResponse>
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

        getRecipesByCountry(Utils.getTag(id.toInt()))

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

    private fun getRecipesByCountry (country: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = recipeService.findRecipeByCountry(country)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.i("Recipes Names", responseBody.toString())

                    runOnUiThread {
                        recipeItems = responseBody.recipes
                        recipeAdapter.updateRecipes(recipeItems)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println(item.icon)

        when (item.itemId) {
            R.id.create_new_recipe -> {
                // Crear tarea
                val intent = Intent(this, CreateRecipe::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchByName (name: String) {
        //binding.progressBar.isVisible = true

        val filteredList = recipeItems.filter { it.name.contains(name, true) }
        recipeAdapter.updateRecipes(filteredList)

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