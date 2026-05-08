package com.koshertech.su.ui.settings

import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.koshertech.su.arch.BaseViewModel
import com.koshertech.su.core.AppContext
import com.koshertech.su.core.Config
import com.koshertech.su.core.Info
import com.koshertech.su.core.R
import com.koshertech.su.core.ktx.toast
import com.koshertech.su.core.utils.RootUtils
import com.koshertech.su.ui.navigation.Route
import com.koshertech.su.view.Shortcuts
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : BaseViewModel() {

    private val _denyListEnabled = MutableStateFlow(Config.denyList)
    val denyListEnabled: StateFlow<Boolean> = _denyListEnabled.asStateFlow()

    val zygiskMismatch get() = Config.zygisk != Info.isZygiskEnabled

    var authenticate: (onSuccess: () -> Unit) -> Unit = { it() }

    fun navigateToDenyList() {
        navigateTo(Route.DenyList)
    }

    fun requestAddShortcut() {
        Shortcuts.addHomeIcon(AppContext)
    }

    fun createHosts() {
        viewModelScope.launch {
            RootUtils.addSystemlessHosts()
            AppContext.toast(R.string.settings_hosts_toast, Toast.LENGTH_SHORT)
        }
    }

    fun toggleDenyList(enabled: Boolean) {
        _denyListEnabled.value = enabled
        val cmd = if (enabled) "enable" else "disable"
        Shell.cmd("magisk --denylist $cmd").submit { result ->
            if (result.isSuccess) {
                Config.denyList = enabled
            } else {
                _denyListEnabled.value = !enabled
            }
        }
    }

    fun withAuth(action: () -> Unit) = authenticate(action)

    fun notifyZygiskChange() {
        if (zygiskMismatch) showSnackbar(R.string.reboot_apply_change)
    }
}
