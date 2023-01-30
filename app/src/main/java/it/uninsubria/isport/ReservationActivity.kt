package it.uninsubria.isport

import android.content.SharedPreferences
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.Period
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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

        val prefs: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val username: String? = prefs.getString("username",null)

        val spinnerAdapterCampi:ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.row)
        val spinnerAdapterOrari:ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.row)
        val spinnerAdapterMesi:ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.row)
        val spinnerAdapterGiorni:ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.row)

        spinnerAdapterCampi.addAll(getCampi())
        spinnerAdapterMesi.addAll(getMesi())

        val campi:Spinner = findViewById(R.id.spinner_campi)
        val orari:Spinner =  findViewById(R.id.listaOrari)
        val mesi:Spinner =  findViewById(R.id.spinner_month)
        val giorni:Spinner = findViewById(R.id.spinner_day)

        campi.adapter = spinnerAdapterCampi
        mesi.adapter = spinnerAdapterMesi

        campi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                val campoSelezionato: String = parent?.getItemAtPosition(position).toString()

                val cursorCampi:Cursor = db.getTipoCampoAndIndirizzoCampoAndGiorniCampo(campoSelezionato)
                while(cursorCampi.moveToNext()) {
                    tipoCampo!!.text = "${cursorCampi.getString(0)}\n" +
                                       "apertura: ${cursorCampi.getString(4)}"
                    "${cursorCampi.getString(1)}, ${cursorCampi.getString(2)}(${cursorCampi.getString(3)})".also { indirizzoCampo!!.text = it }
                }

                spinnerAdapterOrari.clear()
                val cursorOrari:Cursor = db.getOrarioCampo(campoSelezionato)
                while(cursorOrari.moveToNext()) {
                    spinnerAdapterOrari.addAll(setOrariCampo(cursorOrari.getString(0)))
                }
                orari.adapter = spinnerAdapterOrari
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

        regButton.setOnClickListener {
            val db = DatabaseHelper(this@ReservationActivity)
            val idUtente:Int = db.cercaIdUtenteByUsername(username.toString())
            val idCampo:Int = db.cercaIdCampoByNomeCampo(campi.selectedItem.toString().trim())
            val orario:String = orari.selectedItem.toString().trim()
            val giorno:String = formatGiorno(giorni.selectedItem.toString().trim())
            val mese:String = formatMese(mesi.selectedItem.toString().trim())
            val anno:Int = Year.now().value
            val data = "$giorno/$mese/$anno"

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(anno,subStringMese(mese),subStringGiorno(giorno))
            val settimana:Int = calendar.get(Calendar.WEEK_OF_YEAR)
            val formatter:DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dataPrenotazione: LocalDate = LocalDate.parse("$anno-$mese-$giorno",formatter)
            val dataNow:LocalDate = LocalDate.now()
            val dataOdierna:LocalDate = LocalDate.parse(dataNow.toString(),formatter)
            val diff: Period = Period.between(dataOdierna, dataPrenotazione)

            val cursorPrenotazione:Cursor = db.prenotazioneGiaPresente(orario,data)
            val cursorSettimana:Cursor = db.readSettimanaPrenotazioni(settimana,username.toString())

            if(cursorPrenotazione.count == 0) {
                if(diff.days < 7 && diff.months == 0 && diff.years == 0) {
                    if(cursorSettimana.count < 3) {
                        db.insertPrenotazione(idUtente, username.toString(), idCampo,orario,data, settimana)
                    } else {
                        Toast.makeText(this, "Hai già effettuato 3 prenotazioni per questa settimana",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Non puoi prenotare oltre i 7 giorni dalla data odierna",
                        Toast.LENGTH_SHORT).show()
                }
            } else
                Toast.makeText(this, "Esiste gia' una prenotazione del campo " +
                        "'${campi.selectedItem.toString().trim()}' in data '$data' e orario '$orario'",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun subStringMese(mese:String):Int {
        if(mese == "01" || mese == "02" || mese == "03"
            || mese == "04" || mese == "05" || mese == "06"
            || mese == "07" || mese == "08" || mese == "09"){
            mese.replace("0","")
        }
        return Integer.parseInt(mese) - 1
    }

    private fun subStringGiorno(giorno:String):Int {
        if(giorno == "01" || giorno == "02" || giorno == "03"
            || giorno == "04" || giorno == "05" || giorno == "06"
            || giorno == "07" || giorno == "08" || giorno == "09"){
            giorno.replace("0","")
        }
        return Integer.parseInt(giorno)
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

    private fun formatMese(mese:String):String {
        var mese = mese
        when (mese) {
            "Gennaio" -> {
                mese = "01"
            }
            "Febbraio" -> {
                mese = "02"
            }
            "Marzo" -> {
                mese = "03"
            }
            "Aprile" -> {
                mese = "04"
            }
            "Maggio" -> {
                mese = "05"
            }
            "Giugno" -> {
                mese = "06"
            }
            "Luglio" -> {
                mese = "07"
            }
            "Agosto" -> {
                mese = "08"
            }
            "Settembre" -> {
                mese = "09"
            }
            "Ottobre" -> {
                mese = "10"
            }
            "Novembre" -> {
                mese = "11"
            }
            "Dicembre" -> {
                mese = "12"
            }
        }
        return mese
    }

    private fun getCampi(): ArrayList<String> {
        val prefs: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val comune: String? = prefs.getString("comune",null)
        val cursor:Cursor = db.getNomeCampi(comune.toString().trim())
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

    fun setOrariCampo(orario:String):ArrayList<String> {
        val orariCampo:ArrayList<String> = ArrayList()
        var orarioInizio = orario.substringBefore("-")
        orarioInizio = orarioInizio.substringBefore(":")
        var orarioFine = orario.substringAfter("-")
        orarioFine = orarioFine.substringBefore(":")

        while(orarioInizio != orarioFine) {
            val ob:StringBuilder = StringBuilder()
            orariCampo.add(ob.append(orarioInizio)
                    .append(":00-")
                    .append(Integer.parseInt(orarioInizio) + 1)
                    .append(":00").toString())
            orarioInizio = (Integer.parseInt(orarioInizio) + 1).toString()
        }
        return orariCampo
    }
}