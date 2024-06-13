package es.rfvl.ronal_proyect_dam.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.classes.Articulo

class articuloFavAdapter(private val productos: MutableList<Articulo>,private val mListener: OnProductFavClickListener, private val favDeleteListener: OnDeleteClickListener, private val onArticleSelected: (Articulo, Boolean) -> Unit): RecyclerView.Adapter<articuloFavAdapter.ArticulosViewFolder>() {

    private val selectedItems = mutableSetOf<Articulo>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticulosViewFolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_favoriteproduct, parent, false)
        return ArticulosViewFolder(view,favDeleteListener)
    }

    override fun onBindViewHolder(holder: ArticulosViewFolder, position: Int) {
        val producto = productos[position]
        holder.bindItem(producto, selectedItems.contains(producto))
        holder.itemView.setOnClickListener{ mListener.onProductFavClick(producto)}
    }

    override fun getItemCount(): Int {
        return  productos.size
    }

    inner class ArticulosViewFolder(view: View, private val favDeleteListener: OnDeleteClickListener): RecyclerView.ViewHolder(view){
        private val imageArticulo: ImageView = view.findViewById(R.id.imgProductoMiCarrito)
        private val titulo: TextView = view.findViewById(R.id.nameProductoMiCarrito)
        private val price: TextView = view.findViewById(R.id.priceProductoMiCarrito)
        private val btnCheckFav: CheckBox = view.findViewById(R.id.checkBoxProducto)
        private val btnEliminar: ImageView = view.findViewById(R.id.btnBasura)
        private val rate: TextView = view.findViewById(R.id.ratingProductoMiCarrito)

        fun bindItem(i: Articulo,isSelected: Boolean){
            Picasso.get().load(i.image).into(imageArticulo)
            titulo.text = i.title
            price.text = i.price.toString() + " €"
            rate.text = i.rate.toString()

            itemView.setOnClickListener {
                btnCheckFav.isChecked = !btnCheckFav.isChecked
            }

            btnCheckFav.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(i)
                } else {
                    selectedItems.remove(i)
                }
                onArticleSelected(i, isChecked)
            }



            btnEliminar.setOnClickListener {
                favDeleteListener.onDeleteFavClick(i)
            }
        }
    }


    interface OnProductFavClickListener{

        fun onProductFavClick(p: Articulo)
    }

    interface OnDeleteClickListener{

        fun onDeleteFavClick(p: Articulo)
    }


}