package es.rfvl.ronal_proyect_dam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.rfvl.ronal_proyect_dam.Firebase.FavoritosManager
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.classes.Articulo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class articuloAdapter(private val productos: MutableList<Articulo>,private val mListener: OnProductClickListener,private val favListener: OnFavClickListener, private val listaFav: MutableList<String>): RecyclerView.Adapter<articuloAdapter.ArticulosViewFolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): ArticulosViewFolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_articulo, parent, false)

        return ArticulosViewFolder(view, favListener, listaFav)
    }

    override fun onBindViewHolder(holder: articuloAdapter.ArticulosViewFolder, position: Int) {
        val producto = productos[position]
        holder.bindItem(producto)
        holder.itemView.setOnClickListener{ mListener.onProductClick(producto)}
    }

    override fun getItemCount(): Int {
        return  productos.size
    }

    class ArticulosViewFolder(view: View, private val favListener: OnFavClickListener, private val listaFav: MutableList<String>): RecyclerView.ViewHolder(view){
        private val imageArticulo: ImageView = view.findViewById(R.id.imageArticuloInicio)
        private val titulo: TextView = view.findViewById(R.id.textNombreArticuloInicio)
        private val price: TextView = view.findViewById(R.id.precioArticuloInicio)
        private val btnGuardarfavoritos: ImageView = view.findViewById(R.id.btnGuardarFavoritos)

        fun bindItem(i: Articulo){
            Picasso.get().load(i.image).into(imageArticulo)
            titulo.text = i.title
            price.text = i.price.toString() + " €"

            if (listaFav.contains(i.id.toString())){
                btnGuardarfavoritos.setImageResource(R.drawable.guardado)
            }else{
                btnGuardarfavoritos.setImageResource(R.drawable.noguardado)
            }

            btnGuardarfavoritos.setOnClickListener {
                if (listaFav.contains(i.id.toString())){
                    favListener.onFavClick(i,true)
                    btnGuardarfavoritos.setImageResource(R.drawable.noguardado)
                }else{
                    favListener.onFavClick(i,false)
                    btnGuardarfavoritos.setImageResource(R.drawable.guardado)
                }

            }
        }
    }

    interface OnProductClickListener{

        fun onProductClick(p: Articulo)
    }

    interface OnFavClickListener {
        fun onFavClick(p: Articulo, isFavorite: Boolean)
    }

}