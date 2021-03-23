package com.elouanmailly.todo.userinfo

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.elouanmailly.todo.BuildConfig
import com.elouanmailly.todo.databinding.ActivityUserInfoBinding
import com.elouanmailly.todo.network.UserInfo
import com.elouanmailly.todo.network.UserInfoViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        userViewModel.userinfo.observe(this ){
            binding.imageView.load(it.avatar) {
                transformations(CircleCropTransformation())
            }
            binding.firstnameInputField.setText(it.firstName)
            binding.lastnameInputField.setText(it.lastName)
            binding.emailInputField.setText(it.email)

        }
        binding.takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        binding.uploadImageButton.setOnClickListener {
            askStoragePermissionAndGetImage()
        }
        binding.validateInfoButton.setOnClickListener {
            val data = userViewModel.userinfo.value?.let { it1 -> UserInfo(binding.emailInputField.text.toString(), binding.firstnameInputField.text.toString(), binding.lastnameInputField.text.toString(), it1.avatar) }
            if (data != null) {
                userViewModel.updateInfo(data)
                Toast.makeText(this, "Changement sauvegardé ! \uD83D\uDE00", Toast.LENGTH_LONG).show()
            }
        }
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        userViewModel.loadInfo()
    }

    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) openCamera()
                else showExplanationDialog()
            }

    private fun requestCameraPermission() = requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun requestStoragePermission() = requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun askStoragePermissionAndGetImage() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> getImage()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestStoragePermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    private fun handleImage(toUri: Uri) {
        val convertedImage = convert(toUri)

        lifecycleScope.launch {
            userViewModel.updateAvatar(convertedImage)
        }
        binding.imageView.load(toUri)
        Toast.makeText(this, "Image changée ! \uD83D\uDE00", Toast.LENGTH_LONG).show()
    }

    private val photoUri by lazy {
        FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID +".fileprovider",
                File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    private fun openCamera() = takePicture.launch(photoUri)

    private val pickInGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { image ->
                if (image != null) handleImage(image)
                else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
            }

    private fun getImage() = pickInGallery.launch("image/*")

    private lateinit var binding: ActivityUserInfoBinding
    private val userViewModel: UserInfoViewModel by viewModels()
}