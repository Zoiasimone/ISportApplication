package it.uninsubria.isport

import android.content.SharedPreferences
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
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
                val giorniSettimana = db.getGiorniCampo(campi.selectedItem.toString().trim())
                val giornoInizio = giorniSettimana.substringBefore("-")
                val giornoFine = giorniSettimana.substringAfter("-")
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY)
                calendar.set(Calendar.YEAR,2023)
                val giorniApertura:ArrayList<String> = giorniInApertura(giornoInizio,giornoFine)
                when (parent?.getItemAtPosition(position).toString()) {
                    "Gennaio" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 0)
                        while (i < 31) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Febbraio" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 1)
                        while (i < 28) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Marzo" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 2)
                        while (i < 31) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Aprile" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 3)
                        while (i < 30) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Maggio" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 4)
                        while (i < 31) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Giugno" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 5)
                        while (i < 30) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Luglio" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 6)
                        while (i < 31) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Agosto" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 7)
                        while (i < 31) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Settembre" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 8)
                        while (i < 30) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Ottobre" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 9)
                        while (i < 31) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Novembre" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 10)
                        while (i < 30) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
                        }
                        giorni.adapter = spinnerAdapterGiorni
                    }
                    "Dicembre" -> {
                        var i = 0
                        spinnerAdapterGiorni.clear()
                        calendar.set(Calendar.MONTH, 11)
                        while (i < 31) {
                            i += 1
                            calendar.set(Calendar.DAY_OF_MONTH, i)
                            val data = SimpleDateFormat("EE",Locale.ITALY).format(calendar.time)
                            if(giorniApertura.contains(data)) {
                                spinnerAdapterGiorni.add(i.toString())
                            }
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
            } else {
                Toast.makeText(this, "Esiste gia' una prenotazione del campo " +
                            "'${campi.selectedItem.toString().trim()}' in data '$data' e orario '$orario'",
                    Toast.LENGTH_SHORT).show()
            }
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
        var meseFormat = mese
        when (meseFormat) {
            "Gennaio" -> { meseFormat = "01" }
            "Febbraio" -> { meseFormat = "02" }
            "Marzo" -> { meseFormat = "03" }
            "Aprile" -> { meseFormat = "04" }
            "Maggio" -> { meseFormat = "05" }
            "Giugno" -> { meseFormat = "06" }
            "Luglio" -> { meseFormat = "07" }
            "Agosto" -> { meseFormat = "08" }
            "Settembre" -> { meseFormat = "09" }
            "Ottobre" -> { meseFormat = "10" }
            "Novembre" -> { meseFormat = "11" }
            "Dicembre" -> { meseFormat = "12" }
        }
        return meseFormat
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

    private fun giorniInApertura(giornoI:String,giornoF:String): ArrayList<String> {
        var giorniSettimana = "lunmarmergiovensabdom"
        val giorni = ArrayList<String>()
        when(giornoF) {
            "mar" -> giorniSettimana = giorniSettimana.substringBefore("mer")
            "mer" -> giorniSettimana = giorniSettimana.substringBefore("gio")
            "gio" -> giorniSettimana = giorniSettimana.substringBefore("ven")
            "ven" -> giorniSettimana = giorniSettimana.substringBefore("sab")
            "sab" -> giorniSettimana = giorniSettimana.substringBefore("dom")
        }
        when(giornoI) {
            "mar" -> giorniSettimana = giorniSettimana.substringAfter("lun")
            "mer" -> giorniSettimana = giorniSettimana.substringAfter("mar")
            "gio" -> giorniSettimana = giorniSettimana.substringAfter("mer")
            "ven" -> giorniSettimana = giorniSettimana.substringAfter("gio")
            "sab" -> giorniSettimana = giorniSettimana.substringAfter("ven")
        }
        if(giorniSettimana.contains("lun")){
            giorni.add(giorniSettimana.substring(0,3))
        }
        if(giorniSettimana.contains("mar")){
            giorni.add(giorniSettimana.substring(3,6))
        }
        if(giorniSettimana.contains("mer")){
            giorni.add(giorniSettimana.substring(6,9))
        }
        if(giorniSettimana.contains("gio")){
            giorni.add(giorniSettimana.substring(9,12))
        }
        if(giorniSettimana.contains("ven")){
            giorni.add(giorniSettimana.substring(12,15))
        }
        if(giorniSettimana.contains("sab")){
            giorni.add(giorniSettimana.substring(15,18))
        }
        if(giorniSettimana.contains("dom")){
            giorni.add(giorniSettimana.substring(18,21))
        }

        return giorni
    }
}