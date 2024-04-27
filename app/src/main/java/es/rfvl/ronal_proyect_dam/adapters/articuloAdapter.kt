package es.rfvl.ronal_proyect_dam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.classes.Articulo

class articuloAdapter(private val productos: MutableList<Articulo>): RecyclerView.Adapter<articuloAdapter.ArticulosViewFolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticulosViewFolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_articulo, parent, false)
        return ArticulosViewFolder(view)
    }

    override fun onBindViewHolder(holder: articuloAdapter.ArticulosViewFolder, position: Int) {
        val producto = productos[position]
        holder.bindItem(producto)
    }

    override fun getItemCount(): Int {
        return  productos.size
    }

    class ArticulosViewFolder(view: View): RecyclerView.ViewHolder(view){
        private val imageArticulo: ImageView = view.findViewById(R.id.imageArticuloInicio)
        private val titulo: TextView = view.findViewById(R.id.textNombreArticuloInicio)
        private val price: TextView = view.findViewById(R.id.precioArticuloInicio)

        fun bindItem(i: Articulo){
            imageArticulo.setImageResource(i.image)
            titulo.text = i.title
            price.text = i.price.toString()
        }
    }


}