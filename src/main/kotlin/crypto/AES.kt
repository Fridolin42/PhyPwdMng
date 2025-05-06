package crypto

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AES(password: String) {
    val key: SecretKey
    val iv: GCMParameterSpec
    private val algorithm: String = "AES/GCM/NoPadding"

    init {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        this.key = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        this.iv = GCMParameterSpec(128, iv)
    }

    fun encrypt(plain: String): String {
        val cipher = Cipher.getInstance(this.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv)
        return Base64.getEncoder().encodeToString(cipher.doFinal(plain.toByteArray()))
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance(this.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv)
        return String(cipher.doFinal(Base64.getDecoder().decode(encrypted)))
    }
}