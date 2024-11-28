package com.taste.recipes.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import com.taste.recipes.R
import com.taste.recipes.adapters.RecipeAdapter
import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.data.entities.Recipe
import com.taste.recipes.data.providers.RecipeDAO
import com.taste.recipes.databinding.ActivityDetailsRecipeBinding
import com.taste.recipes.data.providers.RetrofitProvider
import com.taste.recipes.services.RecipeService
import com.taste.recipes.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsRecipeBinding
    private lateinit var recipeService: RecipeService
    private lateinit var recipeItemResponse: RecipeItemResponse
    private lateinit var recipeDAO: RecipeDAO
    private lateinit var recipeAdapter: RecipeAdapter

    companion object {
        const val EXTRA_RECIPE_ID = "RECIPE_ID"
        lateinit var session: SessionManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailsRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init () {
        val recipe_id = intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty()
        println("recipe -> ${recipe_id}")

        recipeService = RetrofitProvider.getRetrofit()

        session = SessionManager(applicationContext)

        recipeDAO = RecipeDAO(this)

        //val recipes:List<Recipe> = recipeDAO.findRecipeById(recipe_id)


        getDetailRecipe(recipe_id)

        getSupportActionBarRecipes()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.details_recipe_menu, menu)

        val recipe_id = intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty()

        if (session.isFavorite(recipe_id))
            menu.findItem(R.id.actionFavorite).setIcon(R.drawable.ic_favorite)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println(item.icon)

        when (item.itemId) {
            R.id.actionFavorite -> {
                var id = recipeItemResponse.id

                if (!session.isFavorite(id)) {
                    session.saveRecipe(id, SessionManager.ACTIVE)
                    item.setIcon(R.drawable.ic_favorite)
                } else {
                    session.saveRecipe(id, SessionManager.DES_ACTIVE)
                    item.setIcon(R.drawable.ic_favorite_empty)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDetailRecipe (id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = recipeService.findRecipeById(id)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.i("Recipe", responseBody.toString())
                    recipeItemResponse = responseBody
                    runOnUiThread {
                        createDetails(responseBody)
                    }
                }
            }
        }
    }

    private fun createDetails (recipeItemResponse: RecipeItemResponse) {
        Picasso.get().load(recipeItemResponse.image).into(binding.imgDetailRecipeItem)
         for (ingredient in recipeItemResponse.ingredients) {
            binding.txtIngredients.text = "${binding.txtIngredients.text}\n\n${ingredient}"
        }

        for (instruction in recipeItemResponse.instructions) {
            binding.txtInstructions.text = "${binding.txtInstructions.text}\n\n${instruction}"
        }
    }

    private fun createDetailsRecipeDAO (recipe: Recipe) {
        Picasso.get().load("https://cdn.dummyjson.com/recipe-images/1.webp").into(binding.imgDetailRecipeItem)
        for (ingredient in recipe.ingredients) {
            binding.txtIngredients.text = "${binding.txtIngredients.text} + ${ingredient}"
        }

        for (instruction in recipe.instructions) {
            binding.txtInstructions.text = "${binding.txtInstructions.text} + ${instruction}"
        }
    }

    private fun getSupportActionBarRecipes () {
        val supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Detail Recipes"
        supportActionBar?.setDisplayUseLogoEnabled(true)
        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
        //supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}