package com.example.firebaseauthexampe

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TimeUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseauthexampe.databinding.ActivityHomeBinding
import com.example.firebaseauthexampe.databinding.ActivityUploadBinding
import com.example.firebaseauthexampe.realtimedb.RealtimeDbActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

private const val REQUEST_CODE_IMAGE_PICK = 0

class UploadActivity : AppCompatActivity() {

    var curFile: Uri? = null

    val imageRef = Firebase.storage.reference

    private var _binding: ActivityUploadBinding? = null
    private val binding get() = _binding!!

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        curFile = it
        //binding.ivUploadedImage.setImageURI(it)
        Log.d("ImageUri", it.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_upload)

        val curTime = Calendar.getInstance().timeInMillis
        val fileName = "MyImage-$curTime"
        binding.btnPickImage.setOnClickListener {
            contract.launch("image/*")
        }
        binding.btnUploadImage.setOnClickListener {

            uploadImageToStorage("myImage$curTime")
        }

        binding.btnDownloadFile.setOnClickListener {
            downloadImage("myImage")
        }

        binding.btnDeleteImage.setOnClickListener {
            deleteImage("myImage")

        }
        listFiles()

//        binding.btnRealTimeDb.setOnClickListener {
//            startActivity(Intent(this@UploadActivity,RealtimeDbActivity::class.java))
//        }
    }


    private fun listFiles() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val images = imageRef.child("images/").listAll().await()
            val imageUrl = mutableListOf<String>()
            for (image in images.items) {
                val url = image.downloadUrl.await()
                imageUrl.add(url.toString())
            }
            withContext(Dispatchers.Main) {

                val imageAdapter = ImageAdapter(imageUrl)
                binding.rvImages.apply {
                    adapter = imageAdapter
                    layoutManager = LinearLayoutManager(this@UploadActivity)
                }

                //Toast.makeText(this@UploadActivity,"Deleted Successfully", Toast.LENGTH_SHORT).show()
                // Log.d("TAG", "uploadImageToStorage: ${e.message}")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, e.message, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "deleteImageToStorage: ${e.message}")
            }
        }
    }

    private fun deleteImage(fileName: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            imageRef.child("images/$fileName").delete().await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, "Deleted Successfully", Toast.LENGTH_SHORT)
                    .show()
                // Log.d("TAG", "uploadImageToStorage: ${e.message}")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, e.message, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "deleteImageToStorage: ${e.message}")
            }
        }
    }

    private fun downloadImage(fileName: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = imageRef.child("images/$fileName").getBytes(maxDownloadSize).await()

            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            withContext(Dispatchers.Main) {
                binding.ivUploadedImage.setImageBitmap(bmp)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, e.message, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "uploadImageToStorage: ${e.message}")
            }
        }
    }

    private fun uploadImageToStorage(fileName: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            curFile?.let {
                imageRef.child("images/$fileName").putFile(it).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UploadActivity, "SuccessFully Uploaded", Toast.LENGTH_SHORT)
                        .show()
                    //Log.d("TAG", "uploadImageToStorage: ${e.message}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, e.message, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "uploadImageToStorage: ${e.message}")
            }
        }
    }
}