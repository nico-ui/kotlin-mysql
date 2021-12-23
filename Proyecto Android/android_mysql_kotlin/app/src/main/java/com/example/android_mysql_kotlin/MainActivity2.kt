package com.example.android_mysql_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity2 : AppCompatActivity() {
    var txtNombre2: EditText?=null
    var txtEmail2: EditText?=null
    var txtTelefono2: EditText?=null
    var txtPass2: EditText?=null
    var lblId: TextView?=null
    var id:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        txtNombre2=findViewById(R.id.txt_nombre2)
        txtEmail2=findViewById(R.id.txt_email2)
        txtTelefono2=findViewById(R.id.txt_telefono2)
        txtPass2=findViewById(R.id.txt_pass2)
        lblId=findViewById(R.id.lblId)
        id=intent.getStringExtra("id").toString()
        lblId?.setText(id)
        val queue= Volley.newRequestQueue(this) 
        val url="http://192.168.1.70/android_mysql/consulta.php?id=$id"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->
                txtNombre2?.setText(response.getString("nombre"))
                txtEmail2?.setText(response.getString("email"))
                txtTelefono2?.setText(response.getString("telefono"))
                txtPass2?.setText(response.getString("pass"))
            },Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)
    }

    fun clickRegresar(view: View){
        var intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun clickEditar(view: View){
        val url="http://192.168.1.70/android_mysql/modificar.php"
        val queue=Volley.newRequestQueue(this)
        val resultadoPost = object : StringRequest(Request.Method.POST,url,
                Response.Listener { response ->
                    Toast.makeText(this,"El usuario ha sido editado de forma exitosa",Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this,"Error al editar el usuario $error",Toast.LENGTH_LONG).show()
                }
        ){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String,String>()
                parametros.put("id",id!!)
                parametros.put("nombre",txtNombre2?.text.toString())
                parametros.put("telefono",txtTelefono2?.text.toString())
                parametros.put("email",txtEmail2?.text.toString())
                parametros.put("pass",txtPass2?.text.toString())
                return parametros
            }
        }
        queue.add(resultadoPost)
    }
    fun clickBorrar(view:View){
        val url="http://192.168.1.70/android_mysql/borrar.php"
        val queue=Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST,url,
            Response.Listener { response ->
                Toast.makeText(this,"El usuario se creo de forma exitosa",Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Error al crear el usuario $error",Toast.LENGTH_LONG).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String,String>()
                parametros.put("id",id!!)
                return parametros
            }
        }
        queue.add(resultadoPost)
    }
}