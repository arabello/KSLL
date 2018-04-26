package it.matteopellegrino.ksll.controller

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import dalvik.system.DexClassLoader
import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.RemoteLib
import it.matteopellegrino.ksll.security.Security
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.URL

/**
 * Implementation of [Controller] which stores data as files
 * in the internal storage of the given [Context].
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
internal class LibController(context: Context) : Controller{
    private val baseDirName: String = context.filesDir.absolutePath + File.separator + "ksll"
    private val sapFileName: String = "sapclassname"
    private val metaDataFile: File = File(baseDirName, "metadata")
    private val dexDir = File(context.cacheDir, "dex")
    data class StorageEntity(val libDir: File, val libFile: File, val sapFile: File)
    private var availableLibs: MutableList<RemoteLib> = arrayListOf()

    init{
        dexDir.mkdir()
        if (!metaDataFile.exists()) {
            metaDataFile.parentFile.mkdirs()
            metaDataFile.createNewFile()
        }else{
            try{
                availableLibs = ObjectInputStream(metaDataFile.inputStream()).use { it.readObject() } as MutableList<RemoteLib>
            }catch (ignored: Exception){
                Log.e(javaClass.simpleName, ignored.toString())
            }
        }
    }

    private fun URL.toDirPath(): String = protocol + File.separator + host + File.separator + port + File.separator + path

    private fun filesOf(remoteLib: RemoteLib): StorageEntity = StorageEntity(
            File(baseDirName, remoteLib.url.toDirPath()),
            File(baseDirName,remoteLib.url.toDirPath() + File.separator + "${remoteLib.version}.${remoteLib.extension}") ,
            File(baseDirName, remoteLib.url.toDirPath() + File.separator + sapFileName)
    )

    private fun loadSAP(entity: StorageEntity): Class<*> =
            DexClassLoader(entity.libFile.absolutePath, dexDir.absolutePath, null, javaClass.classLoader)
                    .loadClass(entity.sapFile.readText())

    /**
     * Create directories and files to maintain a persistent representation
     * of [remoteLib] with the corresponding lib [data]
     * @param remoteLib Representation of a [Lib] retrievable remotely
     * @param data bytes of the library content
     * @return The [Lib] physically created
     */
    private fun create(remoteLib: RemoteLib, data: ByteArray): Lib{
        val entity = filesOf(remoteLib)

        entity.libDir.mkdirs()
        entity.libFile.parentFile.mkdirs()
        entity.libFile.writeBytes(data)
        entity.sapFile.parentFile.mkdirs()
        entity.sapFile.createNewFile()
        entity.sapFile.printWriter().use { it.print(remoteLib.SAPClassName) }

        availableLibs.add(remoteLib)
        ObjectOutputStream(metaDataFile.outputStream()).use { it.writeObject(availableLibs) }
        return Lib(loadSAP(entity), remoteLib.version, remoteLib.extension)
    }


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

                    success(create(remoteLib, result.get()))
                }
            }
        }
    }

    override fun find(remoteLib: RemoteLib): Lib? {
        val entity = filesOf(remoteLib)
        return if (!entity.libFile.exists())
            null
        else Lib(loadSAP(entity),
                remoteLib.version,
                remoteLib.extension)
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

    override fun wipe(existingLib: RemoteLib): Boolean {
        if (!availableLibs.remove(existingLib))
            return false
        ObjectOutputStream(metaDataFile.outputStream()).use { it.writeObject(availableLibs) }

        val entity = filesOf(existingLib)
        return entity.libDir.deleteRecursively()
    }

    override fun wipeAll() = availableLibs.forEach {wipe(it)}

    /**
     * Retrieve all logical available [Lib]s
     */
    override fun availableLibs(): List<Lib> {
        val list: MutableList<Lib> = arrayListOf()
        availableLibs.forEach {remoteLib ->
            val lib = find(remoteLib)
            if (lib != null)
                list.add(lib)
        }
        return list
    }
}