package es.rfvl.ronal_proyect_dam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.classes.Comentario

class comentarioAdapter(private val comentarios: MutableList<Comentario>): RecyclerView.Adapter<comentarioAdapter.ComentarioViewFolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): ComentarioViewFolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_comentario_producto, parent, false)

        return ComentarioViewFolder(view)
    }

    override fun onBindViewHolder(holder: ComentarioViewFolder, position: Int) {
        val comentario = comentarios[position]
        holder.bindItem(comentario)
    }

    override fun getItemCount(): Int {
        return  comentarios.size
    }

    class ComentarioViewFolder(view: View): RecyclerView.ViewHolder(view){
        private val usuario: TextView = view.findViewById(R.id.textUsuarioComentario)
        private val comentario: TextView = view.findViewById(R.id.textMessageComentario)

        fun bindItem(i: Comentario){
            usuario.text = i.usuario;
            comentario.text = i.comentario;
        }
    }


}