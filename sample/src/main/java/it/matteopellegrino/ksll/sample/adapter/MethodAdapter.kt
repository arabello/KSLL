package it.matteopellegrino.ksll.sample.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.matteopellegrino.ksll.sample.R
import kotlinx.android.synthetic.main.item_method.view.*
import java.lang.reflect.Method


/**
 * Adapter for a collection of [Method]
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class MethodAdapter(val context: Context, private var objects: List<Method>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    data class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = view.textName
        val params = view.textParameters
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    override fun getItemCount(): Int = objects.size

    fun setItems(objs: List<Method>) {
        objects = objs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_method, parent, false))
        return vh.listen { position, type ->
            //TODO
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myholder = holder as ViewHolder
        val method = objects[position]
        myholder.name.text = method.name

        val b = StringBuilder("(")
        method.parameterTypes.forEach { b.append(it.toString()); b.append(",") }
        b.replace(b.length-1, b.length, ")")

        myholder.params.text = b.toString()
    }
}