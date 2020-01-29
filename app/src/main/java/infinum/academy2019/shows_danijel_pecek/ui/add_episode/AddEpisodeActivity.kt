package infinum.academy2019.shows_danijel_pecek.ui.add_episode

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodePost
import infinum.academy2019.shows_danijel_pecek.ui.shared.onTextChange
import infinum.academy2019.shows_danijel_pecek.ui.show_details.ShowDetailsActivity
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.dialog_layout.*
import java.io.File

class AddEpisodeActivity : AppCompatActivity() {



    private lateinit var viewModel: AddEpisodeViewModel


    companion object {
        const val SHOW_ID_ADD_EPISODE = "SHOW_ID_ADD_EPISODE"
        fun newInstance(context: Context, showId: String): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            intent.putExtra(SHOW_ID_ADD_EPISODE, showId)
            return intent
        }

        const val SEASON_MINIMUM = 0
        const val SEASON_MAXIMUM = 20
        const val EPISODE_MINIMUM = 0
        const val EPISODE_MAXIMUM = 99
        const val CAMERA_PERMISSION = "This app needs your permission to use the camera"
        const val GALLERY_PERMISSION = "This app needs your permission to open gallery"
        const val NO_PERMISSION_CAMERA = "Can't open camera without the permission!"
        const val NO_PERMISSION_GALLERY = "Can't open gallery without the permission!"
        const val PICTURE_WIDTH = 0
        const val PICTURE_HEIGHT = 200
    }

    var exit = true

    private fun postEpisode(){
        viewModel.postEpisodeLiveData?.observe(this, Observer {
            if(it != null){
                addEpisodeProgressBar.visibility = View.GONE
                finish()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)



        setSupportActionBar(toolbarAddEpisode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        viewModel = ViewModelProviders.of(this).get(AddEpisodeViewModel::class.java)
        viewModel.apiError()
        viewModel.apiErrorLiveData?.observe(this, Observer {
            if(it != null){
                if(Utils.networkAvailable(this)){
                    createSnackbar(it)
                }else{
                    createSnackbar(Constants.NO_INTERNET)
                }
                addEpisodeProgressBar.visibility = View.GONE
            }
        })

        val showId = intent.getStringExtra(SHOW_ID_ADD_EPISODE)
        viewModel.liveDataMedia.observe(this, Observer {
            if(it.media != null){
                viewModel.postEpisode(EpisodePost(showId, it.media.id, viewModel.titleInput, viewModel.descriptionInput, viewModel.seasonDefault.toString(),viewModel.episodeDefault.toString()))
                postEpisode()

            }
        })
        titleEditText.setText(viewModel.titleInput)
        descriptionEditText.setText(viewModel.descriptionInput)
        exit = !(viewModel.titleInput.isNotEmpty() || viewModel.descriptionInput.isNotEmpty())
        pickSeasonEpisodeTextView.text = Utils.setSeasonString(viewModel.seasonDefault, viewModel.episodeDefault)

        pickSeasonEpisodeTextView.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_layout)

            setNumberPickerValues(
                dialog.seasonNumberPicker,
                SEASON_MINIMUM,
                SEASON_MAXIMUM,
                viewModel.seasonDefault
            )
            setNumberPickerValues(
                dialog.episodeNumberPicker,
                EPISODE_MINIMUM,
                EPISODE_MAXIMUM,
                viewModel.episodeDefault
            )
            dialog.show()


            dialog.dialogSaveButton.setOnClickListener {
                setNumberPicker(dialog)
                viewModel.seasonDefault = dialog.seasonNumberPicker.value
                viewModel.episodeDefault = dialog.episodeNumberPicker.value
                exit = false
                dialog.dismiss()
            }
        }
        uploadPhotoLinearLayout.setOnClickListener { showPictureDialog() }


        titleEditText.onTextChange { onTextChangeValidation() }
        descriptionEditText.onTextChange { onTextChangeValidation() }

        picassoUpload(viewModel.fileUri, uploadPhotoImageView)

        saveButton.setOnClickListener {
            save()
        }
    }


    private fun onTextChangeValidation() {
        viewModel.titleInput = titleEditText.text.toString().trim()
        viewModel.descriptionInput = descriptionEditText.text.toString().trim()

        exit = !(viewModel.titleInput.isNotEmpty() || viewModel.descriptionInput.isNotEmpty())

        if (viewModel.titleInput.isNotEmpty() && viewModel.descriptionInput.isNotEmpty()) {
            episodeDescriptionInputLayout.error = null
            usernameInputLayout.error = null
            saveButton.isEnabled = true
        } else if (viewModel.titleInput.isEmpty()) {
            episodeDescriptionInputLayout.error = null
            usernameInputLayout.error = Constants.TITLE_EMPTY_WARNING
            saveButton.isEnabled = false
        } else {
            usernameInputLayout.error = null
            episodeDescriptionInputLayout.error = Constants.DESCRIPTION_EMPTY_WARNING
        }
    }

    private fun setNumberPicker(dialog: Dialog) {
        pickSeasonEpisodeTextView.text =
            Utils.setSeasonString(dialog.seasonNumberPicker.value, dialog.episodeNumberPicker.value)

    }


    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)

        val pictureDialogItems = arrayOf("Camera", "Gallery")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> handleCameraPermission()
                1 -> handleGalleryPermission()
            }
        }
        pictureDialog.show()
    }

    private fun handleCameraPermission() {
        if (permissionsCamera()) {
            launchCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                AlertDialog.Builder(this).setTitle(CAMERA_PERMISSION)
                    .setNeutralButton("ok") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            Constants.REQUEST_CAMERA_PERMISSION
                        )
                    }.create().show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.REQUEST_CAMERA_PERMISSION
                )
            }
        }
    }

    private fun handleGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickPhotoFromGallery()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                AlertDialog.Builder(this).setTitle(GALLERY_PERMISSION)
                    .setNeutralButton("ok") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.REQUEST_GALLERY_PERMISSION
                        )
                    }.create().show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.REQUEST_GALLERY_PERMISSION
                )
            }
        }
    }

    private fun permissionsCamera() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && permissionsCamera()) {
                launchCamera()
            } else {
                createSnackbar(NO_PERMISSION_CAMERA)
            }
        } else if (requestCode == Constants.REQUEST_GALLERY_PERMISSION) {
            if (grantResults.isNotEmpty() && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                pickPhotoFromGallery()
            } else {
                createSnackbar(NO_PERMISSION_GALLERY)
            }
        }
    }

    private fun createSnackbar(message: String) {
        val snackbar = Snackbar.make(snackbarLayout, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color))
        snackbar.show()
    }


    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"

        startActivityForResult(pickImageIntent, Constants.PICK_PHOTO_REQUEST)
    }

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        viewModel.fileUri =
            this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(this.packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, Constants.TAKE_PHOTO_REQUEST)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.TAKE_PHOTO_REQUEST) {
            picassoUpload(viewModel.fileUri, uploadPhotoImageView)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_PHOTO_REQUEST) {
            viewModel.fileUri = data?.data
            picassoUpload(viewModel.fileUri, uploadPhotoImageView)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


    private fun picassoUpload(imageUri: Uri?, imageView: ImageView) {
        Picasso.get().load(imageUri)
            .resize(PICTURE_WIDTH, PICTURE_HEIGHT)
            .placeholder(R.drawable.ic_camera)
            .into(imageView)
    }


    private fun save() {
        addEpisodeProgressBar.visibility = View.VISIBLE
        val path = RealFilePathUtil.getPath(this, viewModel.fileUri!!)
        viewModel.uploadMedia(File(path!!))
    }

    override fun onBackPressed() {
        backButton()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    private fun backButton(): Boolean {
        if (exit) {
            resetInputFields()
            finish()
            return true
        } else {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(Constants.BACK_BUTTON_TITLE)
            alertDialog.setMessage(Constants.BACK_BUTTON_MESSAGE)
            alertDialog.setPositiveButton("Yes") { _, _ ->
                resetInputFields()
                finish()
            }

            alertDialog.setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }

            alertDialog.create()
            alertDialog.show()
            return false
        }
    }

    private fun resetInputFields() {
        viewModel.titleInput = ""
        viewModel.descriptionInput = ""
    }

    private fun setNumberPickerValues(numberPicker: NumberPicker, minimum: Int, maximum: Int, default: Int) {
        with(numberPicker) {
            minValue = minimum
            maxValue = maximum
            value = default
        }
    }


}
