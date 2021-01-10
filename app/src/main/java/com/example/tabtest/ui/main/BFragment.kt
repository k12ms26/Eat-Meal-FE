package com.example.tabtest.ui.main


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabtest.MainActivity
import com.example.tabtest.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


//public var photoposition = 0
//public var photoArray = ArrayList<GridItem>()

public data class Photo (
        val user: String?,
        val image: String
)

public data class DeletePhoto(
        val message: String
)

//public data class serverPhoto(
//        val _id: String,
//        val user: String,
//        val image: String
//)

//public data class GetContact(
//        val _id: String,
//        val name: String,
//        val number: String
//
//)

public data class CreatePhoto(
        val result: String,
        val id: String
)

public interface PhotoInterface{
    @GET("api/photos/{user}")
    fun GetUserPhoto(
            @Path("user") user: String?
    ):Call<ArrayList<GridItem>>

    @POST("api/photos")
    fun CreatePhoto(
            @Body photo: Photo
    ): Call<CreatePhoto>

    @DELETE("api/photos/{id}")
    fun DeletePhoto(
            @Path("id") id: String?
    ): Call<DeletePhoto>

}

private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.249.18.133:8080/") // 마지막 / 반드시 들어가야 함
        .addConverterFactory(GsonConverterFactory.create()) // converter 지정
        .build() // retrofit 객체 생성

object PhotoApiObject {
    val retrofitService: PhotoInterface by lazy {
        retrofit.create(PhotoInterface::class.java)
    }
}




class BFragment : Fragment(), FragmentLifecycle, CellClickListner {

    var gestureDetector: ScaleGestureDetector? = null
    var GridItemCount = 3
    var scaleFactor: Float = 1F
    var TouchCount = 0
    var tempScale = 100
    var currentScale = 100
    lateinit var mCurrentPhotoPath: String



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

        val user = Firebase.auth.currentUser
        if (user == null) {
            button.visibility = View.GONE
        }

        button.setOnClickListener {
            dispatchTakePictureIntent()
            println("Touch")
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

        val call = PhotoApiObject.retrofitService.GetUserPhoto(Firebase.auth.currentUser?.uid)
        call.enqueue(object: retrofit2.Callback<ArrayList<GridItem>> {
            override fun onFailure(call: Call<ArrayList<GridItem>>, t: Throwable) {
                println("실패")
            }
            override fun onResponse(call: Call<ArrayList<GridItem>>, response: retrofit2.Response<ArrayList<GridItem>>) {
                println("성공?")
                println(response.body())
                if(response.isSuccessful){
                    response.body()?.let { mAdapter.bindItem(it) }
                    println("성공")
                }
                else{
                    println("성공?실패")


                }
            }
        })



        return view
    }


//    private fun openGallery(){
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.setType("image/*")
//        startActivityForResult(intent, OPEN_GALLERY)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(resultCode == Activity.RESULT_OK){
//            if(requestCode == OPEN_GALLERY){
//                var image : Uri? = data?.data
//                var cr = activity?.contentResolver
//                if(cr != null && image != null) {
//                    val bitmap = when {
//                        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
//                            activity?.contentResolver, image
//                        )
//                        else -> {
//                            val source = ImageDecoder.createSource(cr, image)
//                            ImageDecoder.decodeBitmap(source)
//
//                        }
//                    }
////                    mAdapter.addItem(GridItem(mAdapter.itemCount, bitmap))
//                    isIn = false
//                    for(s in mAdapter.dataList){
//                        if(s.data == data?.data){
//                            isIn = true
//                            break
//                        }
//                    }
//                    if(!isIn) {
//                        mAdapter.addItem(GridItem(mAdapter.itemCount, bitmap, data?.data)) // add photo to recyclerview
//                    }
//                }
//                else{
//                    Log.d("Error", "Something Wrong")
//                }
//            }
//            else{
//                Log.d("Error", "Something Wrong")
//            }
//        }
//        else{
//            Log.d("Error", "Something Wrong")
//        }
//    }


    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }
        // Continue only if the File was successfully created
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.tabtest.fileprovider",
                    it
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        }

//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivity(intent);

        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap // problem!! this bitmap is not original
            println("bitmap 가져옴")
//            imageView.setImageBitmap(imageBitmap)
        }

        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                val file = File(mCurrentPhotoPath)
                        if (Build.VERSION.SDK_INT >= 29) {
                            val source = ImageDecoder.createSource(requireActivity().getContentResolver(), Uri.fromFile(file));
                                try {
                                    val bitmap = ImageDecoder.decodeBitmap(source);
                                    if (bitmap != null) {
//                                        iv_photo.setImageBitmap(bitmap);
                                        println(bitmap)
                                        file.delete()
                                        var NewPhoto = GridItem(null, Firebase.auth.currentUser?.uid, imageToString(bitmap))
                                        val call = PhotoApiObject.retrofitService.CreatePhoto( Photo(Firebase.auth.currentUser?.uid,imageToString(bitmap)))
                                        call.enqueue(object: retrofit2.Callback<CreatePhoto> {
                                            override fun onFailure(call: Call<CreatePhoto>, t: Throwable) {
                                                TODO("Not yet implemented")
                                            }
                                            override fun onResponse(call: Call<CreatePhoto>, response: retrofit2.Response<CreatePhoto>) {

//                                                println(NewContact._id)
                                                Log.d("ADD", response.body()!!.result)
                                                response.body()?.result?.let {
                                                    if (response.body()?.result == "1") {
                                                        Log.d("ADD", response.body()!!.result)
                                                        NewPhoto._id = response.body()?.id
                                                        mAdapter.addItem(NewPhoto)
                                                    }
                                                }
                                            }
                                        })

                                    }
                                }
                                catch (e:IOException) { e.printStackTrace(); } }
                        else {
                            try {
                                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), Uri.fromFile(file));
                                if (bitmap != null) {
//                                    iv_photo.setImageBitmap(bitmap)
                                    println(bitmap)
                                    file.delete()
                                    var NewPhoto = GridItem(null, Firebase.auth.currentUser?.uid, imageToString(bitmap))
                                    val call = PhotoApiObject.retrofitService.CreatePhoto( Photo(Firebase.auth.currentUser?.uid,imageToString(bitmap)))
                                    call.enqueue(object: retrofit2.Callback<CreatePhoto> {
                                        override fun onFailure(call: Call<CreatePhoto>, t: Throwable) {
                                            TODO("Not yet implemented")
                                        }
                                        override fun onResponse(call: Call<CreatePhoto>, response: retrofit2.Response<CreatePhoto>) {

//                                                println(NewContact._id)
                                            Log.d("ADD", response.body()!!.result)
                                            response.body()?.result?.let {
                                                if (response.body()?.result == "1") {
                                                    Log.d("ADD", response.body()!!.result)
                                                    NewPhoto._id = response.body()?.id
                                                    mAdapter.addItem(NewPhoto)
                                                }
                                            }
                                        }
                                    })

                                }
                            }
                        catch (e :IOException ) { e.printStackTrace() }
                        }
            }
        }
        catch (error :Exception ) { error.printStackTrace(); }


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

    override fun onCellSettingClicklistner(v: View?, gridItem: GridItem) {
        val menu = PopupMenu(requireContext(), v)
        MenuInflater(requireContext()).inflate(R.menu.photo_menu, menu.menu)
        menu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.Photo_Delete ->{
                        val call = PhotoApiObject.retrofitService.DeletePhoto(gridItem._id)
                        call.enqueue(object: retrofit2.Callback<DeletePhoto> {
                            override fun onFailure(call: Call<DeletePhoto>, t: Throwable) {
                                println("실패")
                            }
                            override fun onResponse(call: Call<DeletePhoto>, response: retrofit2.Response<DeletePhoto>) {
                                println(response.body())
                                response.body()?.message?.let {
                                    if (response.body()?.message == "photo deleted") {
                                        Log.d("ADD", response.body()!!.message)
                                        mAdapter.deleteItem(gridItem)
                                    }
                                }

                            }
                        })
                    }
                    else ->{
                        return false
                    }
                }
                return false
            }

        })
        menu.show()
    }

    private fun imageToString(bitmap: Bitmap): String {
//        val `in`: InputStream? = requireActivity().contentResolver.openInputStream(path)
//        val bitmap = BitmapFactory.decodeStream(`in`)
//        val bmpCompressed = Bitmap.createScaledBitmap(bitmap, 500, 500, true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        //bitmap을 압축한다 -> JPEG로, 70%로, byteArrayOutputStream은 데이터를 내보내는 기능
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        //imgBytes -> 이러한 압축된 파일을 ByteArray형식으로 만든 형태다
        val imgBytes = byteArrayOutputStream.toByteArray()
        //이러한 ByteArray를 Base64로 변환한 형태를 리턴한다
        return Base64.encodeToString(imgBytes, Base64.DEFAULT)
    }

//    fun sendcurrentpostion(): Int {
//        return photoposition
//

}