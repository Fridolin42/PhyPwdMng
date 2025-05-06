package crypto

import io.ktor.util.encodeBase64
import java.security.MessageDigest

private val sha3 = MessageDigest.getInstance("SHA3-256")

fun sha256(text: String): String {
    return sha3.digest(text.toByteArray()).encodeBase64()
}