package crypto

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.SecureRandom
import javax.crypto.Cipher

class RSA(password: String) {
    val keyPair: KeyPair
    val algorithm = "RSA/ECB/PKCS1PADDING"

    init {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        val secureRandom = SecureRandom()
        keyPairGenerator.initialize(4096, secureRandom)
        this.keyPair = keyPairGenerator.generateKeyPair()
    }

    fun encrypt(plain: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return String(cipher.doFinal(plain.toByteArray()))
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        return String(cipher.doFinal(encrypted.toByteArray()))
    }
}