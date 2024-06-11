package es.rfvl.ronal_proyect_dam

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import es.rfvl.ronal_proyect_dam.fragments.LoginFragment

class LoginActivity : AppCompatActivity(), LoginFragment.FragmentLoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("es.rfvl.ronal_proyect_dam", MODE_PRIVATE)
        val loginSave = prefs.getBoolean("syncLogin", false);

        if (loginSave){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
            setContentView(R.layout.activity_login)
        }
    }

    override fun onLoginClicked() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}