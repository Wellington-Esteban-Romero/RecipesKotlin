package com.taste.recipes.utils

import com.taste.recipes.R
import com.taste.recipes.data.RecipeTag

class RecipeTagProvider {

    companion object  {
        private val recipeTags: List<RecipeTag> = listOf(
            RecipeTag(1, R.string.italian_tag, R.drawable.ic_italy),
            RecipeTag(2, R.string.indian_tag, R.drawable.ic_india),
            RecipeTag(3, R.string.pakistani_tag, R.drawable.ic_pakistan),
            RecipeTag(4, R.string.japanese_tag, R.drawable.ic_japan),
            RecipeTag(5, R.string.korean_tag, R.drawable.ic_italy),
            RecipeTag(6, R.string.mexican_tag, R.drawable.ic_mexico),
            RecipeTag(7, R.string.russian_tag, R.drawable.ic_russia),
            RecipeTag(8, R.string.spanish_tag, R.drawable.ic_spain),
            RecipeTag(9, R.string.vietnamese_tag, R.drawable.ic_vietnam),
            RecipeTag(10, R.string.cuban_tag, R.drawable.ic_cuba)
        )

        fun findAll(): List<RecipeTag> {
            return recipeTags; }
    }
}