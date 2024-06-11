package es.rfvl.ronal_proyect_dam

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.rfvl.ronal_proyect_dam.databinding.ActivityMainBinding
import es.rfvl.ronal_proyect_dam.fragments.InicioFragment
import es.rfvl.ronal_proyect_dam.fragments.LoginFragment
import es.rfvl.ronal_proyect_dam.fragments.MisFavoritosFragment
import es.rfvl.ronal_proyect_dam.fragments.ProfileFragment
import es.rfvl.ronal_proyect_dam.fragments.SearchFragment

class MainActivity : AppCompatActivity() , ProfileFragment.FragmentLogoutListener{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolBar)
        val bnv: BottomNavigationView = binding.bottomNavigationView


        binding.editText.isFocusable = false;

        binding.myToolBar.setOnClickListener {
            val nuevoFragmento = SearchFragment()
            this.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
                .addToBackStack(null)
                .commit()
        }

        binding.editText.setOnClickListener {
            val nuevoFragmento = SearchFragment()
            this.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
                .addToBackStack(null)
                .commit()
        }

        binding.button.setOnClickListener {
            val nuevoFragmento = SearchFragment()
            this.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
                .addToBackStack(null)
                .commit()
        }




        bnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bnvUser -> {
                    val nuevoFragmento = ProfileFragment()
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
                        .addToBackStack(null)
                        .commit()
                    true


                }
                R.id.bnvPrincipal -> {
                    val nuevoFragmento = InicioFragment()
                    this.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                        .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)

                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.bnvShop -> {
                    val nuevoFragmento = MisFavoritosFragment()
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.bnvSearch -> {
                    val nuevoFragmento = SearchFragment()
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

    override fun onLogOutListener() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun selectBottomNavItem(itemId: Int) {
        binding.bottomNavigationView.menu.findItem(itemId)?.isChecked = true
    }
}