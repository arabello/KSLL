package it.matteopellegrino.ksll

import android.content.Context
import it.matteopellegrino.ksll.controller.DexLibController
import java.lang.reflect.Method
import java.net.URL

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
object Ksll{
    fun from(context: Context): Manager = Manager(context)

    enum class LIBTYPE{
        DEX
    }

    class Manager(context: Context){
        private val dexLibController = DexLibController(context)

        fun availableAPI(url: URL): Array<Method> {
            val lib = dexLibController.find(url)
            return if (lib == null)
                arrayOf()
            else{
                val methods = dexLibController.load(lib)
                return if (methods == null) arrayOf() else methods.methods
            }
        }

        fun add(url: URL, SAPClassName: String, libtype: LIBTYPE){
            when(libtype){
                Ksll.LIBTYPE.DEX -> dexLibController.download(url, SAPClassName, {}, {})
            }
        }
    }

}