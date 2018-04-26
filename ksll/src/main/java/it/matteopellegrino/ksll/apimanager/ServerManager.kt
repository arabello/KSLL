package it.matteopellegrino.ksll.apimanager

import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.model.RemoteLib
import java.net.URL

/**
 * An implementation of this interface should work for a specific
 * type of web server that provides an API about libraries that
 * can be downloaded. The implementation is based on a
 * non written contract, such as the structure and the content of the response
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
interface ServerManager {

    /**
     * Async http request the [url], elaborate the response
     * and invoke [success] or [failure]
     *
     * @param url The URL at which a web server is ready to respond (with a KSLA meta inf)
     * @param success Callback function on successful response
     * @param failure Callback function on error
     */
    fun retrieveAvailableAPI(url: URL, success: (remoteLibs: List<RemoteLib>) -> Unit = {}, failure: (cause: Failure) -> Unit = {})
}