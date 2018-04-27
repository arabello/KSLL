package it.matteopellegrino.ksll.sample.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.sample.LibActivity
import it.matteopellegrino.ksll.sample.R
import kotlinx.android.synthetic.main.item_lib.view.*


/**
 * Adapter for a collection of [Lib]
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class LibAdapter( val context: Context, val objects: List<Lib>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    data class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val sapClassName: TextView = view.textSapClassName
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lib, parent, false))
        return vh.listen { position, type ->
            context.startActivity(Intent(context, LibActivity::class.java))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myholder = holder as ViewHolder
        val lib = objects[position]
        myholder.sapClassName.text = lib.SAPClass.name
        myholder.version.text = lib.version
        myholder.extension.text = lib.extension.toString()
    }
}