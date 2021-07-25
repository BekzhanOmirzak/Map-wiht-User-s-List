package com.example.mysocialmediaapp.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.MemeRecyclerAdapter
import com.example.mysocialmediaapp.databinding.ProfileFragmentBinding
import com.example.mysocialmediaapp.models.Meme
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val TAG = "ProfileFragment"
    private var _binding: ProfileFragmentBinding? = null
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
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MemeRecyclerAdapter(requireActivity(), 2)
        binding.profileImg.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Confirmation...")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            FirebaseAuth.getInstance().signOut()
                            SharedPreferences.removeUserDetails()
                            requireActivity().startActivity(
                                Intent(
                                    activity,
                                    Login_Activity::class.java
                                )
                            )
                        }
                    })
                    .setNegativeButton("No", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                        }
                    }).create().show()
                return false
            }
        })
        setUpDataFields()
    }

    private fun setUpDataFields() {

        val posts_list = mutableListOf<Meme>()

        memes_col.get().addOnCompleteListener {
            for (k in it.result!!) {
                val meme = k.toObject(Meme::class.java)
                posts_list.add(meme)
            }
            adapter.updateMemes(posts_list)
            binding.postNumber.setText("${posts_list.size}")
        }

        binding.rvImagePicker.layoutManager = GridLayoutManager(context, 3)
        binding.rvImagePicker.adapter = adapter
        val uid = SharedPreferences.getStoredUserDetails()?.uid!!

        SharedPreferences.getAllFollowingsLists(uid) {
            binding.followingsNum.setText("${it.size}")
        }

        SharedPreferences.getAllFollowers(uid) {
            binding.followersNum.setText("${it.size}")
        }

        val fragment = FollowersAndFollowingsFragment()
        binding.followingsNum.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("uid", uid)
            bundle.putString("key", "following")
            fragment.arguments = bundle
            fragment.show((context as MainActivity).supportFragmentManager.beginTransaction(), "show folling")
        }

        binding.followersNum.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("uid", uid)
            bundle.putString("key", "followers")
            fragment.arguments = bundle
            fragment.show((context as MainActivity).supportFragmentManager.beginTransaction(), "show a dialog")
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}