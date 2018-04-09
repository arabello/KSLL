package it.matteopellegrino.ksll.controller

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import dalvik.system.DexClassLoader
import it.matteopellegrino.ksll.model.Lib
import java.io.File
import java.net.URL

/**
 * Implementation of [LibController] specifically for dex libraries
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal class DexLibController(context: Context) : AbstractLibController(context) {
    override val LIB_DIRNAME: String = "dexlib"
    override val DEFAULT_LIB_FILENAME: String = "ksla.dex"
    override val DEFAULT_SAP_FILENAME: String = "sapclassname"
    private val dexDir = File(context.cacheDir, "dex")

    init{
        dexDir.mkdir()
    }

    override fun loadSAP(libFile: File, sapFile: File): Class<*>? =
        if (!libFile.exists() || !sapFile.exists())
            null
        else
             DexClassLoader(libFile.absolutePath, dexDir.absolutePath, null, javaClass.classLoader)
                    .loadClass(sapFile.readText())

}