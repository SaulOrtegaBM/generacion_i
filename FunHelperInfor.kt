package com.example.generacion_i_g.domain

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*

class FunHelperInfor{
    private val db = FirebaseFirestore.getInstance()

    fun obtenerFechaActual(): String {
        val calendar = Calendar.getInstance()
        val formato = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        return formato.format(calendar.time)
    }
    fun crearOactualiza(idDocument:String){
        val actualiza = hashMapOf(
            "cont" to FieldValue.increment(1)
        )
        db.collection("eventInfoDay").document(idDocument).set(actualiza, SetOptions.merge())
            .addOnSuccessListener {
                val month = optenermes()
                db.collection("eventInfMonth").document(month).update("cont", FieldValue.increment(1))
                    .addOnSuccessListener {  }
                    .addOnFailureListener {}
            }
            .addOnFailureListener { }

    }

    fun crearOactualizaClass(date:String,cambi:String){
        val month = optenermes()
        val dates = obtenerFechaActual()
//        val documentRef = db.collection(cambi).document(month).collection(date)
        val docuF = db.collection(cambi).document(month).collection(dates)


        docuF.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // El documento no existe, crea uno nuevo con un contador inicializado en 1
                    val contadorData = hashMapOf(
                        "contador" to 1
                    )

                    db.collection(cambi).document(month).collection(dates).document().set(contadorData)
                        .addOnSuccessListener {
                            // Documento creado con éxito
                        }
                        .addOnFailureListener { e ->
                            // Manejar errores al crear el documento
                        }
                } else {
                    // El documento ya existe, no se hace nada.
                }
            }
            .addOnFailureListener { e ->
                // Manejar errores al verificar el documento
            }
    }











    fun optenermes():String{
        val calendar = Calendar.getInstance()
        val numberMoth = calendar.get(Calendar.MONTH)

        val nameMonth = arrayOf(
            "Enero","Febrero","Marzo","Aril","Mayo","Junio", "Julio", "Agosto","Septiembre", "Octubre", "Noviembre", "Diciembre"
        )

        return nameMonth[numberMoth]
    }
}