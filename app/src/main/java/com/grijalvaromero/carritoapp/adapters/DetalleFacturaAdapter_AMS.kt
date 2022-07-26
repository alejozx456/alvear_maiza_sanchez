package com.grijalvaromero.carritoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.R
import com.grijalvaromero.carritoapp.configs.Config_AMS
import com.grijalvaromero.carritoapp.modelos.DetalleFactura_AMS
import org.json.JSONObject


class DetalleFacturaAdapter_AMS(var detalles:ArrayList<DetalleFactura_AMS>) : RecyclerView.Adapter<DetalleFacturaAdapter_AMS.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleFacturaAdapter_AMS.ViewHolder {

        val vista  = LayoutInflater.from(parent.context).inflate(R.layout.factura_detalle_item,parent,false)
        return ViewHolder(vista)

    }

    override fun onBindViewHolder(holder: DetalleFacturaAdapter_AMS.ViewHolder, position: Int) {

        holder.binItems(detalles[position])
    }

    override fun getItemCount(): Int {

        return  detalles.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {

        }
        fun binItems(detalle: DetalleFactura_AMS){

            val codigo = itemView.findViewById<TextView>(R.id.textViewCodigoPro)
            codigo.text=detalle.IdPro

            val cantidad= itemView.findViewById<TextView>(R.id.textViewCantidadPro)
            cantidad.text = detalle.Cantidad

            val precio= itemView.findViewById<TextView>(R.id.textViewPrecioPro)
            precio.text = String.format("%.2f", detalle.Precio.toString().toDouble())

            val totalDet= itemView.findViewById<TextView>(R.id.textViewTotalDeta)
            totalDet.text =  String.format("%.2f", detalle.TotalDet.toString().toDouble())

            nombreProducto(detalle.IdPro, itemView.context)

        }

        private fun nombreProducto(idPro: String, contexto:Context) {
            var configAMS = Config_AMS()
            var url = configAMS.ipServidor+ "Producto/"+ idPro
            var jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { respuesta: JSONObject ->
                    var item = respuesta.getJSONObject("data")
                    var _nombre = item.getString("nombrePro")
                    val nombre= itemView.findViewById<TextView>(R.id.textViewNombrePro)
                    nombre.text = _nombre.toString()
                },
                Response.ErrorListener { },
            )

            val queue = Volley.newRequestQueue(contexto)
            queue.add(jsonObjectRequest)

        }

    }
}