package com.example.proyectodd.view

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberSoundPlayer(soundResId: Int): () -> Unit {
    val context = LocalContext.current

    return remember(soundResId) {
        {
            try {
                val mediaPlayer = MediaPlayer.create(context, soundResId)
                mediaPlayer?.apply {
                    setOnCompletionListener { mp ->
                        mp.release()
                    }
                    start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}