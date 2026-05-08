package com.koshertech.su.ui.log

import com.koshertech.su.R
import com.koshertech.su.databinding.DiffItem
import com.koshertech.su.databinding.ItemWrapper
import com.koshertech.su.databinding.ObservableRvItem

class LogRvItem(
    override val item: String
) : ObservableRvItem(), DiffItem<LogRvItem>, ItemWrapper<String> {
    override val layoutRes = R.layout.item_log_textview
}
