package com.grijalvaromero.carritoapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.R
import com.grijalvaromero.carritoapp.VerProducto
import com.grijalvaromero.carritoapp.configs.Conexion_AMS
import com.grijalvaromero.carritoapp.configs.Config_AMS
import com.grijalvaromero.carritoapp.modelos.ItemCarrito_AMS
import com.grijalvaromero.carritoapp.modelos.Producto_AMS
import org.json.JSONObject

class CarritoAdapter_AMS (val productos:ArrayList<ItemCarrito_AMS>, val idCliente:String) : RecyclerView.Adapter<CarritoAdapter_AMS.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoAdapter_AMS.ViewHolder {

        val vista  = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito,parent,false)
        return ViewHolder(vista)

    }

    override fun onBindViewHolder(holder: CarritoAdapter_AMS.ViewHolder, position: Int) {

        holder.binItems(productos[position])
    }

    override fun getItemCount(): Int {

        return  productos.size
    }

    fun delete(position: Int, contexto: Context){
        var conexionAMS = Conexion_AMS(contexto)
        var  db = conexionAMS.writableDatabase
        val ItemProducto = productos[position]
       db.delete("carrito","id_producto="+ItemProducto.id,null)
        productos.removeAt(position);
        notifyDataSetChanged()
    }

    fun editar(position: Int, contexto: Context){
        val ItemProducto = productos[position]
        var conexionAMS = Conexion_AMS(contexto)
        var  db = conexionAMS.writableDatabase

        var intent = Intent( contexto,VerProducto::class.java)
        intent.putExtra("Id",ItemProducto.id)
        intent.putExtra("bandera",ItemProducto.cantidad)
        intent.putExtra("idCliente", idCliente)
        db.delete("carrito","id_producto="+ItemProducto.id,null)

        productos.removeAt(position)

        notifyDataSetChanged()
        contexto.startActivity( intent)

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {

        }

        fun binItems(producto: ItemCarrito_AMS){

            val nombre= itemView.findViewById<TextView>(R.id.nombreCarrito)
            val precio= itemView.findViewById<TextView>(R.id.precioCarrito)
            val cantidad= itemView.findViewById<EditText>(R.id.editTextCantidadItem)
            val imagen= itemView.findViewById<ImageView>(R.id.imagenCarrito)
            val total = itemView.findViewById<TextView>(R.id.textViewtotalCarrito)
            val txtCantidadItem= itemView.findViewById<EditText>(R.id.editTextCantidadItem)
            txtCantidadItem.setText(producto.cantidad)
            recargar(itemView.context,producto.id,nombre,precio,imagen,producto.cantidad,total)


        }

        fun recargar(contexto: Context, id:String, nombreVer:TextView,precioVer:TextView,imagen:ImageView, cantidad:String,totalVer:TextView){
            var configAMS = Config_AMS()
            var url = configAMS.ipServidor+ "Producto/"+ id

            var jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { respuesta: JSONObject ->
                    var item = respuesta.getJSONObject("data")
                  var  productoAMS = Producto_AMS(
                        item.getString("idProducto"),
                        item.getString("nombrePro"),
                        item.getString("precioPro"),
                        item.getString("cantidadPro"),
                        item.getString("imagenPro"))
                    nombreVer.text = productoAMS.nombre
                    precioVer.text = "$ "+ String.format("%.2f", productoAMS.precio.toString().toDouble())
                    var total:Double = productoAMS.precio.toDouble() * cantidad.toDouble()
                    totalVer.text = "Total "+"$ "+ String.format("%.2f", total.toString().toDouble())

                    cargarImagen(productoAMS.imagen,imagen,contexto)
                },
                Response.ErrorListener {  },)

            val queue = Volley.newRequestQueue(contexto)
            queue.add(jsonObjectRequest)
        }
        fun cargarImagen(imagenURL:String,imagenVer:ImageView, contexto:Context){

            val queue =  Volley.newRequestQueue(contexto)
            var imageRequest = ImageRequest(imagenURL,
                Response.Listener { respuesta ->
                    imagenVer.setImageBitmap(respuesta)
                },0,0,ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888
                ,Response.ErrorListener {

                })
            queue.add(imageRequest)
        }

    }



}
