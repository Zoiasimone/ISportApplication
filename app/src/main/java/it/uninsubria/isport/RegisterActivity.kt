package it.uninsubria.isport

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterActivity: AppCompatActivity() {

    private var nome: EditText? = null
    private var cognome: EditText? = null
    private var dataDiNascita: EditText? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var telefono: EditText? = null
    private val db = DatabaseHelper(this@RegisterActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        nome = findViewById(R.id.nomeutente)
        cognome = findViewById(R.id.cognomeUtente)
        dataDiNascita = findViewById(R.id.nascitaUtente)
        email = findViewById(R.id.emailUtente)
        password = findViewById(R.id.passwordUtente)
        telefono = findViewById(R.id.telefonoUtente)
        val registerButton:Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            db.addUtente(nome?.text.toString(),cognome?.text.toString().trim(),dataDiNascita?.text.toString().trim(),
                email?.text.toString().trim(),password?.text.toString().trim(),telefono?.text.toString().trim())
            val editor: SharedPreferences.Editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit()
            editor.putString("username", email?.text.toString().trim())
            editor.putString("password", password?.text.toString().trim())
            editor.apply()
            //val intent = Intent(this@RegisterActivity, UserSportViewActivity::class.java)
            startActivity(intent)
        }
    }
}