package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import it.matteopellegrino.ksll.controller.DexLibController
import it.matteopellegrino.ksll.model.RemoteLib
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = URL("http://192.168.1.150:8080/dex")
        val SAPClassName = "it.matteopellegrino.ksla.KslaSAP"

        val remoteLib = RemoteLib(url, SAPClassName)
        val ctrl = DexLibController(this)

        ctrl.retrieve(remoteLib, {lib ->  println(lib.SAPClass.name)
        }, {
            error("Error cannot retrieve")
        })


    }


}
