package com.taste.recipes.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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

    // Registrar el launcher para seleccionar imágenes
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Guardar la URI de la imagen seleccionada
            recipe.img = uri.toString()

            // Mostrar la imagen en un ImageView (opcional)
            //binding.imageViewSelected.setImageURI(uri)
        }
    }

    companion object {
        const val EXTRA_RECIPE_CREATE_TAG_ID = "RECIPE_CREATE_TAG_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
        iniEvent()
    }

    private fun init () {

        val id = intent.getStringExtra(EXTRA_RECIPE_CREATE_TAG_ID).orEmpty()
        println(id +" - " + Utils.getTag(id.toInt()))

        recipeDAO = RecipeDAO(this)

        recipe = Recipe(-1,"")

        getSupportActionBarRecipes()
    }

    private fun iniEvent () {

        binding.btnSaveRecipe.setOnClickListener {
            saveRecipe()
        }

        binding.btnSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun validateTask(): Boolean {
        var isValid = true

        if (recipe.title.trim().isEmpty()) {
            binding.textFieldTitleName.error = "Escribe algo"
            isValid = false
        } else {
            binding.textFieldTitleName.error = null
        }
        if (recipe.title.length > 50) {
            binding.textFieldTitleName.error = "Te pasaste"
            isValid = false
        } else {
            binding.textFieldTitleName.error = null
        }

        // Validación de los ingredientes
        if (recipe.ingredients.trim().isEmpty()) {
            binding.textFieldIngredients.error = "Escribe los ingredientes"
            isValid = false
        } else {
            binding.textFieldIngredients.error = null
        }

        // Validación de las instrucciones
        if (recipe.instructions.trim().isEmpty()) {
            binding.textFieldInstructions.error = "Escribe las instrucciones"
            isValid = false
        } else {
            binding.textFieldInstructions.error = null
        }

        // Validación del tiempo de preparación
        if (recipe.prepTimeMinutes <= 0) {
            binding.textFieldPrepTime.error = "El tiempo de preparación debe ser mayor a 0"
            isValid = false
        } else {
            binding.textFieldPrepTime.error = null
        }

        // Validación del tiempo de cocción
        if (recipe.cookTimeMinutes <= 0) {
            binding.textFieldCookTime.error = "El tiempo de cocción debe ser mayor a 0"
            isValid = false
        } else {
            binding.textFieldCookTime.error = null
        }

        // Validación de las porciones
        if (recipe.servings.toInt() <= 0) {
            binding.textFieldServings.error = "Las porciones deben ser mayores a 0"
            isValid = false
        } else {
            binding.textFieldServings.error = null
        }

        // Validar Dificultad
        if (recipe.difficulty.trim().isEmpty()) {
            binding.textFieldDifficulty.error = "Selecciona una dificultad"
            isValid = false
        } else {
            binding.textFieldDifficulty.error = null
        }

        // Validación de la imagen
        if (recipe.img.trim().isEmpty()) {
            binding.btnSelectImage.error = "Proporciona una imagen válida"
            isValid = false
        } else {
            binding.btnSelectImage.error = null
        }
        return isValid
    }

    private fun saveRecipe() {
        recipe.title = binding.textFieldTitleName.editText?.text.toString()
        recipe.ingredients = binding.textFieldIngredients.editText?.text.toString()
        recipe.instructions = binding.textFieldInstructions.editText?.text.toString()
        if (binding.textFieldPrepTime.editText?.text.toString().isEmpty()){
            recipe.prepTimeMinutes = 0
        } else {
            recipe.prepTimeMinutes = binding.textFieldPrepTime.editText?.text.toString().toInt()
        }
        if (binding.textFieldCookTime.editText?.text.toString().isEmpty()){
            recipe.cookTimeMinutes = 0
        } else {
            recipe.cookTimeMinutes = binding.textFieldCookTime.editText?.text.toString().toInt()
        }
        if (binding.textFieldServings.editText?.text.toString().isEmpty()){
            recipe.servings = "0"
        } else {
            recipe.servings = binding.textFieldServings.editText?.text.toString()
        }
        recipe.difficulty = binding.textFieldDifficulty.editText?.text.toString()
        recipe.category = intent.getStringExtra(EXTRA_RECIPE_CREATE_TAG_ID).orEmpty()

        if (validateTask()) {
            // Si la tarea existe la actualizamos si no la insertamos
            recipeDAO.insert(recipe)
            finish()
        }
    }

    private fun getSupportActionBarRecipes () {
        val supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Create Recipe"
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}