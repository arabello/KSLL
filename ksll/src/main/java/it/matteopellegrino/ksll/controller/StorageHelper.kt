package it.matteopellegrino.ksll.controller

import android.content.Context
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.RemoteLib
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.URL

/**
 * TODO: Add class description
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

    fun resolveLibFile(remoteLib: RemoteLib): File =
            File(baseDirName,remoteLib.url.toDirPath() + File.separator + "${remoteLib.version}.${remoteLib.extension}")

    fun resolveSapFile(url: URL): File =
            File(baseDirName, url.toDirPath() + File.separator + sapFileName)

    fun create(remoteLib: RemoteLib, data: ByteArray): Pair<File, File>{
        val libFile = resolveLibFile(remoteLib)
        libFile.parentFile.mkdirs()
        libFile.writeBytes(data)
        val sapFile = resolveSapFile(remoteLib.url)
        sapFile.printWriter().use { it.print(remoteLib.SAPClassName) }
        return Pair(libFile, sapFile)
    }

    fun delete(existingLib: Lib): Boolean {
        if (!availableLibs.remove(existingLib) || !existingLib.file.exists())
            return false

        existingLib.file.parentFile.listFiles().forEach { it.delete() }
        existingLib.file.parentFile.delete()
        ObjectOutputStream(metaDataFile.outputStream()).writeObject(availableLibs)
        return true
    }

    fun availableLibs(): List<Lib> = availableLibs

}