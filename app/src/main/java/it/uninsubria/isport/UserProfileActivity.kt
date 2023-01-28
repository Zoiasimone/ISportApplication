package it.uninsubria.isport

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get

class UserProfileActivity: AppCompatActivity() {

    private var nome: TextView? = null
    private var cognome: TextView? = null
    private var dataDiNascita: TextView? = null
    private var email: TextView? = null
    private var telefono: TextView? = null
    private val db = DatabaseHelper(this@UserProfileActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        nome = findViewById(R.id.nomeProfilo)
        cognome = findViewById(R.id.cognomeProfilo)
        dataDiNascita = findViewById(R.id.nascitaProfilo)
        email = findViewById(R.id.emailProfilo)
        telefono = findViewById(R.id.telefonoProfilo)

        val prefs:SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val username: String? = prefs.getString("username",null)

        val cursor:Cursor = db.getUtente(username.toString())
        while(cursor.moveToNext()){
            nome!!.text = cursor.getString(1)
            cognome!!.text = cursor.getString(2)
            dataDiNascita!!.text = cursor.getString(3)
            email!!.text = cursor.getString(4)
            telefono!!.text = cursor.getString(6)
        }

        aggiungiCampiInArray()

        val arrayOfPrenotazioni: ArrayList<PrenotazioneModel?> = aggiungiCampiInArray()

        val adapterPrenotazioni = PrenotazioneAdapter(this, arrayOfPrenotazioni)
        val listView: ListView = findViewById(R.id.listaPrenotazioni)
        listView.adapter = adapterPrenotazioni

        listView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val image:ImageView = parent?.getItemAtPosition(R.id.deleteButtonImage) as ImageView
                image.isClickable = true
                image.setOnClickListener{ confirmDialog() }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun aggiungiCampiInArray() : ArrayList<PrenotazioneModel?> {
        val id:Int = db.cercaIdUtenteByUsername(email?.text.toString().trim())
        val cursor: Cursor = db.readPrenotazioni(id)
        val prenotazioni:ArrayList<PrenotazioneModel?> = ArrayList()
        var prenotazione:PrenotazioneModel?
        if(cursor.count == 0) {
            Toast.makeText(this, "Nessuna prenotazione trovata!", Toast.LENGTH_SHORT).show()
        } else {
            while(cursor.moveToNext()) {
                prenotazione = PrenotazioneModel(
                    idCampoPrenotazione = cursor.getInt(0).toString().trim(),
                    nomeCampoPrenotazione = cursor.getString(1).toString().trim(),
                    tipoCampoPrenotazione = cursor.getString(2).toString().trim(),
                    indirizzoPrenotazione = cursor.getString(3).toString().trim(),
                    cittaPrenotazione = cursor.getString(4).toString().trim(),
                    provinciaPrenotazione = cursor.getString(5).toString().trim(),
                    orarioPrenotazione = cursor.getString(6).toString().trim(),
                    dataPrenotazione = cursor.getString(7).toString().trim())
                prenotazioni.add(prenotazione)
            }
        }
        return prenotazioni
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Sei sicuro di voler eliminare la prenotazione?")
        builder.setPositiveButton("Si") { dialogInterface, i ->
            val db = DatabaseHelper(this@UserProfileActivity)
            val listView: ListView = findViewById(R.id.listaPrenotazioni)
            val listaOraData = ArrayList<String>()
            listView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val orario = parent?.getItemIdAtPosition(R.id.orarioCampoPrenotato)
                    val data = parent?.getItemIdAtPosition(R.id.dataCampoPrenotato)
                    listaOraData.add(orario.toString().trim())
                    listaOraData.add(data.toString().trim())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
            db.deletePrenotazione(listaOraData[0],listaOraData[1])
            finish()
        }
        builder.setNegativeButton("No") { dialogInterface, i -> }
        builder.create().show()
    }
}