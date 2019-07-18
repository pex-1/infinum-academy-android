package infinum.academy2019.shows_danijel_pecek.addEpisode

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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_add_episode.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.squareup.picasso.Picasso
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.model.Episode
import kotlinx.android.synthetic.main.dialog_layout.*

private var fileUri: Uri? = null
private var titleInput = ""
private var descriptionInput = ""
private var seasonDefault = 1
private var episodeDefault = 1



class AddEpisodeActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, AddEpisodeActivity::class.java)

        const val TITLE = "TITLE"
        const val DESCRIPTION = "DESCRIPTION"
        const val URI = "URI"
        const val SEASON = "SEASON"
        const val EPISODE = "EPISODE"
    }

    var exit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        setSupportActionBar(toolbarAddEpisode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(savedInstanceState != null){
            recoverData(savedInstanceState)
        }

        pickSeasonEpisodeTextView.text = "S ${seasonDefault.toString().padStart(2, '0')}, E ${episodeDefault.toString().padStart(2, '0')}"

        pickSeasonEpisodeTextView.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_layout)

            setNumberPickerValues(dialog.seasonNumberPicker, 0, 20, seasonDefault)
            setNumberPickerValues(dialog.episodeNumberPicker, 0, 99, episodeDefault)
            dialog.show()

            dialog.dialogSaveButton.setOnClickListener {
                setNumberPicker(dialog)
                seasonDefault = dialog.seasonNumberPicker.value
                episodeDefault = dialog.episodeNumberPicker.value
                exit = false
                dialog.dismiss()
            }
        }


        uploadPhotoLinearLayout.setOnClickListener { showPictureDialog() }


        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                titleInput = titleEditText.text.toString().trim()
                descriptionInput = descriptionEditText.text.toString().trim()

                exit = !(titleInput.isNotEmpty() || descriptionInput.isNotEmpty())

                if (titleInput.isNotEmpty() && descriptionInput.isNotEmpty()) {
                    episodeDescriptionInputLayout.error = null
                    usernameInputLayout.error = null
                    saveButton.isEnabled = true
                } else if (titleInput.isEmpty()) {
                    episodeDescriptionInputLayout.error = null
                    usernameInputLayout.error = Constants.TITLE_EMPTY_WARNING
                    saveButton.isEnabled = false
                } else {
                    usernameInputLayout.error = null
                    episodeDescriptionInputLayout.error = Constants.DESCRIPTION_EMPTY_WARNING
                }

            }
        }
        titleEditText.addTextChangedListener(textWatcher)
        descriptionEditText.addTextChangedListener(textWatcher)

        saveButton.setOnClickListener {
            save()
        }
    }

    private fun recoverData(savedInstanceState: Bundle) {
        picassoUpload(savedInstanceState.getParcelable(URI), uploadPhotoImageView)
        titleInput = savedInstanceState.getString(TITLE).toString()
        descriptionInput = savedInstanceState.getString(DESCRIPTION).toString()
        seasonDefault = savedInstanceState.getInt(SEASON)
        episodeDefault = savedInstanceState.getInt(EPISODE)
    }

    private fun setNumberPicker(dialog: Dialog) {
        pickSeasonEpisodeTextView.text = "S ${dialog.seasonNumberPicker.value.toString().padStart(2, '0')}, " +
                "E ${dialog.episodeNumberPicker.value.toString().padStart(2, '0')}"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TITLE, titleInput)
        outState.putString(DESCRIPTION, descriptionInput)
        outState.putParcelable(URI, fileUri)
        outState.putInt(SEASON, seasonDefault)
        outState.putInt(EPISODE, episodeDefault)

        super.onSaveInstanceState(outState)
    }


    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        //pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Camera", "Gallery")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> handleCameraPermission()
                1 -> pickPhotoFromGallery()
            }
        }
        pictureDialog.show()
    }

    private fun handleCameraPermission() {
        if (permissions()) {
            launchCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this).setTitle("Treba nam dozvola za korištenje kamere")
                    .setNeutralButton("ok") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CAMERA_PERMISSION)
                    }.create().show()
            }
            else {
                //step 2: If not, ask the user for permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), Constants.REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    private fun permissions() = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    //step 4: Check for results so we can act accordingly
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == Constants.REQUEST_CAMERA_PERMISSION){
            if(grantResults.isNotEmpty() && permissions()){
                launchCamera()
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CAMERA_PERMISSION)
            }
        }
    }


    //pick a photo from gallery
    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"

        startActivityForResult(pickImageIntent, Constants.PICK_PHOTO_REQUEST)
    }

    //launch the camera to take photo via intent
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, Constants.TAKE_PHOTO_REQUEST)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.TAKE_PHOTO_REQUEST) {
            //photo from camera
            picassoUpload(fileUri, uploadPhotoImageView)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_PHOTO_REQUEST) {
            //photo from gallery
            fileUri = data?.data
            picassoUpload(fileUri, uploadPhotoImageView)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


    private fun picassoUpload(imageUri: Uri?, imageView: ImageView){
        Picasso.with(this).load(imageUri).resize(0, 200)
            .placeholder(R.drawable.ic_camera)
            .into(imageView)
    }


    private fun save() {
        val episode = Episode(titleEditText.text.toString(), descriptionEditText.text.toString(), pickSeasonEpisodeTextView.text.toString(), fileUri)
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.EPISODES_LIST, episode)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


    private fun setNumberPickerValues(numberPicker: NumberPicker, minimum: Int, maximum: Int, default: Int) {
        with(numberPicker) {
            minValue = minimum
            maxValue = maximum
            value = default
        }
    }

    override fun onBackPressed() {
        backButton()

    }

    override fun onSupportNavigateUp(): Boolean {
        backButton()
        return true

    }

    private fun backButton() {
        if (exit) finish()
        else {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(Constants.BACK_BUTTON_TITLE)
            alertDialog.setMessage(Constants.BACK_BUTTON_MESSAGE)
            alertDialog.setPositiveButton("Yes") { _, _ ->
                finish()
            }

            alertDialog.setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }

            alertDialog.create()
            alertDialog.show()
        }
    }
}
