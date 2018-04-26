package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.Ksll
import it.matteopellegrino.ksll.apimanager.RESTManager
import it.matteopellegrino.ksll.model.Lib
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val success: (Lib) -> Unit = {lib ->
            println(lib.SAPClass.name)
        }

        val failure: (Failure) -> Unit = {error ->
            val msg = when(error){
                Failure.NotTrustedData -> "Signature verification failed. Library not trusted."
                Failure.HTTPRequestError -> "Connection problem. Cannot retrieve library."
                else -> {
                    "Unexpected error. Cannot load library."
                }
            }

            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }

        val ksll = Ksll(this, RESTManager())

        ksll.load(URL("http://192.168.1.157:8080"), success, failure, true)
        ksll.load(URL("http://192.168.1.157:8090"), success, failure, true)
    }


}
