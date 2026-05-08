package com.koshertech.su.dialog

import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.lifecycle.lifecycleScope
import com.koshertech.su.core.BuildConfig
import com.koshertech.su.core.Info
import com.koshertech.su.core.R
import com.koshertech.su.core.ktx.reboot
import com.koshertech.su.core.ktx.toast
import com.koshertech.su.core.tasks.MagiskInstaller
import com.koshertech.su.events.DialogBuilder
import com.koshertech.su.ui.home.HomeViewModel
import com.koshertech.su.view.MagiskDialog
import com.topjohnwu.superuser.internal.UiThreadHandler
import kotlinx.coroutines.launch

class EnvFixDialog(private val vm: HomeViewModel, private val code: Int) : DialogBuilder {

    override fun build(dialog: MagiskDialog) {
        dialog.apply {
            setTitle(R.string.env_fix_title)
            setMessage(R.string.env_fix_msg)
            setButton(MagiskDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
                doNotDismiss = true
                onClick {
                    dialog.apply {
                        setTitle(R.string.setup_title)
                        setMessage(R.string.setup_msg)
                        resetButtons()
                        setCancelable(false)
                    }
                    dialog.activity.lifecycleScope.launch {
                        MagiskInstaller.FixEnv().exec { success ->
                            dialog.dismiss()
                            context.toast(
                                if (success) R.string.reboot_delay_toast else R.string.setup_fail,
                                Toast.LENGTH_LONG
                            )
                            if (success)
                                UiThreadHandler.handler.postDelayed(5000) { reboot() }
                        }
                    }
                }
            }
            setButton(MagiskDialog.ButtonType.NEGATIVE) {
                text = android.R.string.cancel
            }
        }

        if (code == 2 || // No rules block, module policy not loaded
            Info.env.versionCode != BuildConfig.APP_VERSION_CODE ||
            Info.env.versionString != BuildConfig.APP_VERSION_NAME) {
            dialog.setMessage(R.string.env_full_fix_msg)
            dialog.setButton(MagiskDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
                onClick {
                    vm.onMagiskPressed()
                    dialog.dismiss()
                }
            }
        }
    }
}
