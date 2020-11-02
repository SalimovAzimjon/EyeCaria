package uz.napa.eyecaria.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.fragment_camera.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.repository.Repository
import uz.napa.eyecaria.util.Resource
import uz.napa.eyecaria.util.ViewModelFactory
import java.io.File

private const val PERMISSION_REQ = 101
private const val CAMERA_PIC_REQUEST = 1001

class CameraFragment : BaseFragment(R.layout.fragment_camera, R.color.colorAccent) {

    private var bitmapImage: Bitmap? = null
    private var imageUri: Uri? = null

    private val viewModel by viewModels<CameraViewModel> { ViewModelFactory(Repository()) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        askPermission()
        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
        btn_open_camera.setOnClickListener {
            askPermission()
        }

        viewModel.eyeResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    showLoading()
                }
                is Resource.Success -> {
                    hideLoading()
                    val isNormal = it.data!!.percent >= 65
                    val drawImage = InputImage.fromBitmap(bitmapImage!!, 0)
                    val options = FaceDetectorOptions.Builder()
                        .setClassificationMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build()
                    val detector = FaceDetection.getClient(options)
                    detector.process(drawImage)
                        .addOnSuccessListener { faces ->
                            img.setLandMark(
                                faces,
                                if (isNormal) Color.GREEN else Color.RED,
                                img.width.toFloat() / bitmapImage?.width!!.toFloat(),
                                img.height.toFloat() / bitmapImage?.height!!.toFloat()
                            )
                            img.setImageBitmap(bitmapImage)
                        }
                        .addOnFailureListener { e ->
                        }
                    val text = if (isNormal) getString(R.string.normal) else getString(R.string.abnormal)
                    Toast.makeText(
                        requireContext(),
                        text,
                        Toast.LENGTH_SHORT
                    ).show()
                    tv_cancer.text = text
                }
                is Resource.Error -> {
                    hideLoading()
                    Snackbar.make(img,getString(R.string.try_again),Snackbar.LENGTH_SHORT).show()
                    tv_cancer.text = getString(R.string.calculating_error)
                    openCamera()
                }
            }
        })
    }

    private fun showLoading() {
        progress_bar.isVisible = true
        tv_cancer.text = getString(R.string.calculating)
    }

    private fun hideLoading(){
        progress_bar.isVisible = false
        tv_cancer.text = getString(R.string.eye_cancer)
    }

    private fun askPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_REQ
            )
        else
            openCamera()
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    val thumbnail = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, imageUri
                    )
                    bitmapImage =
                        rotateImageIfRequired(thumbnail, Uri.parse(getRealPathFromURI(imageUri)))
                    val file = File(getRealPathFromURI(imageUri))
                    viewModel.uploadImage(file)
                    img.clearLandMark()
                    img.setImageResource(0)
                    img.setImageBitmap(bitmapImage)

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.try_again),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = requireActivity().managedQuery(contentUri, data, null, null, null)
        val columnIndex: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ) {
            if (grantResults.isNotEmpty()) {
                grantResults.forEach {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                openCamera()
            }
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap? {
        val ei = selectedImage.path?.let { ExifInterface(it) }
        val orientation: Int =
            ei!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg =
            Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }


}