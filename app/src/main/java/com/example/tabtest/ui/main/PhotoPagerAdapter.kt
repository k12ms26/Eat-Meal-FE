package com.example.tabtest.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.tabtest.R
import uk.co.senab.photoview.PhotoViewAttacher


class PhotoPagerAdapter internal constructor(
    private val context: Context,
    private val image: ArrayList<GridItem>
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //val inflater = LayoutInflater.from(container.context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.photo, container, false)
        val imageView: ImageView = view.findViewById(R.id.photo_view)
//        imageView.setImageResource(image[position].getImageId(container.context))
        imageView.setImageBitmap(image[position].photo)
        PhotoViewAttacher(imageView)
        val viewpager = container as ViewPager
        viewpager.addView(view,0)
        Log.d("Success", "Success Image view")
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return image.size
    }

}