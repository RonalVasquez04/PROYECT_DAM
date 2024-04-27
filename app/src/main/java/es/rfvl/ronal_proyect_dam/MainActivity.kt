package es.rfvl.ronal_proyect_dam

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.rfvl.ronal_proyect_dam.databinding.ActivityMainBinding
import es.rfvl.ronal_proyect_dam.fragments.InicioFragment
import es.rfvl.ronal_proyect_dam.fragments.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolBar)
        val bnv: BottomNavigationView = binding.bottomNavigationView

        bnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bnvUser -> {
                    val nuevoFragmento = LoginFragment()
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
                        .addToBackStack(null)
                        .commit()
                    binding.myToolBar.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.GONE
                    true


                }
                R.id.bnvPrincipal -> {
                    val nuevoFragmento = InicioFragment()
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}