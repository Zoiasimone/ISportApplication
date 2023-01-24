package it.uninsubria.isport

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "ISportApplication.db", null, 1) {
    private val context: Context

    init {
        this.context = context
    }

    private val queryUtente = "CREATE TABLE IF NOT EXISTS Utente " +
            "(IdUtente INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NomeUtente TEXT, " +
            "CognomeUtente TEXT, " +
            "NascitaUtente TEXT, " +
            "EmailUtente TEXT, " +
            "PasswordUtente TEXT, " +
            "TelefonoUtente TEXT, " +
            "FlgAdmin TEXT DEFAULT 0);"

    private val queryCampo = "CREATE TABLE IF NOT EXISTS Campo" +
            "(IdCampo INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NomeCampo TEXT, " +
            "TipoCampo TEXT, " +
            "OrarioCampo TEXT);"

    private val queryPrenotazione = "CREATE TABLE IF NOT EXISTS Prenotazione" +
            "(IdPrenotazione INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdCampo INTEGER, " +
            "IdUtente INTEGER, " +
            "UsernameUtente TEXT, " +
            "OrarioPrenotazione TEXT, " +
            "DataPrenotazione TEXT, " +
            "FOREIGN KEY (IdCampo) REFERENCES Campo(IdCampo), " +
            "FOREIGN KEY (IdUtente) REFERENCES Utente(IdUtente), " +
            "FOREIGN KEY (UsernameUtente) REFERENCES Utente(EmailUtente));"

    private val insertUtenti = "INSERT INTO Utente(nomeUtente,CognomeUtente,NascitaUtente," +
            "EmailUtente,PasswordUtente,TelefonoUtente,FlgAdmin) " +
            "VALUES('Simone','Zoia','14/09/1999','zoiasimone@yahoo.it','Pass1','3460983453','1')," +
            "('Matteo','Gallazzi','03/11/1998','matteogallazzi@gmail.com','Pass2','3457968542','0');"

    private val insertCampi = "INSERT INTO Campo(NomeCampo,TipoCampo,OrarioCampo) " +
                "VALUES('Campo comunale','Calcio a 5','9:00-22-00')," +
                "('Centro sportivo Galeazzi','Tennis singolo/doppio','10:00-20:00')," +
                "('Centro cestistico','Basket','13:00-21:00');"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(queryUtente)
        db.execSQL(queryCampo)
        db.execSQL(queryPrenotazione)
        db.execSQL(insertUtenti)
        db.execSQL(insertCampi)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        //db.execSQL("DROP TABLE IF EXISTS Utente")
        //db.execSQL("DROP TABLE IF EXISTS Campo")
        //db.execSQL("DROP TABLE IF EXISTS Prenotazione")
        onCreate(db)
    }

    fun addUtente(
        nomeUtente: String?, cognomeUtente: String?, nascitaUtente: String?,
        emailUtente: String?, passwordUtente: String?, telefonoUtente: String?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("NomeUtente", nomeUtente)
        cv.put("CognomeUtente", cognomeUtente)
        cv.put("NascitaUtente", nascitaUtente)
        cv.put("EmailUtente", emailUtente)
        cv.put("PasswordUtente", passwordUtente)
        cv.put("TelefonoUtente", telefonoUtente)
        val result = db.insert("Utente", null, cv)
        if (result == -1L) {
            Toast.makeText(context, "Qualcosa e' andato storto", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Account creato correttamente!", Toast.LENGTH_SHORT).show()
        }
    }

    fun cercaUtente(username: String?, password: String?): ArrayList<String> {
        val db = this.readableDatabase

        val cursor:Cursor = db.rawQuery(
            "SELECT EmailUtente, PasswordUtente, FlgAdmin FROM Utente " +
                    "WHERE EmailUtente = '$username' AND PasswordUtente = '$password'",null)

        val lista: ArrayList<String> = ArrayList()
        if(cursor.moveToFirst()) {
            lista.add(cursor.getString(0))
            lista.add(cursor.getString(1))
            lista.add(cursor.getString(2))
        } else{
            cursor.close()
        }
        return lista
    }

    fun readAllCampi(): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT * FROM Campo", null)
    }

    fun getPrenotazioni(username:String):Cursor{
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT IdUtente FROM Prenotazione " +
                "WHERE UsernameUtente = '$username'",null)
    }
}