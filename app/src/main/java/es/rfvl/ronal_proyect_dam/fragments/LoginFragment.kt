package es.rfvl.ronal_proyect_dam.fragments

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import es.rfvl.ronal_proyect_dam.Firebase.AuthManager
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mListener: FragmentLoginListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val textView = binding.btnRecuperarContrasenya
        underlineText(textView)

        binding.btnIniciarSesion.setOnClickListener {
            login()
        }

        binding.btnRecuperarContrasenya.setOnClickListener {
            resetPassword()
        }
        binding.btnRegistrarse.setOnClickListener {
            register()
        }


        return binding.root
    }

    private fun underlineText(textView: TextView) {
        val text = textView.text.toString()
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, 0)
        textView.text = spannableString
    }

    private fun login(){
        val password = binding.TextPasswordLogin.text.toString()
        val user = binding.TextUsuarioLogin.text.toString()
        if (user.isNotEmpty() && password.isNotEmpty()){
            if (!user.isNullOrBlank() && !password.isNullOrBlank()){
                lifecycleScope.launch(){
                    val userLogged = AuthManager().login(user,password)
                    withContext(Dispatchers.Main){
                        if (userLogged != null){

                            //mListener.onLoginClicked()

                            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
                            val nombre = prefs.getString("signature","");
                            with(prefs.edit()){
                                putString("signature",user)
                                putBoolean("syncLogin",true)
                                //putBoolean("syncCheck", false)
                                apply()
                            }

                            mListener.onLoginClicked()

                            Toast.makeText(requireContext(), "INICO DE SESIÓN CORRECTO", Toast.LENGTH_LONG).show()


                        }else{
                            Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_LONG).show()
                            Snackbar.make(binding.root, "Registrese si no tiene una cuenta creada", Snackbar.LENGTH_SHORT).show()

                        }
                    }

                }
            }
        }else{
            Snackbar.make(binding.root, "Debe rellenar el usuario y la contraseña", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun resetPassword(){
        val nuevoFragmento = ResetPasswordFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainerviewLogin, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
    }

    private fun register(){
        val nuevoFragmento = RegisterFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainerviewLogin, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
    }

    interface FragmentLoginListener{
        fun onLoginClicked()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentLoginListener){
            mListener = context
        }else{
            throw Exception("EEROR")
        }
    }



}