package com.taste.recipes.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import com.taste.recipes.R
import com.taste.recipes.data.entities.Recipe
import com.taste.recipes.data.providers.RecipeDAO
import com.taste.recipes.databinding.ActivityDetailsRecipeBinding
import com.taste.recipes.data.providers.RetrofitProvider
import com.taste.recipes.services.RecipeService
import com.taste.recipes.utils.SessionManager

class DetailsRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsRecipeBinding
    private lateinit var recipeService: RecipeService
    private lateinit var recipeDAO: RecipeDAO
    private lateinit var recipes:List<Recipe>
    private lateinit var imgEdit:ImageView

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
        initListener()
    }

    override fun onResume() {
        super.onResume()

        val recipe_id = intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty()

        recipes = recipeDAO.findRecipeById(recipe_id)

        createDetails(recipes)

        getSupportActionBarRecipes()
    }

    private fun init () {

        val recipe_id = intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty()

        imgEdit = findViewById(R.id.imgEdit)

        recipeService = RetrofitProvider.getRetrofit()

        session = SessionManager(applicationContext)

        recipeDAO = RecipeDAO(this)

        recipes = recipeDAO.findRecipeById(recipe_id)

        createDetails(recipes)

        getSupportActionBarRecipes()
    }

    private fun initListener () {

        val id = intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty()

        imgEdit.setOnClickListener {
            val intent = Intent(this, CreateRecipe::class.java)
            intent.putExtra(CreateRecipe.EXTRA_IS_DETAILS, "true")
            intent.putExtra(EXTRA_RECIPE_ID, id)
            startActivity(intent)
        }

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

        val id = intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty()

        when (item.itemId) {
            R.id.actionFavorite -> {

                if (!session.isFavorite(id)) {
                    session.saveRecipe(id, SessionManager.ACTIVE)
                    item.setIcon(R.drawable.ic_favorite)
                } else {
                    session.saveRecipe(id, SessionManager.DES_ACTIVE)
                    item.setIcon(R.drawable.ic_favorite_empty)
                }
            }
            R.id.actionDelete -> {
                deleteRecipe(recipes[0])
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDetails (recipes:List<Recipe>) {
        Picasso.get().load(recipes[0].img).into(binding.imgDetailRecipeItem)
        binding.txtIngredients.text = recipes[0].ingredients.split(", ").joinToString("\n\n")

        binding.txtInstructions.text = recipes[0].instructions.split(", ").joinToString("\n\n")

    }

    private fun deleteRecipe(recipe: Recipe) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Recipe")
            .setMessage("Are you sure delete the recipe?")
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                // Borramos la receta en caso de pulsar el boton OK
                recipeDAO.deleteRecipe(intent.getStringExtra(EXTRA_RECIPE_ID).orEmpty())
                finish()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    private fun getSupportActionBarRecipes () {
        val supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Detail Recipes"
        supportActionBar?.setDisplayUseLogoEnabled(true)
        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}