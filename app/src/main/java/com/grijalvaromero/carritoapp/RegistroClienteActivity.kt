package com.grijalvaromero.carritoapp

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
import com.grijalvaromero.carritoapp.configs.Config_AMS
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



        if(validarCampos_AMS(binding)) {
            if(validarCedula_AMS(cedula)){
                if(validacionClave_AMS(clave)){

                    bandera= true
                }else{
                    Toast.makeText(this,"La clave de tener minimo 6 caracteres, " +
                            "2 mayusculas, 2 minusculas ,numero,y  caracter especial",Toast.LENGTH_LONG).show()
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
            var configAMS = Config_AMS()
            var url = configAMS.ipServidor+ "Cliente"

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
                    Toast.makeText(applicationContext, "Usuario Insertado con exito", Toast.LENGTH_LONG).show()
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

    private fun validarCampos_AMS(binding: ActivityRegistroClienteBinding): Boolean {

        if (binding.editTextClienteCedula.text.toString().equals("")) return  false
        if (binding.editTextClienteApellido.text.toString().equals("")) return  false
        if (binding.editTextClienteDireccion.text.toString().equals("")) return  false
        if (binding.editTextClienteNombre.text.toString().equals("")) return  false
        if (binding.editTextClienteClave.text.toString().equals("")) return  false

        return true
    }


    private fun validarCedula_AMS(cedula: String): Boolean {
        var cedulavalida_AMS = false

        try {
            if (cedula.length === 10)
            {
                val tercerd_AMS = cedula.substring(2, 3).toInt()
                if (tercerd_AMS < 6) {
                    val validar_AMS = intArrayOf(2, 1, 2, 1, 2, 1, 2, 1, 2)
                    val ver_AMS = cedula.substring(9, 10).toInt()
                    var comprobrarSuma_AMS = 0
                    var digito = 0
                    for (i in 0 until cedula.length - 1) {
                        digito = cedula.substring(i, i + 1).toInt() * validar_AMS[i]
                        comprobrarSuma_AMS += digito % 10 + digito / 10
                    }
                    if (comprobrarSuma_AMS % 10 == 0 && comprobrarSuma_AMS % 10 == ver_AMS) {
                        cedulavalida_AMS = true
                    } else if (10 - comprobrarSuma_AMS % 10 == ver_AMS) {
                        cedulavalida_AMS = true
                    } else {
                        cedulavalida_AMS = false
                    }
                } else {
                    cedulavalida_AMS = false
                }
            } else {
                cedulavalida_AMS = false
            }
        } catch (nfe: NumberFormatException) {
            cedulavalida_AMS = false
        } catch (err: Exception) {
            // println("Una excepcion ocurrio en el proceso de validadcion")
            cedulavalida_AMS = false
        }
        if (!cedulavalida_AMS) {
            println("La Cédula ingresada no es correcta")
        }
        return cedulavalida_AMS
    }

    private fun validarClave_AMS( clave: String): Boolean {

        var mayuscula = false
        var numero= false;
        var minuscula = false;
        var caracter = false;

        var bandera=false


        if (clave.length in 6..10){
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

    private fun validacionClave_AMS(clave:String):Boolean{
        val pattern=Regex("^(?=.*?[A-Z]{2,})(?=.*?[a-z]{2,})(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{6,10}$",RegexOption.IGNORE_CASE)
        return pattern.containsMatchIn(clave)


    }



}