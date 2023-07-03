package com.av_projects.paintapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.av_projects.paintapp.MainActivity.Companion.paintBrush
import com.av_projects.paintapp.MainActivity.Companion.path

class PaintView: View {

    var params: ViewGroup.LayoutParams? = null

    companion object{
        var pathList = ArrayList<Path>()
        var colorList = ArrayList<Int>()
        var currentBrush = Color.BLACK
    }

    constructor(context: Context) : this(context, null){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        paintBrush.isAntiAlias = true
        paintBrush.color = currentBrush
        paintBrush.style = Paint.Style.STROKE
        paintBrush.strokeJoin = Paint.Join.ROUND
        paintBrush.strokeWidth = 8f

        //Give alignment with respect to parent layout
        params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // moments of user finger/pen on the dashboard
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // remove ? = bc don't want mandatory
        var x  = event.x
        var y = event.y

        // to perform action according to user moments
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE ->{
                path.lineTo(x,y)
                pathList.add(path)
                colorList.add(currentBrush)
            }
            else -> return false
        }
        //It is used to tell Non-UI thread that some changes is has been made to UI
        postInvalidate() // very imp to add after when
        return false;
    }

    override fun onDraw(canvas: Canvas) {

        for(i in pathList.indices){
            paintBrush.setColor(colorList[i])
            canvas.drawPath(pathList[i], paintBrush)
            //It is similar to above one, which tells non-UI threads about changes which are made on UI.
            invalidate()
        }
    }

}