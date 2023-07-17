package com.frkn.photosharingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class FeedActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        auth = FirebaseAuth.getInstance();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var menuInflater = menuInflater;
        menuInflater.inflate(R.menu.options_menu , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            auth.signOut()
            var intent = Intent(applicationContext , MainActivity::class.java);
            startActivity(intent)
            finish()
        }
        else if(item.itemId == R.id.sharingPhoto){
            var intent = Intent(applicationContext,SharingPhotoActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}