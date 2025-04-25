package data.structure

import kotlinx.serialization.Serializable

@Serializable
data class SerializableFolder(val name: String, val children: List<SerializableFolder>, val entries: MutableList<SerializableEntry>)
