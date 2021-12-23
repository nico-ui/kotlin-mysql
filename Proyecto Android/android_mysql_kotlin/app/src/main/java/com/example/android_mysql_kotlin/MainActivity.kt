package com.example.android_mysql_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    var txtk_nombre:EditText?=null;
    var txtk_email:EditText?=null;
    var txtk_telefono:EditText?=null;
    var txtk_pass:EditText?=null;
    var tbUsuarios: TableLayout?=null
    var idGlobal:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtk_nombre = findViewById(R.id.txt_nombre)
        txtk_email = findViewById(R.id.txt_email)
        txtk_telefono = findViewById(R.id.txt_telefono)
        txtk_pass = findViewById(R.id.txt_pass)
        tbUsuarios=findViewById(R.id.tbl)
        /*for (i in 0 until 5){
           val registro= LayoutInflater.from(this).inflate(R.layout.table_row,null,false)
           val colNombre=registro.findViewById<View>(R.id.txtNombreCol) as TextView
           val colEmail=registro.findViewById<View>(R.id.txtEmailCol) as TextView
           val colEditar=registro.findViewById<View>(R.id.btnEditarCol)
           val colBorrar=registro.findViewById<View>(R.id.btnBorrarCol)
           colNombre.text="Nombre $i"
           colEmail.text="Email $i"
           colEditar.id=i
           colBorrar.id=i
           tbUsuarios?.addView(registro)
       }*/
        refrescarTabla()
    }
    fun refrescarTabla(){
        tbUsuarios?.removeAllViews()
        var queue=Volley.newRequestQueue(this)
        var url="http://192.168.1.70/android_mysql/consultaTodo.php"
        var jsonObjectRequest= JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { response ->
                try {
                    var jsonArray=response.getJSONArray("data")
                    for(i in 0 until jsonArray.length() ){
                        var jsonObject=jsonArray.getJSONObject(i)
                        val registro= LayoutInflater.from(this).inflate(R.layout.table_row,null,false)
                        val colNombre=registro.findViewById<View>(R.id.txtNombreCol) as TextView
                        val colEmail=registro.findViewById<View>(R.id.txtEmailCol) as TextView
                        val colEditar=registro.findViewById<View>(R.id.btnEditarCol)
                        val colBorrar=registro.findViewById<View>(R.id.btnBorrarCol)
                        colNombre.text=jsonObject.getString("nombre")
                        colEmail.text=jsonObject.getString("email")
                        colEditar.id=jsonObject.getString("id").toInt()
                        colBorrar.id=jsonObject.getString("id").toInt()
                        tbUsuarios?.addView(registro)
                    }
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            },Response.ErrorListener { error ->

            }
        )
        queue.add(jsonObjectRequest)
    }

    fun clickTablaEditar(view: View){
        idGlobal=view.id.toString()
        val queue= Volley.newRequestQueue(this)
        val url="http://192.168.1.70/android_mysql/consulta.php?id=$idGlobal"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->
                txtk_nombre?.setText(response.getString("nombre"))
                txtk_email?.setText(response.getString("email"))
                txtk_telefono?.setText(response.getString("telefono"))
                txtk_pass?.setText(response.getString("pass"))
            },Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)
        //Toast.makeText(this,view.id.toString(),Toast.LENGTH_LONG).show()
    }
    fun clickbtnEditar(view: View){
        val url="http://192.168.1.70/android_mysql/modificar.php"
        val queue=Volley.newRequestQueue(this)
        val resultadoPost = object : StringRequest(Request.Method.POST,url,
            Response.Listener { response ->
                Toast.makeText(this,"El usuario ha sido editado de forma exitosa",Toast.LENGTH_LONG).show()
                refrescarTabla()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Error al editar el usuario $error",Toast.LENGTH_LONG).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String,String>()
                parametros.put("id",idGlobal!!)
                parametros.put("nombre",txtk_nombre?.text.toString())
                parametros.put("telefono",txtk_telefono?.text.toString())
                parametros.put("email",txtk_email?.text.toString())
                parametros.put("pass",txtk_pass?.text.toString())
                return parametros
            }
        }
        queue.add(resultadoPost)
    }
    fun clickTablaBorrar(view: View){
        val url="http://192.168.1.70/android_mysql/borrar.php"
        val queue=Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST,url,
            Response.Listener { response ->
                Toast.makeText(this,"El usuario se creo de forma exitosa",Toast.LENGTH_LONG).show()
                refrescarTabla()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Error al crear el usuario $error",Toast.LENGTH_LONG).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String,String>()
                parametros.put("id",view.id.toString())
                return parametros
            }
        }
        queue.add(resultadoPost)
        //Toast.makeText(this,view.id.toString(),Toast.LENGTH_LONG).show()
    }
    fun clickbtnInsertar(view: View){
        var url="http://192.168.1.70/android_mysql/insertar.php"
        val queue=Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST,url,
                Response.Listener<String> { response ->
                    Toast.makeText(this,"Usuario insertado exitosamente",Toast.LENGTH_LONG).show()
                    refrescarTabla()
                },Response.ErrorListener { error ->
            Toast.makeText(this,"Error $error ",Toast.LENGTH_LONG).show()
        }){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String,String>()
                parametros.put("nombre",txtk_nombre?.text.toString())
                parametros.put("email",txtk_email?.text.toString())
                parametros.put("telefono",txtk_telefono?.text.toString())
                parametros.put("pass",txtk_pass?.text.toString())
                return parametros
            }
        }
        queue.add(resultadoPost)
    }
    /*
    fun clickBuscar(view: View){
        var txtId=findViewById<EditText>(R.id.txt_id)
        var intent= Intent(this,MainActivity2::class.java)
        intent.putExtra("id",txtId.text.toString())
        startActivity(intent)
    }
    */
    fun clickLimpiar(view:View){
        txtk_nombre?.setText("")
        txtk_email?.setText("")
        txtk_telefono?.setText("")
        txtk_pass?.setText("")
    }

}