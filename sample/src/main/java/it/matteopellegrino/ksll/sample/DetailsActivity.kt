package it.matteopellegrino.ksll.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import it.matteopellegrino.ksll.Ksll
import it.matteopellegrino.ksll.apimanager.RESTManager
import it.matteopellegrino.ksll.model.RemoteLib
import it.matteopellegrino.ksll.require
import it.matteopellegrino.ksll.sample.adapter.MethodAdapter
import kotlinx.android.synthetic.main.activity_details.*
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.item_remotelib.*
import kotlinx.android.synthetic.main.toolbar.*


class DetailsActivity : AppCompatActivity() {

    companion object {
        val REMOTE_LIB_EXTRA_KEY = "remotelib"
    }

    lateinit var ksll: Ksll
    lateinit var remoteLib: RemoteLib

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)

        ksll = Ksll(this, RESTManager())
        remoteLib = intent.getSerializableExtra(REMOTE_LIB_EXTRA_KEY) as RemoteLib

        supportActionBar?.title = remoteLib.SAPClassName

        textSapClassName.text = remoteLib.SAPClassName
        textUrl.text = remoteLib.url.toString()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_remotelib, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_delete -> {
                deleteLibrary()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteLibrary(){
        val b = AlertDialog.Builder(this)
        b.setMessage(getString(R.string.action_irreversible))
        b.setNegativeButton(getString(R.string.cancel)){dialog, _ -> dialog.dismiss() }
        b.setPositiveButton(getString(R.string.delete)){ _, _ ->
            val outcome = ksll.wipe(remoteLib)
            val msg = if (outcome) getString(R.string.action_delete_success) else getString(R.string.action_delete_failure)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RemoteLibListActivity::class.java))
            finish()
        }
        b.show()
    }
}
