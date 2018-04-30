package it.matteopellegrino.ksll.sample.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.model.RemoteLib
import it.matteopellegrino.ksll.sample.RemoteLibActivity
import it.matteopellegrino.ksll.sample.R
import kotlinx.android.synthetic.main.item_remotelib.view.*


/**
 * Adapter for a collection of [Lib]
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class RemoteLibAdapter(val context: Context, private var objects: List<RemoteLib>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    data class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val sapClassName: TextView = view.textSapClassName
        val url: TextView = view.textUrl
        val version: TextView = view.textVersion
        val extension: TextView = view.textExtension
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    override fun getItemCount(): Int = objects.size

    fun setItems(objs: List<RemoteLib>) {
        objects = objs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_remotelib, parent, false))
        return vh.listen { position, type ->
            val intent = Intent(context, RemoteLibActivity::class.java)
            intent.putExtra(RemoteLibActivity.REMOTE_LIB_EXTRA_KEY, objects[position])
            context.startActivity(intent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myholder = holder as ViewHolder
        val lib = objects[position]
        myholder.sapClassName.text = lib.SAPClassName
        myholder.url.text = lib.url.toString()
        myholder.version.text = lib.version
        myholder.extension.text = lib.extension.toString()
    }
}