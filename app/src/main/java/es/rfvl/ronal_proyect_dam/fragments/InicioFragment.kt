package es.rfvl.ronal_proyect_dam.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.rfvl.ronal_proyect_dam.DataApi.ArticuloService
import es.rfvl.ronal_proyect_dam.DataApi.RetrofitObject
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.adapters.articuloAdapter
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.databinding.FragmentInicioBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class InicioFragment : Fragment() , articuloAdapter.OnProductClickListener{

    private lateinit var binding: FragmentInicioBinding
    private lateinit var mAdapter: articuloAdapter
    private lateinit var listArticulos: MutableList<Articulo>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(layoutInflater)
        loadArticles()
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        listArticulos = emptyList<Articulo>().toMutableList()

        mAdapter = articuloAdapter(listArticulos,this)
        binding.myRecViewInicio.adapter = mAdapter
        binding.myRecViewInicio.layoutManager = GridLayoutManager(requireContext(),2)
    }

    private fun loadArticles(){

            lifecycleScope.launch(Dispatchers.IO) {

                val call = RetrofitObject.getInstance().create(ArticuloService::class.java).getListArticles();
                val response = call.body()

                withContext(Dispatchers.Main){

                    if (response != null){

                            for (article in response){
                                listArticulos.add(Articulo(article.title,article.price,article.image, article.id,article.description,article.category,article.rating.rate,article.rating.count))
                                mAdapter.notifyDataSetChanged()
                            }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show() // Asegúrate de que la ActionBar esté visible

        val toolbar = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.myToolBar)
        if (toolbar.visibility != View.VISIBLE) {
            toolbar.visibility = View.VISIBLE // Si la Toolbar no está visible, hazla visible
        }
        activity?.supportActionBar?.show()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE


        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                activity.supportFragmentManager.popBackStack()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onProductClick(d: Articulo) {
        val nuevoFragmento = DetailProductFragment()
        val args = Bundle()

        args.putInt("idArticle",d.id)
        args.putString("titleArticle", d.title)
        args.putDouble("priceArticle",d.price)
        args.putString("descriptionArticle", d.description)
        args.putString("categoryArticle", d.category)
        args.putString("imageArticle", d.image)
        args.putDouble("rateArticle",d.rate)
        args.putInt("countRateArticle",d.countRate)
        nuevoFragmento.arguments = args

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentContainerViewMAIN, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
    }


}