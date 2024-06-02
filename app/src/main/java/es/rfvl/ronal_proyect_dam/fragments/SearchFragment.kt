package es.rfvl.ronal_proyect_dam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.rfvl.ronal_proyect_dam.DataApi.ArticuloCategories
import es.rfvl.ronal_proyect_dam.DataApi.RetrofitObject
import es.rfvl.ronal_proyect_dam.R
import es.rfvl.ronal_proyect_dam.adapters.CategoriesAdapter
import es.rfvl.ronal_proyect_dam.classes.Articulo
import es.rfvl.ronal_proyect_dam.databinding.FragmentSearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() , CategoriesAdapter.OnCategoryClickListener{

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mAdapter: CategoriesAdapter
    private lateinit var listCategories: MutableList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        loadData()
        setUpRecyclerView()
        return binding.root
    }

    private fun loadData(){
        lifecycleScope.launch(Dispatchers.IO) {

            val call = RetrofitObject.getInstance().create(ArticuloCategories::class.java).getListCategories();
            val response = call.body()

            withContext(Dispatchers.Main){

                if (response != null){

                    for (article in response){
                        listCategories.add(article)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        listCategories = emptyList<String>().toMutableList()

        mAdapter = CategoriesAdapter(listCategories, this)
        binding.recCategories.adapter = mAdapter
        binding.recCategories.layoutManager = GridLayoutManager(requireContext(),2)
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onCategoryClick(p: String) {
        TODO("Not yet implemented")
    }

}