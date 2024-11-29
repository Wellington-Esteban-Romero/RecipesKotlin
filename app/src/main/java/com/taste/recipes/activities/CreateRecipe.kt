package com.taste.recipes.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.taste.recipes.R

import com.taste.recipes.data.entities.Recipe
import com.taste.recipes.data.providers.RecipeDAO
import com.taste.recipes.databinding.ActivityCreateRecipeBinding

class CreateRecipe : AppCompatActivity() {

    private lateinit var binding:ActivityCreateRecipeBinding
    private lateinit var recipeDAO: RecipeDAO
    private lateinit var recipe: Recipe
    var isEditing: Boolean = false

    // Registrar el launcher para seleccionar imágenes
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            this.contentResolver.takePersistableUriPermission(uri, flag)
            recipe.img = uri.toString()
            // Mostrar la imagen en un ImageView (opcional)
            //binding.imageViewSelected.setImageURI(uri)
        }
    }

    companion object {
        const val EXTRA_RECIPE_CREATE_TAG_ID = "RECIPE_CREATE_TAG_ID"
        const val EXTRA_IS_DETAILS = "IS_DETAILS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
        initListener()
    }

    private fun init () {

        val isDetails = intent.getStringExtra(EXTRA_IS_DETAILS).orEmpty()

        recipeDAO = RecipeDAO(this)

        recipe = if (isDetails.toBoolean()) {
            isEditing = true
            recipeDAO.findRecipeById(intent.getStringExtra(DetailsRecipe.EXTRA_RECIPE_ID).orEmpty())[0]
        } else {
            isEditing = false
            Recipe(-1,"")
        }

        loadData()

        getSupportActionBarRecipes()
    }

    private fun initListener () {

        binding.btnSaveRecipe.setOnClickListener {
            saveRecipe()
        }

        binding.btnSelectImage.setOnClickListener {
            if(ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this))
                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun validateTask(): Boolean {
        var isValid = true

        if (recipe.title.trim().isEmpty()) {
            binding.textFieldTitleName.error = "Write something"
            isValid = false
        } else {
            binding.textFieldTitleName.error = null
        }
        if (recipe.title.length > 50) {
            binding.textFieldTitleName.error = "Is over 50 characters"
            isValid = false
        } else {
            binding.textFieldTitleName.error = null
        }

        if (recipe.ingredients.trim().isEmpty()) {
            binding.textFieldIngredients.error = "Write the ingredients"
            isValid = false
        } else {
            binding.textFieldIngredients.error = null
        }

        if (recipe.instructions.trim().isEmpty()) {
            binding.textFieldInstructions.error = "Write the instructions"
            isValid = false
        } else {
            binding.textFieldInstructions.error = null
        }

        if (recipe.prepTimeMinutes <= 0) {
            binding.textFieldPrepTime.error = "Preparation time must be greater than 0"
            isValid = false
        } else {
            binding.textFieldPrepTime.error = null
        }

        if (recipe.cookTimeMinutes <= 0) {
            binding.textFieldCookTime.error = "Cooking time must be greater than 0"
            isValid = false
        } else {
            binding.textFieldCookTime.error = null
        }

        if (recipe.servings.toInt() <= 0) {
            binding.textFieldServings.error = "Servings must be greater than 0"
            isValid = false
        } else {
            binding.textFieldServings.error = null
        }

        if (recipe.difficulty.trim().isEmpty()) {
            binding.textFieldDifficulty.error = "Select a difficulty"
            isValid = false
        } else {
            binding.textFieldDifficulty.error = null
        }

        if (recipe.img.trim().isEmpty()) {
            binding.btnSelectImage.error = "Provides a valid image"
            isValid = false
        } else {
            binding.btnSelectImage.error = null
        }
        return isValid
    }

    private fun loadData() {
        binding.editTextTitle.setText(recipe.title)
        binding.editTextIngredients.setText(recipe.ingredients)
        binding.editTextInstructions.setText(recipe.instructions)
        binding.editTextPrepTime.setText(recipe.prepTimeMinutes.toString())
        binding.editTextCookTime.setText(recipe.cookTimeMinutes.toString())
        binding.editTextServings.setText(recipe.servings)
        binding.editTextDifficulty.setText(recipe.difficulty)
        binding.imageViewSelected.setImageURI(recipe.img.toUri())
        if (!isEditing) {
            binding.btnSaveRecipe.text = "Save Recipe"
        } else {
            binding.btnSaveRecipe.text = "Edit Recipe"
        }
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

        if (!isEditing) {
            recipe.category = intent.getStringExtra(EXTRA_RECIPE_CREATE_TAG_ID).orEmpty()
        }

        if (validateTask()) {

            if (recipe.id != -1L) {
                recipeDAO.update(recipe)
            } else {
                recipeDAO.insert(recipe)
            }
            finish()
        }
    }

    private fun getSupportActionBarRecipes () {
        val supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title =  if (isEditing) {
                                        "Edit Recipe"
                                    } else {
                                        "Create Recipe"
                                    }
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}