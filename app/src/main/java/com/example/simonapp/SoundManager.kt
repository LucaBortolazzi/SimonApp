package com.example.simonapp

import android.content.Context
import android.media.MediaPlayer

private val noteResources = listOf(
    R.raw.c4,
    R.raw.d4,
    R.raw.e4,
    R.raw.f4,
    R.raw.g4,
    R.raw.a4
)

//istanza singla riutilizzata per tutte le note
private var currentPlayer: MediaPlayer? = null

fun playTone(context: Context, colorIndex: Int) {
    try {
        currentPlayer?.stop()
        currentPlayer?.release()
        currentPlayer = null

        val player = MediaPlayer.create(context, noteResources[colorIndex])
        player?.setOnCompletionListener {
            it.release()
            currentPlayer = null
        }
        player?.start()
        currentPlayer = player
    } catch (e: Exception) {}
}