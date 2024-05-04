package es.rfvl.ronal_proyect_dam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.databinding.FragmentMisComprasBinding

class MisComprasFragment : Fragment() {

    private lateinit var binding: FragmentMisComprasBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMisComprasBinding.inflate(layoutInflater)
        return binding.root
    }

}