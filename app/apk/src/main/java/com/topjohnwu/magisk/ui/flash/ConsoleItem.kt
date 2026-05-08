package com.koshertech.su.ui.flash

import com.koshertech.su.R
import com.koshertech.su.databinding.DiffItem
import com.koshertech.su.databinding.ItemWrapper
import com.koshertech.su.databinding.RvItem

class ConsoleItem(
    override val item: String
) : RvItem(), DiffItem<ConsoleItem>, ItemWrapper<String> {
    override val layoutRes = R.layout.item_console_md2
}
