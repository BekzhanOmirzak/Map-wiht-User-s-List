package com.example.mysocialmediaapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.MemeRecyclerAdapter
import com.example.mysocialmediaapp.models.Meme
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore

class PersonProfileFragment : DialogFragment() {

    private val collections =
        FirebaseFirestore.getInstance().collection("users")

    private lateinit var adapter: MemeRecyclerAdapter
    private lateinit var img_user: ImageView
    private lateinit var num_post: TextView
    private lateinit var rec_view: RecyclerView
    private lateinit var num_followers: TextView
    private lateinit var num_followings: TextView
    private var uid: String = ""

    val TAG = "PersonProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = LayoutInflater.from(requireActivity())
            .inflate(R.layout.person_profile_fragment, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MemeRecyclerAdapter(requireActivity(), 2)
        num_post = view.findViewById(R.id.post_number)
        rec_view = view.findViewById(R.id.rv_image_picker)
        img_user = view.findViewById(R.id.profile_img)
        num_followings = view.findViewById(R.id.followings_num)
        num_followers = view.findViewById(R.id.followers_num)
        val bundle = this.arguments
        if (bundle != null) {
            uid = bundle.getString("uid")!!
            setDataForUser(uid)
        }
    }

    private fun setDataForUser(uid: String) {
        val collection = collections.document(uid).collection("memes")
        Log.i(TAG, "setDataForUser: Incoming uid $uid")
        val memes = mutableListOf<Meme>()
        collection.get().addOnCompleteListener {
            for (k in it.result!!) {
                memes.add(k.toObject(Meme::class.java))
            }
            adapter.updateMemes(memes)
            num_post.setText("${memes.size}")
        }
        Log.i(TAG, "setDataForUser: ${memes.size}")

        rec_view.also {
            it.layoutManager = GridLayoutManager(requireActivity(), 2)
            it.adapter = adapter
        }

        SharedPreferences.getAllFollowingsLists(uid) {
            num_followings.setText(if (it.isEmpty()) "0" else "${it.size}")
        }

        val fragment = FollowersAndFollowingsFragment()
        num_followings.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("uid", uid)
            bundle.putString("key", "following")
            fragment.arguments = bundle
            fragment.show(
                (context as MainActivity).supportFragmentManager.beginTransaction(),
                "show folling"
            )
        }

        num_followers.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("uid", uid)
            bundle.putString("key", "followers")
            fragment.arguments = bundle
            fragment.show(
                (context as MainActivity).supportFragmentManager.beginTransaction(),
                "show a dialog"
            )
        }

        SharedPreferences.getAllFollowers(uid) {
            num_followers.setText(if (it.isEmpty()) "0" else "${it.size}")
        }


    }

}