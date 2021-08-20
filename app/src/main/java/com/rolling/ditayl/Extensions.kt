package com.rolling.ditayl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


inline fun <reified T:Activity> startNewActivity(context: Context, bundle: Bundle? = null){
    val intent = Intent(context, T::class.java)
    bundle?.let { intent.putExtras(it) }
    context.startActivity(intent)

}
fun Context.toast(message: String, is_long:Boolean = false){
    val duration:Int = if(is_long)Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, message, duration).show()
}

fun TextInputEditText.isNotEmpty(textInputLayout: TextInputLayout, errorMessage:String) : Boolean{
    return if(this.text.toString().isEmpty()){
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = errorMessage + " cannot be empty"
        false
    }else{
        textInputLayout.isErrorEnabled = false
        true
    }
}

fun TextInputEditText.setCustomTextChangeListener(textInputLayout: TextInputLayout, errorMessage:String){
    this.addTextChangedListener(object :TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            isNotEmpty(textInputLayout, errorMessage)

        }

        override fun afterTextChanged(p0: Editable?) {

        }

    })

}