package com.moventy.utils

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class VideoEffect(val id: String, val name: String, val ffmpegCommand: String)
data class TransitionEffect(val id: String, val name: String, val ffmpegCommand: String)
data class TextStyle(val id: String, val name: String, val style: String)

object EffectManager {

    fun loadVideoEffects(context: Context): List<VideoEffect> {
        val jsonString = loadJSONFromAsset(context, "video_effects.json")
        val jsonArray = JSONArray(jsonString)
        val list = mutableListOf<VideoEffect>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                VideoEffect(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    ffmpegCommand = obj.getString("ffmpegCommand")
                )
            )
        }
        return list
    }

    fun loadTransitionEffects(context: Context): List<TransitionEffect> {
        val jsonString = loadJSONFromAsset(context, "transition_effects.json")
        val jsonArray = JSONArray(jsonString)
        val list = mutableListOf<TransitionEffect>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                TransitionEffect(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    ffmpegCommand = obj.getString("ffmpegCommand")
                )
            )
        }
        return list
    }

    fun loadTextStyles(context: Context): List<TextStyle> {
        val jsonString = loadJSONFromAsset(context, "text_styles.json")
        val jsonArray = JSONArray(jsonString)
        val list = mutableListOf<TextStyle>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                TextStyle(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    style = obj.getString("style")
                )
            )
        }
        return list
    }

    private fun loadJSONFromAsset(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
