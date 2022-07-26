package com.grijalvaromero.carritoapp.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.R
import com.grijalvaromero.carritoapp.VerProducto
import com.grijalvaromero.carritoapp.modelos.Producto_AMS

class ProductoAdapter_AMS(var productoAMS:ArrayList<Producto_AMS>) : RecyclerView.Adapter<ProductoAdapter_AMS.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoAdapter_AMS.ViewHolder {

        val vista  = LayoutInflater.from(parent.context).inflate(R.layout.item_producto,parent,false)
        return ViewHolder(vista)

    }

    override fun onBindViewHolder(holder: ProductoAdapter_AMS.ViewHolder, position: Int) {

        holder.binItems(productoAMS[position])
    }

    override fun getItemCount(): Int {

        return  productoAMS.size
    }

    fun setFilterList(_productoAMS:ArrayList<Producto_AMS>){
        this.productoAMS=_productoAMS
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
     init {

     }
        fun binItems(productoAMS: Producto_AMS){

            //configuracion cuando le dan clic
            itemView.setOnClickListener {

                var intent = Intent(itemView.context,VerProducto::class.java)
                intent.putExtra("Id",productoAMS.id)
                intent.putExtra("bandera","0")
                itemView.context.startActivity( intent)
            }

            val codigo = itemView.findViewById<TextView>(R.id.textViewCodigo)
            codigo.text=productoAMS.id

            val nombre= itemView.findViewById<TextView>(R.id.item_nombre)
            nombre.text =productoAMS.nombre

            val precio= itemView.findViewById<TextView>(R.id.item_precio)
            precio.text = "$"+ String.format("%.2f", productoAMS.precio.toString().toDouble())

            val stock= itemView.findViewById<TextView>(R.id.item_stock)
            stock.text =productoAMS.stock

            val imagen= itemView.findViewById<ImageView>(R.id.item_imagen)
            val queue =  Volley.newRequestQueue(itemView.context)
            var imageRequest = ImageRequest(productoAMS.imagen,
                Response.Listener { respuesta ->
                    imagen.setImageBitmap(respuesta)

                },0,0,ImageView.ScaleType.FIT_XY,Bitmap.Config.ARGB_8888
                ,Response.ErrorListener {

                })

            queue.add(imageRequest)
        }

    }
}