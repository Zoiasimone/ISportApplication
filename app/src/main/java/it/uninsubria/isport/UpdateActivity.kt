package it.uninsubria.isport

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class UpdateActivity : AppCompatActivity() {
    private var id: String? = null
    private var nome: String? = null
    private var tipo: String? = null
    private var indirizzo: String? = null
    private var citta: String? = null
    private var provincia: String? = null
    private var orario: String? = null

    private var nomeCampo:EditText? = null
    private var tipoCampo:EditText? = null
    private var indirizzoCampo:EditText? = null
    private var cittaCampo:EditText? = null
    private var provinciaCampo:EditText? = null
    private var orarioCampo:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        nomeCampo = findViewById(R.id.nomeCampo2)
        tipoCampo = findViewById(R.id.tipoCampo2)
        indirizzoCampo = findViewById(R.id.indirizzoCampo2)
        cittaCampo = findViewById(R.id.cittaCampo2)
        provinciaCampo = findViewById(R.id.provinciaCampo2)
        orarioCampo = findViewById(R.id.orarioCampo2)
        val updateButton:Button = findViewById(R.id.update_button)
        val deleteButton:Button = findViewById(R.id.delete_button)

        getIntentData()

        val ab:ActionBar? = supportActionBar
        ab?.title = nomeCampo!!.text.toString().trim()

        updateButton.setOnClickListener {
            val db = DatabaseHelper(this@UpdateActivity)
            nome = nomeCampo!!.text.toString().trim { it <= ' ' }
            tipo = tipoCampo!!.text.toString().trim { it <= ' ' }
            indirizzo = indirizzoCampo!!.text.toString().trim { it <= ' ' }
            citta = cittaCampo!!.text.toString().trim { it <= ' ' }
            provincia = provinciaCampo!!.text.toString().trim { it <= ' ' }
            orario = orarioCampo!!.text.toString().trim { it <= ' ' }
            db.updateCampo(id!!, nome!!, tipo!!, indirizzo!!, citta!!, provincia!!, orario!!)
        }
    }

    private fun getIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("nome") &&
            intent.hasExtra("tipo") && intent.hasExtra("indirizzo") &&
            intent.hasExtra("citta") && intent.hasExtra("provincia") &&
            intent.hasExtra("orario")) {
            //Getting Data from Intent
            id = intent.getStringExtra("id")
            nome = intent.getStringExtra("nome")
            tipo = intent.getStringExtra("tipo")
            indirizzo = intent.getStringExtra("indirizzo")
            citta = intent.getStringExtra("citta")
            provincia = intent.getStringExtra("provincia")
            orario = intent.getStringExtra("orario")

            nomeCampo!!.setText(nome)
            tipoCampo!!.setText(tipo)
            indirizzoCampo!!.setText(indirizzo)
            cittaCampo!!.setText(citta)
            provinciaCampo!!.setText(provincia)
            orarioCampo!!.setText(orario)
            Log.d("ISport", "$nome, $tipo, $indirizzo, $citta, $provincia , $orario")
        } else {
            Toast.makeText(this, "Nessun dato trovato", Toast.LENGTH_SHORT).show()
        }
    }
}