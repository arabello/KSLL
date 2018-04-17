package it.matteopellegrino.ksll.controller

import android.content.Context
import dalvik.system.DexClassLoader
import it.matteopellegrino.ksll.model.LibExtension
import it.matteopellegrino.ksll.model.LibExtension.*
import java.io.File

/**
 * Implementation of [LibController] specifically for dex library.
 * This is the default controller because Android only supports dex files,
 * instead of pure java .class. The library can be provided with different extension
 * specified by [DexController.SUPPORTED_EXTENSIONS]
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal class DexController(context: Context) : AbstractController(context) {
    override val SUPPORTED_EXTENSIONS: List<LibExtension> = listOf(JAR, DEX, ZIP, APK)
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