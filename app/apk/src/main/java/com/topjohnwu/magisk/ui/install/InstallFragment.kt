package com.koshertech.su.ui.install

import com.koshertech.su.R
import com.koshertech.su.arch.BaseFragment
import com.koshertech.su.arch.viewModel
import com.koshertech.su.databinding.FragmentInstallMd2Binding
import com.koshertech.su.core.R as CoreR

class InstallFragment : BaseFragment<FragmentInstallMd2Binding>() {

    override val layoutRes = R.layout.fragment_install_md2
    override val viewModel by viewModel<InstallViewModel>()

    override fun onStart() {
        super.onStart()
        requireActivity().setTitle(CoreR.string.install)
    }
}
