package it.matteopellegrino.ksll

import android.os.Environment
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import dalvik.system.DexClassLoader
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class JarLoaderTest {
    @Test
    fun loadJar() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val libPath = "/sdcard/ksla/ksla.jar"
        val tmpDir =  File(appContext.filesDir, "dex")
        tmpDir.mkdir()

        val classloader = DexClassLoader(libPath, tmpDir.absolutePath, null, javaClass.classLoader)
        val classToLoad = classloader.loadClass("it.matteopellegrino.ksla.KslaManager") as Class<*>
        println(classToLoad.name)
    }
}
