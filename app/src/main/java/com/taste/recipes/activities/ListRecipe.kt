package com.taste.recipes.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.taste.recipes.R
import com.taste.recipes.adapters.RecipeAdapter
import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.data.entities.Recipe
import com.taste.recipes.data.providers.RecipeDAO
import com.taste.recipes.databinding.ActivityRecipeBinding
import com.taste.recipes.data.providers.RetrofitProvider
import com.taste.recipes.services.RecipeService
import com.taste.recipes.utils.SessionManager
import com.taste.recipes.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private lateinit var recipeItems:List<Recipe>
    private lateinit var recipeService: RecipeService
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeDAO: RecipeDAO
    private var recipeList: MutableList<Recipe> = mutableListOf()
    private lateinit var msg_empty: TextView
    private lateinit var country:String

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
    }

    private fun init () {

        recipeService = RetrofitProvider.getRetrofit()

        session = SessionManager(applicationContext)

        recipeDAO = RecipeDAO(this)

        msg_empty = findViewById(R.id.msg_empty_recipe)

        val id = intent.getStringExtra(EXTRA_RECIPE_TAG_ID).orEmpty()

        country = Utils.getTag(id.toInt())

        recipeAdapter = RecipeAdapter() { recipeItem ->
            onItemSelect(recipeItem)
        }

        binding.rvRecipes.apply {
            layoutManager = LinearLayoutManager(this@ListRecipe)
            adapter = recipeAdapter
            hasFixedSize()
        }


        getSupportActionBarRecipes()
    }

    override fun onResume() {
        super.onResume()

        val id = intent.getStringExtra(EXTRA_RECIPE_TAG_ID).orEmpty()
        val recipes:List<Recipe> = recipeDAO.findAllByCategory(id)
        recipeItems = recipes
        recipeAdapter.updateRecipes(recipeItems)
        recipeAdapter.notifyDataSetChanged()
    }

    private fun onItemSelect(recipe: Recipe) {
        val intent = Intent(this, DetailsRecipe::class.java)
        intent.putExtra(DetailsRecipe.EXTRA_RECIPE_ID, recipe.id.toString())

        if (!session.isFavorite(recipe.id.toString())) session.saveRecipe(recipe.id.toString(), SessionManager.DES_ACTIVE)
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
                val intent = Intent(this, CreateRecipe::class.java)
                intent.putExtra(CreateRecipe.EXTRA_IS_DETAILS, "false")
                intent.putExtra(
                    CreateRecipe.EXTRA_RECIPE_CREATE_TAG_ID,
                    this.intent.getStringExtra(EXTRA_RECIPE_TAG_ID).orEmpty()
                )
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchByName (name: String) {

        val filteredList = recipeItems.filter { it.title.contains(name, true) }

        if (recipeItems.isEmpty()) {
            binding.rvRecipes.visibility = View.GONE
            msg_empty.visibility = View.VISIBLE
        } else {
            binding.rvRecipes.visibility = View.VISIBLE
            msg_empty.visibility = View.GONE
            recipeAdapter.updateRecipes(filteredList)
        }
    }

    private fun getSupportActionBarRecipes () {
        val supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Recipes $country"
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}