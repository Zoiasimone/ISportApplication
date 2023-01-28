package it.uninsubria.isport

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class UserSportViewActivity : AppCompatActivity() {

    private val db = DatabaseHelper(this@UserSportViewActivity)

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sport_view)
        val registrationButton: Button = findViewById(R.id.registration_button)

        val arrayOfCampi: ArrayList<CampoModel?> = aggiungiCampiInArray()

        val adapterCampi = UsersCampoAdapter(this, arrayOfCampi)
        val listView:ListView = findViewById(R.id.listaCampi)
        listView.adapter = adapterCampi

        registrationButton.setOnClickListener {
            val intent = Intent(this@UserSportViewActivity, ReservationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun aggiungiCampiInArray() : ArrayList<CampoModel?> {
        val prefs:SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val username: String? = prefs.getString("username",null)

        val comune:String = db.getComuneUtente(username.toString())
        val cursor:Cursor = db.readAllCampi()
        val campi:ArrayList<CampoModel?> = ArrayList()
        var campo:CampoModel?
        if(cursor.count == 0) {
            Toast.makeText(this, "Nessun Campo trovato!", Toast.LENGTH_SHORT).show()
        } else {
            while(cursor.moveToNext()) {
                if(cursor.getString(4) == comune) {
                    campo = CampoModel(
                        idCampo = cursor.getInt(0).toString().trim(),
                        nomeCampo = cursor.getString(1).toString().trim(),
                        tipoCampo = cursor.getString(2).toString().trim(),
                        indirizzoCampo = cursor.getString(3).toString().trim(),
                        cittaCampo = cursor.getString(4).toString().trim(),
                        provinciaCampo = cursor.getString(5).toString().trim(),
                        orarioCampo = cursor.getString(6).toString().trim()
                    )
                    campi.add(campo)
                }
            }
        }
        return campi
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.profileItem){
            val intent = Intent(this@UserSportViewActivity,UserProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}

