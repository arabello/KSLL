package it.matteopellegrino.ksll.controller

import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.RemoteLib
import java.net.URL

/**
 * Define the API for a generic controller that manages [Lib]
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
interface LibController {
    /**
     * Parent path containing all [Lib] currently available locally
     */
    val BASE_LIB_DIRNAME: String

    /**
     * Async download a file from [RemoteLib.url], store it and
     * return the corresponding [Lib] representation
     *
     * @param remoteLib Containing all info to accomplish the download and the SAP class loading
     * @param success Callback function with a [Lib] param for the downloaded lib
     * @param failure Callback for errors
     */
    fun download(remoteLib: RemoteLib, success: (lib: Lib) -> Unit = {}, failure: () -> Unit = {})

    /**
     * Search in the local storage if a [Lib]
     * corresponding to the remote one is available
     *
     * @param remoteLib The remote lib that should is stored locally
     * @return A [Lib] representation or null if [remoteLib] is not available locally
     */
    fun find(remoteLib: RemoteLib) : Lib?

    /**
     * Try to [LibController.find] a library locally.
     * If not available try to [LibController.download] it.
     *
     * The [success] callback works for both case, [failure] as well.
     *
     * @param remoteLib Containing all info to accomplish the [download] or [find]
     * @param success Callback function with a [Class] param for the sap class from the retrieved lib
     * @param failure Callback for errors
     */
    fun retrieve(remoteLib: RemoteLib, success: (lib: Lib) -> Unit = {}, failure: () -> Unit = {})

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
     * Execute [wipe] converting the given [RemoteLib] to the local representation [Lib]
     */
    fun wipe(remoteLib: RemoteLib): Boolean
}