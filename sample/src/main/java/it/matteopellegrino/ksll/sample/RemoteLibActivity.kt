package it.matteopellegrino.ksll.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import it.matteopellegrino.ksll.Ksll
import it.matteopellegrino.ksll.apimanager.RESTManager
import it.matteopellegrino.ksll.model.RemoteLib
import it.matteopellegrino.ksll.require
import it.matteopellegrino.ksll.sample.adapter.MethodAdapter
import kotlinx.android.synthetic.main.activity_remotelib.*
import android.support.v7.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.item_remotelib.*


class RemoteLibActivity : AppCompatActivity() {

    companion object {
        val REMOTE_LIB_EXTRA_KEY = "remotelib"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remotelib)
        val ksll = Ksll(this, RESTManager())
        val remoteLib = intent.getSerializableExtra(REMOTE_LIB_EXTRA_KEY) as RemoteLib

        textSapClassName.text = remoteLib.SAPClassName
        textVersion.text = remoteLib.version
        textExtension.text = remoteLib.extension.toString()

        val adapter = MethodAdapter(this, arrayListOf())
        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(recyclerViewApis.context, layoutManager.orientation)

        recyclerViewApis.layoutManager = layoutManager
        recyclerViewApis.adapter = adapter

        recyclerViewApis.addItemDecoration(dividerItemDecoration)


        remoteLib.require(ksll){obj, methods ->
            adapter.setItems(methods)
        }
    }
}
