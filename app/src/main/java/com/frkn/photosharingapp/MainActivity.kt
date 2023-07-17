package com.frkn.photosharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth;
    private lateinit var passwordText : TextView;
    private lateinit var emailText : TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance();

        emailText =findViewById<TextView>(R.id.editTextTextEmailAddress)
        passwordText  = findViewById<TextView>(R.id.passwordText)

    }


    fun login(view : View){

        auth.signInWithEmailAndPassword(emailText.text.toString() , passwordText.text.toString()).addOnCompleteListener { t ->
            if(t.isSuccessful){


                var intent = Intent(applicationContext , FeedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { ex ->
            Toast.makeText(this , ex.localizedMessage , Toast.LENGTH_LONG).show()
        }
    }


    fun signin(view :View){



        auth.createUserWithEmailAndPassword(emailText.text.toString() , passwordText.text.toString()).addOnCompleteListener { task ->

            if(task.isSuccessful){

                var intent = Intent(this , FeedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { ex ->
            Toast.makeText(applicationContext,ex.localizedMessage ,Toast.LENGTH_LONG).show()
        }
    }
}