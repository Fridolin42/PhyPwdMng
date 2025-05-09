package crypto

import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.util.Base64
import javax.crypto.Cipher

class RSA {
    private val keyPair: KeyPair
    private val algorithm = "RSA/ECB/PKCS1PADDING"

    init {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        val secureRandom = SecureRandom()
        keyPairGenerator.initialize(4096, secureRandom)
        this.keyPair = keyPairGenerator.generateKeyPair()
    }

    fun encrypt(plain: String, publicKeyString: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyFromString(publicKeyString))
        return Base64.getEncoder().encodeToString(cipher.doFinal(plain.toByteArray()))
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        return String(cipher.doFinal(Base64.getDecoder().decode(encrypted)))
    }

    fun getPublicKey(): String {
        return Base64.getEncoder().encodeToString(keyPair.public.encoded)
    }

    private fun publicKeyFromString(publicKeyStr: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(publicKeyStr)
        val spec = java.security.spec.X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(spec)
    }

    fun sign(data: String): String {
        val signature = Signature.getInstance("SHA3-256withRSA")
        signature.initSign(keyPair.private)
        signature.update(data.toByteArray())
        return Base64.getEncoder().encodeToString(signature.sign())
    }

    fun verify(data: String, signatureString: String, publicKeyString: String): Boolean {
        val publicKey = publicKeyFromString(publicKeyString)
        val signature = Signature.getInstance("SHA3-256withRSA")
        signature.initVerify(publicKey)
        signature.update(data.toByteArray())
        return signature.verify(Base64.getDecoder().decode(signatureString))
    }
}