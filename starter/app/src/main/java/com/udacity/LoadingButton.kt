package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColor
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private lateinit var text:String
    private lateinit var loadingText:String
    private lateinit var baseColor:Color
    private lateinit var loadingColor:Color
    private val valueAnimator = ValueAnimator.ofFloat(0.0f,1.0f)
     var loadingPercentage:Float=0f
    private var incrment=80L
    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }
    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        loadingPercentage=it.animatedValue as Float
        it.duration+=incrment
        invalidate()
        if(loadingPercentage==1.0f){
            it.duration=1000L
            incrment=80L

            loadingPercentage=0.0f
            valueAnimator.setFloatValues(loadingPercentage,1.0f)
            buttonState=ButtonState.Completed


        }
    }

    init {
        isClickable=true
        valueAnimator.duration = 1000L
        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
            //downloadId=getInt(R.styleable.LoadingButton_downloadID,0)
           // loadingPercentage=getFloat(R.styleable.LoadingButton_LoadingPercentage,0f)
            text = getString(R.styleable.LoadingButton_Text)!!
            loadingText = getString(R.styleable.LoadingButton_LoadingText)!!
            baseColor = getColor(R.styleable.LoadingButton_LoadingColor,Color.BLUE).toColor()
            loadingColor = getColor(R.styleable.LoadingButton_LoadingColor,Color.CYAN).toColor()
        }
        valueAnimator.addUpdateListener(updateListener)

    }

    override fun performClick(): Boolean {
        if (!super.performClick()) return true

        valueAnimator.start()
        buttonState=ButtonState.Loading
        invalidate()
        return true
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color =baseColor.toArgb()

//        typeface = Typeface.create( "", Typeface.BOLD)
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color =Color.WHITE

//        typeface = Typeface.create( "", Typeface.BOLD)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private val loadPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color =loadingColor.toArgb()
        alpha=150
//        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private val loadOvalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color =Color.argb(255,238, 108, 77)
//        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private val rec=RectF(0F, 0F, widthSize.toFloat(), heightSize.toFloat())
    private var lRec=RectF(0F, 0F, loadingPercentage* widthSize.toFloat(), heightSize.toFloat())
    private var oval=RectF(4*widthSize.toFloat()/6F,  2*heightSize.toFloat()/9F, 6*widthSize.toFloat()/8F, 6*heightSize.toFloat()/9F)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        if(downloadManager!=null){
//            var x= downloadManager!!.query(
//                DownloadManager.Query()
//                    .setFilterById(downloadId.toLong())
//            )
//
//            x.moveToFirst()
//            var t=x.getDouble(10)
//            var d=x.getDouble(12)
//            Log.i("Download",((d/t).toString()))
//        }
               //Log.i("Download",(loadingPercentage.toString()))
        rec.set(0F, 0F, widthSize.toFloat(), heightSize.toFloat())
        oval.set(4*widthSize.toFloat()/6F,  2*heightSize.toFloat()/9F, 6*widthSize.toFloat()/8F, 6*heightSize.toFloat()/9F)
        Log.i("Download",widthSize.toString())
        lRec.set(0F, 0F, loadingPercentage* widthSize.toFloat(), heightSize.toFloat())
        canvas?.drawRect(rec,paint)
        canvas?.drawRect(lRec,loadPaint)
        canvas?.drawArc(oval, 270F, loadingPercentage*360F, true, loadOvalPaint)

        if(buttonState==ButtonState.Loading){
            canvas?.drawText(loadingText, (widthSize/2).toFloat(), (heightSize/2).toFloat(),textPaint)
        }else{


        canvas?.drawText(text, (widthSize/2).toFloat(), (heightSize/2).toFloat(),textPaint)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)

    }

    fun DownloadCompleted() {
        valueAnimator.duration=1000L
        incrment=0L
        valueAnimator.setFloatValues(loadingPercentage,1.0f)
        valueAnimator.start()


    }

}