package it.matteopellegrino.ksll.controller

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import dalvik.system.DexClassLoader
import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.LibExtension
import it.matteopellegrino.ksll.model.RemoteLib
import it.matteopellegrino.ksll.security.Security
import java.io.File

/**
 * TODO
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal class LibController(context: Context) : Controller{
    val storage = StorageHelper(context)

    override fun download(remoteLib: RemoteLib, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit) {
        remoteLib.url.toString().httpGet().response{ _, response, result ->
            Log.d(javaClass.simpleName, "Response ready: ${result.component2()}" )
            when(result){
                is Result.Failure -> {
                    Log.e(javaClass.simpleName, response.responseMessage)
                    failure(Failure.HTTPRequestError)
                }
                is Result.Success -> {
                    if (!Security.verifySignature(remoteLib.publicKey, result.get(), remoteLib.signature)) {
                        failure(Failure.NotTrustedData)
                        return@response
                    }

                    val (libFile, sapFile) = storage.create(remoteLib, result.get())

                    val sap = loadSAP(libFile, sapFile)
                    if (sap == null)
                        failure(Failure.CannotLoadSAPClass)
                    else
                        success(Lib(libFile, sap, remoteLib.version, remoteLib.extension))
                }
            }
        }
    }

    override fun find(remoteLib: RemoteLib): Lib? {
        val file = storage.resolveLibFile(remoteLib)
        if (!file.exists()) return null
        val sapFile = storage.resolveSapFile(remoteLib.url)
        if (!sapFile.exists()) return null

        val sap = loadSAP(file, sapFile)
        val ext = LibExtension.from(file.extension)
        return if (sap == null || ext == null) null else Lib(file, sap, file.nameWithoutExtension, ext)
    }

    override fun retrieve(remoteLib: RemoteLib, success: (lib: Lib) -> Unit, failure: (cause: Failure) -> Unit) {
        val lib = find(remoteLib)
        if (lib == null) {
            Log.d(javaClass.simpleName, "Lib $remoteLib doest not exist locally. Downloading..." )
            download(remoteLib, success, failure)
        }else {
            Log.d(javaClass.simpleName, "Lib $remoteLib ready locally. Loading..." )
            success(lib)
        }
    }

    fun loadSAP(libFile: File, sapFile: File): Class<*>? = if (!libFile.exists() || !sapFile.exists())
                null
            else
                DexClassLoader(libFile.absolutePath, storage.dexDir.absolutePath, null, javaClass.classLoader)
                        .loadClass(sapFile.readText())

    override fun wipe(existingLib: Lib): Boolean = storage.delete(existingLib)

    override fun wipe(remoteLib: RemoteLib): Boolean {
        val lib = find(remoteLib) ?: return false
        return wipe(lib)
    }

    override fun wipeAll() = storage.availableLibs().forEach { wipe(it) }

    override fun availableLibs(): List<Lib> = storage.availableLibs()
}