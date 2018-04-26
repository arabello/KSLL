package it.matteopellegrino.ksll.security

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import kotlin.math.sign


/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
object Security {
    private val KEY_FACTORY_ALGORITHM = "RSA"
    private val SIGNATURE_ALGORITHM = "SHA512withRSA"

    /**
     * Verifies that the data was signed with the given signature.
     * The signedData is the content of the library as text.
     * @param base64PublicKey the base64-encoded public key to use for verifying.
     * @param signedData the signed text string (signed, not encrypted)
     * @param signature the signature for the data, signed with the private key
     */
    fun verifySignature(base64PublicKey: String, signedData: ByteArray, signature: String): Boolean{
        if (signedData.isEmpty() || TextUtils.isEmpty(base64PublicKey) || TextUtils.isEmpty(signature)) {
            Log.e(javaClass.simpleName, "Signature verification failed: missing data.")
            return false
        }

        val key = Security.generatePublicKey(base64PublicKey)
        return Security.verify(key, signedData, signature)
    }

    /**
     * Generates a PublicKey instance from a string containing the
     * Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    private fun generatePublicKey(encodedPublicKey: String): PublicKey {
        try {
            val decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
            return keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            Log.e(javaClass.simpleName, "Invalid key specification.")
            throw IllegalArgumentException(e)
        }
    }

    /**
     * Verifies that the signature from the server matches the computed
     * signature on the data.  Returns true if the data is correctly signed.
     *
     * @param publicKey public key associated with the developer account
     * @param signedData signed data from server
     * @param signature server signature
     * @return true if the data and signature match
     */
    private fun verify(publicKey: PublicKey, signedData: ByteArray, signature: String): Boolean {
        val signatureBytes: ByteArray
        try {
            signatureBytes = Base64.decode(signature, Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            Log.e(javaClass.simpleName, "Base64 decoding failed.")
            return false
        }

        try {
            val sig = Signature.getInstance(SIGNATURE_ALGORITHM)
            sig.initVerify(publicKey)
            sig.update(signedData)
            if (!sig.verify(signatureBytes)) {
                Log.e(javaClass.simpleName, "Signature verification failed.")
                return false
            }
            return true
        } catch (e: NoSuchAlgorithmException) {
            Log.e(javaClass.simpleName, "NoSuchAlgorithmException.")
        } catch (e: InvalidKeyException) {
            Log.e(javaClass.simpleName, "Invalid key specification.")
        } catch (e: SignatureException) {
            Log.e(javaClass.simpleName, "Signature exception.")
        }

        return false
    }
}