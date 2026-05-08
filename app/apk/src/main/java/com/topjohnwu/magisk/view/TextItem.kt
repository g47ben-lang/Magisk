package com.koshertech.su.view

import com.koshertech.su.R
import com.koshertech.su.databinding.DiffItem
import com.koshertech.su.databinding.ItemWrapper
import com.koshertech.su.databinding.RvItem

class TextItem(override val item: Int) : RvItem(), DiffItem<TextItem>, ItemWrapper<Int> {
    override val layoutRes = R.layout.item_text
}
