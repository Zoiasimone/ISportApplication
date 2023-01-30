package it.uninsubria.isport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class AddSportActivity : AppCompatActivity() {

    private var nomeCampo: EditText? = null
    private var tipoCampo:EditText? = null
    private var indirizzoCampo:EditText? = null
    private var cittaCampo:EditText? = null
    private var provinciaCampo:EditText? = null
    private var orarioCampo:EditText? = null
    private var giorniCampo:EditText? = null
    private var addSportButton: Button? = null
    private val db = DatabaseHelper(this@AddSportActivity)

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sport)
        nomeCampo = findViewById(R.id.nomeCampo)
        tipoCampo = findViewById(R.id.tipoCampo)
        indirizzoCampo = findViewById(R.id.indirizzoCampo)
        cittaCampo = findViewById(R.id.cittaCampo)
        provinciaCampo = findViewById(R.id.provinciaCampo)
        orarioCampo = findViewById(R.id.orarioCampo)
        giorniCampo = findViewById(R.id.giorniCampo)
        addSportButton = findViewById(R.id.addSportButton)
        addSportButton?.setOnClickListener {
            db.addCampo(
                nomeCampo = nomeCampo?.text.toString().trim(),
                tipoCampo = tipoCampo?.text.toString().trim(),
                indirizzoCampo = indirizzoCampo?.text.toString().trim(),
                cittaCampo = cittaCampo?.text.toString().trim(),
                provinciaCampo = provinciaCampo?.text.toString().trim(),
                orarioCampo = orarioCampo?.text.toString().trim(),
                giorniCampo = giorniCampo?.text.toString().trim()
            )
            val intent = Intent(this@AddSportActivity,AdminSportViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}