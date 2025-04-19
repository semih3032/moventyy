package com.moventy.utils

import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode

object FFmpegCommandRunner {

    fun runCommand(command: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        Log.d("FFmpegRunner", "Running command: $command")

        FFmpegKit.executeAsync(command) { session ->
            val returnCode = session.returnCode
            if (ReturnCode.isSuccess(returnCode)) {
                Log.d("FFmpegRunner", "Command succeeded")
                onSuccess()
            } else {
                val failMessage = session.failStackTrace ?: "Unknown error"
                Log.e("FFmpegRunner", "Command failed: $failMessage")
                onFailure(failMessage)
            }
        }
    }
}
