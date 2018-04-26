package it.matteopellegrino.ksll.controller

import android.content.Context
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.RemoteLib
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.URL

/**
 * Helper class for create, delete and maintain files related to different
 * [Lib]s
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class StorageHelper(context: Context) {
    val baseDirName: String = context.filesDir.absolutePath + File.separator + "ksll"
    private val sapFileName: String = "sapclassname"
    private val metaDataFile: File = File(baseDirName, "metadata")
    val dexDir = File(context.cacheDir, "dex")
    private var availableLibs: MutableList<Lib> = arrayListOf()

    init{
        dexDir.mkdir()
        if (!metaDataFile.exists()) {
            metaDataFile.parentFile.mkdirs()
            metaDataFile.createNewFile()
        }else{
            try{
                availableLibs = ObjectInputStream(metaDataFile.inputStream()).readObject() as MutableList<Lib>
            }catch (ignored: Exception){}
        }
    }

    private fun URL.toDirPath(): String = protocol + File.separator + host + File.separator + port + File.separator + path

    /**
     * Resolve [remoteLib] into a [File] building
     * the path related to the current storage
     * @param remoteLib the lib to be resolved
     * @return a file instance, may no exists
     */
    fun fileOf(remoteLib: RemoteLib): File =
            File(baseDirName,remoteLib.url.toDirPath() + File.separator + "${remoteLib.version}.${remoteLib.extension}")

    /**
     * Resolve [url] into a [File] building
     * the path related to the current storage
     * @param url identifying a library
     * @return a file instance, may no exists
     */
    fun sapFileOf(url: URL): File =
            File(baseDirName, url.toDirPath() + File.separator + sapFileName)

    /**
     * Create directories and files to maintain a persistent representation
     * of [remoteLib] with the corresponding lib [data]
     * @param remoteLib Representation of a [Lib] retrievable remotely
     * @param data bytes of the library content
     * @return a [Pair] of the [Lib] file and the corresponding SAP file created
     */
    fun create(remoteLib: RemoteLib, data: ByteArray): Pair<File, File>{
        val libFile = fileOf(remoteLib)
        libFile.parentFile.mkdirs()
        libFile.writeBytes(data)
        val sapFile = sapFileOf(remoteLib.url)
        sapFile.printWriter().use { it.print(remoteLib.SAPClassName) }
        return Pair(libFile, sapFile)
    }

    /**
     * Try to remove physically and logically an existing [Lib]
     * @param existingLib The [Lib] you want to delete
     * @return false if [existingLib] does not exist or an error occurred. True otherwise
     */
    fun delete(existingLib: Lib): Boolean {
        if (!availableLibs.remove(existingLib) || !existingLib.file.exists())
            return false

        existingLib.file.parentFile.listFiles().forEach { it.delete() }
        existingLib.file.parentFile.delete()
        ObjectOutputStream(metaDataFile.outputStream()).writeObject(availableLibs)
        return true
    }

    /**
     * Retrieve all logical available [Lib]s
     */
    fun availableLibs(): List<Lib> = availableLibs

}