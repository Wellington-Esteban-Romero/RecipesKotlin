package com.taste.recipes.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.taste.recipes.R

class CreateRecipe : AppCompatActivity() {

    private lateinit var btnAddIngredient:Button
    private lateinit var txtAddIngredient:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init () {

        getSupportActionBarRecipes()

        btnAddIngredient = findViewById(R.id.btnAddIngredient)
        txtAddIngredient = findViewById(R.id.txtAddIngredient)

        btnAddIngredient.setOnClickListener {

            val view:View = LayoutInflater.from(this).inflate(R.layout.dialog_recipe, null)

            val editText:TextInputEditText = view.findViewById(R.id.textDialogFieldIngredient)

            MaterialAlertDialogBuilder(this)
                .setTitle("add Ingredient")
                .setView(view)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    println(editText.text.toString())
                    txtAddIngredient.text = "${txtAddIngredient.text}\n\n${editText.text}"
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
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