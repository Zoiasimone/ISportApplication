package it.uninsubria.isport

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private var username: EditText? = null
    private var password: EditText? = null
    private val db = DatabaseHelper(this@LoginActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.username_login)
        password = findViewById(R.id.password_login)
        val loginButton:Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val utente: ArrayList<String> = db.cercaUtente(username?.text.toString().trim(),password?.text.toString().trim())

            if(utente[0] == username?.text.toString().trim() && utente[1] == password?.text.toString().trim()) {
                val editor: SharedPreferences.Editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit()
                editor.putString("username", username?.text.toString().trim())
                editor.putString("password", password?.text.toString().trim())
                editor.apply()
                if(utente[2] == "1"){
                    val intent = Intent(this@LoginActivity, AdminSportViewActivity::class.java)
                    startActivity(intent)
                } else if(utente[2] == "0") {
                    val intent = Intent(this@LoginActivity, UserSportViewActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Username e/o password non validi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}