package it.matteopellegrino.ksll

import android.content.Context
import it.matteopellegrino.ksll.apimanager.ServerManager
import it.matteopellegrino.ksll.controller.LibController
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.RemoteLib
import java.net.MalformedURLException
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
    private val ctrl = LibController(context)

    fun availableLibs(): List<RemoteLib> = ctrl.availableLibs()

    /**
     * Retrieve all libraries exposed at [url]. If [shouldUpdate] is true, then a version checking will performed
     * and each library may be updated.
     * ATTENTION the callbacks function are invoked for each library found
     *
     * @param url A web url that expose the API for this module
     * @param shouldUpdate True to update each library if necessary, default is true
     * @param success Callback for successful result providing the [Lib] loaded as param. May be multiple invoked
     * @param failure Callback for failed result. May be multiple invoked
     */
    fun multipleLoad(url: URL,
                     success: (lib: Lib) -> Unit,
                     failure: (cause: Failure) -> Unit,
                     shouldUpdate: Boolean = true){
        manager.retrieveAvailableAPI(url, {remoteLibs ->
            remoteLibs.forEach {remoteLib ->
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
     * Retrieve all libraries exposed at [url]. If [shouldUpdate] is true, then a version checking will performed
     * and each library may be updated.
     * ATTENTION the callbacks function are invoked for each library found
     *
     * @param url A web url that expose the API for this module
     * @param shouldUpdate True to update each library if necessary, default is true
     * @param success Callback for successful result providing the [Lib] loaded as param. May be multiple invoked
     * @param failure Callback for failed result. May be multiple invoked
     */
    fun multipleLoad(url: String,
                     success: (lib: Lib) -> Unit,
                     failure: (cause: Failure) -> Unit,
                     shouldUpdate: Boolean = true) = multipleLoad(URL(url), success, failure, shouldUpdate)

    /**
     * Retrieve a library exposed at [url]. If [shouldUpdate] is true, then a version checking will performed
     * and the library may be updated.
     * ATTENTION If multiple libraries are found at the given url ONLY the first will be considered
     *
     * @param url A web url that expose the API for this module
     * @param shouldUpdate True to update the library if necessary, default is true
     * @param success Callback for successful result providing the [Lib] loaded as param
     * @param failure Callback for failed result
     */
    fun load(url: URL,
             success: (lib: Lib) -> Unit,
             failure: (cause: Failure) -> Unit,
             shouldUpdate: Boolean = true){
        manager.retrieveAvailableAPI(url, {remoteLibs ->
            val remoteLib = remoteLibs.first()
            ctrl.retrieve(remoteLib, { lib ->
                if (shouldUpdate && lib.version != remoteLib.version)
                    ctrl.download(remoteLib, success, failure)
                else
                    success(lib)
            }, failure)
        }, failure)
    }

    /**
     * Retrieve a library exposed at [url]. If [shouldUpdate] is true, then a version checking will performed
     * and the library may be updated.
     * ATTENTION If multiple libraries are found at the given url ONLY the first will be considered
     *
     * @param url A web url that expose the API for this module
     * @param shouldUpdate True to update the library if necessary, default is true
     * @param success Callback for successful result providing the [Lib] loaded as param
     * @param failure Callback for failed result
     */
    fun load(url: String,
             success: (lib: Lib) -> Unit,
             failure: (cause: Failure) -> Unit,
             shouldUpdate: Boolean = true) = load(URL(url), success, failure, shouldUpdate)

    /**
     * Search a library identified by [url] into the storage.
     * @param url The unique identifier of the library
     * @return A [Lib] instance if the library exists, otherwise null
     */
    fun localLoad(url: URL): Lib?{
        ctrl.availableLibs().forEach {
            if (it.url == url)
                return ctrl.find(it)
        }
        return null
    }

    /**
     * Invoke [localLoad] converting String [url] to URL type
     */
    fun localLoad(urlString: String): Lib?{
        try {
            val url = URL(urlString)
            ctrl.availableLibs().forEach {
                if (it.url == url)
                    return ctrl.find(it)
            }
            return null
        }catch (e: MalformedURLException){
            return null
        }
    }
}

fun String.localLoad(instance: Ksll) = instance.localLoad(this)

fun URL.localLoad(instance: Ksll) = instance.localLoad(this)

fun String.load(instance: Ksll, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit, shouldUpdate: Boolean = true) =
        instance.load(this, success, failure, shouldUpdate)

fun URL.load(instance: Ksll, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit, shouldUpdate: Boolean = true) =
        instance.load(this, success, failure, shouldUpdate)

fun String.multipleLoad(instance: Ksll, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit, shouldUpdate: Boolean = true) =
        instance.multipleLoad(this, success, failure, shouldUpdate)

fun URL.multipleLoad(instance: Ksll, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit, shouldUpdate: Boolean = true) =
        instance.multipleLoad(this, success, failure, shouldUpdate)
