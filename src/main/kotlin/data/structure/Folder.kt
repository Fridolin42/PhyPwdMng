package data.structure

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Folder(val name: MutableState<String>, val children: SnapshotStateList<Folder>, val entries: SnapshotStateList<Entry>)
