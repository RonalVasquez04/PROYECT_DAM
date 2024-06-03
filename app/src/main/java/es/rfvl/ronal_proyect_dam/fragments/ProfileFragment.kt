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
import es.rfvl.ronal_proyect_dam.DataApi.RetrofitObject
import es.rfvl.ronal_proyect_dam.Firebase.CompradosManager
import es.rfvl.ronal_proyect_dam.Firebase.FavoritosManager
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.adapters.articuloAdapter
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() , articuloAdapter.OnProductClickListener , articuloAdapter.OnFavClickListener{

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mAdapter: articuloAdapter
    private lateinit var listArticulos: MutableList<Articulo>
    private lateinit var listArticulosFav: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
        val direccion = prefs.getString("dirección","");
        binding.textDireccion.setText(direccion)
        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
        binding.btnEditar.setOnClickListener {
            binding.btnGuardarDireccion.visibility = View.VISIBLE
            binding.textDireccion.isEnabled = true

            editar()
        }
        binding.btnGuardarDireccion.setOnClickListener {
            binding.btnGuardarDireccion.visibility = View.GONE
            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
            val nombre = prefs.getString("signature","");
            with(prefs.edit()){
                putString("dirección",binding.textDireccion.text.toString())
                apply()
            }
            binding.textDireccion.isEnabled = false

        }
        loadArticles()
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        listArticulos = emptyList<Articulo>().toMutableList()
        listArticulosFav = emptyList<String>().toMutableList()

        mAdapter = articuloAdapter(listArticulos,this, this, listArticulosFav)
        binding.recViewMisCompras.adapter = mAdapter
        binding.recViewMisCompras.layoutManager = GridLayoutManager(requireContext(),2)
    }

    private fun loadArticles(){

        lifecycleScope.launch(Dispatchers.IO) {
            val prefs = requireActivity().getSharedPreferences(
                "es.rfvl.ronal_proyect_dam",
                Context.MODE_PRIVATE
            );
            val nombre = prefs.getString("signature", "");
            var productosFavoritos = nombre?.let { FavoritosManager().getProductosFavoritos(it) }!!
            val productosComprados = nombre?.let { CompradosManager().getProductosComprados(it) }
            val call = RetrofitObject.getInstance().create(ArticuloService::class.java).getListArticles();
            val response = call.body()

            withContext(Dispatchers.Main){

                if (response != null){
                    for (article in response){
                        if (productosFavoritos != null) {
                            if (productosFavoritos.contains(article.id.toString())){
                                listArticulosFav.add(article.id.toString())
                            }
                        }
                    }
                    for (article in response){
                        if (productosComprados != null) {
                            if (productosComprados.contains(article.id.toString())){
                                listArticulos.add(Articulo(article.title,article.price,article.image, article.id,article.description,article.category,article.rating.rate,article.rating.count))
                            }
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
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
    fun editar(){

    }
}