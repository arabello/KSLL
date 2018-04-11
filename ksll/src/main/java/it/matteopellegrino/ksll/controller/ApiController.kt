package it.matteopellegrino.ksll.controller

import it.matteopellegrino.ksll.model.RemoteLib
import java.net.URL

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
interface ApiController {

    /**
     * Async http request the [url], elaborate the response
     * and invoke [success] or [failure]
     *
     * @param url The URL at which a web server is ready to respond (with a KSLA meta inf)
     * @param success Callback function on successful response
     * @param failure Callback function on error
     */
    fun retrieveAvailableAPI(url: URL, success: (remoteLibs: List<RemoteLib>) -> Unit = {}, failure: () -> Unit = {})
}