package com.grijalvaromero.carritoapp.configs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Conexion(var contexto: Context ): SQLiteOpenHelper(contexto, "carrito", null,1) {


    override fun onCreate(p0: SQLiteDatabase?) {
        var tablaCArrito ="CREATE TABLE carrito(id INTEGER not null primary key autoincrement, id_producto INTEGER, cantidad INTEGER,id_Factura INTEGER)"
        p0?.execSQL(tablaCArrito)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}