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

    fun filterHoroscope(horoscope: List<RecipeTag>) {
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
    //private val dateHoroscope = view.findViewById<TextView>(R.id.dateHoroscope)
    //private val imgHoroscope = view.findViewById<ImageView>(R.id.imgHoroscope)
    //private val favoriteImageView = view.findViewById<ImageView>(R.id.imgFavorite)

    fun render(recipeTag: RecipeTag, onClickListener: (RecipeTag) -> Unit) {
        val context = itemView.context
        txtName.text = context.getString(recipeTag.name)

        itemView.setOnClickListener {
            onClickListener(recipeTag)
        }
           /* dateHoroscope.text = context.getString(horoscope.date)
            imgHoroscope.setImageResource(horoscope.image)
            }*/

    /*if (SessionManager(context).isFavoritextName.text.toString()))
        favoriteImageView.visibility = View.VISIBLE
    else
        favoriteImageView.visibility = View.GONE*/
    }
}
