package it.matteopellegrino.ksll.model

import java.net.URL

/**
 * Data class representing the logic entity of a remote library.
 *
 * @param url Location where the library is stored and available
 * @param SAPClassName Name of the class existing into the corresponding library,
 * used as Service Access Point from the API exposed by the library
 * @param version Version of the library. String format choosen by the library provider
 * @param extension File extension of the library. Defines the type and
 * which controller should resolve it
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
data class RemoteLib(val url: URL, val SAPClassName: String, val version: String, val extension: String)