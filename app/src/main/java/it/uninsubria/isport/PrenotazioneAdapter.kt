package it.uninsubria.isport

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog


class PrenotazioneAdapter(context:Context, prenotazioni:ArrayList<PrenotazioneModel?>) :
    ArrayAdapter<PrenotazioneModel?>(context,R.layout.lista_prenotazioni, prenotazioni) {

    private class ViewHolder {
        var idCampoPrenotazioneText:TextView? = null
        var nomeCampoPrenotazioneText:TextView? = null
        var tipoCampoPrenotazioneText:TextView? = null
        var indirizzoCampoPrenotazioneText:TextView? = null
        var cittaCampoPrenotazioneText:TextView? = null
        var orarioCampoPrenotazioneText:TextView? = null
        var dataCampoPrenotazioneText:TextView? = null
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var convertView = view
        val prenotazione:PrenotazioneModel? = getItem(position)
        val viewHolder:ViewHolder

        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.lista_prenotazioni, parent, false)
            viewHolder.idCampoPrenotazioneText = convertView!!.findViewById(R.id.idCampoPrenotato)
            viewHolder.nomeCampoPrenotazioneText = convertView.findViewById(R.id.nomeCampoPrenotato)
            viewHolder.tipoCampoPrenotazioneText = convertView.findViewById(R.id.tipoCampoPrenotato)
            viewHolder.indirizzoCampoPrenotazioneText = convertView.findViewById(R.id.indirizzoCampoPrenotato)
            viewHolder.cittaCampoPrenotazioneText = convertView.findViewById(R.id.cittaCampoPrenotato)
            viewHolder.orarioCampoPrenotazioneText = convertView.findViewById(R.id.orarioCampoPrenotato)
            viewHolder.dataCampoPrenotazioneText = convertView.findViewById(R.id.dataCampoPrenotato)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.idCampoPrenotazioneText?.text = prenotazione?.idCampoPrenotazione.toString().trim()
        viewHolder.nomeCampoPrenotazioneText?.text = prenotazione?.nomeCampoPrenotazione.toString().trim()
        viewHolder.tipoCampoPrenotazioneText?.text = prenotazione?.tipoCampoPrenotazione.toString().trim()
        viewHolder.indirizzoCampoPrenotazioneText?.text = prenotazione?.indirizzoPrenotazione.toString().trim()
        viewHolder.cittaCampoPrenotazioneText?.text = "${prenotazione?.cittaPrenotazione.toString()}(${prenotazione?.provinciaPrenotazione.toString()})".trim()
        viewHolder.orarioCampoPrenotazioneText?.text = prenotazione?.orarioPrenotazione.toString().trim()
        viewHolder.dataCampoPrenotazioneText?.text = prenotazione?.dataPrenotazione.toString().trim()

        return convertView
    }
}