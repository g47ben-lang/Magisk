package com.koshertech.su.ui.surequest

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeProvider
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.koshertech.su.R
import com.koshertech.su.arch.VMFactory
import com.koshertech.su.core.base.ActivityExtension
import com.koshertech.su.core.base.UntrackedActivity
import com.koshertech.su.core.su.SuCallbackHandler
import com.koshertech.su.core.su.SuCallbackHandler.REQUEST
import com.koshertech.su.core.wrap
import com.koshertech.su.ui.theme.MagiskTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SuRequestActivity : ComponentActivity(), UntrackedActivity {

    private val extension = ActivityExtension(this)
    private val viewModel: SuRequestViewModel by lazy {
        ViewModelProvider(this, VMFactory)[SuRequestViewModel::class.java]
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.wrap())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        extension.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setHideOverlayWindows(true)
        }
        setTheme(R.style.Floating)
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) { viewModel.denyPressed() }
        viewModel.finishActivity = { finish() }

        viewModel.authenticate = { onSuccess ->
            val input = EditText(this@SuRequestActivity)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            val dTitle = "\u05d0\u05d9\u05de\u05d5\u05ea\u0020\u05de\u05e0\u05d4\u05dc\u0020\u05de\u05e2\u05e8\u05db\u05ea"
            val dMsg = "\u05d4\u05d6\u05df\u0020\u05e1\u05d9\u05e1\u05de\u05d4\u0020\u05db\u05d3\u05d9\u0020\u05dc\u05d4\u05e2\u05e0\u05d9\u05e7\u0020\u05d4\u05e8\u05e9\u05d0\u05d5\u05ea\u003a"
            val dOk = "\u05d0\u05d9\u05e9\u05d5\u05e8"
            val dCancel = "\u05d1\u05d9\u05d8\u05d5\u05dc"
            val tErr = "\u05e1\u05d9\u05e1\u05de\u05d4\u0020\u05e9\u05d2\u05d5\u05d9\u05d4\u0021\u0020\u05d4\u05d4\u05e8\u05e9\u05d0\u05d4\u0020\u05e0\u05d3\u05d7\u05ea\u05d4\u002e"
            val target = "\u05ea\u05d5\u05d3\u05d4 \u05dc\u05d4\u05e9\u05dd"

            AlertDialog.Builder(this@SuRequestActivity)
                .setTitle(dTitle)
                .setMessage(dMsg)
                .setView(input)
                .setPositiveButton(dOk) { _, _ ->
                    val password = input.text.toString()
                    if (password == target) {
                        onSuccess()
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

        if (viewModel.useTapjackProtection) {
            window.decorView.rootView.accessibilityDelegate = EmptyAccessibilityDelegate
        }

        setContent {
            MagiskTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    SuRequestScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        extension.onSaveInstanceState(outState)
    }

    override fun finish() {
        super.finishAndRemoveTask()
    }

    private object EmptyAccessibilityDelegate : View.AccessibilityDelegate() {
        override fun sendAccessibilityEvent(host: View, eventType: Int) {}
        override fun performAccessibilityAction(host: View, action: Int, args: Bundle?) = true
        override fun sendAccessibilityEventUnchecked(host: View, event: AccessibilityEvent) {}
        override fun dispatchPopulateAccessibilityEvent(host: View, event: AccessibilityEvent) = true
        override fun onPopulateAccessibilityEvent(host: View, event: AccessibilityEvent) {}
        override fun onInitializeAccessibilityEvent(host: View, event: AccessibilityEvent) {}
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {}
        override fun addExtraDataToAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo, extraDataKey: String, arguments: Bundle?) {}
        override fun onRequestSendAccessibilityEvent(host: ViewGroup, child: View, event: AccessibilityEvent): Boolean = false
        override fun getAccessibilityNodeProvider(host: View): AccessibilityNodeProvider? = null
    }
}
