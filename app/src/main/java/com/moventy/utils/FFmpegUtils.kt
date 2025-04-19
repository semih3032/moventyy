package com.moventy.utils

import android.content.Context
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode

object FFmpegUtils {

    fun applyBasicFilter(
        context: Context,
        inputPath: String,
        outputPath: String,
        onComplete: (Boolean) -> Unit
    ) {
        val command = "-y -i $inputPath -vf hue=s=0 -preset ultrafast $outputPath"

        FFmpegKit.executeAsync(command) { session ->
            val returnCode = session.returnCode

            if (ReturnCode.isSuccess(returnCode)) {
                Log.d("FFmpegUtils", "Filter applied successfully.")
                onComplete(true)
            } else {
                Log.e("FFmpegUtils", "FFmpeg command failed: ${returnCode}")
                onComplete(false)
            }
        }
    }
}
