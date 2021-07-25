package com.example.mysocialmediaapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.MemeRecyclerAdapter
import com.example.mysocialmediaapp.databinding.HomeFragmentBinding
import com.example.mysocialmediaapp.models.Meme
import com.example.mysocialmediaapp.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val TAG = "HomeFragment"
    lateinit var homeViewModel: HomeViewModel
    var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MemeRecyclerAdapter

    private val collections =
        FirebaseFirestore.getInstance().collection("users")
    val memes_col = collections.document(FirebaseAuth.getInstance().uid!!).collection("memes")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MemeRecyclerAdapter(requireActivity(), 1)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        initRecViewSetUp()
    }


    private fun initRecViewSetUp() {
        binding.rvImagePicker.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvImagePicker.adapter = adapter

        homeViewModel.getLiveDateMemes().observe(requireActivity()) {
            Log.i(TAG, "initRecViewSetUp: $it ")
            adapter.updateMemes(it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}