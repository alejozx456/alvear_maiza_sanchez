package com.grijalvaromero.carritoapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.adapters.DetalleFacturaAdapter_AMS
import com.grijalvaromero.carritoapp.configs.Config_AMS
import com.grijalvaromero.carritoapp.databinding.ActivityFacturaVerBinding
import com.grijalvaromero.carritoapp.modelos.DetalleFactura_AMS
import org.json.JSONObject

lateinit var  IdRegistro:String
private var recyclerView: RecyclerView?=null;
var detalles = ArrayList<DetalleFactura_AMS>()

class FacturaVerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_factura_ver)
        IdRegistro= intent.getStringExtra("IdRegistro")


        val binding = ActivityFacturaVerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detalles.clear()
        recyclerView = binding.ListaDetalleFactura
        recyclerView?.layoutManager = GridLayoutManager(this,1)

        cargarMestro(binding)
        cargarDetalle(binding)
    }

    private fun cargarMestro(binding: ActivityFacturaVerBinding) {

        var configAMS = Config_AMS()
        var url = configAMS.ipServidor+ "Venta"+"/"+ IdRegistro

        var jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { respuesta: JSONObject ->
                var item = respuesta.getJSONObject("data")

                binding.textViewVistaFacturaNumero.text = item.getString("idVenta")
                binding.textViewVistaFacturaIVA.text = "$" + String.format("%.2f",item.getString("iva").toString().toDouble())
                binding.textViewVistaFacturaSubtotal.text =  "$" + String.format("%.2f", item.getString("subtotal").toString().toDouble())
                binding.textViewVistaFacturaTotal.text = "$" +  String.format("%.2f",item.getString("total").toString().toDouble())
                var fecha = item.getString("fechaVen").split("T00:00:00").toTypedArray()
                binding.textViewVistaFacturaFecha.text =  fecha[0]
                var clienteID = item.getString("idCliente")
                cargarCliente(clienteID,binding)

            },
            Response.ErrorListener { },
        )

        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)

    }

    private fun cargarCliente(clienteID: String,binding: ActivityFacturaVerBinding) {
        var configAMS = Config_AMS()
        var url = configAMS.ipServidor+ "cliente"+"/"+ clienteID

        var jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { respuesta: JSONObject ->
                var item = respuesta.getJSONObject("data")
                binding.textViewVistaFacturaCedula.text  = item.getString("cedulaCli").toString()
                binding.textViewVistaFacturaNOmbre.text = item.getString("nombreCli").toString()+" " +item.getString("apellidoCli").toString()


            },
            Response.ErrorListener { },
        )

        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)

    }

    fun cargarDetalle(binding: ActivityFacturaVerBinding){

        var configAMS = Config_AMS()
        var url = configAMS.ipServidor+ "DetalleVenta" +"/"+ IdRegistro

        var jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { respuesta: JSONObject ->
                var datos = respuesta.getJSONArray("data")
                for (i in 0 until datos.length()) {
                    val item = datos.getJSONObject(i)
                    Log.i("detalle",item.getString("idPro").toString())
                    detalles.add(
                        DetalleFactura_AMS(
                            item.getString("idPro"),
                            item.getString("precio"),
                            item.getString("cantidad"),
                            item.getString("totalDeta"),
                            "n/a"
                        )
                    )

                }

                var adapater = DetalleFacturaAdapter_AMS(detalles)
                recyclerView?.adapter = adapater

            },
            Response.ErrorListener { },
        )

        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)

    }


}