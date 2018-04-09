package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import it.matteopellegrino.ksll.Ksll
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ksll = Ksll.from(this)

        val url = URL("http://192.168.1.150:8080/dex")
        val SAPClassName = "it.matteopellegrino.ksla.KslaSAP"

        ksll.add(url, SAPClassName, Ksll.LIBTYPE.DEX)


    }


}
