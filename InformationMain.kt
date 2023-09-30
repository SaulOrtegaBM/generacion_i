package com.example.generacion_i_g.administrador.information

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.generacion_i_g.R
import com.example.generacion_i_g.databinding.ActivityInfoMainBinding
import com.example.generacion_i_g.datepiker.DatePickerFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InformationMain:AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var  binding:ActivityInfoMainBinding
    private  var  db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInfoMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        val month = resources.getStringArray(R.array.month)
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item,
            month
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@InformationMain
        }

        val contries = resources.getStringArray(R.array.ubicaciojnes)
        val adapter2 = ArrayAdapter(
            this,
            R.layout.list_item,
            contries
        )



        with(binding.autoCompleteTextView2){
            setAdapter(adapter2)
            onItemClickListener = this@InformationMain
        }
        binding.dateEditText.setOnClickListener { showDatePickerDialog() }
        binding.btnDay.setOnClickListener {
            val fecha = binding.dateEditText.text.toString()

            if(fecha.isNotBlank()){
                convertDateToFirestoreRef(fecha)
            }else{
                Toast.makeText(this,"Seleccione Fecha",Toast.LENGTH_SHORT).show()
            }
        }







    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day,month + 1,year) }
        datePicker.show(supportFragmentManager,"datePicker")

    }

    private fun onDateSelected(day:Int,month: Int, year:Int){
        binding.dateEditText.setText("$day/$month/$year" )

    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this,item, Toast.LENGTH_SHORT).show()

    }

    // Función para convertir una fecha en formato "dd/MM/yyyy" a una referencia Firestore
    private fun convertDateToFirestoreRef(date: String) {
        val components = date.split("/")
        if (components.size == 3) {
            val day = components[0]
            val month = components[1]
            val year = components[2]
             db.collection("eventInfoDay").document(day)
                .collection(month).document(year).get().addOnSuccessListener{ documentSnapshot ->
                     if (documentSnapshot.exists()){
                         val getfecha= documentSnapshot.get("cont")
                         binding.tvAsistencias.text = "Total de asistenncias por Dia: " + getfecha.toString()
                     }else{
                         binding.tvAsistencias.text = "No hay Asistencias"
                     }
                 }

        } else {
            throw IllegalArgumentException("Fecha inválida: $date")
        }
    }

}