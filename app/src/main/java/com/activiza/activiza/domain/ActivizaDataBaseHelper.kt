package com.activiza.activiza.domain

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.data.UserSettings
import com.activiza.activiza.data.UsuarioData
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

            //create usuario
            //private const val COLUMN_ID = "id"
            private const val TABLE_NAME_USUARIOS = "usuarios"
            //private const val COLUMN_NAME = "nombre"
            private const val COLUMN_TOKEN = "token"
            //private const val COLUMN_ENTRENADOR = "entrenador"
            private const val COLUMN_PASSWORD = "password"

            //create Detalles
            private const val TABLE_NAME_DETALLE_USUARIOS = "detalles_usuarios"
            private const val COLUMN_ALTURA = "altura"
            private const val COLUMN_PESO = "peso"
            private const val COLUMN_GENERO = "genero"
            private const val COLUMN_OBJETIVO = "objetivo"
            private const val COLUMN_ID_USUARIO = "id_usuario"

            //create user settings
            private const val TABLE_USER_SETTINGS = "user_settings"
            private const val COLUMN_VOLUME = "volume"
            private const val COLUMN_NOTIFICATIONS = "notifications"
            private const val COLUMN_VIBRATION = "vibration"
            private const val COLUMN_DARK_MODE = "dark_mode"


        }

    override fun onCreate(db: SQLiteDatabase?) {
        //Tabla con todas las Rutinas
        val createTableRutinas =
            "CREATE TABLE $TABLE_NAME_RUTINAS (" +
                    "$COLUMN_ID integer PRIMARY KEY," +
                    "$COLUMN_NAME varchar(255), " +
                    "$COLUMN_ENTRENADOR varchar(255), " +
                    "$COLUMN_DESCRIPCION TEXT, " +
                    "$COLUMN_MEDIA varchar(255))"
        db?.execSQL(createTableRutinas)

        // Crear la tabla para los Ejercicios
        val createEjercicios = "CREATE TABLE $TABLE_NAME_EJERCICIOS (" +
                "$COLUMN_ID INTEGER  PRIMARY KEY," +
                "$COLUMN_ID_RUTINA INTEGER," +
                "$COLUMN_DESCRIPCION TEXT," +
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

        val CREATE_TABLE_USUARIOS = ("CREATE TABLE " + TABLE_NAME_USUARIOS + "("
                + COLUMN_TOKEN + " TEXT NOT NULL,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_ENTRENADOR + " BOOLEAN NOT NULL"
                + ")")
        db?.execSQL(CREATE_TABLE_USUARIOS)

        val CREATE_TABLE_DETALLES_USUARIOS = ("CREATE TABLE " + TABLE_NAME_DETALLE_USUARIOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ALTURA + " REAL,"
                + COLUMN_PESO + " REAL,"
                + COLUMN_GENERO + " TEXT,"
                + COLUMN_OBJETIVO + " TEXT,"
                + COLUMN_ID_USUARIO + " INTEGER,"
                + " FOREIGN KEY (" + COLUMN_ID_USUARIO + ") REFERENCES " + TABLE_NAME_USUARIOS + "(" + COLUMN_ID + ")"
                + ")")
        db?.execSQL(CREATE_TABLE_DETALLES_USUARIOS)

        //create user settings
        val createTableUserSettings = ("CREATE TABLE $TABLE_USER_SETTINGS (" +
                "$COLUMN_TOKEN TEXT NOT NULL," +
                "$COLUMN_VOLUME INTEGER," +
                "$COLUMN_NOTIFICATIONS BOOLEAN," +
                "$COLUMN_VIBRATION BOOLEAN," +
                "$COLUMN_DARK_MODE BOOLEAN)")

        db?.execSQL(createTableUserSettings)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME_RUTINAS"
        db?.execSQL(dropTableQuery)
        val dropTableQueryTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME_EJERCICIOS"
        db?.execSQL(dropTableQueryTableQuery)
        val dropTableQueryEntrenamientos = "DROP TABLE IF EXISTS $TABLE_NAME_ENTRENAMIENTOS"
        db?.execSQL(dropTableQueryEntrenamientos)
        val dropTableQueryUsuarios = "DROP TABLE IF EXISTS $TABLE_NAME_USUARIOS"
        db?.execSQL(dropTableQueryUsuarios)
        val dropTableQueryDetalleUsuarios = "DROP TABLE IF EXISTS $TABLE_NAME_DETALLE_USUARIOS"
        db?.execSQL(dropTableQueryDetalleUsuarios)
        onCreate(db)
    }
    fun insertRutina(rutinas: RutinaData) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, rutinas.id)
            put(COLUMN_NAME, rutinas.nombre)
            put(COLUMN_ENTRENADOR, rutinas.entrenador)
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
    fun insertUsuario(usuario: UsuarioData) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, usuario.nombre)
            put(COLUMN_TOKEN, usuario.token)
            put(COLUMN_ENTRENADOR, usuario.entrenador)
            put(COLUMN_PASSWORD, usuario.password)
            // Aquí podrías incluir más columnas si deseas
        }
        db.insert(TABLE_NAME_USUARIOS, null, values)
        db.close()
    }
    fun insertDetallesUsuario(detalles: DetallesUsuarioData, id:Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ALTURA, detalles.altura)
            put(COLUMN_PESO, detalles.peso)
            put(COLUMN_GENERO, detalles.genero)
            put(COLUMN_OBJETIVO, detalles.objetivo)
            put(COLUMN_ID_USUARIO, id)
            // Aquí podrías incluir más columnas si deseas
        }
        db.insert(TABLE_NAME_DETALLE_USUARIOS, null, values)
        db.close()
    }
    fun getDetallesUsuario(): DetallesUsuarioData? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_DETALLE_USUARIOS"
        val cursor = db.rawQuery(query, null)

        var detallesUsuario: DetallesUsuarioData? = null

        if (cursor.moveToFirst()) {
            val altura = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ALTURA))
            val peso = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PESO))
            val genero = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENERO))
            val objetivo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBJETIVO))

            detallesUsuario = DetallesUsuarioData(altura, peso, genero, objetivo)
        }

        cursor.close()
        db.close()

        return detallesUsuario
    }

    @SuppressLint("Range")
    fun getUsuario(): UsuarioData? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME_USUARIOS,
            arrayOf(COLUMN_TOKEN, COLUMN_NAME, COLUMN_PASSWORD, COLUMN_ENTRENADOR),
            null,
            null,
            null,
            null,
            null
        )

        var usuario: UsuarioData? = null

        if (cursor.moveToFirst()) {
            val token = cursor.getString(cursor.getColumnIndex(COLUMN_TOKEN))
            val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val valorEntero = cursor.getInt(cursor.getColumnIndex(COLUMN_ENTRENADOR))
            val entrenador = valorEntero == 1
            usuario = UsuarioData(token, nombre, password, entrenador)
        }

        cursor.close()
        db.close()
        return usuario
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
                val descripcion = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                val media = it.getString(it.getColumnIndexOrThrow(COLUMN_MEDIA))

                // Aquí debes manejar la lista de ejercicios, dependiendo de cómo estén almacenados en la base de datos

                rutina = RutinaData(id, nombre, descripcion, entrenador, listOf(), media)
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
        val query = "SELECT * FROM $TABLE_NAME_ENTRENAMIENTOS WHERE $COLUMN_ID = ? AND $COLUMN_FECHA = ? AND $COLUMN_COMPLETADO = 1"
        val cursor = db.rawQuery(query, arrayOf(entrenamientoId.toString(), fecha))
        val completado = cursor.count > 0 // Verificar si se encontraron entrenamientos completados
        cursor.close()
        db.close()
        return completado
    }
    fun obtenerCantidadEntrenamientosCompletados(): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_NAME_ENTRENAMIENTOS WHERE $COLUMN_COMPLETADO = 1"
        val cursor = db.rawQuery(query, null)
        var count = 0
        cursor.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        cursor.close()
        db.close()
        return count
    }
    @SuppressLint("Range")
    fun obtenerIdEntrenamientoPorIdEjercicio(ejercicioId: Int): Int {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_NAME_ENTRENAMIENTOS WHERE $COLUMN_ID_EJERCICIO = $ejercicioId LIMIT 1"
        val cursor = db.rawQuery(query, null)
        var entrenamientoId = -1 // Valor predeterminado si no se encuentra ningún entrenamiento para el ejercicio
        cursor.use {
            if (it.moveToFirst()) {
                entrenamientoId = it.getInt(it.getColumnIndex(COLUMN_ID))
            }
        }
        db.close()
        return entrenamientoId
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
        val query = "UPDATE $TABLE_NAME_ENTRENAMIENTOS SET $COLUMN_FECHA = ?, $COLUMN_COMPLETADO = 0 WHERE $COLUMN_ID_EJERCICIO IN (SELECT $COLUMN_ID FROM $TABLE_NAME_EJERCICIOS WHERE $COLUMN_ID_RUTINA = ?)"
        val args = arrayOf(nuevaFecha, rutinaId.toString())
        db.execSQL(query, args)
        db.close()
    }
    fun updateUsuario(usuario: UsuarioData): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TOKEN, usuario.token)
            put(COLUMN_NAME, usuario.nombre)
            put(COLUMN_PASSWORD, usuario.password)
            put(COLUMN_ENTRENADOR, usuario.entrenador)
        }

        val rowsAffected = db.update(
            TABLE_NAME_USUARIOS,
            values,
            "$COLUMN_TOKEN = ?",
            arrayOf(usuario.nombre.toString())
        )

        db.close()
        return rowsAffected > 0
    }
    fun obtenerToken(): String {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_TOKEN FROM $TABLE_NAME_USUARIOS"
        val cursor = db.rawQuery(query, null)
        var count:String = ""
        cursor.use {
            if (it.moveToFirst()) {
                count = it.getString(0)
            }
        }
        cursor.close()
        db.close()
        return count
    }


    fun borrarUsuario(token: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME_USUARIOS, "$COLUMN_TOKEN = ?", arrayOf(token))
        db.delete(TABLE_NAME_DETALLE_USUARIOS, "$COLUMN_ID_USUARIO = (SELECT $COLUMN_ID FROM $TABLE_NAME_USUARIOS WHERE $COLUMN_TOKEN = ?)", arrayOf(token))
        db.close()
    }


}