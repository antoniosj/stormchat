package com.antoniosj.stormchat.controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.antoniosj.stormchat.R
import com.antoniosj.stormchat.services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginLoginBtnClicked(view: View) {
        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordTxt.text.toString()

        hideKeyboard()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.loginUser(email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
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
            enableSpinner(false)
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }

    }

    fun loginCreateUserBtnClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, plz try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
    fun enableSpinner(enabled: Boolean) {

        if (enabled) {

            loginSpinner.visibility = View.VISIBLE
        } else {

            loginSpinner.visibility = View.INVISIBLE
        }

        loginLoginBtn.isEnabled = !enabled
        loginCreateUserBtn.isEnabled = !enabled
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
