package it.matteopellegrino.ksll.controller

import android.content.Context
import dalvik.system.DexClassLoader
import it.matteopellegrino.ksll.model.LibExtension
import java.io.File

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal class JarLibController(context: Context) : AbstractLibController(context){
    override val LIB_EXTENSION: LibExtension = LibExtension.JAR
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