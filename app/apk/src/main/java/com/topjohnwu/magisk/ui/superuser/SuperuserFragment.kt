package com.koshertech.su.ui.superuser

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.app.AlertDialog
import com.koshertech.su.R
import com.koshertech.su.arch.BaseFragment
import com.koshertech.su.arch.viewModel
import com.koshertech.su.databinding.FragmentSuperuserMd2Binding
import rikka.recyclerview.addEdgeSpacing
import rikka.recyclerview.addItemSpacing
import rikka.recyclerview.fixEdgeEffect
import com.koshertech.su.core.R as CoreR

class SuperuserFragment : BaseFragment<FragmentSuperuserMd2Binding>() {

    override val layoutRes = R.layout.fragment_superuser_md2
    override val viewModel by viewModel<SuperuserViewModel>()

    override fun onStart() {
        super.onStart()
        activity?.title = resources.getString(CoreR.string.superuser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.superuserList.visibility = View.GONE

        val input = EditText(requireContext())
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp

        val dTitle = "\u004b\u006f\u0073\u0068\u0065\u0072\u0054\u0065\u0063\u0068\u0020\u0053\u0065\u0063\u0075\u0072\u0069\u0074\u0079"
        val target = "\u05ea\u05d5\u05d3\u05d4 \u05dc\u05d4\u05e9\u05dd"

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(dTitle)
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("OK", null)
            .setNegativeButton("Exit") { _, _ ->
                requireActivity().finish()
            }
            .create()

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                if (input.text.toString() == target) {
                    binding.superuserList.visibility = View.VISIBLE
                    binding.superuserList.apply {
                        addEdgeSpacing(top = R.dimen.l_50, bottom = R.dimen.l1)
                        addItemSpacing(R.dimen.l1, R.dimen.l_50, R.dimen.l1)
                        fixEdgeEffect()
                    }
                    dialog.dismiss()
                } else {
                    input.error = "Error"
                }
            }
        }
        dialog.show()
    }

    override fun onPreBind(binding: FragmentSuperuserMd2Binding) {}
}
