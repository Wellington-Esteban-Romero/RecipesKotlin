package com.taste.recipes.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taste.recipes.R
import com.taste.recipes.adapters.RecipeTagAdapter
import com.taste.recipes.data.RecipeTag
import com.taste.recipes.utils.RecipeTagProvider

class MainActivity : AppCompatActivity() {

    private lateinit var recipeTagAdapter: RecipeTagAdapter
    private lateinit var recipeTags: List<RecipeTag>
    private lateinit var rvTags: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init () {

        rvTags = findViewById(R.id.rvTags)

        recipeTags = RecipeTagProvider.findAll()

        recipeTagAdapter = RecipeTagAdapter(recipeTags) { tag ->
            onItemSelect(tag)
        }

        rvTags.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = recipeTagAdapter
        }
    }

    private fun onItemSelect(recipeTag: RecipeTag) {
        val intent = Intent(this, ListRecipe::class.java)
        intent.putExtra("id", recipeTag.id.toString())

       // var name = getString(recipeTag.name)

        /*if (!session.isFavorite(name))
            session.saveHoroscope(name, SessionManager.DES_ACTIVE)*/

        startActivity(intent)
    }

}