package com.antoniosj.stormchat.controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.antoniosj.stormchat.R
import com.antoniosj.stormchat.services.AuthService
import com.antoniosj.stormchat.services.UserDataService
import com.antoniosj.stormchat.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]" //RGBA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {

        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        imgvCreateAvatar.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        imgvCreateAvatar.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = r.toDouble() / 255
        val savedB = r.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
        println(avatarColor)
    }

    fun createUserClicked(view: View) {

        enableSpinner(true)
        val userName = edtCreateUserName.text.toString()
        val email = edtCreateEmail.text.toString()
        val password = edtCreatePasswordText.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {

                    AuthService.loginUser(email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(userName, email, userAvatar, avatarColor) {
                                createSuccess ->
                                if (createSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Make sure user name, email and password are filled in", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, plz try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
    fun enableSpinner(enabled: Boolean) {

        if (enabled) {

            createSpinner.visibility = View.VISIBLE
        } else {

            createSpinner.visibility = View.INVISIBLE
        }

        btnCreateUser.isEnabled = !enabled
        imgvCreateAvatar.isEnabled = !enabled
        btnBackgroundColor.isEnabled = !enabled
    }




}
