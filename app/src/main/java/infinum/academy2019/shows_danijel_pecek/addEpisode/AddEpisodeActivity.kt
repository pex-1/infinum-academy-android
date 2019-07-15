package infinum.academy2019.shows_danijel_pecek.addEpisode

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_add_episode.*
import androidx.appcompat.app.AlertDialog
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.model.Episode


class AddEpisodeActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, AddEpisodeActivity::class.java)
    }

    var exit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        setSupportActionBar(toolbarAddEpisode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val titleInput = titleEditText.text.toString().trim()
                val descriptionInput = descriptionEditText.text.toString().trim()

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
            val episode = Episode(
                titleEditText.text.toString(),
                descriptionEditText.text.toString()
            )
            val resultIntent = Intent()
            resultIntent.putExtra(Constants.EPISODES_LIST, Utils.serialize(episode))
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
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
        if(exit) finish()
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
