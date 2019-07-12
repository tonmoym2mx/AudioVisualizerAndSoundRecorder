package com.tonmoym2mx.audiovisualizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class AudioVisualizer extends View {

    private int count =0;

    private ArrayList<Integer> arrayList;

    public AudioVisualizer(Context context) {
        super(context);
        init(null);
    }

    public AudioVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AudioVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    @SuppressLint("NewApi")
    public AudioVisualizer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        arrayList = new ArrayList<>();
        for(int i=0;i<=180;i++){
            arrayList.add(5);
        }
    }
    public void  addAmplitude(int amp){
        arrayList.remove(0);
        arrayList.add(amp);
        invalidate();
    }
    public void reset(){
       // arrayList = new ArrayList<>();
        for(int i=0;i<=180;i++){
            arrayList.remove(i);
            arrayList.add(i,5);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        int heightDiv = canvas.getHeight()/2;
        for(Integer i :arrayList){
            canvas.drawLine(count,
                    map(i,0,20000,heightDiv,0),
                    count,
                    map(i,0,20000,heightDiv,canvas.getHeight()),
                    paint);

            count +=canvas.getWidth()/150;
        }
        count =0;

    }
    long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

}
