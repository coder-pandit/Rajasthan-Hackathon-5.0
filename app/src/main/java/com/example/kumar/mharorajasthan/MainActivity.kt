package com.example.kumar.mharorajasthan

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.user_details_dialog.*
import java.util.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val ANONYMOUS = "anonymous"
    private val RC_SIGN_IN = 1
    private val RC_GET_DETAILS = 2
    private val RESULT_LOAD_IMAGE = 1

    private lateinit var headerView: View
    private var mUsername: String? = null
    private var mAuth: FirebaseAuth? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.relativeLayout, HomeFragment())
                .commit()

        mAuth = FirebaseAuth.getInstance()
        mUsername = ANONYMOUS

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // user is signed in
                mUsername = user.displayName
            } else {
                // user is signed out
                mUsername = ANONYMOUS
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        AuthUI.IdpConfig.PhoneBuilder()
                                                .setDefaultCountryIso("in")
                                                .build(),
                                        AuthUI.IdpConfig.GoogleBuilder().build()))
                                .build(),
                        RC_SIGN_IN)
            }
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // getting header view for nav_view
        headerView = nav_view.getHeaderView(0)

        // setting click event to header
        headerView.nav_profile_layout.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        nav_view.setNavigationItemSelectedListener(this)

        updateUI()
    }

    override fun onPause() {
        super.onPause()
        if (mAuthStateListener != null) {
            mAuth!!.removeAuthStateListener(mAuthStateListener!!)
        }
    }

    override fun onBackPressed() {
        if (mAuth?.currentUser != null) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        } else finish()
    }

    override fun onResume() {
        super.onResume()
        mAuth!!.addAuthStateListener(mAuthStateListener!!)
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()

                startActivityForResult(Intent(this, UserSignupDetails::class.java), RC_GET_DETAILS)
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else if (requestCode == RC_GET_DETAILS) {
            if (resultCode == UserSignupDetails.RC_SIGNUP_RESULT) {
                mUsername = data?.getStringExtra(UserSignupDetails.DISPLAY_NAME_EXTRA)

                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.relativeLayout, HomeFragment())
                        .commit()

                updateUI()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val fragment: Fragment
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                fragment = HomeFragment()
            }
            R.id.nav_packages -> {
                fragment = PackagesFragment()
            }
            R.id.nav_restro -> {
                fragment = RestaurantsFragment()
            }
            R.id.nav_logout -> {
                fragment = HomeFragment()
                // sign out
                AuthUI.getInstance().signOut(this)
            }
            else -> {
                fragment = HomeFragment()
            }
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.relativeLayout, fragment)
                .commit()


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateUI() {
        headerView.nav_profile_name.text = mUsername
        headerView.nav_profile_desc.text = if (mAuth?.currentUser?.email != null)
            mAuth?.currentUser?.email
        else
            mAuth?.currentUser?.phoneNumber

        Picasso.get()
                .load(mAuth?.currentUser?.photoUrl)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(headerView.nav_profile_image)
    }
}
