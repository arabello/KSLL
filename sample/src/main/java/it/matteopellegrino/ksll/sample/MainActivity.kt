package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import it.matteopellegrino.ksll.api.RESTManager
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = URL("http://192.168.1.150:8080")

        val api = RESTManager()

        api.retrieveAvailableAPI(url, {remoteLibs ->
            println(remoteLibs)
        }, {
            error("Error cannot retrieve")
        })
    }


}
