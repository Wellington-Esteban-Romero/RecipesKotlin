package com.taste.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.taste.recipes.R
import com.taste.recipes.data.RecipeItemResponse
import com.taste.recipes.databinding.ItemRecipeBinding

class RecipeAdapter (private var superheros: List<RecipeItemResponse> = emptyList(),
                     private val onClickListener: (RecipeItemResponse) -> Unit): RecyclerView.Adapter<SuperheroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        return SuperheroViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        )
    }

    override fun getItemCount() = superheros.size

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        holder.bind(superheros[position], onClickListener)
    }

    fun updateSuperheroes (list: List<RecipeItemResponse>) {
        superheros = list
        notifyDataSetChanged()
    }
}

class SuperheroViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val itemRecipeBinding = ItemRecipeBinding.bind(view)

    fun bind(recipeItemResponse: RecipeItemResponse, onClickListener: (RecipeItemResponse) -> Unit) {
        itemRecipeBinding.nameItem.text = recipeItemResponse.name
        Picasso.get().load(recipeItemResponse.image).into(itemRecipeBinding.imgRecipeItem);
        itemView.setOnClickListener {
            onClickListener(recipeItemResponse)
        }
    }
}