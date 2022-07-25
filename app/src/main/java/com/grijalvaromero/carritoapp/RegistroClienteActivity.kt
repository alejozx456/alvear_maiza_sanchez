package com.grijalvaromero.carritoapp

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.configs.Config
import com.grijalvaromero.carritoapp.databinding.ActivityRegistroClienteBinding
import org.json.JSONObject


class RegistroClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_registro_cliente)

        val binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.buttonCliienteRegistrar.setOnClickListener {
        var cedula = binding.editTextClienteCedula.text.toString()
        var clave= binding.editTextClienteClave.text.toString()
        var bandera:Boolean= false



        if(validarCampos(binding)) {
            if(validarCedula_AMS(cedula)){
                if(validarClave(clave)){

                    bandera= true
                }else{
                    Toast.makeText(this,"La clave de tener minimo 4 caracteres, " +
                            "mayuscula, minuscula,numero,y  caracter especial",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"Cedula incorrecta",Toast.LENGTH_LONG).show()
            }

        }else{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Campos Incompletos")
            builder.setMessage("LLene todos los campos")
            builder.setPositiveButton("Aceptar") { dialog, which ->
            }
            builder.show()
        }




        if(bandera){
            var config = Config()
            var url = config.ipServidor+ "Cliente"

            val params = HashMap<String,String>()
            params["cedulaCli"] =  binding.editTextClienteCedula.text.toString()
            params["nombreCli"] = binding.editTextClienteNombre.text.toString()
            params["apellidoCli"] = binding.editTextClienteApellido.text.toString()
            params["direccionCli"] = binding.editTextClienteDireccion.text.toString()
            params["contrasenia"] = binding.editTextClienteClave.text.toString()
            val jsonObject = JSONObject(params as Map<*, *>?)

            // Volley post request with parameters
            val request = JsonObjectRequest(
                Request.Method.POST,url, jsonObject,
                Response.Listener {
                    // Process the json
                    Toast.makeText(applicationContext, "Cliente Insertado con exito", Toast.LENGTH_LONG).show()
                    var inte = Intent(this,LoginActivity::class.java)
                    startActivity(inte)

                }, Response.ErrorListener{
                    // Error in request
                    Toast.makeText(applicationContext, "No se Inserto con exito", Toast.LENGTH_LONG).show()

                })

            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            val queue = Volley.newRequestQueue(this)
            queue.add(request)

        }


    }


    }

    private fun validarCampos(binding: ActivityRegistroClienteBinding): Boolean {

        if (binding.editTextClienteCedula.text.toString().equals("")) return  false
        if (binding.editTextClienteApellido.text.toString().equals("")) return  false
        if (binding.editTextClienteDireccion.text.toString().equals("")) return  false
        if (binding.editTextClienteNombre.text.toString().equals("")) return  false
        if (binding.editTextClienteClave.text.toString().equals("")) return  false

        return true
    }


    private fun validarCedula_AMS(x: String): Boolean {
       return true

    }

    private fun validarClave( clave: String): Boolean {

        var mayuscula = false
        var numero= false;
        var minuscula = false;
        var caracter = false;

        var bandera=false


        if (clave.length>=4){
            for (item in clave)
            {
                Log.i("clave",item.toString())
                if (Character.isDigit(item))   numero = true
                if (Character.isUpperCase(item)) mayuscula = true
                if (Character.isLowerCase(item)) minuscula = true
                if(!Character.isLetterOrDigit(item)) caracter = true
            }

        }else{
            bandera=false
        }

        if (numero && mayuscula && minuscula && caracter)  bandera = true

        return bandera
    }



}