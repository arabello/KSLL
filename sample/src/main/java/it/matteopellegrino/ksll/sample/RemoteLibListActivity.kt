package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import it.matteopellegrino.ksll.Failure
import it.matteopellegrino.ksll.Ksll
import it.matteopellegrino.ksll.apimanager.RESTManager
import it.matteopellegrino.ksll.sample.adapter.RemoteLibAdapter
import it.matteopellegrino.ksll.sample.dialog.AddLibDialogBuilder
import kotlinx.android.synthetic.main.activity_remotelib_list.*

class RemoteLibListActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remotelib_list)
        val ksll = Ksll(baseContext, RESTManager())
        val adapter = RemoteLibAdapter(this, ksll.availableLibs())
        val ksllFailure: (Failure) -> Unit = { error ->
            val msg = when(error){
                Failure.NotTrustedData -> "Signature verification failed. Library not trusted."
                Failure.HTTPRequestError -> "Connection problem. Cannot retrieve library."
                else -> {
                    "Unexpected error. Cannot load library."
                }
            }

            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }

        recyclerViewLibs.layoutManager = LinearLayoutManager(this)
        recyclerViewLibs.adapter = adapter

        fabAddLib.setOnClickListener {
            val dialog = AddLibDialogBuilder(this)
            dialog.setPositiveButton("Load"){ _, _ ->
                ksll.load(dialog.input.text.toString(), {
                    adapter.setItems(ksll.availableLibs())
                    adapter.notifyDataSetChanged()
                }, ksllFailure)
            }
            dialog.show()
        }

    }

}
