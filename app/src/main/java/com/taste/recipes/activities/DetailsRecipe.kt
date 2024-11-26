package com.taste.recipes.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import com.taste.recipes.R
import com.taste.recipes.adapters.RecipeAdapter
import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.databinding.ActivityDetailsRecipeBinding
import com.taste.recipes.databinding.ActivityRecipeBinding
import com.taste.recipes.services.RecipeService
import com.taste.recipes.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsRecipeBinding
    private lateinit var recipeService: RecipeService
    private lateinit var recipeAdapter: RecipeAdapter

    companion object {
        const val EXTRA_RECIPE_ID = "RECIPE_ID"
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

        recipeService = RetrofitProvider.getRetrofit()

        val recipe_id = intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty()
        println("recipe -> ${recipe_id}")

        getDetailRecipe(recipe_id)
        getSupportActionBarRecipes()
    }

    private fun getDetailRecipe (id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = recipeService.findRecipeById(id)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.i("Recipe", responseBody.toString())

                    runOnUiThread {
                        createDetails(responseBody)
                    }
                }
            }
        }
    }

    private fun createDetails (recipeItemResponse: RecipeItemResponse) {
        Picasso.get().load(recipeItemResponse.image).into(binding.imgDetailRecipeItem)
    }

    private fun getSupportActionBarRecipes () {
        var supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Detail Recipes"
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}