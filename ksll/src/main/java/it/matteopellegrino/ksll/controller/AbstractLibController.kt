package it.matteopellegrino.ksll.controller

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import it.matteopellegrino.ksll.model.Lib
import java.io.File
import java.net.URL

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal abstract class AbstractLibController(context: Context) : LibController{
    final override val BASE_LIB_DIRNAME: String = context.filesDir.absolutePath + File.separator + "ksll"
    abstract val LIB_DIRNAME: String
    abstract val DEFAULT_LIB_FILENAME: String
    abstract val DEFAULT_SAP_FILENAME: String

    private fun resolveCtrlLibDir(): File = File(BASE_LIB_DIRNAME, LIB_DIRNAME)

    private fun URL.toDirPath(): String = protocol + File.separator + host + File.separator + port + File.separator + path

    private fun resolveLibFile(url: URL): File = File(File(resolveCtrlLibDir(), url.toDirPath()), DEFAULT_LIB_FILENAME)
    private fun resolveSapFile(url: URL): File = File(File(resolveCtrlLibDir(), url.toDirPath()), DEFAULT_SAP_FILENAME)

    final override fun download(url: URL, SAPClassName: String, success: (lib: Lib) -> Unit, failure: () -> Unit) {

        url.toString().httpGet().response{ _, response, result ->
            Log.d(javaClass.name, "Response ready: ${result.component2()}" )
            when(result){
                is Result.Failure -> {
                    Log.e(javaClass.name, response.responseMessage)
                    failure()
                }
                is Result.Success -> {
                    val libFile = resolveLibFile(url)
                    Log.d(javaClass.name, "Creating '$libFile'")
                    libFile.parentFile.mkdirs()
                    libFile.writeBytes(result.get())
                    val sapFile = resolveSapFile(url)
                    Log.d(javaClass.name, "Creating '$sapFile'")
                    sapFile.printWriter().use { it.print(SAPClassName) }
                    success(Lib(libFile, SAPClassName))
                }
            }
        }
    }

    final override fun find(url: URL): Lib? {
        val file = resolveLibFile(url)
        if (!file.exists()) return null
        val sap = resolveSapFile(url)
        if (!sap.exists()) return null
        return Lib(file, sap.readText())
    }

    final override fun retrieve(url: URL, SAPClassName: String, success: (lib: Lib) -> Unit, failure: () -> Unit) {
        val lib = find(url)
        if (lib == null) {
            Log.d(javaClass.name, "Lib $url doest not exist locally. Downloading..." )
            download(url, SAPClassName, success, failure)
        }else {
            Log.d(javaClass.name, "Lib $url ready locally. Loading..." )
            success(lib)
        }
    }

    final override fun wipe(existingLib: Lib): Boolean {
        if (!existingLib.file.exists()) return false
        existingLib.file.parentFile.listFiles().forEach { it.delete() }
        return existingLib.file.parentFile.delete()
    }
}