package com.taste.recipes.utils

import com.taste.recipes.R
import com.taste.recipes.data.RecipeTag

class RecipeTagProvider {

    companion object  {
        private val recipeTags: List<RecipeTag> = listOf(
            RecipeTag(1, R.string.italian_tag),
            RecipeTag(2, R.string.indian_tag),
            RecipeTag(3, R.string.pakistani_tag),
            RecipeTag(4, R.string.japanese_tag),
            RecipeTag(5, R.string.korean_tag)
        )

        fun findAll(): List<RecipeTag> {
            return recipeTags; }
    }
}