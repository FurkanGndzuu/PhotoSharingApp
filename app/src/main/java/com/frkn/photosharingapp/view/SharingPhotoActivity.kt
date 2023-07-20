package com.frkn.photosharingapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.frkn.photosharingapp.models.Post
import com.frkn.photosharingapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class SharingPhotoActivity : AppCompatActivity() {

    var ChoosenImage: Uri? = null
    var ChoosenBitmap: Bitmap? = null
    private var READ_IMAGE_CODE: Int = 1;

    private lateinit var auth: FirebaseAuth;
    private lateinit var storage: FirebaseStorage;
    private lateinit var database: FirebaseFirestore;

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sharin_photo)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    fun SavePost(view: View) {

        var imageName: String = UUID.randomUUID().toString()+".jpg";

        var reference = storage.reference;
        val imageReference = reference.child("Images").child(imageName);
        if (ChoosenImage != null) {
            imageReference.putFile(ChoosenImage!!).addOnSuccessListener { taskSnapshot ->
                val uploadedImageReference =
                    FirebaseStorage.getInstance().reference.child("Images").child(imageName)
                uploadedImageReference.downloadUrl.addOnSuccessListener {
                    var post: Post = Post(
                        findViewById<TextView>(R.id.Description).text.toString(), it.toString(),
                        Timestamp.now(), auth.currentUser!!.email.toString()
                    );

                    database.collection("Post").add(post).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Post is added successfully",
                                Toast.LENGTH_LONG
                            ).show();
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG)
                            .show();
                    }
                }
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {

                Toast.makeText(
                    applicationContext,
                    "Permission is already granted",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) -> {

                findViewById<View>(R.id.photoarea).showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_MEDIA_IMAGES
                    )

                }
            }

            else -> {

                requestPermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_IMAGES
                )

            }
        }
    }

    fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            ChoosenImage = data.data

            if (ChoosenImage != null) {

                if (Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(this.contentResolver, ChoosenImage!!)
                    ChoosenBitmap = ImageDecoder.decodeBitmap(source)
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(ChoosenBitmap)

                } else {
                    ChoosenBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, ChoosenImage)
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(ChoosenBitmap)

                }


            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

