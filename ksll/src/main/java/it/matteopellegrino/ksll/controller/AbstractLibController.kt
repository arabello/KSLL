package it.matteopellegrino.ksll.controller

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.LibExtension
import it.matteopellegrino.ksll.model.RemoteLib
import java.io.File
import java.net.URL

/**
 * This abstract controller implement all methods common
 * between different implementation of [LibController].
 * An implementation should extends this class and provide
 * all non implemented methods and property.
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal abstract class AbstractLibController(context: Context) : LibController{
    final override val BASE_LIB_DIRNAME: String = context.filesDir.absolutePath + File.separator + "ksll"
    val DEFAULT_SAP_FILENAME: String = "sapclassname"

    private fun resolveCtrlLibDir(): File = File(BASE_LIB_DIRNAME, LIB_EXTENSION.toString())

    private fun URL.toDirPath(): String = protocol + File.separator + host + File.separator + port + File.separator + path

    private fun resolveLibFile(remoteLib: RemoteLib): File = File(File(resolveCtrlLibDir(), remoteLib.url.toDirPath()), "${remoteLib.version}.${remoteLib.extension}")
    private fun resolveSapFile(url: URL): File = File(File(resolveCtrlLibDir(), url.toDirPath()), DEFAULT_SAP_FILENAME)

    abstract fun loadSAP(libFile: File, sapFile: File): Class<*>?

    final override fun download(remoteLib: RemoteLib, success: (lib: Lib) -> Unit, failure: () -> Unit) {
        remoteLib.url.toString().httpGet().response{ _, response, result ->
            Log.d(javaClass.simpleName, "Response ready: ${result.component2()}" )
            when(result){
                is Result.Failure -> {
                    Log.e(javaClass.simpleName, response.responseMessage)
                    failure()
                }
                is Result.Success -> {
                    val libFile = resolveLibFile(remoteLib)
                    Log.d(javaClass.simpleName, "Creating '$libFile'")
                    libFile.parentFile.mkdirs()
                    libFile.writeBytes(result.get())
                    val sapFile = resolveSapFile(remoteLib.url)
                    Log.d(javaClass.simpleName, "Creating '$sapFile'")
                    sapFile.printWriter().use { it.print(remoteLib.SAPClassName) }


                    val sap = loadSAP(libFile, sapFile)
                    if (sap == null)
                        failure()
                    else
                        success(Lib(libFile, sap, remoteLib.version, remoteLib.extension))
                }
            }
        }
    }

    final override fun find(remoteLib: RemoteLib): Lib? {
        val file = resolveLibFile(remoteLib)
        if (!file.exists()) return null
        val sapFile = resolveSapFile(remoteLib.url)
        if (!sapFile.exists()) return null

        val sap = loadSAP(file, sapFile)
        val ext = LibExtension.from(file.extension)
        return if (sap == null || ext == null) null else Lib(file, sap, file.nameWithoutExtension, ext)
    }

    final override fun retrieve(remoteLib: RemoteLib, success: (lib: Lib) -> Unit, failure: () -> Unit) {
        val lib = find(remoteLib)
        if (lib == null) {
            Log.d(javaClass.simpleName, "Lib $remoteLib doest not exist locally. Downloading..." )
            download(remoteLib, success, failure)
        }else {
            Log.d(javaClass.simpleName, "Lib $remoteLib ready locally. Loading..." )
            success(lib)
        }
    }

    final override fun wipe(existingLib: Lib): Boolean {
        if (!existingLib.file.exists()) return false
        existingLib.file.parentFile.listFiles().forEach { it.delete() }
        return existingLib.file.parentFile.delete()
    }

    final override fun wipe(remoteLib: RemoteLib): Boolean {
        val lib = find(remoteLib)
        return if (lib == null) false else wipe(lib)
    }
}