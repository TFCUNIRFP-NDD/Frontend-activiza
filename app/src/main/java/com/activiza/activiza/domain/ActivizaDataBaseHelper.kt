package com.activiza.activiza.domain

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

            //create entrenamiento
            //private const val COLUMN_ID = "id"
            private const val TABLE_NAME_ENTRENAMIENTOS = "entrenamiento"
            private const val COLUMN_ID_EJERCICIO = "id_ejercicio"
            private const val COLUMN_FECHA = "fecha"
            private const val COLUMN_COMPLETADO = "completado"

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

        //Crear la tabla para los entrenamientos
        val createEntrenamientosTable = ("CREATE TABLE $TABLE_NAME_ENTRENAMIENTOS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_ID_EJERCICIO INTEGER," +
                "$COLUMN_FECHA DATE," +
                "$COLUMN_COMPLETADO INTEGER," +
                "FOREIGN KEY($COLUMN_ID_EJERCICIO) REFERENCES $TABLE_NAME_EJERCICIOS($COLUMN_ID) ON DELETE CASCADE)")
        db?.execSQL(createEntrenamientosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME_RUTINAS"
        db?.execSQL(dropTableQuery)
        val dropTableQueryTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME_EJERCICIOS"
        db?.execSQL(dropTableQueryTableQuery)
        val dropTableQueryEntrenamientos = "DROP TABLE IF EXISTS $TABLE_NAME_ENTRENAMIENTOS"
        db?.execSQL(dropTableQueryEntrenamientos)
        onCreate(db)
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

        // Inserta automáticamente un entrenamiento asociado con el estado de completitud inicializado en 0 (no completado)
        val entrenamientoValues = ContentValues().apply {
            put(COLUMN_ID_EJERCICIO, ejercicio.id)
            put(COLUMN_FECHA, obtenerFechaActual()) // Puedes definir una función para obtener la fecha actual
            put(COLUMN_COMPLETADO, 0) // 0 representa "no completado"
        }
        db.insert(TABLE_NAME_ENTRENAMIENTOS, null, entrenamientoValues)

        db.close()
    }
    fun obtenerFechaActual(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
    fun borrarEjerciciosYRutinas() {
        val db = writableDatabase
        // Borrar todos los ejercicios
        val borrarEjerciciosQuery = "DELETE FROM $TABLE_NAME_EJERCICIOS"
        db?.execSQL(borrarEjerciciosQuery)

        // Borrar todos los entrenamientos
        val borrarEntrenamientosQuery = "DELETE FROM $TABLE_NAME_ENTRENAMIENTOS"
        db?.execSQL(borrarEntrenamientosQuery)

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
    fun getEjercicio(id:Int): EjerciciosData? {
        var ejercicio: EjerciciosData? = null
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_EJERCICIOS where id = $id"
        val cursor = db.rawQuery(query, null)
        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val descripcion = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                val repeticiones = it.getInt(it.getColumnIndexOrThrow(COLUMN_REPETICIONES))
                val duracion = it.getInt(it.getColumnIndexOrThrow(COLUMN_DURACION))
                val descanso = it.getInt(it.getColumnIndexOrThrow(COLUMN_DESCANSO))
                val media = it.getString(it.getColumnIndexOrThrow(COLUMN_MEDIA))

                // Aquí debes manejar la lista de ejercicios, dependiendo de cómo estén almacenados en la base de datos

                ejercicio = EjerciciosData(id, nombre, descripcion, repeticiones, duracion, descanso, media)
            }else{
                null //si no se encuentra ningún dato, devuelve null
            }
        }
        db.close()
        return ejercicio
    }
    @SuppressLint("Range")
    fun getEjerciciosDeRutina(rutinaId: Int): ArrayList<EjerciciosData> {
        val ejerciciosList = ArrayList<EjerciciosData>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_EJERCICIOS WHERE $COLUMN_ID_RUTINA = $rutinaId"
        val cursor = db.rawQuery(query, null)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(it.getColumnIndex(COLUMN_ID))
                    val nombre = cursor.getString(it.getColumnIndex(COLUMN_NAME))
                    val descripcion = cursor.getString(it.getColumnIndex(COLUMN_DESCRIPCION))
                    val repeticiones = cursor.getInt(it.getColumnIndex(COLUMN_REPETICIONES))
                    val duracion = cursor.getInt(it.getColumnIndex(COLUMN_DURACION))
                    val descanso = cursor.getInt(it.getColumnIndex(COLUMN_DESCANSO))
                    val media = cursor.getString(it.getColumnIndex(COLUMN_MEDIA))
                    val ejercicio = EjerciciosData(id, nombre , descripcion, repeticiones, duracion, descanso, media)
                    ejerciciosList.add(ejercicio)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return ejerciciosList
    }
    fun marcarEntrenamientoComoCompletado(entrenamientoId: Int, fecha: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COMPLETADO, 1) // 1 representa "completado"
        }
        db.update(TABLE_NAME_ENTRENAMIENTOS, values, "$COLUMN_ID = ? AND $COLUMN_FECHA = ?", arrayOf(entrenamientoId.toString(), fecha))
        db.close()
    }
    @SuppressLint("Range")
    fun obtenerEstadoDeRutina(rutinaId: Int, fecha: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_NAME_EJERCICIOS WHERE $COLUMN_ID_RUTINA = $rutinaId"
        val cursor = db.rawQuery(query, null)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val ejercicioId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    if (!todosEntrenamientosCompletadosParaEjercicio(ejercicioId, fecha)) {
                        return false // Si algún entrenamiento no está completado, la rutina no está completa
                    }
                } while (cursor.moveToNext())
            }
            return true // Si todos los entrenamientos están completados, la rutina está completa
        }
    }
    fun todosEntrenamientosCompletadosParaEjercicio(ejercicioId: Int, fecha: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_ENTRENAMIENTOS WHERE $COLUMN_ID_EJERCICIO = $ejercicioId AND $COLUMN_FECHA = '$fecha' AND $COLUMN_COMPLETADO = 1"
        val cursor = db.rawQuery(query, null)
        val todosCompletados = cursor.count > 0
        cursor.close()
        db.close()
        return todosCompletados
    }

    fun obtenerEstadoDeEntrenamiento(entrenamientoId: Int, fecha: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_ENTRENAMIENTOS WHERE $COLUMN_ID = $entrenamientoId AND $COLUMN_FECHA = '$fecha' AND $COLUMN_COMPLETADO = 1"
        val cursor = db.rawQuery(query, null)
        val completado = cursor.count > 0 // Verificar si se encontraron entrenamientos completados
        cursor.close()
        db.close()
        return completado
    }
    @SuppressLint("Range")
    fun obtenerFecha() : String? {
        val db = this.readableDatabase
        val query = "SELECT fecha FROM $TABLE_NAME_ENTRENAMIENTOS LIMIT 1"
        val cursor = db.rawQuery(query, null)
        var fecha: String? = null
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    fecha = cursor.getString(cursor.getColumnIndex(COLUMN_FECHA))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return fecha
    }
    @SuppressLint("Range")
    fun cambiarFechaActual(nuevaFecha:String, rutinaId:Int) {
        val db = this.writableDatabase
        val query = "UPDATE $TABLE_NAME_ENTRENAMIENTOS SET $COLUMN_FECHA = ?, $COLUMN_COMPLETADO = 1 WHERE $COLUMN_ID_EJERCICIO IN (SELECT $COLUMN_ID FROM $TABLE_NAME_EJERCICIOS WHERE $COLUMN_ID_RUTINA = ?)"
        val args = arrayOf(nuevaFecha, rutinaId.toString())
        db.execSQL(query, args)
        db.close()
    }
    @SuppressLint("Range")
    fun obtenerIdRutinaPorIdEjercicio(ejercicioId: Int): Int {
        val db = this.readableDatabase
        var idRutina = -1 // Valor predeterminado en caso de que no se encuentre ninguna coincidencia

        val query = "SELECT $COLUMN_ID_RUTINA FROM $TABLE_NAME_EJERCICIOS WHERE $COLUMN_ID = ?"
        val selectionArgs = arrayOf(ejercicioId.toString())

        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor.moveToFirst()) {
            idRutina = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_RUTINA))
        }

        cursor.close()
        db.close()

        return idRutina
    }
}