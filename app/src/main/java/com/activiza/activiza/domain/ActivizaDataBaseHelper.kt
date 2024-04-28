package com.activiza.activiza.domain

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData

class ActivizaDataBaseHelper(context:Context) :
    SQLiteOpenHelper(context, DATABASENAME, null, DATABASE_VERSION){
        companion object{

            private const val DATABASENAME = "activiza.db"
            private const val DATABASE_VERSION = 1

            //Create all Rutinas
            private const val TABLE_NAME_RUTINAS = "rutinas"
            private const val COLUMN_ID = "id"
            private const val COLUMN_NAME = "name"
            private const val COLUMN_ENTRENADOR = "entrenador"
            private const val COLUMN_TIPO = "tipo"
            private const val COLUMN_DESCRIPCION = "descripcion"
            private const val COLUMN_MEDIA = "media"

            //create all ejercicios
            private const val TABLE_NAME_EJERCICIOS = "ejercicios"
            private const val COLUMN_ID_RUTINA = "id_rutina"
            //private const val COLUMN_ID = "id"
            //private const val COLUMN_DESCRIPCION = "descripcion"
            private const val COLUMN_REPETICIONES = "repeticiones"
            private const val COLUMN_DURACION = "duracion"
            private const val COLUMN_DESCANSO = "descanso"
            //private const val COLUMN_MEDIA = "media"

        }

    override fun onCreate(db: SQLiteDatabase?) {
        //Tabla con todas las Rutinas
        val createTableRutinas =
            "CREATE TABLE $TABLE_NAME_RUTINAS (" +
                    "$COLUMN_ID integer PRIMARY KEY," +
                    "$COLUMN_NAME varchar(255), " +
                    "$COLUMN_ENTRENADOR varchar(255), " +
                    "$COLUMN_TIPO varchar(255), " +
                    "$COLUMN_DESCRIPCION varchar(255), " +
                    "$COLUMN_MEDIA varchar(255))"
        db?.execSQL(createTableRutinas)

        // Crear la tabla para los Ejercicios
        val createEjercicios = "CREATE TABLE $TABLE_NAME_EJERCICIOS (" +
                "$COLUMN_ID INTEGER  PRIMARY KEY," +
                "$COLUMN_ID_RUTINA INTEGER," +
                "$COLUMN_DESCRIPCION VARCHAR(255)," +
                "$COLUMN_NAME VARCHAR(255)," +
                "$COLUMN_REPETICIONES INTEGER," +
                "$COLUMN_DURACION INTEGER," +
                "$COLUMN_DESCANSO INTEGER," +
                "$COLUMN_MEDIA VARCHAR(255)," +
                "FOREIGN KEY($COLUMN_NAME) REFERENCES $TABLE_NAME_RUTINAS($COLUMN_NAME) ON DELETE CASCADE)"
        db?.execSQL(createEjercicios)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        /*
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME_RUTINAS"
        db?.execSQL(dropTableQuery)
        val dropTableQueryTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME_EJERCICIOS"
        db?.execSQL(dropTableQueryTableQuery)
        onCreate(db)
        */
    }
    fun insertRutina(rutinas: RutinaData) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, rutinas.id)
            put(COLUMN_NAME, rutinas.nombre)
            put(COLUMN_ENTRENADOR, rutinas.entrenador)
            put(COLUMN_TIPO, rutinas.tipo)
            put(COLUMN_DESCRIPCION, rutinas.descripcion)
            put(COLUMN_MEDIA, rutinas.media)
        }
        db.insert(TABLE_NAME_RUTINAS, null, values)
        db.close()
    }
    fun insertEjercicio(ejercicio: EjerciciosData, id_rutina:Int) {
        val db = writableDatabase
        if (ejercicio.duracion == null) {
               ejercicio.duracion = 0
            }
        val values = ContentValues().apply {
            put(COLUMN_ID, ejercicio.id)
            put(COLUMN_ID_RUTINA, id_rutina)
            put(COLUMN_DESCRIPCION, ejercicio.descripcion)
            put(COLUMN_NAME, ejercicio.nombre)
            put(COLUMN_REPETICIONES, ejercicio.repeticiones)
            put(COLUMN_DURACION, ejercicio.duracion)
            put(COLUMN_DESCANSO, ejercicio.descanso)
            put(COLUMN_MEDIA, ejercicio.media)
        }
        db.insert(TABLE_NAME_EJERCICIOS, null, values)
        db.close()
    }
    fun borrarEjerciciosYRutinas() {
        val db = writableDatabase
        // Borrar todos los ejercicios
        val borrarEjerciciosQuery = "DELETE FROM $TABLE_NAME_EJERCICIOS"
        db?.execSQL(borrarEjerciciosQuery)

        // Borrar todas las rutinas
        val borrarRutinasQuery = "DELETE FROM $TABLE_NAME_RUTINAS"
        db?.execSQL(borrarRutinasQuery)
    }
    fun obtenerPrimeraRutina(): RutinaData? {
        var rutina: RutinaData? = null
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_RUTINAS LIMIT 1"
        val cursor = db.rawQuery(query, null)
        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val entrenador = it.getString(it.getColumnIndexOrThrow(COLUMN_ENTRENADOR))
                val tipo = it.getString(it.getColumnIndexOrThrow(COLUMN_TIPO))
                val descripcion = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                val media = it.getString(it.getColumnIndexOrThrow(COLUMN_MEDIA))

                // Aquí debes manejar la lista de ejercicios, dependiendo de cómo estén almacenados en la base de datos

                rutina = RutinaData(id, nombre, tipo, descripcion, entrenador, listOf(), media)
            }else{
                null //si no se encuentra ningún dato, devuelve null
            }
        }
        db.close()
        return rutina
    }
}