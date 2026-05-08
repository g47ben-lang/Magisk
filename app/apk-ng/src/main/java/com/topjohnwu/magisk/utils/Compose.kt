package com.koshertech.su.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalResources
import com.koshertech.su.core.utils.TextHolder

@Composable
fun textHolder(holder: TextHolder) = holder.getText(LocalResources.current)
