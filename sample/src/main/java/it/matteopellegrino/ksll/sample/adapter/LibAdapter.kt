package it.matteopellegrino.ksll.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import it.matteopellegrino.ksll.model.Lib
import it.matteopellegrino.ksll.sample.R
import android.widget.TextView
import kotlinx.android.synthetic.main.item_lib.view.*


/**
 * TODO: Add class description
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class LibAdapter(context: Context, objects: MutableList<Lib>?):
        ArrayAdapter<Lib>(context, R.layout.item_lib, objects) {

    data class ViewHolder(val SAPClassName: TextView, val version: TextView, val extension: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var retView: View

        if(convertView == null){
            retView = LayoutInflater.from(context).inflate(R.layout.item_lib, parent, false)
            holder = ViewHolder(retView.textSapClassName, retView.textVersion, retView.textExtension)
            val lib = getItem(position) ?: return retView

            holder.SAPClassName.text = lib.SAPClass.name
            holder.version.text = lib.version
            holder.extension.text = lib.extension.toString()

            retView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        return retView
    }
}