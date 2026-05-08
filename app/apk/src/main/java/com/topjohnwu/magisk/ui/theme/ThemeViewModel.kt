package com.koshertech.su.ui.theme

import com.koshertech.su.arch.BaseViewModel
import com.koshertech.su.core.Config
import com.koshertech.su.dialog.DarkThemeDialog
import com.koshertech.su.events.RecreateEvent
import com.koshertech.su.view.TappableHeadlineItem

class ThemeViewModel : BaseViewModel(), TappableHeadlineItem.Listener {

    val themeHeadline = TappableHeadlineItem.ThemeMode

    override fun onItemPressed(item: TappableHeadlineItem) = when (item) {
        is TappableHeadlineItem.ThemeMode -> DarkThemeDialog().show()
    }

    fun saveTheme(theme: Theme) {
        if (!theme.isSelected) {
            Config.themeOrdinal = theme.ordinal
            RecreateEvent().publish()
        }
    }
}
