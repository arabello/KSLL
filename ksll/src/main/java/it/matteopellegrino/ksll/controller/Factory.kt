package it.matteopellegrino.ksll.controller

import android.content.Context
import it.matteopellegrino.ksll.model.LibExtension

/**
 * Factory class for the package [it.matteopellegrino.ksll.controller]
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal class Factory(private val context: Context) {

    /**
     * Find the correct [LibController] based on [extension] of the library
     * you want to resolve
     *
     * @param extension A file extension without the dot
     */
    fun forExtension(extension: String): LibController?{
        return try{
            val ext = LibExtension.valueOf(extension)

            when(ext){
                LibExtension.DEX -> DexLibController(context)
            }
        }catch (e: IllegalArgumentException){
            null
        }
    }
}