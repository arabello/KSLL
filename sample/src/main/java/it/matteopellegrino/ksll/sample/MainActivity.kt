package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.Ksll
import it.matteopellegrino.ksll.apimanager.RESTManager
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = URL("http://192.168.1.157:8090")
        val ksll = Ksll(this, RESTManager())

        ksll.load(url, { lib ->
            val sap = lib.SAPClass.newInstance()
            println(lib.SAPClass.name)
        }, {error ->
            val msg = when(error){
                Failure.NotTrustedData -> "Signature verification failed. Library not trusted."
                Failure.HTTPRequestError -> "Connection problem. Cannot retrieve library."
                else -> {
                    "Unexpected error. Cannot load library."
                }
            }

            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }, true)
    }


}
