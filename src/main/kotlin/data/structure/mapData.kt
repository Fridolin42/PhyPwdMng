package data.structure

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

fun map(sRootFolder: SerializableFolder): Folder {
    val children = mutableStateListOf<Folder>()
    val entries = mutableStateListOf<Entry>()
    val root = Folder(mutableStateOf(sRootFolder.name), children, entries)
    sRootFolder.children.forEach { children.add(map(it)) }
    sRootFolder.entries.forEach { entries.add(Entry(mutableStateOf(it.website), mutableStateOf(it.username), it.id)) }
    return root
}