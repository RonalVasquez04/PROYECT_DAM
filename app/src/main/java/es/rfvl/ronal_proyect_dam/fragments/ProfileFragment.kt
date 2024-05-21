package es.rfvl.ronal_proyect_dam.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        return binding.root
    }

    private fun cerrarSesion(){
        val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
        with(prefs.edit()){
            putString("signature","")
            putBoolean("syncLogin",false)
            apply()
        }
        fragmentManager?.popBackStack()
    }

}