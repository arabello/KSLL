package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.Ksll
import it.matteopellegrino.ksll.apimanager.RESTManager
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.sample.adapter.LibAdapter
import kotlinx.android.synthetic.main.activity_main.*

class LibListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ksll = Ksll(this, RESTManager())

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


        //ksll.load(URL("http://192.168.1.157:8080"), success, failure, true)
        //ksll.load(URL("http://192.168.1.157:8090"), success, failure, true)

        libRecyclerView.layoutManager = LinearLayoutManager(this)
        libRecyclerView.adapter = LibAdapter(this, ksll.availableLibs())
    }


}
