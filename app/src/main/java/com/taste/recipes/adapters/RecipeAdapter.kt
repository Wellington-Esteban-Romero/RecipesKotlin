package com.taste.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.taste.recipes.R
import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.data.RecipeTag
import com.taste.recipes.databinding.ItemRecipeBinding

class RecipeAdapter (private var recipes: List<RecipeItemResponse> = emptyList(),
                     private val onClickListener: (RecipeItemResponse) -> Unit): RecyclerView.Adapter<SuperheroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        return SuperheroViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        )
    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        holder.bind(recipes[position], onClickListener)
    }

    fun updateRecipes (list: List<RecipeItemResponse>) {
        recipes = list
        notifyDataSetChanged()
    }

    fun loadRecipes(listRecipes: List<RecipeItemResponse>) {
        this.recipes = listRecipes;
        notifyDataSetChanged()
    }
}

class SuperheroViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val itemRecipeBinding = ItemRecipeBinding.bind(view)
    //private val favoriteImageView = view.findViewById<ImageView>(R.id.imgFavorite)

    fun bind(recipeItemResponse: RecipeItemResponse, onClickListener: (RecipeItemResponse) -> Unit) {
        itemRecipeBinding.nameItem.text = recipeItemResponse.name
        Picasso.get().load(recipeItemResponse.image).into(itemRecipeBinding.imgRecipeItem);
        itemView.setOnClickListener {
            onClickListener(recipeItemResponse)
        }

        /*if (SessionManager(context).isFavoritextName.text.toString()))
                favoriteImageView.visibility = View.VISIBLE
                    else
                favoriteImageView.visibility = View.GONE*/
    }
}