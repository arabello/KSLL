package it.matteopellegrino.ksll.model

/**
 * Data class representing the logic entity of a local library.
 *
 * @param path Directory containing all files needed by the library
 * @param version Version of the library. String format choosen by the library provider
 * @param extension File extension of the library. Defines the type and
 * which controller should resolve it
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
data class Lib(val SAPClass: Class<*>,
               val version: String,
               val extension: LibExtension)