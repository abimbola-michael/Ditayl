package com.rolling.ditayl

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.appcompat.app.AppCompatActivity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    var details:ArrayList<Detail> = arrayListOf()
    var lastPosition:Int = Constants.NO_POSITION
    var lastDetail:Detail? = null
    var detailsAdapter:DetailsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        readDetails()
        setClick()
        toggleAddNewText()

    }
    fun toggleAddNewText(){
        if(DetailsManager.details.size > 0){
            textAddNew.visibility = View.GONE
        }else{
            textAddNew.visibility = View.VISIBLE
        }
        val spannableString = SpannableString(getString(R.string.add_new_text))
        val addNewStr = "Add New";
        val startPos = spannableString.indexOf(addNewStr)

        spannableString.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ResourcesCompat.getColor(resources, R.color.blue_500,null)
                ds.isUnderlineText = false
            }
            override fun onClick(p0: View) {
                addNew()
            }
        }, startPos, startPos + addNewStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textAddNew.isClickable = true
        textAddNew.movementMethod = LinkMovementMethod.getInstance()
        textAddNew.text = spannableString
    }
    fun setClick(){
        fabAddNew.setOnClickListener { view ->
            addNew()
        }
    }
    fun addNew(){
        startNewActivity<CreateEditDetailsActivity>(this)
        resetLastDetailsAndPosition()
    }

    fun clearAllDetails(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to clear all Details")
            .setPositiveButton("Yes") { dialogInterface, i ->
                DetailsManager.details.clear()
                detailsAdapter?.notifyDataSetChanged()
                toggleAddNewText()
                resetLastDetailsAndPosition()
                toast("Details cleared successfully")

            }
            .setNegativeButton(
                "No"
            ) { dialogInterface, i -> dialogInterface.dismiss() }.show()
    }
    fun readDetails() {
        details = DetailsManager.details
        detailsAdapter = DetailsAdapter(this, details,{
            lastDetail = DetailsManager.details.get(it)
            lastPosition = it
            val bundle = Bundle()
            bundle.putInt(Constants.DETAILS_POSITION, it)
            startNewActivity<CreateEditDetailsActivity>(this, bundle)
        },{
            toggleAddNewText()
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        detailsRecyclerView.layoutManager = layoutManager
        detailsRecyclerView.adapter = detailsAdapter
        resetLastDetailsAndPosition()

    }
    fun resetLastDetailsAndPosition(){
        if(lastDetail != null && lastPosition != Constants.NO_POSITION){
            lastPosition = Constants.NO_POSITION
            lastDetail = null
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
            R.id.action_new_details -> {
                addNew()
                true
            }
            R.id.action_clear_all -> {
                clearAllDetails()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    override fun onResume() {
        super.onResume()
        resetDetails()
    }
    fun resetDetails(){
        if(DetailsManager.details.size > 0 && lastDetail != null && lastPosition != Constants.NO_POSITION) {
            if (DetailsManager.details.get(lastPosition) != lastDetail) {
                detailsAdapter?.notifyDataSetChanged()
                detailsRecyclerView.scrollToPosition(DetailsManager.active_details_postion)
            }
        }else{
            detailsAdapter?.notifyDataSetChanged()
        }
        resetLastDetailsAndPosition()
        toggleAddNewText()
    }


}