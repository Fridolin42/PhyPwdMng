package crypto

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AES(private val password: String) {
    private val algorithm: String = "AES/GCM/NoPadding"

    private fun genKey(salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    private fun genSalt(): ByteArray {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun genIV(): GCMParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return GCMParameterSpec(128, iv)
    }

    fun encrypt(plain: String): String {
        val iv = genIV()
        val salt = genSalt()
        val key = genKey(salt)

        val cipher = Cipher.getInstance(this.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val encryptedBytes = cipher.doFinal(plain.toByteArray())

        val combined = ByteArray(encryptedBytes.size + iv.iv.size + salt.size)
        System.arraycopy(iv.iv, 0, combined, 0, iv.iv.size)
        System.arraycopy(salt, 0, combined, iv.iv.size, salt.size)
        System.arraycopy(encryptedBytes, 0, combined, iv.iv.size + salt.size, encryptedBytes.size)

        return Base64.getEncoder().encodeToString(combined)
    }

    fun decrypt(encrypted: String): String {
        val decoded = Base64.getDecoder().decode(encrypted)
        val ivBytes = decoded.copyOfRange(0, 16)
        val saltBytes = decoded.copyOfRange(16, 32)
        val cipherBytes = decoded.copyOfRange(32, decoded.size)

        val cipher = Cipher.getInstance(this.algorithm)
        val key = genKey(saltBytes)

        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, ivBytes))
        return String(cipher.doFinal(cipherBytes))
    }
}