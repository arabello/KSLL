package it.matteopellegrino.ksll.controller

import it.matteopellegrino.ksll.model.Lib
import java.net.URL

/**
 * Define the API for a generic controller that manages [Lib]
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal interface LibController {
    /**
     * Parent path containing all [Lib] currently available locally
     */
    val BASE_LIB_DIRNAME: String

    /**
     * Async download a file from [url], store it and
     * return the corresponding [Lib] representation
     *
     * @param url Over which the http is elaborated
     * @param SAPClassName Used to instance [Lib],
     * should be valid for the application context
     * @param success Callback function with a [Lib] param for the downloaded lib
     * @param failure Callback for errors
     */
    fun download(url: URL, SAPClassName: String, success: (lib: Lib) -> Unit = {}, failure: () -> Unit = {})

    /**
     * Search into local storage for a [Lib] that should be
     * available remotely by [url]
     *
     * @param url Used to match the [Lib.file] path
     */
    fun find(url: URL) : Lib?

    /**
     * Try to [LibController.find] a library locally.
     * If not available try to [LibController.download] it.
     *
     * The [success] callback works for both case, [failure] as well.
     *
     * @param url Used to match the [Lib.file] path
     * @param SAPClassName Used to instance [Lib] should be valid for the application context
     * @param success Callback function with a [Class] param for the sap class from the retrieved lib
     * @param failure Callback for errors
     */
    fun retrieve(url: URL, SAPClassName: String, success: (lib: Lib) -> Unit = {}, failure: () -> Unit = {})

    /**
     * Delete all files and directories concerned by
     * the given [existingLib]
     *
     * @param existingLib A lib to be deleted, if it doesn't exist
     * this action has no effect
     * @return The outcome of the operation as boolean
     */
    fun wipe(existingLib: Lib): Boolean

    /**
     * Search from the local storage the [Lib.file],
     * if it exits try to load the SAP class using [Lib.SAPClassName].
     * The strategy used depends by the controller implementation,
     * in order to provide different type of libs such as dex or jar.
     *
     * @param existingLib The [Lib] you want to load
     * @return Whatever type of class loaded, null if an error occurred
     */
    fun load(existingLib: Lib): Class<*>?
}