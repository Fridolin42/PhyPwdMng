package data.structure

import androidx.compose.runtime.MutableState

data class Entry(var website: MutableState<String>, var username: MutableState<String>, val id: Long)
