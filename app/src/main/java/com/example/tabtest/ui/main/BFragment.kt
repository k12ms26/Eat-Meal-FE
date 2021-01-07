package com.example.tabtest.ui.main


import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabtest.MainActivity
import com.example.tabtest.R


//public var photoposition = 0
//public var photoArray = ArrayList<GridItem>()

class BFragment : Fragment(), FragmentLifecycle, CellClickListner {

    var gestureDetector: ScaleGestureDetector? = null
    var GridItemCount = 3
    var scaleFactor: Float = 1F
    var TouchCount = 0
    var tempScale = 100
    var currentScale = 100

    private val OPEN_GALLERY = 1
    var imageList : ArrayList<GridItem> = ArrayList<GridItem>()
    private val mAdapter = GridRecyclerAdapter(this) // pass ClickListner object (do override method in BFragmentclass) to Adapter
    var isIn = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_b, container, false)
        view.findViewById<RecyclerView>(R.id.recycler_view_grid).apply{
            this.adapter = mAdapter
            layoutManager = GridLayoutManager(context,GridItemCount)
        }
        val button: ImageButton = view.findViewById(R.id.add_btn)
        button.setOnClickListener {
            openGallery()
        }

        //// GESTURE START
        (activity as MainActivity).registerMyOnTouchListener(object : MainActivity.MyOnTouchListener{
            override fun OnTouch(ev: MotionEvent?) {
                println("my on Touch")
                scaleFactor = 1F
                TouchCount = 0
                gestureDetector?.onTouchEvent(ev)

            }
        })

        gestureDetector = ScaleGestureDetector(requireContext(), object: ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                scaleFactor *= detector!!.scaleFactor
//                scaleFactor = if (scaleFactor < 1) 1F else scaleFactor // prevent our view from becoming too small //

//                scaleFactor = ((scaleFactor * 100) as Int).toFloat() / 100 // Change precision to help with jitter when user just rests their fingers //
                scaleFactor = scaleFactor*100

                var add = false
                var minus = false
                if(99.5 < scaleFactor && scaleFactor < 100.5){
                    tempScale = 100
                    currentScale = 100
                }
                else if(scaleFactor <= 90 && scaleFactor > 70){
                    //add
                    tempScale = 80
                }
                else if(scaleFactor <= 70 && scaleFactor > 40){
                    //add
                    tempScale = 60
                }
                else if(scaleFactor <= 40){
                    //add
                    tempScale = 40
                }
                else if(scaleFactor >= 110 && scaleFactor < 130){
                    //minus
                    tempScale = 120
                }
                else if(scaleFactor >= 130 && scaleFactor < 160){
                    //minus
                    tempScale = 140
                }
                else if(scaleFactor >= 160){
                    //minus
                    tempScale = 160

                }

                println("currentScale is $currentScale")
                println("tempScale is $tempScale")
                if((tempScale == 40 || tempScale == 80 || tempScale == 60) && currentScale > tempScale){
                    Log.d("Add image", "Add image")
                    currentScale = tempScale
                    add = true
                }
                else if((tempScale == 120 || tempScale == 140 || tempScale == 160) && currentScale < tempScale){
                    Log.d("minus image", "minus image")
                    currentScale = tempScale
                    minus = true
                }

                if(add){
                    if(GridItemCount <= 5) {
                        GridItemCount = GridItemCount + 2
                    }
                    else{
                        tempScale = 100
                        currentScale = 100
                    }
                }
                else if(minus){
                    if(GridItemCount >= 3){
                        GridItemCount = GridItemCount - 2
                    }
                    else{
                        tempScale = 100
                        currentScale = 100
                    }
                }






                view.findViewById<RecyclerView>(R.id.recycler_view_grid).apply{
                    this.adapter = mAdapter
                    layoutManager = GridLayoutManager(context,GridItemCount)
                }


                println("Scale factor is : " + scaleFactor)

                return super.onScale(detector)
            }


//            override fun onContextClick(e: MotionEvent?): Boolean {
//                return super.onContextClick(e)
//            }
//
//            override fun onDoubleTap(e: MotionEvent?): Boolean {
//                println("Double Tap")
//                if(GridItemCount == 3){
//                    GridItemCount = 5
//                } else{
//                    GridItemCount =3
//                }
//                view.findViewById<RecyclerView>(R.id.recycler_view_grid).apply{
//                    this.adapter = mAdapter
//                    layoutManager = GridLayoutManager(context,GridItemCount)
//                }
//
//                return super.onDoubleTap(e)
//            }
//
//            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
//                return super.onDoubleTapEvent(e)
//            }
//
//            override fun onDown(e: MotionEvent?): Boolean {
//                return super.onDown(e)
//            }
//
//            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
//                return super.onFling(e1, e2, velocityX, velocityY)
//            }
//
//            override fun onLongPress(e: MotionEvent?) {
//                super.onLongPress(e)
//            }
//
//            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//                return super.onScroll(e1, e2, distanceX, distanceY)
//            }
//
//            override fun onShowPress(e: MotionEvent?) {
//                super.onShowPress(e)
//            }
//
//            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
//                return super.onSingleTapConfirmed(e)
//            }
//
//            override fun onSingleTapUp(e: MotionEvent?): Boolean {
//                return super.onSingleTapUp(e)
//            }
//
//            override fun equals(other: Any?): Boolean {
//                return super.equals(other)
//            }
//
//            override fun hashCode(): Int {
//                return super.hashCode()
//            }
//
//            override fun toString(): String {
//                return super.toString()
//            }
        })

        //// GESTURE END




        return view
    }


    private fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var image : Uri? = data?.data
                var cr = activity?.contentResolver
                if(cr != null && image != null) {
                    val bitmap = when {
                        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver, image
                        )
                        else -> {
                            val source = ImageDecoder.createSource(cr, image)
                            ImageDecoder.decodeBitmap(source)

                        }
                    }
//                    mAdapter.addItem(GridItem(mAdapter.itemCount, bitmap))
                    isIn = false
                    for(s in mAdapter.dataList){
                        if(s.data == data?.data){
                            isIn = true
                            break
                        }
                    }
                    if(!isIn) {
                        mAdapter.addItem(GridItem(mAdapter.itemCount, bitmap, data?.data)) // add photo to recyclerview
                    }
                }
                else{
                    Log.d("Error", "Something Wrong")
                }
            }
            else{
                Log.d("Error", "Something Wrong")
            }
        }
        else{
            Log.d("Error", "Something Wrong")
        }
    }


    override fun onPauseFragment() {
        Log.d("tab","pauseB")
        this.onDestroyView()
    }

    override fun onResumeFragment() {
        Log.d("tab","resumeB")
        this.onResume()
    }

    override fun onCellClickListner(currentposition: Int, photolist: ArrayList<GridItem>) {
        // What to do when cell clicked
//        Toast.makeText(requireContext(),"Cell clicked", Toast.LENGTH_SHORT).show()
//        photoposition = currentposition
//        photoArray = photolist

        val mDialog = PhotoDialog() // make dialog object
        mDialog.show(requireFragmentManager(), "PHOTO") //dialog show
        mDialog.PhotoPosition = currentposition // send current position to dialog fragment
        mDialog.PhotoArray = photolist // send photoArray to dialog position


//        println(photoposition)
//        val intent = Intent(requireContext(), PhotoView::class.java)
//
//        intent.putExtra("key", 3)
//        requireContext().startActivity(intent)

    }

//    fun sendcurrentpostion(): Int {
//        return photoposition
//

}