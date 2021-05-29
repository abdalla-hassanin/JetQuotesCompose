package com.abdalla.jetquotescompose.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

// used to copy quote to clipboard
fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

// used to share quote to other application
fun Context.shareToOthers(quote: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, quote)
    startActivity(Intent.createChooser(intent, "Share via"))
}

