package data

import kotlinx.serialization.Serializable

@Serializable
data class Folder(val name: String, val children: List<Folder>, val entries: List<Entry>)
