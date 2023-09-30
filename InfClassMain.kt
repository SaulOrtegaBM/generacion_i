package com.example.generacion_i_g.administrador.information

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.generacion_i_g.databinding.ActivityInfClassBinding

class InfClassMain:AppCompatActivity() {
    lateinit var  binding:ActivityInfClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



//
//    binding.btnmonth.setOnClickListener {
//        val mes = binding.autoCompleteTextView.text.toString()
//        if (mes.isNotBlank()){
//            db.collection("eventInfMonth").document(mes).get()
//                .addOnSuccessListener { documentSnapshot ->
//                    if (documentSnapshot.exists()){
//                        binding.resultado.text = "Asistencias por mes: " + documentSnapshot.get("cont").toString()
//
//                    }else{
//                        binding.resultado.text = "No hay Asistencias"
//                    }
//
//                }.addOnFailureListener {
//                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show() }
//
//            db.collection("eventCreateInf").document(mes).get().addOnSuccessListener { ds->
//                if (ds.exists()){
//                    binding.resultadoMes.text = "Clases Creadas por mes: " + ds.get("cont").toString()
//                }else{
//                    binding.resultado.text = "No hay Clases Creadas "
//                }
//            }.addOnFailureListener {  Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()  }
//        }else{
//            Toast.makeText(this,"Seleccione Mes", Toast.LENGTH_SHORT).show()
//        }
//
//
//    }
}