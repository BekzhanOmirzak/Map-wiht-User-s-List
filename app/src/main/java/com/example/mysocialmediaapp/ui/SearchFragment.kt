package com.example.mysocialmediaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.Person_Adapter
import com.example.mysocialmediaapp.databinding.SearchFragmentBinding
import com.example.mysocialmediaapp.models.User
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment(R.layout.search_fragment) {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private val collection = FirebaseFirestore.getInstance().collection("users")
    private lateinit var personAdapter: Person_Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personAdapter = Person_Adapter(requireActivity(),SharedPreferences.getStoredUserDetails()?.uid!!)
        binding.recView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recView.adapter = personAdapter

        binding.btnSearch.setOnClickListener {
            val search = binding.searchTxt.text
            if (search != null) {
                SearchForParticularPerson(search.toString())
            }
        }

    }

    private fun SearchForParticularPerson(str: String) {
        if (str.equals(SharedPreferences.getStoredUserDetails()?.login))
            return

        val list = mutableListOf<User>()
        collection.whereEqualTo("login", str).get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (k in it.result!!) {
                    list.add(k.toObject(User::class.java))
                }
                personAdapter.updatePeopleList(list)
            }
        }


    }


}