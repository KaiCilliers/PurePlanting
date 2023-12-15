package com.sunrisekcdeveloper.addEdit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


internal fun Context.shouldShowRationale(permission: String): Boolean {
    return findActivity().shouldShowRequestPermissionRationale(permission)
}

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

internal fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.findActivity().packageName, null)
    ).also(::startActivity)
}

internal fun Context.createTempFileUri(): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "PNG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".png", /* suffix */
        filesDir
    )
    return FileProvider.getUriForFile(
        this,
        "com.sunrisekcdeveloper.pureplanting" + ".provider", image
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
internal fun <T> StateFlow<T>.collectImmediatelyAsState(
): State<T> = collectAsState(value, Dispatchers.Main.immediate)