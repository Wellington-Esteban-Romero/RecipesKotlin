package com.taste.recipes.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.taste.recipes.R
import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.data.entities.Recipe
import com.taste.recipes.databinding.ItemRecipeBinding
import com.taste.recipes.utils.SessionManager

class RecipeAdapter (private var recipes: List<Recipe> = emptyList(),
                     private val onClickListener: (Recipe) -> Unit): RecyclerView.Adapter<RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        )
    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position], onClickListener)
    }

    fun updateRecipes (list: List<Recipe>) {
        recipes = list
        notifyDataSetChanged()
    }
}

class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val itemRecipeBinding = ItemRecipeBinding.bind(view)
    private val favoriteImageView = view.findViewById<ImageView>(R.id.imgFavorite)

    fun bind(recipe: Recipe, onClickListener: (Recipe) -> Unit) {
        val context = itemView.context

        itemRecipeBinding.nameItem.text = recipe.title

        if (recipe.img.startsWith("https://"))
            Picasso.get().load(recipe.img).into(itemRecipeBinding.imgRecipeItem)
        else
            itemRecipeBinding.imgRecipeItem.setImageURI(Uri.parse(recipe.img))

        itemView.setOnClickListener {
            onClickListener(recipe)
        }

        if (SessionManager(context).isFavorite(recipe.id.toString()))
            favoriteImageView.visibility = View.VISIBLE
        else
            favoriteImageView.visibility = View.GONE
    }
}