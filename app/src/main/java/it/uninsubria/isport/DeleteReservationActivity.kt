package it.uninsubria.isport

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class DeleteReservationActivity : AppCompatActivity() {

    val db = DatabaseHelper(this@DeleteReservationActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_reservation)
        val deleteButton:Button = findViewById(R.id.deletePrenotazioneButton)

        val prefs: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val username: String? = prefs.getString("username",null)

        val spinnerAdapterDate: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.row)
        val spinnerAdapterOrari: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.row)

        spinnerAdapterDate.addAll(getDate())

        val date: Spinner = findViewById(R.id.spinnerDate)
        val orari: Spinner =  findViewById(R.id.spinnerOrari)

        date.adapter = spinnerAdapterDate

        date.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val dataSelezionata: String = parent?.getItemAtPosition(position).toString()
                val cursor:Cursor = db.getOrariByData(dataSelezionata,username.toString())
                spinnerAdapterOrari.clear()
                while(cursor.moveToNext()) {
                    spinnerAdapterOrari.add(cursor.getString(0))
                }
                orari.adapter = spinnerAdapterOrari
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminare la prenotazione in data '${date.selectedItem}' " +
                    "e orario '${orari.selectedItem}'?")
            builder.setPositiveButton("Si") { dialogInterface, i ->
                val db = DatabaseHelper(this@DeleteReservationActivity)
                db.deletePrenotazione(orari.selectedItem.toString().trim(),
                    date.selectedItem.toString().trim())
                val intent = Intent(this@DeleteReservationActivity,UserProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialogInterface, i -> }
            builder.create().show()
        }
    }

    private fun getDate(): ArrayList<String> {
        val prefs: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val username: String? = prefs.getString("username",null)
        val cursor: Cursor = db.getDatePrenotazioni(username.toString().trim())
        val date = ArrayList<String>()
        while(cursor.moveToNext()) {
            val data:String = cursor.getString(0)
            if(!date.contains(data)) {
                date.add(cursor.getString(0))
            }
        }
        return date
    }
}