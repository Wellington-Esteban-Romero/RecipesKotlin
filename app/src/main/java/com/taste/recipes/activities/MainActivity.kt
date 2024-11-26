package com.taste.recipes.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taste.recipes.R
import com.taste.recipes.adapters.RecipeTagAdapter
import com.taste.recipes.data.RecipeTag
import com.taste.recipes.providers.RecipeTagProvider

class MainActivity : AppCompatActivity() {

    private lateinit var recipeTagAdapter: RecipeTagAdapter
    private lateinit var recipeTags: List<RecipeTag>
    private lateinit var msg_empty: TextView
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

        msg_empty = findViewById(R.id.msg_empty)

        recipeTagAdapter = RecipeTagAdapter(recipeTags) { tag ->
            onItemSelect(tag)
        }

        rvTags.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = recipeTagAdapter
        }

        getSupportActionBarRecipes()
    }

    private fun onItemSelect(recipeTag: RecipeTag) {
        val intent = Intent(this, ListRecipe::class.java)
        intent.putExtra(ListRecipe.EXTRA_RECIPE_TAG_ID, recipeTag.id.toString())
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem = menu?.findItem(R.id.actionSearch)!!

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

    private fun searchByName (name: String) {
        val recipeTags: ArrayList<RecipeTag> = ArrayList()

        for (item in this.recipeTags) {
            if (getString(item.name).lowercase().contains(name.lowercase())
            ) {
                recipeTags.add(item);
            }
        }

        if (recipeTags.isEmpty()) {
            rvTags.visibility = View.GONE
            msg_empty.visibility = View.VISIBLE
        } else {
            rvTags.visibility = View.VISIBLE
            msg_empty.visibility = View.GONE
            recipeTagAdapter.filterCountry(recipeTags)
        }
    }

    private fun getSupportActionBarRecipes () {
        var supportActionBar = supportActionBar;
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Recipes for country"
        //supportActionBar?.setLogo(R.drawable.ic_zodiac);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        val colorDrawable = ColorDrawable(getResources().getColor(R.color.menu_color, null))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
    }
}