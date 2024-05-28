package es.rfvl.ronal_proyect_dam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.databinding.FragmentDetailProductBinding
import es.rfvl.ronal_proyect_dam.databinding.FragmentProfileBinding

class DetailProductFragment : Fragment() {

    private lateinit var binding: FragmentDetailProductBinding

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



        return binding.root
    }

}