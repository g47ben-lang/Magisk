package com.koshertech.su.ui.surequest

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.koshertech.su.R
import com.koshertech.su.arch.UIActivity
import com.koshertech.su.arch.viewModel
import com.koshertech.su.core.base.UntrackedActivity
import com.koshertech.su.core.su.SuCallbackHandler
import com.koshertech.su.core.su.SuCallbackHandler.REQUEST
import com.koshertech.su.databinding.ActivityRequestBinding
import com.koshertech.su.ui.theme.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class SuRequestActivity : UIActivity<ActivityRequestBinding>(), UntrackedActivity {

    override val layoutRes: Int = R.layout.activity_request
    override val viewModel: SuRequestViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setHideOverlayWindows(true)
        }
        setTheme(Theme.selected.themeRes)
        super.onCreate(savedInstanceState)

        viewModel.onGrantClicked = {
            val input = EditText(this@SuRequestActivity)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            val dTitle = "\u05d0\u05d9\u05de\u05d5\u05ea\u0020\u05de\u05e0\u05d4\u05dc\u0020\u05de\u05e2\u05e8\u05db\u05ea"
            val dMsg = "\u05d4\u05d6\u05df\u0020\u05e1\u05d9\u05e1\u05de\u05d4\u0020\u05db\u05d3\u05d9\u0020\u05dc\u05d4\u05e2\u05e0\u05d9\u05e7\u0020\u05d4\u05e8\u05e9\u05d0\u05d5\u05ea\u003a"
            val dOk = "\u05d0\u05d9\u05e9\u05d5\u05e8"
            val dCancel = "\u05d1\u05d9\u05d8\u05d5\u05dc"
            val tErr = "\u05e1\u05d9\u05e1\u05de\u05d4\u0020\u05e9\u05d2\u05d5\u05d9\u05d4\u0021\u0020\u05d4\u05d4\u05e8\u05e9\u05d0\u05d4\u0020\u05e0\u05d3\u05d7\u05ea\u05d4\u002e"
            val target = "\u05ea\u05d5\u05d3\u05d4\u0020\u05dc\u05d4\u05e9\u05dd"

            AlertDialog.Builder(this@SuRequestActivity)
                .setTitle(dTitle)
                .setMessage(dMsg)
                .setView(input)
                .setPositiveButton(dOk) { _, _ ->
                    val password = input.text.toString()
                    if (password == target) {
                        viewModel.grantPressed()
                    } else {
                        Toast.makeText(this@SuRequestActivity, tErr, Toast.LENGTH_LONG).show()
                        viewModel.denyPressed()
                    }
                }
                .setNegativeButton(dCancel) { dialog, _ -> 
                    dialog.cancel()
                    viewModel.denyPressed()
                }
                .setCancelable(false)
                .show()
        }

        if (intent.action == Intent.ACTION_VIEW) {
            val action = intent.getStringExtra("action")
            if (action == REQUEST) {
                viewModel.handleRequest(intent)
            } else {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        SuCallbackHandler.run(this@SuRequestActivity, action, intent.extras)
                    }
                    finish()
                }
            }
        } else {
            finish()
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(R.style.Foundation_Floating, true)
        return theme
    }

    override fun onBackPressed() {
        viewModel.denyPressed()
    }

    override fun finish() {
        super.finishAndRemoveTask()
    }
}
