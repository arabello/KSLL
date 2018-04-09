package it.matteopellegrino.ksll.model

import java.net.URL

/**
 * Data class representing the logic entity of a remote library.
 * An instance of this class consists of a valid URL from which
 * is possible to download the library file and a Service Access Point (SAP) class name.
 *
 * @param url Location where the library is stored and available
 * @param SAPClassName Name of the class existing into the corresponding library,
 * used as Service Access Point from the API exposed by the library
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
data class RemoteLib(val url: URL, val SAPClassName: String)