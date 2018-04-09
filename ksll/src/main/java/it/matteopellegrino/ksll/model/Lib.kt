package it.matteopellegrino.ksll.model

import java.io.File

/**
 * Data class representing the logic entity of a local library.
 * An instance of this class consists of a file or path
 * and a Service Access Point class name.
 *
 * @param file File or path containing the library. May not exist on instance time
 * @param SAPClassName The java class name existing in the library used to access all
 * services of the library
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal data class Lib(val file: File, val SAPClassName: String)