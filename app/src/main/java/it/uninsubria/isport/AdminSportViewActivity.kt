package it.uninsubria.isport

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminSportViewActivity: AppCompatActivity() {

    private val db = DatabaseHelper(this@AdminSportViewActivity)

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sport_view)
        val addButton:Button = findViewById(R.id.add_button)

        val arrayOfCampi: ArrayList<CampoModel?> = aggiungiCampiInArray()

        val adapterCampi = AdminCampoAdapter(this, arrayOfCampi)
        val listView: ListView = findViewById(R.id.listaCampi)
        listView.adapter = adapterCampi

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == 1 && result.data != null) {
                recreate()
            }
        }

        addButton.setOnClickListener{
            val intent = Intent(this@AdminSportViewActivity,AddSportActivity::class.java)
            startActivity(intent)
        }
    }

    private fun aggiungiCampiInArray() : ArrayList<CampoModel?> {
        val cursor:Cursor = db.readAllCampi()
        val campi:ArrayList<CampoModel?> = ArrayList()
        var campo:CampoModel?
        if(cursor.count == 0) {
            Toast.makeText(this, "Nessun Campo trovato!", Toast.LENGTH_SHORT).show()
        } else {
            while(cursor.moveToNext()) {
                campo = CampoModel(idCampo = cursor.getInt(0).toString().trim(),
                    nomeCampo = cursor.getString(1).toString().trim(),
                    tipoCampo = cursor.getString(2).toString().trim(),
                    indirizzoCampo = cursor.getString(3).toString().trim(),
                    cittaCampo = cursor.getString(4).toString().trim(),
                    provinciaCampo = cursor.getString(5).toString().trim(),
                    orarioCampo = cursor.getString(6).toString().trim())
                campi.add(campo)
            }
        }
        return campi
    }
}