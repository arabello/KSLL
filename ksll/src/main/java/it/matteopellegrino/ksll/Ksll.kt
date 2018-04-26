package it.matteopellegrino.ksll

import android.content.Context
import it.matteopellegrino.ksll.apimanager.ServerManager
import it.matteopellegrino.ksll.controller.LibController
import it.matteopellegrino.ksll.model.Lib
import java.net.URL

/**
 * Top level class entry point. A instance of this class expose the functionality of all
 * Kotlin Safety Library Loader module. The [manager] is an implementation of [ServerManager]
 * used to dialog with the server. For classic usage you can use default implementation
 * provided by [it.matteopellegrino.ksll.api] package.
 * You can provide your own implementation, according to the server you are working with.
 *
 * @param context Android [Context] from which the class is instanced
 * @param manager To manages the interaction with a server
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class Ksll(private val context: Context, private val manager: ServerManager){
    /**
     * Retrieve all libraries exposed at [url]. If [shouldUpdate] is true, then a version checking will performed
     * and each library may be updated.
     * ATTENTION the callbacks function are invoked for each library found
     *
     * @param url A web url that expose the API for this module
     * @param shouldUpdate True to update each library if necessary, default is false
     * @param success Callback for successful result providing the [Lib] loaded as param. May be multiple invoked
     * @param failure Callback for failed result. May be multiple invoked
     */
    fun multipleLoad(url: URL, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit, shouldUpdate: Boolean = false){
        manager.retrieveAvailableAPI(url, {remoteLibs ->
            remoteLibs.forEach {remoteLib ->
                val ctrl = LibController(context)
                ctrl.retrieve(remoteLib, {lib ->
                    if (shouldUpdate && lib.version != remoteLib.version)
                        ctrl.download(remoteLib, success, failure)
                    else
                        success(lib)
                }, failure)
            }
        }, failure)
    }

    /**
     * Retrieve a library exposed at [url]. If [shouldUpdate] is true, then a version checking will performed
     * and the library may be updated.
     * ATTENTION If multiple libraries are found at the given url ONLY the first will be considered
     *
     * @param url A web url that expose the API for this module
     * @param shouldUpdate True to update the library if necessary, default is false
     * @param success Callback for successful result providing the [Lib] loaded as param
     * @param failure Callback for failed result
     */
    fun load(url: URL, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit, shouldUpdate: Boolean = false){
        manager.retrieveAvailableAPI(url, {remoteLibs ->
            val remoteLib = remoteLibs.first()
            val ctrl = LibController(context)
            ctrl.retrieve(remoteLib, { lib ->
                if (shouldUpdate && lib.file.nameWithoutExtension != remoteLib.version)
                    ctrl.download(remoteLib, success, failure)
                else
                    success(lib)
            }, failure)
        }, failure)
    }
}