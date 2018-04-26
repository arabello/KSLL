package it.matteopellegrino.ksll.model

import java.io.File

/**
 * Data class representing the logic entity of a local library.
 *
 * @param file File or path containing the library available form the internal storage
 * @param SAPClass The java class existing in the library used to access retrieves all exposed
 * services of the corresponding library
 * @param version Version of the library. String format choosen by the library provider
 * @param extension File extension of the library. Defines the type and
 * which controller should resolve it
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
data class Lib(val file: File, val SAPClass: Class<*>, val version: String, val extension: LibExtension){
    override fun equals(other: Any?): Boolean {
        if (!super.equals(other))
            return false
        val otherLib = other as Lib
        if (otherLib.file != file)
            return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}