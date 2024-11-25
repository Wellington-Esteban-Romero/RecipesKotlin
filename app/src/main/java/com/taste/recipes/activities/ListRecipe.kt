package com.taste.recipes.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.superheroes.utils.RetrofitProvider
import com.taste.recipes.R
import com.taste.recipes.adapters.RecipeAdapter
import com.taste.recipes.data.RecipeTag
import com.taste.recipes.databinding.ActivityRecipeBinding
import com.taste.recipes.services.RecipeService
import com.taste.recipes.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private lateinit var recipeService: RecipeService
    private lateinit var recipeAdapter: RecipeAdapter

    companion object {
        const val EXTRA_RECIPE_TAG_ID = "RECIPE_TAG_ID"
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

        val id = intent.getStringExtra(EXTRA_RECIPE_TAG_ID).orEmpty()
        println(id +" - " + Utils.getTag(id.toInt()))

        getRecipe(Utils.getTag(id.toInt()))

        recipeAdapter = RecipeAdapter() { recipeItem ->
            onItemSelect()
        }

        binding.rvRecipe.apply {
            layoutManager = LinearLayoutManager(this@ListRecipe)
            adapter = recipeAdapter
            hasFixedSize()
        }

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

    private fun onItemSelect(recipeTag: RecipeTag) {
        val intent = Intent(this, ListRecipe::class.java)
        intent.putExtra(DetailsRecipe.EXTRA_RECIPE_ID, recipeTag.id.toString())

        // var name = getString(recipeTag.name)

        /*if (!session.isFavorite(name))
            session.saveHoroscope(name, SessionManager.DES_ACTIVE)*/

        startActivity(intent)
    }
}