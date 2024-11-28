package com.taste.recipes.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.taste.recipes.R
import com.taste.recipes.data.entities.Recipe
import com.taste.recipes.data.providers.RecipeDAO
import com.taste.recipes.databinding.ActivityCreateRecipeBinding
import com.taste.recipes.utils.Utils

class CreateRecipe : AppCompatActivity() {

    private lateinit var binding:ActivityCreateRecipeBinding
    private lateinit var recipeDAO: RecipeDAO
    private lateinit var recipe: Recipe

    companion object {
        const val EXTRA_RECIPE_CREATE_TAG_ID = "RECIPE_CREATE_TAG_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init () {

        val id = intent.getStringExtra(EXTRA_RECIPE_CREATE_TAG_ID).orEmpty()
        println(id +" - " + Utils.getTag(id.toInt()))

        recipeDAO = RecipeDAO(this)

        binding.saveRecipe.setOnClickListener {
            saveRecipe()
        }

        recipe = Recipe(-1,"")

        getSupportActionBarRecipes()
    }

    private fun validateTask(): Boolean {
        // Comprobamos el texto introducido para mostrar posibles errores
        if (recipe.title.trim().isEmpty()) {
            binding.textFieldTitleName.error = "Escribe algo"
            return false
        } else {
            binding.textFieldTitleName.error = null
        }
        if (recipe.title.length > 50) {
            binding.textFieldTitleName.error = "El titulo de ser mayor de 50 caracteres"
            return false
        } else {
            binding.textFieldTitleName.error = null
        }
        return true
    }

    private fun saveRecipe() {
        recipe.title = binding.textFieldTitleName.editText?.text.toString()
        recipe.ingredients = binding.textFieldIngredient.editText?.text.toString()
        recipe.instructions = binding.textFieldInstructions.editText?.text.toString()
        recipe.category = intent.getStringExtra(EXTRA_RECIPE_CREATE_TAG_ID).orEmpty()

        if (validateTask()) {
            // Si la tarea existe la actualizamos si no la insertamos
            recipeDAO.insert(recipe)
            finish()
        }
    }

    private fun getSupportActionBarRecipes () {
        var supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Create Recipe"
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}