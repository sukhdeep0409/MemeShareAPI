package com.example.memeshareapi

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentImageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme() {

        pg.visibility = View.VISIBLE

        //Instantiate the request queue
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        //Request the string response provided by url
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentImageURL = response.getString("url")
                Glide.with(this).load(currentImageURL).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pg.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pg.visibility = View.GONE
                        return false
                    }

                }).into(memeImageView)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    fun shareMeme(view: android.view.View) {
        Intent(Intent.ACTION_SEND).also {
            it.putExtra(Intent.EXTRA_TEXT, currentImageURL)
            it.type = "text/plain"
            val chooser = Intent.createChooser(it, "Share this meme using")
            startActivity(chooser)
        }
    }

    fun nextMeme(view: android.view.View) { loadMeme() }
}