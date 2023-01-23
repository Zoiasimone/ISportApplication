package it.uninsubria.isport

import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserSportViewActivity : AppCompatActivity() {

    private val db = DatabaseHelper(this@UserSportViewActivity)
    private var idCampo:ArrayList<String>? = null
    private var nomeCampo:ArrayList<String>? = null
    private var tipoCampo:ArrayList<String>? = null
    private var indirizzoCampo:ArrayList<String>? = null
    private var cittaCampo:ArrayList<String>? = null
    private var provinciaCampo:ArrayList<String>? = null
    private var orarioCampo:ArrayList<String>? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sport_view)
        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)

        idCampo = ArrayList()
        nomeCampo = ArrayList()
        tipoCampo = ArrayList()
        indirizzoCampo = ArrayList()
        cittaCampo = ArrayList()
        provinciaCampo = ArrayList()
        orarioCampo = ArrayList()

        aggiungiCampiNegliArray()

        val customAdapterUser = CustomAdapterUser(this,this@UserSportViewActivity,
            idCampo!!, nomeCampo!!, tipoCampo!!,indirizzoCampo!!,cittaCampo!!,provinciaCampo!!,orarioCampo!!)
        recyclerView.recycledViewPool.clear()
        recyclerView.adapter = customAdapterUser
        recyclerView.layoutManager = LinearLayoutManager(this@UserSportViewActivity)

    }

    private fun aggiungiCampiNegliArray(){
        val cursor:Cursor = db.readAllCampi()
        if(cursor.count == 0) {
            Toast.makeText(this, "Nessun Campo trovato!", Toast.LENGTH_SHORT).show()
        } else {
            while(cursor.moveToNext()) {
                idCampo!!.add(cursor.getString(0))
                nomeCampo!!.add(cursor.getString(1))
                tipoCampo!!.add(cursor.getString(2))
                indirizzoCampo!!.add(cursor.getString(3))
                cittaCampo!!.add(cursor.getString(4))
                provinciaCampo!!.add(cursor.getString(5))
                orarioCampo!!.add(cursor.getString(6))
            }
        }
    }
}

