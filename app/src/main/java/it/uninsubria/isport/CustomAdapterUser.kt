package it.uninsubria.isport

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterUser constructor(
    private val context: Context,
    private val activity: Activity,
    private var idCampo:ArrayList<String> = ArrayList(),
    private var nomeCampo:ArrayList<String> = ArrayList(),
    private var tipoCampo:ArrayList<String> = ArrayList(),
    private var indirizzoCampo:ArrayList<String> = ArrayList(),
    private var cittaCampo:ArrayList<String> = ArrayList(),
    private var provinciaCampo:ArrayList<String> = ArrayList(),
    private var orarioCampo:ArrayList<String> = ArrayList()):
    RecyclerView.Adapter<CustomAdapterUser.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.lista_sport_user,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.idCampoText.text.trim()[position]
        holder.nomeCampoText.text.trim()[position]
        holder.tipoCampoText.text.trim()[position]
        holder.indirizzoCampoText.text.trim()[position]
        holder.cittaCampoText.text.trim()[position]
        holder.provinciaCampoText.text.trim()[position]
        holder.orarioCampoText.text.trim()[position]
    }

    override fun getItemCount(): Int {
        return idCampo.count()
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var idCampoText:TextView = itemView.findViewById(R.id.idCampoText)
        var nomeCampoText:TextView = itemView.findViewById(R.id.nomeCampoText)
        var tipoCampoText:TextView = itemView.findViewById(R.id.tipoCampoText)
        var orarioCampoText:TextView = itemView.findViewById(R.id.orarioCampoText)
        var indirizzoCampoText:TextView = itemView.findViewById(R.id.indirizzoCampoText)
        var cittaCampoText:TextView = itemView.findViewById(R.id.cittaCampoText)
        var provinciaCampoText:TextView = itemView.findViewById(R.id.provinciaCampoText)
    }
}