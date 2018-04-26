package it.matteopellegrino.ksll.controller

import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.RemoteLib

/**
 * Define the API for a generic controller that resolve [RemoteLib]s
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal interface Controller {

    /**
     * Try to [Controller.find] a library locally.
     * If not available try to [Controller.download] it.
     *
     * The [success] callback works for both case, [failure] as well.
     *
     * @param remoteLib Containing all info to accomplish the [download] or [find]
     * @param success Callback function with a [Class] param for the sap class from the retrieved lib
     * @param failure Callback for errors
     */
    fun retrieve(remoteLib: RemoteLib, success: (lib: Lib) -> Unit = {}, failure: (cause: Failure) -> Unit = {})

    /**
     * Async download a file from [RemoteLib.url], store it and
     * return the corresponding [Lib] representation
     *
     * @param remoteLib Containing all info to accomplish the download and the SAP class loading
     * @param success Callback function with a [Lib] param for the downloaded lib
     * @param failure Callback for errors
     */
    fun download(remoteLib: RemoteLib, success: (lib: Lib) -> Unit = {}, failure: (cause: Failure) -> Unit = {})

    /**
     * Search in the local storage if a [Lib]
     * corresponding to the remote one is available
     *
     * @param remoteLib The remote lib that should is stored locally
     * @return A [Lib] representation or null if [remoteLib] is not available locally
     */
    fun find(remoteLib: RemoteLib) : Lib?

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

    /**
     * Execute [wipe] for each stored library
     */
    fun wipeAll()

    /**
     * Get all libraries available locally from the storage
     * @return a list composed by each [Lib] saved in the storage
     */
    fun availableLibs(): List<Lib>
}