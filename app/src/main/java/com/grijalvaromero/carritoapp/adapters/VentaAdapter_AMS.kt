package com.grijalvaromero.carritoapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grijalvaromero.carritoapp.FacturaVerActivity
import com.grijalvaromero.carritoapp.R
import com.grijalvaromero.carritoapp.modelos.Venta


class VentaAdapter_AMS(var ventas:ArrayList<Venta>) : RecyclerView.Adapter<VentaAdapter_AMS.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaAdapter_AMS.ViewHolder {

        val vista  = LayoutInflater.from(parent.context).inflate(R.layout.item_venta,parent,false)
        return ViewHolder(vista)

    }

    override fun onBindViewHolder(holder: VentaAdapter_AMS.ViewHolder, position: Int) {

        holder.binItems(ventas[position])
    }

    override fun getItemCount(): Int {

        return  ventas.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {

        }
        fun binItems(venta: Venta){
            //configuracion cuando le dan clic

            itemView.setOnClickListener {

                var intent = Intent(itemView.context, FacturaVerActivity::class.java)
                intent.putExtra("IdRegistro",venta.idVenta)
                itemView.context.startActivity( intent)
            }



            val numFactura = itemView.findViewById<TextView>(R.id.textViewVistaVentaFactura)
            numFactura.text=venta.idVenta.toString()

            val fechaVen= itemView.findViewById<TextView>(R.id.textViewVistaVentaFecha)
           var fecha = venta.fechaVen.split("T00:00:00").toTypedArray()
            fechaVen.text = fecha[0]

            val total= itemView.findViewById<TextView>(R.id.textViewVistaVentaTotal)
            total.text = "$"+ String.format("%.2f", venta.total.toString().toDouble())

        }

    }
}