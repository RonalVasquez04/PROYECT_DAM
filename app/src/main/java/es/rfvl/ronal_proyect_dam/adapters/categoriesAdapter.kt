package es.rfvl.ronal_proyect_dam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.fragments.SearchFragment

class categoriesAdapter(private val categories: List<String>, private val mListener: OnCategoryClickListener) :
    RecyclerView.Adapter<categoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_categories, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bindItem(category)
        holder.itemView.setOnClickListener{ mListener.onCategoryClick(category)}
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nombre: TextView = view.findViewById(R.id.nameCategory)
        fun bindItem(i: String){
            nombre.text = i

        }
    }

    interface OnCategoryClickListener {
        fun onCategoryClick(p: String)
    }
}
