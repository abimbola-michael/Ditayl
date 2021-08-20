package com.rolling.ditayl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_create_edit_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_create_edit_details.*

class CreateEditDetailsActivity : AppCompatActivity() {
    var details_position:Int = Constants.NO_POSITION;
    var imgUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_edit_details)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        details_position = savedInstanceState?.getInt(Constants.DETAILS_POSITION)?:
                intent.getIntExtra(Constants.DETAILS_POSITION, Constants.NO_POSITION)

        if(details_position != Constants.NO_POSITION){
            setDetails()
            actionBar?.title = getString(R.string.edit_details)
            changePhoto.text = getString(R.string.change_photo)
        }else{
            actionBar?.title = getString(R.string.new_details)
            changePhoto.text = getString(R.string.add_photo)
        }
        setClick()
        checkAll()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.DETAILS_POSITION, details_position)
    }
    fun setDetails(){
        val detail:Detail = DetailsManager.details.get(details_position);
        imgUri = Uri.parse(detail.uri)
        edtName.setText(detail.name)
        edtEmail.setText(detail.email)
        edtPhone.setText(detail.phone)
        if(!detail.uri.equals("") && detail.uri != null){
            Glide.with(applicationContext).load(detail.uri).into(userPhoto)
        }else{
            Glide.with(applicationContext).load(R.drawable.def_photo).into(userPhoto)
        }

    }
    fun saveDetails(){
        val name = edtName.text.toString()
        val email = edtEmail.text.toString()
        val phone = edtPhone.text.toString()
        val uri:String = imgUri?.toString() ?: ""
        val detail = Detail(uri, name, email, phone);

        if (details_position != Constants.NO_POSITION){
            DetailsManager.details.set(details_position, detail)
            DetailsManager.active_details_postion = details_position
        }else{
            DetailsManager.details.add(detail)
            DetailsManager.active_details_postion = DetailsManager.details.size - 1
        }
        toast("Details saved successfully")
        finish()
    }

    fun setClick(){
        userPhoto.setOnClickListener {
            selectPhoto()
        }
        changePhoto.setOnClickListener {
            selectPhoto()
        }
        fabSave.setOnClickListener {
            if(isValidDetails()){
                saveDetails()
            }
        }

    }
    fun selectPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, Constants.REQUEST_CODE)
    }
    fun checkAll(){
        edtEmail.setCustomTextChangeListener(tilEmail,"Email")
        edtPhone.setCustomTextChangeListener(tilPhone,"Phone number")
        edtName.setCustomTextChangeListener(tilName,"Name")
    }
    fun isValidDetails():Boolean{
        if (edtEmail.isNotEmpty(tilEmail,"Email") &&
            edtPhone.isNotEmpty(tilPhone,"Phone number") &&
            edtName.isNotEmpty(tilName,"Name")) {
            return true
        }else{
            edtEmail.isNotEmpty(tilEmail,"Email")
            edtPhone.isNotEmpty(tilPhone,"Phone number")
            edtName.isNotEmpty(tilName,"Name")
            return false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, uri_data: Intent?) {
        super.onActivityResult(requestCode, resultCode, uri_data)
        if(requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK){
            imgUri = uri_data?.data
            Glide.with(applicationContext).load(imgUri).into(userPhoto)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}