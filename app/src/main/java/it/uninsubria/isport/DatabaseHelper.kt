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

    private val queryUtente = "CREATE TABLE IF NOT EXISTS Utente" +
            "(IdUtente INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NomeUtente TEXT, " +
            "CognomeUtente TEXT, " +
            "NascitaUtente TEXT, " +
            "ComuneUtente TEXT, " +
            "EmailUtente TEXT, " +
            "PasswordUtente TEXT, " +
            "TelefonoUtente TEXT, " +
            "FlgAdmin TEXT DEFAULT 0);"

    private val queryCampo = "CREATE TABLE IF NOT EXISTS Campo" +
            "(IdCampo INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NomeCampo TEXT, " +
            "TipoCampo TEXT, " +
            "IndirizzoCampo TEXT, " +
            "CittaCampo TEXT, " +
            "ProvinciaCampo TEXT, " +
            "OrarioCampo TEXT, " +
            "GiorniCampo TEXT);"

    private val queryPrenotazione = "CREATE TABLE IF NOT EXISTS Prenotazione" +
            "(IdPrenotazione INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdCampo INTEGER, " +
            "IdUtente INTEGER, " +
            "UsernameUtente TEXT, " +
            "OrarioPrenotazione TEXT, " +
            "DataPrenotazione TEXT, " +
            "SettimanaPrenotazione INTEGER, " +
            "FOREIGN KEY (IdCampo) REFERENCES Campo(IdCampo), " +
            "FOREIGN KEY (IdUtente) REFERENCES Utente(IdUtente), " +
            "FOREIGN KEY (UsernameUtente) REFERENCES Utente(EmailUtente));"

    private val insertUtenti = "INSERT INTO Utente(nomeUtente,CognomeUtente,NascitaUtente," +
            "ComuneUtente,EmailUtente,PasswordUtente,TelefonoUtente,FlgAdmin) " +
            "VALUES('Simone','Zoia','14/09/1999','Gorla Maggiore','zoiasimone@yahoo.it','Pass1','3460983453','1')," +
            "('Matteo','Gallazzi','03/11/1998','Busto Arsizio','matteogallazzi@gmail.com','Pass2','3457968542','0');"

    private val insertCampi = "INSERT INTO Campo(NomeCampo,TipoCampo,IndirizzoCampo,CittaCampo,ProvinciaCampo,OrarioCampo,GiorniCampo) " +
            "VALUES('Campo comunale','Calcio a 5','Via Roma 38','Busto Arsizio','VA','9:00-22:00','lun-ven')," +
            "('Centro sportivo Galeazzi','Tennis singolo/doppio','Via Giudici 10','Busto Arsizio','VA','10:00-20:00','lun-sab')," +
            "('Centro cestistico','Basket','Via Verdi 46','Gallarate','VA','13:00-21:00','lun-ven');"

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
        nomeUtente: String?, cognomeUtente: String?, nascitaUtente: String?,comune:String?,
        emailUtente: String?, passwordUtente: String?, telefonoUtente: String?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("NomeUtente", nomeUtente)
        cv.put("CognomeUtente", cognomeUtente)
        cv.put("NascitaUtente", nascitaUtente)
        cv.put("ComuneUtente", comune)
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

    fun cercaUtente(username: String?, password: String?): Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT EmailUtente, PasswordUtente, FlgAdmin FROM Utente " +
                    "WHERE EmailUtente = '$username' AND PasswordUtente = '$password'", null
        )
    }

    fun readAllCampi(): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT * FROM Campo", null)
    }

    fun addCampo(nomeCampo: String?, tipoCampo: String?, indirizzoCampo: String?,
                 cittaCampo: String?, provinciaCampo:String?, orarioCampo: String?,
                 giorniCampo: String?) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("NomeCampo", nomeCampo)
        cv.put("TipoCampo", tipoCampo)
        cv.put("IndirizzoCampo", indirizzoCampo)
        cv.put("CittaCampo", cittaCampo)
        cv.put("ProvinciaCampo", provinciaCampo)
        cv.put("OrarioCampo", orarioCampo)
        cv.put("GiorniCampo", giorniCampo)
        val result = db.insert("Campo", null, cv)
        if (result == -1L) {
            Toast.makeText(context, "Qualcosa e' andato storto", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Campo aggiunto correttamente!", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateCampo(row_id:String, nome:String, tipo:String, indirizzo:String,
                    citta:String, provincia:String, orario:String, giorni:String) {
        val db:SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()
        cv.put("NomeCampo", nome)
        cv.put("TipoCampo", tipo)
        cv.put("IndirizzoCampo", indirizzo)
        cv.put("CittaCampo", citta)
        cv.put("ProvinciaCampo", provincia)
        cv.put("OrarioCampo", orario)
        cv.put("GiorniCampo", giorni)
        val result = db.update("Campo", cv, "IdCampo=?", arrayOf(row_id)).toLong()
        if (result == -1L) {
            Toast.makeText(context, "Qualcosa e' andato storto", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Campo aggiornato correttamente!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getPrenotazioni(username:String):Cursor{
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT IdUtente FROM Prenotazione " +
                "WHERE UsernameUtente = '$username'",null)
    }

    fun getTipoCampoAndIndirizzoCampoAndGiorniCampo(campo:String): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        val cursor:Cursor = db.rawQuery("SELECT IdCampo FROM Campo WHERE NomeCampo = '$campo'",null)
        cursor.moveToFirst()
        val id:Int = cursor.getInt(0)
        return db.rawQuery("SELECT TipoCampo,IndirizzoCampo,CittaCampo,ProvinciaCampo,GiorniCampo " +
                "               FROM Campo WHERE IdCampo = $id", null)
    }

    fun cercaIdUtenteByUsername(username: String): Int {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT IdUtente FROM Utente WHERE EmailUtente = '$username'", null)
        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    fun cercaIdCampoByNomeCampo(nomeCampo: String): Int {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT IdCampo FROM Campo WHERE NomeCampo = '$nomeCampo'", null)
        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    fun prenotazioneGiaPresente(orario:String, data:String):Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT IdCampo FROM Prenotazione WHERE OrarioPrenotazione = '$orario' AND DataPrenotazione = '$data'",null)
    }

    fun insertPrenotazione(idUtente:Int, username: String, idCampo:Int, orario: String, data: String, settimana:Int) {
        val db:SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()
        cv.put("IdUtente", idUtente)
        cv.put("UsernameUtente", username)
        cv.put("IdCampo", idCampo)
        cv.put("OrarioPrenotazione", orario)
        cv.put("DataPrenotazione", data)
        cv.put("SettimanaPrenotazione", settimana)
        val result = db.insert("Prenotazione", null, cv)
        if (result == -1L) {
            Toast.makeText(context, "Qualcosa e' andato storto", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Prenotazione effettuata!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getNomeCampi(comune:String): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT NomeCampo FROM Campo WHERE CittaCampo = '$comune'", null)
    }

    fun getUtente(username: String):Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT * FROM Utente WHERE EmailUtente = '$username'", null)
    }

    fun deletePrenotazione(orario:String, data:String) {
        val db:SQLiteDatabase = this.writableDatabase
        val row_id = db.rawQuery("SELECT IdPrenotazione FROM Prenotazione WHERE OrarioPrenotazione = '$orario' AND DataPrenotazione = '$data'", null)
        row_id.moveToFirst()
        val result = db.delete("Prenotazione", "IdPrenotazione=?", arrayOf(row_id.getString(0))).toLong()
        if (result == -1L) {
            Toast.makeText(context, "Qualcosa e' andato storto", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Prenotazione cancellata correttamente", Toast.LENGTH_SHORT).show()
        }
    }

    fun readPrenotazioni(id:Int): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT c.IdCampo,c.NomeCampo,c.TipoCampo,c.IndirizzoCampo," +
                "c.CittaCampo,c.ProvinciaCampo,p.OrarioPrenotazione,p.DataPrenotazione " +
                "FROM Prenotazione p " +
                "JOIN Campo c ON p.IdCampo=c.IdCampo " +
                "WHERE p.IdUtente = $id", null)
    }

    fun readSettimanaPrenotazioni(settimana:Int, username:String): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT SettimanaPrenotazione " +
                               "FROM Prenotazione WHERE SettimanaPrenotazione = $settimana " +
                               "AND UsernameUtente = '$username'", null)
    }

    fun getComuneUtente(username:String):String {
        val db:SQLiteDatabase = this.readableDatabase
        val cursor:Cursor = db.rawQuery("SELECT ComuneUtente FROM Utente " +
                "WHERE EmailUtente = '$username'",null)
        cursor.moveToFirst()
        return cursor.getString(0)
    }

    fun deleteCampo(row_id: String?) {
        val db:SQLiteDatabase = this.writableDatabase
        val result = db.delete("Campo", "IdCampo=?", arrayOf(row_id)).toLong()
        if (result == -1L) {
            Toast.makeText(context, "Qualcosa e' andato storto", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Campo cancellato correttamente", Toast.LENGTH_SHORT).show()
        }
    }

    fun getDatePrenotazioni(username:String): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT DataPrenotazione FROM Prenotazione " +
                " WHERE UsernameUtente = '$username'", null)
    }

    fun getOrariByData(data:String,username:String): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT OrarioPrenotazione " +
                               "FROM Prenotazione " +
                               "WHERE DataPrenotazione = '$data' " +
                               "AND UsernameUtente = '$username'",null)
    }

    fun getOrarioCampo(nomeCampo: String): Cursor {
        val db:SQLiteDatabase = this.readableDatabase
        return db.rawQuery("SELECT OrarioCampo FROM Campo WHERE NomeCampo = '$nomeCampo'",null)
    }
}