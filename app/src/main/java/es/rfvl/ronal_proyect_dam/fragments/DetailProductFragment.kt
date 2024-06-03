package es.rfvl.ronal_proyect_dam.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import es.rfvl.ronal_proyect_dam.DataApi.ArticuloService
import es.rfvl.ronal_proyect_dam.DataApi.RetrofitObject
import es.rfvl.ronal_proyect_dam.Firebase.ComentariosManager
import es.rfvl.ronal_proyect_dam.Firebase.CompradosManager
import es.rfvl.ronal_proyect_dam.Firebase.FavoritosManager
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.adapters.articuloFavAdapter
import es.rfvl.ronal_proyect_dam.adapters.comentarioAdapter
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.classes.Comentario
import es.rfvl.ronal_proyect_dam.databinding.FragmentDetailProductBinding
import es.rfvl.ronal_proyect_dam.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailProductFragment : Fragment() {

    private lateinit var binding: FragmentDetailProductBinding
    private lateinit var mAdapter: comentarioAdapter
    private var listComentarios = mutableListOf<Comentario>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProductBinding.inflate(layoutInflater)

        val idArticulo = arguments?.getInt("idArticle")
        val titleArticulo = arguments?.getString("titleArticle")
        val priceArticulo = arguments?.getDouble("priceArticle")
        val descriptionArticulo = arguments?.getString("descriptionArticle")
        val categoryArticulo = arguments?.getString("categoryArticle")
        val imageArticulo = arguments?.getString("imageArticle")
        val rateArticulo = arguments?.getDouble("rateArticle")
        val countArticulo = arguments?.getInt("countRateArticle")

        binding.nameProductDetail.text = titleArticulo
        binding.priceProductDetail.text = priceArticulo.toString()  + " €"
        binding.calificationProductDetail.text = rateArticulo.toString()
        binding.textDescripcion.text = descriptionArticulo

        Picasso.get().load(imageArticulo).into(binding.imgProductDetail)

        binding.btnAgregarAlCarrito.setOnClickListener {
            if (idArticulo != null) {
                addFav(idArticulo)
            }
        }
        binding.sendButton.setOnClickListener {
            val textoEnviar = binding.messageEditText.text.toString()
            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE)
            val nombre = prefs.getString("signature", "").toString()
            lifecycleScope.launch(Dispatchers.IO) {
                ComentariosManager().addComentarioProducto(idArticulo.toString(),nombre,textoEnviar)
                withContext(Dispatchers.Main){
                    binding.messageEditText.text = null
                    binding.recComentarios.scrollToPosition(listComentarios.size -1 )
                }
            }
        }
        binding.btnComprarYa.setOnClickListener {
            comprar(idArticulo.toString())
        }


        setUpRecyclerView(idArticulo.toString())
        return binding.root
    }

    private fun setUpRecyclerView(id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            ComentariosManager().getComentariosFlow(id).collect{ datosUpdated ->
                listComentarios.clear()
                listComentarios.addAll(datosUpdated)
                withContext(Dispatchers.Main){
                    mAdapter.notifyDataSetChanged()
                    binding.recComentarios.scrollToPosition(listComentarios.size -1 )
                }
            }

        }

        mAdapter = comentarioAdapter(listComentarios)
        binding.recComentarios.adapter = mAdapter
        binding.recComentarios.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
    }


    fun addFav(id: Int){
        lifecycleScope.launch(Dispatchers.IO) {
            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
            val nombre = prefs.getString("signature","");
            if (nombre != null) {
                    FavoritosManager().addProductoFavorito(id.toString(), nombre)
            }
        }
    }

    fun comprar(id: String){
        lifecycleScope.launch(Dispatchers.IO) {
            val prefs = requireActivity().getSharedPreferences("es.rfvl.ronal_proyect_dam", Context.MODE_PRIVATE);
            val nombre = prefs.getString("signature","");
            if (nombre != null) {
                    CompradosManager().addProductoComprado(id, nombre)
            }
        }
    }



}