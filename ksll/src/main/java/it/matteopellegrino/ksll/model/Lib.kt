package it.matteopellegrino.ksll.model

import java.io.File

/**
 * Data class representing the logic entity of a local library.
 * An instance of this class consists of a file or path
 * and a Service Access Point (SAP) Class
 *
 * @param file File or path containing the library available form the internal storage
 * @param SAPClass The java class existing in the library used to access retrieves all exposed
 * services of the corresponding library
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal data class Lib(val file: File, val SAPClass: Class<*>)