package es.rfvl.ronal_proyect_dam.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import es.rfvl.ronal_proyect_dam.Firebase.AuthManager
import es.rfvl.ronal_proyect_dam.databinding.FragmentResetPasswordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)

        binding.btnSubmit.setOnClickListener {
            val email = binding.textoEmailReset.text.toString()

            if (!email.isNullOrBlank()){
                lifecycleScope.launch(Dispatchers.IO) {
                    AuthManager().resetPassword(email)
                    withContext(Dispatchers.Main){

                        Toast.makeText(requireContext(),"Se enviara el email", Toast.LENGTH_LONG).show()
                        fragmentManager?.popBackStack()

                    }
                }
            }
        }

        binding.btnGoBackResetPassword.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        return binding.root
    }


}