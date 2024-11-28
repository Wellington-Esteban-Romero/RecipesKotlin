package com.taste.recipes.utils


class Utils {

    companion object {
        fun getTag(id: Int): String {
            val recipeTag = listOf(
                "Italian",
                "Indian",
                "Pakistani",
                "Japanese",
                "Korean",
                "Mexican",
                "Russian",
                "Spanish",
                "Vietnamese",
                "Cuban",
                "Brazilian",
                "Moroccan",
                "Lebanese",
                "Hawaiian"
            )
            return recipeTag[id - 1]
        }
    }
}