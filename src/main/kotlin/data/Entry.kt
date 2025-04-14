package data

import kotlinx.serialization.Serializable

@Serializable
data class Entry(val website: String, val username: String)
