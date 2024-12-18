package com.taste.recipes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taste.recipes.R
import com.taste.recipes.data.RecipeTag

class RecipeTagAdapter(
    private var tags: List<RecipeTag>,
    private val onClickListener: (RecipeTag) -> Unit
) : RecyclerView.Adapter<HoroscopeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        return HoroscopeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        )
    }

    fun filterCountry(horoscope: List<RecipeTag>) {
        this.tags = horoscope;
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        holder.render(tags[position], onClickListener)
    }

    override fun getItemCount() = tags.size
}

class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val txtName = view.findViewById<TextView>(R.id.name)
    private val imgCountry = view.findViewById<ImageView>(R.id.imgCountry)

    fun render(recipeTag: RecipeTag, onClickListener: (RecipeTag) -> Unit) {
        val context = itemView.context
        txtName.text = context.getString(recipeTag.name)

        itemView.setOnClickListener {
            onClickListener(recipeTag)
        }
        imgCountry.setImageResource(recipeTag.img)
    }
}
