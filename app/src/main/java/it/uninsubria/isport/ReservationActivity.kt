package it.uninsubria.isport

import android.content.SharedPreferences
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.Year


class ReservationActivity : AppCompatActivity() {

    private var tipoCampo:TextView? = null
    private var indirizzoCampo:TextView? = null
    val db = DatabaseHelper(this@ReservationActivity)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)
        tipoCampo = findViewById(R.id.tipoCampo3)
        indirizzoCampo = findViewById(R.id.indirizzoCampo3)
        val regButton:Button = findViewById(R.id.reg_button)

        val spinnerAdapterCampi:ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.row)
        val spinnerAdapterOrari:ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.row)
        val spinnerAdapterMesi:ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.row)
        val spinnerAdapterGiorni:ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.row)

        spinnerAdapterCampi.addAll(getCampi())
        spinnerAdapterOrari.addAll(getOrari())
        spinnerAdapterMesi.addAll(getMesi())

        val campi:Spinner = findViewById(R.id.spinner_campi)
        val orari:Spinner =  findViewById(R.id.listaOrari)
        val mesi:Spinner =  findViewById(R.id.spinner_month)
        val giorni:Spinner = findViewById(R.id.spinner_day)

        campi.adapter = spinnerAdapterCampi
        orari.adapter = spinnerAdapterOrari
        mesi.adapter = spinnerAdapterMesi

        campi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                val campoSelezionato: String = parent?.getItemAtPosition(position).toString()
                val cursor:Cursor = db.getTipoCampoAndIndirizzoCampo(campoSelezionato)
                while(cursor.moveToNext()) {
                    tipoCampo!!.text = cursor.getString(0)
                    "${cursor.getString(1)}, ${cursor.getString(2)}(${cursor.getString(3)})".also { indirizzoCampo!!.text = it }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        mesi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (parent?.getItemAtPosition(position).toString()) {
                    "Febbraio" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        while (i < 28) {
                            i += 1
                            spinnerAdapterGiorni.add(i.toString())
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Aprile", "Giugno", "Settembre", "Novembre" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        while (i < 30) {
                            i += 1
                            spinnerAdapterGiorni.add(i.toString())
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Gennaio", "Marzo", "Maggio", "Luglio", "Agosto", "Ottobre", "Dicembre" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        while (i < 31) {
                            i += 1
                            spinnerAdapterGiorni.add(i.toString())
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        val prefs: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val username: String? = prefs.getString("username",null)

        regButton.setOnClickListener {
            val db = DatabaseHelper(this@ReservationActivity)
            val idUtente:Int = db.cercaIdUtenteByUsername(username.toString())
            val idCampo:Int = db.cercaIdCampoByNomeCampo(campi.selectedItem.toString().trim())
            val orario:String = orari.selectedItem.toString().trim()
            val giorno:String = formatGiorno(giorni.selectedItem.toString().trim())
            val mese:String = formatMese(mesi.selectedItem.toString().trim())
            val anno:Int = Year.now().value
            val data = "$giorno/$mese/$anno"

            val cursor:Cursor = db.prenotazioneGiaPresente(orario,data)
            if(cursor.count == 0)
                db.insertPrenotazione(idUtente, username.toString(), idCampo, orario, data)
            else
                Toast.makeText(this, "Esiste gia' una prenotazione del campo '${campi.selectedItem.toString().trim()}' in data '$data' e orario '$orario'",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun getOrari(): ArrayList<String> {
        val orari = ArrayList<String>()
        orari.add("8:00-9:00")
        orari.add("9:00-10:00")
        orari.add("10:00-11:00")
        orari.add("11:00-12:00")
        orari.add("12:00-13:00")
        orari.add("13:00-14:00")
        orari.add("14:00-15:00")
        orari.add("15:00-16:00")
        orari.add("16:00-17:00")
        orari.add("17:00-18:00")
        orari.add("18:00-19:00")
        orari.add("19:00-20:00")
        orari.add("20:00-21:00")
        orari.add("21:00-22:00")
        orari.add("22:00-23:00")
        return orari
    }

    private fun getMesi(): ArrayList<String> {
        val mesi = ArrayList<String>()
        mesi.add("Gennaio")
        mesi.add("Febbraio")
        mesi.add("Marzo")
        mesi.add("Aprile")
        mesi.add("Maggio")
        mesi.add("Giugno")
        mesi.add("Luglio")
        mesi.add("Agosto")
        mesi.add("Settembre")
        mesi.add("Ottobre")
        mesi.add("Novembre")
        mesi.add("Dicembre")
        return mesi
    }

    private fun getCampi(): ArrayList<String> {
        val cursor:Cursor = db.getNomeCampi()
        val campi = ArrayList<String>()
        while(cursor.moveToNext())
            campi.add(cursor.getString(0))
        return campi
    }

    private fun formatGiorno(giorno:String):String {
        var giorno = giorno
        if(giorno == "1" || giorno == "2" || giorno == "3" || giorno == "4"
            || giorno == "5" || giorno == "6" || giorno == "7" ||
            giorno == "8" || giorno == "9"){
            giorno = "0$giorno"
        }
        return giorno
    }

    private fun formatMese(mese:String):String {
        var mese = mese
        if(mese == "Gennaio") {
            mese = "01"
        } else if(mese == "Febbraio") {
            mese = "02"
        } else if(mese == "Marzo") {
            mese = "03"
        } else if(mese == "Aprile") {
            mese = "04"
        } else if(mese == "Maggio") {
            mese = "05"
        } else if(mese == "Giugno") {
            mese = "06"
        } else if(mese == "Luglio") {
            mese = "07"
        } else if(mese == "Agosto") {
            mese = "08"
        } else if(mese == "Settembre") {
            mese = "09"
        } else if(mese == "Ottobre") {
            mese = "10"
        } else if(mese == "Novembre") {
            mese = "11"
        }else if(mese == "Dicembre") {
            mese = "12"
        }
        return mese
    }
}