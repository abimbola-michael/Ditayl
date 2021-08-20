package com.rolling.ditayl

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rolling.ditayl.DetailsAdapter.DetailsViewHolder
import kotlinx.android.synthetic.main.details_item.view.*

class DetailsAdapter(var mContext: Context,
                     var mDetails:ArrayList<Detail>,
                     var clickListener: (pos:Int) -> Unit,
                     var emptyListListener: () -> Unit
) : RecyclerView.Adapter<DetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val view:View = LayoutInflater.from(mContext).inflate(R.layout.details_item, parent, false);
        return DetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val detail:Detail = mDetails.get(position);
        holder.itemView.name.text = detail.name
        holder.itemView.email.text = detail.email
        holder.itemView.phone.text = detail.phone

        if(!detail.uri.equals("") && detail.uri != null){
            Glide.with(mContext.applicationContext).load(detail.uri).into(holder.itemView.photo)
        }else{
            Glide.with(mContext.applicationContext).load(R.drawable.def_photo).into(holder.itemView.photo)
        }


        holder.itemView.setOnClickListener { clickListener(position) }
        holder.itemView.setOnLongClickListener {
            showMoreOption(position)
            true
        }
        holder.itemView.more.setOnClickListener { showMoreOption(position) }

    }

    override fun getItemCount(): Int = mDetails.size
    inner class DetailsViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    }

    private fun showMoreOption(position: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
        builder.setItems(options, { dialogInterface, i ->
                when(i){
                    0->{
                        val bundle = Bundle()
                        bundle.putInt(Constants.DETAILS_POSITION, position)
                        startNewActivity<CreateEditDetailsActivity>(mContext, bundle)
                    }
                    1->{
                       deleteDetail(position)
                    }

                }

            }).show()
    }
    fun deleteDetail(position: Int) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Are you sure you want to Delete this Details")
            .setPositiveButton(
                "Yes"
            ) { dialogInterface, i ->
                mDetails.removeAt(position)
                notifyItemRemoved(position);
                notifyItemChanged(position);

                mContext.toast("Details deleted successfully")
                if(mDetails.size == 0){
                    emptyListListener()
                }
            }
            .setNegativeButton(
                "No"
            ) { dialogInterface, i -> dialogInterface.dismiss() }.show()
    }


}