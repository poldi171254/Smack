package com.zudiewiener.smack.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.zudiewiener.smack.R
import com.zudiewiener.smack.services.AuthService
import com.zudiewiener.smack.services.UserDataService
import com.zudiewiener.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View){
        val random = Random()
        val colour = random.nextInt(2) // number between 0 & 1
        val avatar = random.nextInt(28) // we have avatar files between 0 & 27

        if (colour == 0){
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(
            userAvatar,
            "drawable",
            packageName
        )
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateBackgroundColourClicked(view: View){
        val random = Random()
        // generate colours in RGB
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        // Convert to value between 0 & 1 for iOS (just in case)
        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255
        avatarColor = "[$savedR,$savedG,$savedB, 1]"
    }

    fun createUserClicked(view: View){
        enableSpinner(true)
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            AuthService.registerUser(this,email, password){registerSuccess ->
                if (registerSuccess){
                    AuthService.loginUser(this,email,password){loginSuccess ->
                        if (loginSuccess){
                            AuthService.createUser(this,userName,email,userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess){
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    // LocalBroadcastManager.ge
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
            Toast.makeText(this, "Make sure all fields are filled", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    }

    fun errorToast(){
        Toast.makeText(this, "Something went wrong in the user creation", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean){
        if (enable){
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE

        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        generateBackgroundColBtn.isEnabled = !enable
    }
}