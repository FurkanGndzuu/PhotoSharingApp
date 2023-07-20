package com.frkn.photosharingapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frkn.photosharingapp.models.Post
import com.frkn.photosharingapp.R
import com.frkn.photosharingapp.adapter.FeedsRecycler
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth;
    private lateinit var database : FirebaseFirestore;
    private lateinit var recyclerViewAdapter : FeedsRecycler;

    var posts  = ArrayList<Post>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance()

        getDatas()

       var  layoutManager =  LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.recyclerView1).layoutManager = layoutManager;
        recyclerViewAdapter = FeedsRecycler(posts)
        findViewById<RecyclerView>(R.id.recyclerView1).adapter = recyclerViewAdapter;



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var menuInflater = menuInflater;
        menuInflater.inflate(R.menu.options_menu, menu)
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
            var intent = Intent(applicationContext, SharingPhotoActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun AddPhoto(view : View){
        var intent = Intent(applicationContext , SharingPhotoActivity::class.java)
        startActivity(intent)
    }

    fun logout(view : View){
        auth.signOut()
        var intent = Intent(applicationContext , MainActivity::class.java);
        startActivity(intent)
        finish()
    }


    fun getDatas(){
        database.collection("Post").orderBy("date" , Query.Direction.DESCENDING).addSnapshotListener{
                snapshot , exception ->
            if(exception != null){
                Toast.makeText(applicationContext , exception.localizedMessage , Toast.LENGTH_LONG)
            }
            else{
                if(snapshot != null){
                    if(!snapshot.isEmpty){
                        var documents = snapshot.documents

                        for (document in documents){
                            val description = document.get("description") as String
                            val imageUrl = document.get("imageUrl") as String
                            val date =  document.get("date") as Timestamp
                            val userEmail = document.get("userEmail") as String
                            val post : Post = Post( description , imageUrl ,date, userEmail)
                            posts.add(post)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }

        }
    }

}