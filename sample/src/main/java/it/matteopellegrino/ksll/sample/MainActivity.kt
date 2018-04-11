package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import it.matteopellegrino.ksll.Ksll
import it.matteopellegrino.ksll.api.RESTManager
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = URL("http://192.168.1.150:8080")
        val ksll = Ksll(this, RESTManager())

        ksll.multipleLoad(url, { lib ->
            val sap = lib.SAPClass.newInstance()
            println(lib.SAPClass.getMethod("echo", String::class.java).invoke(sap, "The magic!") as String)
        }, {
            error("Error cannot retrieve")
        }, true)
    }


}
