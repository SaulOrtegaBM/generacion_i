package com.example.generacion_i_g.administrador.information

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.generacion_i_g.databinding.ActivityManuInfoBinding

class MenuInfoMain:AppCompatActivity() {
    lateinit var binding:ActivityManuInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManuInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.asistenciaday.setOnClickListener {
            val intent = Intent(this,InformationMain::class.java).apply {

            }
            startActivity(intent)
        }

        binding.asistenciaClass.setOnClickListener {
            val intent = Intent(this,InformationMain::class.java).apply {

            }
            startActivity(intent)
        }


    }
}