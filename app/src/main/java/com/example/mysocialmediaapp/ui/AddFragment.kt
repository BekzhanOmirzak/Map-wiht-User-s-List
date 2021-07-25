package com.example.mysocialmediaapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.ImagePickerAdapter
import com.example.mysocialmediaapp.databinding.AddFragmentBinding
import com.example.mysocialmediaapp.util.isPermissionGranted
import com.example.mysocialmediaapp.util.requestPermission

class AddFragment : Fragment(R.layout.add_fragment) {


    companion object {
        private const val PICK_REQUEST_CODE = 101
        private const val READ_EXTERNAL_PHOTOS_CODE = 748
        private const val READ_PHOTOS_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val TAG = "AddFragment"
    }

    var _bindging: AddFragmentBinding? = null
    private val binding get() = _bindging!!
    private val chooseImageUris = mutableListOf<Uri>()
    private lateinit var imagePickerAdapter: ImagePickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindging = AddFragmentBinding.inflate(layoutInflater, container, false)
        return _bindging!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvImagePicker.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rvImagePicker.setHasFixedSize(true)
        imagePickerAdapter = ImagePickerAdapter(
            requireActivity(),
            chooseImageUris,
            6,
            object : ImagePickerAdapter.ImageClickListener {
                override fun onPlaceHolderClicked() {
                    if (isPermissionGranted(requireActivity(), READ_PHOTOS_PERMISSION)) {
                        LaunchingIntentForPhotos()
                    } else {
                        requestPermission(
                            requireActivity(),
                            READ_PHOTOS_PERMISSION,
                            READ_EXTERNAL_PHOTOS_CODE
                        )
                    }
                }
            })
        binding.rvImagePicker.adapter = imagePickerAdapter

        binding.btnPick.setOnClickListener {

        }
    }

    private fun saveImagesToFireBase() {

        for ((index, uri) in chooseImageUris.withIndex()) {
            val imageByteArray = getImageByteArray(uri)
        }
    }

    private fun getImageByteArray(uri: Uri): ByteArray {
        return byteArrayOf()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_EXTERNAL_PHOTOS_CODE) {
            if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LaunchingIntentForPhotos()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "You have to allow the user to give permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onDestroy() {
        super.onDestroy()
        _bindging = null
    }

    private fun LaunchingIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Choose pictures? "), PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != PICK_REQUEST_CODE || resultCode != Activity.RESULT_OK || data == null) {
            Log.i(TAG, "onActivityResult: user didn't take any photos")
            return
        }

        val selectedUri = data.data
        val clipdata = data.clipData

        if (clipdata != null) {
            Log.i(TAG, "onActivityResult: ${clipdata.itemCount}  $clipdata")
            for (k in 0 until clipdata.itemCount) {
                val clip_item = clipdata.getItemAt(k)
                if (chooseImageUris.size < 18) {
                    chooseImageUris.add(clip_item.uri)
                }
            }
        } else if (selectedUri != null) {
            chooseImageUris.add(selectedUri)
        }
        imagePickerAdapter.notifyDataSetChanged()
    }


}