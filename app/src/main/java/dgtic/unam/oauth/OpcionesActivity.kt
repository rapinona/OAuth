package dgtic.unam.oauth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import dgtic.unam.oauth.databinding.ActivityOpcionesBinding

enum class TipoProovedor {
    CORREO,
    GOOGLE,
    FACEBOOK
}

class OpcionesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpcionesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOpcionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Datos de la actividad
        var bundle: Bundle? = intent.extras
        var email: String? = bundle?.getString("email")
        var proovedor: String? = bundle?.getString("proovedor")

        inicio(email?:"", proovedor?:"")

        // Guardar los datos
        val preferencias = getSharedPreferences(getString(R.string.file_preferencia), Context.MODE_PRIVATE).edit()
        preferencias.putString("email", email)
        preferencias.putString("proovedor", proovedor)
        preferencias.apply()
    }

    private fun inicio(email: String, proovedor: String) {
        binding.mail.text = email
        binding.provedor.text = proovedor

        binding.closeSesion.setOnClickListener {
            val preferencias = getSharedPreferences(getString(R.string.file_preferencia), Context.MODE_PRIVATE).edit()
            preferencias.clear()
            preferencias.apply()

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}