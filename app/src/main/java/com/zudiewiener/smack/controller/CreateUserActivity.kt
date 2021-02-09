package com.zudiewiener.smack.controller

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zudiewiener.smack.R
import com.zudiewiener.smack.services.AuthService
import com.zudiewiener.smack.services.UserDataService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()
        AuthService.registerUser(this,email, password){registerSuccess ->
            if (registerSuccess){
                AuthService.loginUser(this,email,password){loginSuccess ->
                    if (loginSuccess){
                       AuthService.createUser(this,userName,email,userAvatar, avatarColor) { createSuccess ->
                            if (createSuccess){
                                finish()
                            }
                       }
                    }
                }
            }

        }
    }
}