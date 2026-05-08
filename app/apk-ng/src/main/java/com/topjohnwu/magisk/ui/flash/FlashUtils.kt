package com.koshertech.su.ui.flash

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.koshertech.su.core.Const
import com.koshertech.su.core.cmp
import com.koshertech.su.ui.MainActivity

object FlashUtils {

    const val INTENT_FLASH = "com.koshertech.su.intent.FLASH"
    const val EXTRA_FLASH_ACTION = "flash_action"
    const val EXTRA_FLASH_URI = "flash_uri"

    fun installIntent(context: Context, file: Uri): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            component = MainActivity::class.java.cmp(context.packageName)
            action = INTENT_FLASH
            putExtra(EXTRA_FLASH_ACTION, Const.Value.FLASH_ZIP)
            putExtra(EXTRA_FLASH_URI, file.toString())
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context, file.hashCode(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
