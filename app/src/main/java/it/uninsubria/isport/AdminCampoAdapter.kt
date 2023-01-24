package it.uninsubria.isport

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView


class AdminCampoAdapter(context:Context, campi:ArrayList<CampoModel?>) :
    ArrayAdapter<CampoModel?>(context,R.layout.lista_campi, campi) {

    private class ViewHolder {
        var idCampoText:TextView? = null
        var nomeCampoText:TextView? = null
        var tipoCampoText:TextView? = null
        var indirizzoCampoText:TextView? = null
        var cittaCampoText:TextView? = null
        var orarioCampoText:TextView? = null
        var mainLayout:LinearLayout? = null
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var convertView = view
        val campo:CampoModel? = getItem(position)
        val viewHolder:ViewHolder

        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.lista_campi, parent, false)
            viewHolder.idCampoText = convertView!!.findViewById(R.id.idCampoText)
            viewHolder.nomeCampoText = convertView.findViewById(R.id.nomeCampoText)
            viewHolder.tipoCampoText = convertView.findViewById(R.id.tipoCampoText)
            viewHolder.indirizzoCampoText = convertView.findViewById(R.id.indirizzoCampoText)
            viewHolder.cittaCampoText = convertView.findViewById(R.id.cittaCampoText)
            viewHolder.orarioCampoText = convertView.findViewById(R.id.orarioCampoText)
            viewHolder.mainLayout = convertView.findViewById(R.id.mainLayout)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.idCampoText?.text = campo?.idCampo.toString().trim()
        viewHolder.nomeCampoText?.text = campo?.nomeCampo.toString().trim()
        viewHolder.tipoCampoText?.text = campo?.tipoCampo.toString().trim()
        viewHolder.indirizzoCampoText?.text = campo?.indirizzoCampo.toString().trim()
        viewHolder.cittaCampoText?.text = "${campo?.cittaCampo.toString()}(${campo?.provinciaCampo.toString()})".trim()
        viewHolder.orarioCampoText?.text = campo?.orarioCampo.toString().trim()
        viewHolder.mainLayout = convertView.findViewById(R.id.mainLayout)
        viewHolder.mainLayout?.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", campo?.idCampo.toString().trim())
            intent.putExtra("nome", campo?.nomeCampo.toString().trim())
            intent.putExtra("tipo", campo?.tipoCampo.toString().trim())
            intent.putExtra("indirizzo", campo?.indirizzoCampo.toString().trim())
            intent.putExtra("citta", campo?.cittaCampo.toString().trim())
            intent.putExtra("provincia", campo?.provinciaCampo.toString().trim())
            intent.putExtra("orario", campo?.orarioCampo.toString().trim())
            context.startActivity(intent)
        }
        return convertView
    }
}