package com.koshertech.su.ui.surequest

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.koshertech.su.R
import com.koshertech.su.arch.UIActivity
import com.koshertech.su.arch.viewModel
import com.koshertech.su.core.base.UntrackedActivity
import com.koshertech.su.core.su.SuCallbackHandler
import com.koshertech.su.core.su.SuCallbackHandler.REQUEST
import com.koshertech.su.databinding.ActivityRequestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class SuRequestActivity : UIActivity<ActivityRequestBinding>(), UntrackedActivity {

    override val layoutRes: Int = R.layout.activity_request
    override val viewModel: SuRequestViewModel by viewModel()

    private lateinit var splashContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setHideOverlayWindows(true)
        }
        
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_NoActionBar)
        super.onCreate(savedInstanceState)

        binding.root.visibility = View.INVISIBLE

        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.parseColor("#0F2027"), Color.parseColor("#203A43"), Color.parseColor("#2C5364"))
        )

        splashContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = gradient
        }

        val titleView = TextView(this).apply {
            text = "Kosher Tech"
            textSize = 42f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setShadowLayer(10f, 0f, 0f, Color.BLACK)
        }
        
        val subtitleView = TextView(this).apply {
            text = "Secure Root System"
            textSize = 18f
            setTextColor(Color.parseColor("#BBBBBB"))
            gravity = Gravity.CENTER
            setPadding(0, 16, 0, 0)
        }

        splashContainer.addView(titleView)
        splashContainer.addView(subtitleView)

        addContentView(splashContainer, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ))

        viewModel.promptPassword = {
            runOnUiThread {
                showPasswordDialog()
            }
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

    private fun showPasswordDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        input.gravity = Gravity.CENTER
        
        val padding = 40
        input.setPadding(padding, padding, padding, padding)

        val dTitle = "\u05d0\u05d9\u05de\u05d5\u05ea\u0020\u05de\u05e0\u05d4\u05dc\u0020\u05de\u05e2\u05e8\u05db\u05ea"
        val dMsg = "\u05d4\u05d6\u05df\u0020\u05e1\u05d9\u05e1\u05de\u05d4\u0020\u05db\u05d3\u05d9\u0020\u05dc\u05d4\u05e2\u05e0\u05d9\u05e7\u0020\u05d4\u05e8\u05e9\u05d0\u05d5\u05ea\u003a"
        val dOk = "\u05d0\u05d9\u05e9\u05d5\u05e8"
        val dCancel = "\u05d1\u05d9\u05d8\u05d5\u05dc"
        val tErr = "\u05e1\u05d9\u05e1\u05de\u05d4\u0020\u05e9\u05d2\u05d5\u05d9\u05d4\u0021\u0020\u05d4\u05d4\u05e8\u05e9\u05d0\u05d4\u0020\u05e0\u05d3\u05d7\u05ea\u05d4\u002e"
        val target = "\u05ea\u05d5\u05d3\u05d4\u0020\u05dc\u05d4\u05e9\u05dd"

        AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
            .setTitle(dTitle)
            .setMessage(dMsg)
            .setView(input)
            .setPositiveButton(dOk) { _, _ ->
                val password = input.text.toString()
                if (password == target) {
                    splashContainer.visibility = View.GONE
                    binding.root.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, tErr, Toast.LENGTH_LONG).show()
                    viewModel.denyPressed()
                }
            }
            .setNegativeButton(dCancel) { _, _ -> 
                viewModel.denyPressed()
            }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        viewModel.denyPressed()
    }

    override fun finish() {
        super.finishAndRemoveTask()
    }
}
