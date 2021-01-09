package com.example.tabtest.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tabtest.R
import java.io.ByteArrayInputStream

class GridRecyclerAdapter(private val cellClickListner: CellClickListner) : RecyclerView.Adapter<GridRecyclerAdapter.ItemViewHolder>() {

    var dataList = ArrayList<GridItem>() // list of photo

    var mPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridRecyclerAdapter.ItemViewHolder {
        Log.d("position", "onCreateViewHolder")
        val holder = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_image, parent, false)) // item that have(hold) photo
        holder.itemView.setOnClickListener { //set listner by CellClicklistner that from BFragment
            setPosition(holder.adapterPosition)
//            Toast.makeText(parent.context, "${holder.adapterPosition} 아이템 클릭!", Toast.LENGTH_SHORT).show()
            cellClickListner.onCellClickListner(holder.adapterPosition, dataList) // get cellClickListner object and do onCellClickListner method when Click the itemView
        }
        return holder
    }

    fun bindItem(items: ArrayList<GridItem>){

        this.dataList = items
        notifyDataSetChanged()
    }

    fun deleteItem(item: GridItem){
        dataList.remove(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount", "size : " + dataList.size)
        return dataList.size
    }

    override fun onBindViewHolder(holder: GridRecyclerAdapter.ItemViewHolder, position: Int) {
        holder.bind(dataList[position])
//        holder.itemView.setOnClickListener {
//            cellClickListner.onCellClickListner(this.getPosition())
//        }
    }

    fun getPosition():Int{
        return mPosition
    }

    private fun setPosition(position: Int){
        mPosition = position
    }

    fun addItem(gridItem: GridItem){
        dataList.add(gridItem)
        //갱신처리 해야함
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        if(position > 0){
            dataList.removeAt(position)
            //갱신처리해야함
            notifyDataSetChanged()
        }
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
        private val image = itemView.findViewById<ImageView>(R.id.grid_image)

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.setHeaderTitle("Select The Action");
            menu?.add(0, v!!.getId(), 0, "Delete");//groupId, itemId, order, title
        }

        fun bind(gridItem: GridItem){
            image.setImageBitmap(stringToImage(gridItem.image))
//                notifyDataSetChanged()
            Log.d("Success", "Success to find Image view")

            itemView.setOnLongClickListener (object: View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    cellClickListner.onCellSettingClicklistner(v, gridItem)
                    return true
                }
            })

        }



    }

    private fun stringToImage(string: String): Bitmap{
        //Base64String 형태를 ByteArray로 풀어줘야 한다
        val data: String = string
        //데이터 base64 형식으로 Decode
        val txtPlainOrg = ""
        val bytePlainOrg = Base64.decode(data, 0)
        //byte[] 데이터  stream 데이터로 변환 후 bitmapFactory로 이미지 생성
        val inStream = ByteArrayInputStream(bytePlainOrg)
        val bm = BitmapFactory.decodeStream(inStream)
        return bm
//        imageView.setImageBitmap(bm)
        /*val gotoImage = string.toByteArray()
        //이러한 ByteArray를 stream데이터로 변환 후, bitmap으로 풀어줘야 한다
        val inStream = ByteArrayInputStream(gotoImage)
        val bm = BitmapFactory.decodeStream(inStream)
        Log.d("worked?", "worked!")
        return imageView.setImageBitmap(bm)*/
        //imageView.visibility = View.VISIBLE
    }

}