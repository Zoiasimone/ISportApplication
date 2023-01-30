package it.uninsubria.isport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class UsersCampoAdapter(context:Context, campi:ArrayList<CampoModel?>) :
    ArrayAdapter<CampoModel?>(context,R.layout.lista_campi, campi) {

    private class ViewHolder {
        var idCampoText:TextView? = null
        var nomeCampoText:TextView? = null
        var tipoCampoText:TextView? = null
        var indirizzoCampoText:TextView? = null
        var cittaCampoText:TextView? = null
        var orarioCampoText:TextView? = null
        var giorniCampoText:TextView? = null
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
            viewHolder.giorniCampoText = convertView.findViewById(R.id.giorniCampoText)

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
        viewHolder.giorniCampoText?.text = "apertura: ${campo?.giorniCampo.toString().trim()}"

        return convertView
    }
}