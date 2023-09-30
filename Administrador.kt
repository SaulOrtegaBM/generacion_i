package com.example.generacion_i_g.administrador.menuadmin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.generacion_i_g.databinding.ActivityHomaAdminBinding
import com.google.android.material.navigation.NavigationView
import com.example.generacion_i_g.R
import com.example.generacion_i_g.administrador.addperson.AddPersonActivity
import com.example.generacion_i_g.administrador.eventos.MainCrudActivity
import com.example.generacion_i_g.administrador.information.InformationMain
import com.example.generacion_i_g.administrador.information.MenuInfoMain
import com.example.generacion_i_g.administrador.photoAdmin.PhotoAddActivity
import com.example.generacion_i_g.administrador.photoAdmin.VideoPhotos
import com.example.generacion_i_g.administrador.roles.AddRoles
import com.example.generacion_i_g.administrador.scaner.MainScaner
import com.example.generacion_i_g.domain.FunHelperInfor
import com.example.generacion_i_g.foto.FotosMainActivity
import com.example.generacion_i_g.maps.MainMapsActivity
import com.example.generacion_i_g.message.MainMesageActivity
import com.example.generacion_i_g.mychats.MyChatsAdminActivity
import com.example.generacion_i_g.options.OptionsMainActivity
import com.example.generacion_i_g.profile.Profile
import com.example.generacion_i_g.ui.login.LoginActivity
import com.example.generacion_i_g.videos.VideosMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_homa_admin.textViewwhere

@Suppress("DEPRECATION")

class Administrador:AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var  binding:ActivityHomaAdminBinding
    private lateinit var  toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var navView:NavigationView
    private val email = Firebase.auth.currentUser?.email.toString()
    private lateinit var  auth: FirebaseAuth
    private  var  db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomaAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.layoutAsis.setOnClickListener { initScanner() }
        binding.eventosgo.setOnClickListener { showEventos() }
        binding.verprofile.setOnClickListener { showProfile() }
        binding.gomaps.setOnClickListener { showMaps() }
        binding.messgaprop.setOnClickListener { showUsersRoles() }
        binding.roles.setOnClickListener { showAddRoles() }
        binding.fotos.setOnClickListener { showAddPhotos() }
        binding.verfotos.setOnClickListener { showfotos() }
        binding.addvideos.setOnClickListener { showAddVideos() }
        binding.vervideo.setOnClickListener { showVidMain() }
        binding.logross.setOnClickListener {
            Toast.makeText(this,"Funcion no dispobible",Toast.LENGTH_SHORT).show()
        }
        binding.gys.setOnClickListener { showAddPerson() }
        binding.imageView13.setOnClickListener { showVerMessages() }
        binding.imageView14.setOnClickListener { showOpcions() }
        binding.verInformacion.setOnClickListener { showInfoMenu() }
        drawer = binding.layoudAdmin
        toggle = ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = binding.navView2
        navigationView.setNavigationItemSelectedListener(this)
        main(email)


    }
    private fun main(email:String){

        val ref =db.collection("users").document(email)
        ref.addSnapshotListener { snapshot, error ->
            if (error!= null){
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()){
                binding.textView8.text = snapshot.getString("name").toString()+ " " +  snapshot.getString("apellidom").toString()
                navView = binding.navView2
                val headerView = navView.getHeaderView(0)
                val nombreMenuFirebase: TextView = headerView.findViewById(R.id.nombre_menu_firebase)
                nombreMenuFirebase.text = snapshot.getString("name").toString() + " " + snapshot.getString("apellidom").toString()
                val tipocuentafire:TextView = headerView.findViewById(R.id.tipo_de_cuenta_fire)
                binding.textView9.text = snapshot.getString("tipoCuenta").toString()
                tipocuentafire.text =snapshot.getString("tipoCuenta").toString()
                textViewwhere.text = snapshot.getString("cambi").toString()
                val uris =  snapshot.get("urlImagen").toString()
                val uri: Uri = Uri.parse(uris)
                actualizarInterfaz(uri)
            }
        }

    }

    private fun actualizarInterfaz(photoUrl: Uri){
        Glide
            .with(this)
            .load(photoUrl)
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.ic_perfil)
            .into(binding.imageView12)
        navView = binding.navView2
        val headerView = navView.getHeaderView(0)
        val navheaderimg: ImageView= headerView.findViewById(R.id.nav_header_imageView)
        Glide
            .with(this)
            .load(photoUrl)
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.ic_perfil)
            .into(navheaderimg)
    }

    private fun initScanner() {
        IntentIntegrator(this).initiateScan()

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        //resivimos el valor  que el lector qr leyo en este caso es elpphone de la otra persona que lo ocupamos comoo key para acceder al crud
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            } else {
                val ch = result.contents
                val db = FirebaseFirestore.getInstance()
                val classfun = FunHelperInfor()
                val dataUpdate = hashMapOf(
                    "contLogros" to FieldValue.increment(1),
                    "contMenLogros" to FieldValue.increment(1)
                )
                db.collection("users").document(ch?:String()).update(dataUpdate as Map<String, Any>)
                    .addOnSuccessListener {
                        val date = classfun.obtenerFechaActual()
                        val cambi = binding.textViewwhere.text.toString()

                        classfun.crearOactualiza(date)
                        classfun.crearOactualizaClass(date, cambi)

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Surgio un error", Toast.LENGTH_SHORT).show()
                    }



                Toast.makeText(
                    this,
                    "el valor escaneado es: $ch",
                    Toast.LENGTH_SHORT
                ).show()

            }
        } else {

            super.onActivityResult(requestCode, resultCode, data)
        }
    }







    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        when(item.itemId){
            R.id.nav_Eventos -> showEventos()
            R.id.nav_Asistencia -> showAsistencia()
            R.id.nav_messagess -> showUsersRoles()
            R.id.nav_Roles -> showAddRoles()
            R.id.nav_logros -> showPhones()
            R.id.nav_profile -> showProfile()
            R.id.nav_serrarsecion -> signOut()
            R.id.nav_videos -> showVidMain()
            R.id.nave_maps -> showMaps()
            R.id.nav_addVideos ->showAddVideos()
            R.id.nav_addPhotos ->showAddPhotos()
            R.id.nav_verPotho -> showfotos()
            R.id.nav_verAddPerson -> showAddPerson()
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun  showAsistencia(){
        val homeIntent = Intent(this,MainScaner::class.java).apply {

        }
        startActivity(homeIntent)

    }

    private fun  showEventos(){
        val homeIntent = Intent(this,MainCrudActivity::class.java).apply {

        }
        startActivity(homeIntent)

    }

    private fun  showMaps(){
        val homeIntent = Intent(this,MainMapsActivity::class.java).apply {

        }
        startActivity(homeIntent)

    }

    private fun  showProfile(){
        val homeIntent = Intent(this,Profile::class.java).apply {

            val content = binding.textView9.text.toString()
            putExtra("email", email)
            putExtra("rol",content)
//            Toast.makeText(this,content.toString(),Toast.LENGTH_SHORT).show()

        }
        startActivity(homeIntent)

    }



    private fun  showAddRoles(){
        val homeIntent = Intent(this,AddRoles::class.java).apply {

        }
        startActivity(homeIntent)

    }

    private fun  showUsersRoles(){
        val homeIntent = Intent(this,MainMesageActivity::class.java).apply {

        }
        startActivity(homeIntent)

    }

    private fun  showPhones(){
        Toast.makeText(this,"Modulo no disponible",Toast.LENGTH_SHORT).show()

    }



    private fun  showAddPhotos(){
        val homeIntent = Intent(this,PhotoAddActivity::class.java).apply {

        }
        startActivity(homeIntent)

    }

    private fun  showAddVideos(){
        val homeIntent = Intent(this,VideoPhotos::class.java).apply {

        }
        startActivity(homeIntent)

    }
    private fun  showVidMain(){
        val homeIntent = Intent(this,VideosMainActivity::class.java).apply {

        }
        startActivity(homeIntent)

    }

    private fun  showfotos(){
        val homeIntent = Intent(this,FotosMainActivity::class.java).apply {

        }
        startActivity(homeIntent)

    }
    private fun  showAddPerson(){
        val homeIntent = Intent(this,AddPersonActivity::class.java).apply {

        }
        startActivity(homeIntent)

    }
    private fun showVerMessages(){
        val message = Intent(this,MyChatsAdminActivity::class.java).apply{
                putExtra("email", email)

        }
        this.startActivity(message)

    }
    private fun showOpcions(){
        val content = binding.textView9.text.toString()
//            Toast.makeText(this,content.toString(),Toast.LENGTH_SHORT).show()
        val  options = Intent(this,OptionsMainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("rol",content)
        }
        this.startActivity(options)

    }
    private fun showInfo(){
        val info = Intent(this,InformationMain::class.java).apply {

            val cambi = binding.textViewwhere.text.toString()
            putExtra("cambi",cambi)
        }
        this.startActivity(info)
    }

    private fun showInfoMenu(){
        val info = Intent(this,MenuInfoMain::class.java).apply {

            val cambi = binding.textViewwhere.text.toString()
            putExtra("cambi",cambi)
        }
        this.startActivity(info)
    }

    private  fun signOut(){
        auth = Firebase.auth
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        finish()
    }
}