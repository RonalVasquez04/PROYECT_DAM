package es.rfvl.ronal_proyect_dam.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.rfvl.ronal_proyect_dam.DataApi.ArticuloService
import es.rfvl.ronal_proyect_dam.DataApi.RetrofitObject
import es.rfvl.ronal_proyect_dam.Firebase.FavoritosManager
import es.rfvl.ronal_proyect_dam.MainActivity
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.adapters.articuloAdapter
import es.rfvl.ronal_proyect_dam.adapters.articuloFavAdapter
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.databinding.FragmentFavoritosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MisFavoritosFragment : Fragment()  , articuloFavAdapter.OnProductFavClickListener, articuloFavAdapter.OnDeleteClickListener{

    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var mAdapter: articuloFavAdapter
    private lateinit var listArticulos: MutableList<Articulo>
    private lateinit var productosFavoritos: MutableList<String>
    private val selectedArticles = mutableListOf<Articulo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(layoutInflater)
        binding.btnComprar.setOnClickListener {
            Toast.makeText(requireContext(),selectedArticles.size.toString(), Toast.LENGTH_SHORT).show()
        }
        loadArticles()
        val activity = requireActivity() as MainActivity
        activity.selectBottomNavItem(R.id.bnvShop)
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        listArticulos = emptyList<Articulo>().toMutableList()

        mAdapter = articuloFavAdapter(listArticulos, this, this) { articulo, isSelected ->
            if (isSelected) {
                selectedArticles.add(articulo)
            } else {
                selectedArticles.remove(articulo)
            }
        }
        binding.recyclerFav.adapter = mAdapter
        binding.recyclerFav.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
    }
    private fun loadArticles(){
        lifecycleScope.launch(Dispatchers.IO) {
            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
            val nombre = prefs.getString("signature","");
            productosFavoritos = nombre?.let { FavoritosManager().getProductosFavoritos(it) }!!

            val call = RetrofitObject.getInstance().create(ArticuloService::class.java).getListArticles();
            val response = call.body()

            withContext(Dispatchers.Main){
                if (productosFavoritos.isNullOrEmpty()) {
                    binding.textNoFavoritos.visibility = View.VISIBLE
                }
                if (response != null){

                    for (article in response){
                        if (productosFavoritos != null) {
                            if (productosFavoritos.contains(article.id.toString())){
                                listArticulos.add(Articulo(article.title,article.price,article.image, article.id,article.description,article.category,article.rating.rate,article.rating.count))
                            }
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }


    override fun onProductFavClick(d: Articulo) {
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
        //(activity as MainActivity).selectBottomNavItem(R.id.bnvShop)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onDeleteFavClick(p: Articulo) {
        lifecycleScope.launch(Dispatchers.IO) {
            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
            val nombre = prefs.getString("signature","");
            if (nombre != null) {
                    FavoritosManager().deleteProductoFavorito(p.id.toString(), nombre)
            }


        }
    }
}