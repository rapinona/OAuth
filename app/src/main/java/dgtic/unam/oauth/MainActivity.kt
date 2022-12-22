package dgtic.unam.oauth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import dgtic.unam.oauth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sesiones()
        validate()
    }

    private fun sesiones() {
        val preferencias = getSharedPreferences(getString(R.string.file_preferencia), Context.MODE_PRIVATE)
        var email:String? = preferencias.getString("email", null)
        var proovedor:String? = preferencias.getString("proovedor", null)

        if (email != null && proovedor != null) {
            opciones(email, TipoProovedor.valueOf(proovedor))
        }
    }

    private fun validate() {
        binding.updateUser.setOnClickListener {
            if (!binding.username.text.toString().isEmpty() && !binding.password.text.toString().isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener{
                    if (it.isComplete) {
                        // Mandar a la activity
                        Toast.makeText(binding.signin.context, "Enlace con exito", Toast.LENGTH_SHORT).show()
                        opciones(it.result?.user?.email?:"", TipoProovedor.CORREO)
                    } else {
                        // Error
                        alert()
                    }
                }
            }
        }
    }

    private fun alert() {
        val bulder = AlertDialog.Builder(this)
        bulder.setTitle("Mensaje")
        bulder.setMessage("Se produjo un error, contacte al provesor")
        bulder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = bulder.create()
        dialog.show()
    }

    private fun opciones(email: String, proovedor: TipoProovedor) {
        var pasos = Intent(this, OpcionesActivity::class.java).apply {
            putExtra("email", email)
            putExtra("proovedor", proovedor.name)
        }

        startActivity(pasos)
    }

}