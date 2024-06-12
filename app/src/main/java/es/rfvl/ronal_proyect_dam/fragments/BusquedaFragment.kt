package es.rfvl.ronal_proyect_dam.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import es.rfvl.ronal_proyect_dam.DataApi.ArticuloService
import es.rfvl.ronal_proyect_dam.DataApi.ArticulosByCategory
import es.rfvl.ronal_proyect_dam.DataApi.RetrofitObject
import es.rfvl.ronal_proyect_dam.Firebase.FavoritosManager
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.adapters.articuloAdapter
import es.rfvl.ronal_proyect_dam.adapters.busquedaAdapter
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.databinding.FragmentBusquedaBinding
import es.rfvl.ronal_proyect_dam.databinding.FragmentInicioBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BusquedaFragment : Fragment() , busquedaAdapter.OnProductClickListener , busquedaAdapter.OnFavClickListener{
    private lateinit var binding: FragmentBusquedaBinding
    private lateinit var mAdapter: busquedaAdapter
    private lateinit var listArticulos: MutableList<Articulo>
    private lateinit var listArticulosFav: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusquedaBinding.inflate(layoutInflater)
        val categoria = arguments?.getString("nombreCategoria")
        if (categoria != null) {
            loadArticles(categoria)
        }

        setUpRecyclerView()
        return binding.root
    }
    private fun setUpRecyclerView() {
        listArticulos = emptyList<Articulo>().toMutableList()
        listArticulosFav = emptyList<String>().toMutableList()

        mAdapter = busquedaAdapter(listArticulos,this, this,listArticulosFav)
        binding.recViewBusqueda.adapter = mAdapter
        binding.recViewBusqueda.layoutManager = GridLayoutManager(requireContext(),2)
    }

    private fun loadArticles(categoria: String){

        lifecycleScope.launch(Dispatchers.IO) {

            //val call = RetrofitObject.getInstance().create(ArticuloService::class.java).getListArticles();
            val call = RetrofitObject.getInstance().create(ArticulosByCategory::class.java).getListCategoriesProducts(categoria);
            val response = call.body()

            val prefs = requireActivity().getSharedPreferences(
                "es.rfvl.ronal_proyect_dam",
                Context.MODE_PRIVATE
            );
            val nombre = prefs.getString("signature", "");
            var productosFavoritos = nombre?.let { FavoritosManager().getProductosFavoritos(it) }!!

            withContext(Dispatchers.Main){
                if (response != null) {
                    for (article in response){
                        if (productosFavoritos != null) {
                            if (productosFavoritos.contains(article.id.toString())){
                                listArticulosFav.add(article.id.toString())
                            }
                        }
                    }
                    for (article in response){
                        listArticulos.add(Articulo(article.title,article.price,article.image, article.id,article.description,article.category,article.rating.rate,article.rating.count))
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
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

    override fun onFavClick(p: Articulo, isFavorite: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
            val nombre = prefs.getString("signature","");
            if (nombre != null) {
                if (isFavorite){
                    FavoritosManager().deleteProductoFavorito(p.id.toString(), nombre)
                }else{
                    FavoritosManager().addProductoFavorito(p.id.toString(), nombre)
                }
            }
        }
    }
}