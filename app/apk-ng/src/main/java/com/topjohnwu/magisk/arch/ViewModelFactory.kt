package com.koshertech.su.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.koshertech.su.core.di.ServiceLocator
import com.koshertech.su.ui.home.HomeViewModel
import com.koshertech.su.ui.install.InstallViewModel
import com.koshertech.su.ui.log.LogViewModel
import com.koshertech.su.ui.superuser.SuperuserViewModel
import com.koshertech.su.ui.surequest.SuRequestViewModel

object VMFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(ServiceLocator.networkService)
            LogViewModel::class.java -> LogViewModel(ServiceLocator.logRepo)
            SuperuserViewModel::class.java -> SuperuserViewModel(ServiceLocator.policyDB)
            InstallViewModel::class.java ->
                InstallViewModel(ServiceLocator.networkService)
            SuRequestViewModel::class.java ->
                SuRequestViewModel(ServiceLocator.policyDB, ServiceLocator.timeoutPrefs)
            else -> modelClass.newInstance()
        } as T
    }
}
