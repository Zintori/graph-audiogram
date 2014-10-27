package com.zintori.graphchartlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;

public class GraphView extends View{
	
	private static final int MIN_LINES = 3;
    private static final int MAX_LINES = 14;
    private static final int[] DISTANCES = { 10 };
    
	private float[] leftDatapoints;
	private float[] rightDatapoints;
	private Paint paint = new Paint();
	private Path path = new Path();
	private Bitmap leftMarker;
	private Bitmap rightMarker;
	
	
	public GraphView (Context context, AttributeSet attrs) {
        super(context, attrs);
        leftMarker = BitmapFactory.decodeResource(getResources(),
				R.drawable.blue_cross);
        rightMarker = BitmapFactory.decodeResource(getResources(),
				R.drawable.red_circle);
        float[] leftDatapoints = { (float) 15, (float) 10, (float) 15, (float) 15, (float) 20, (float) 20, (float) 15, (float) 15};
        float[] rightDatapoints = { (float) 35, (float) 40, (float) 40, (float) 60, (float) 65, (float) 70, (float) 90, (float) 100};
        setChartData(leftDatapoints, rightDatapoints);
    }
	
	public float getScreenDensityRatio() {
		return this.getResources().getDisplayMetrics().density;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		float maxValue = getMax(leftDatapoints);
		
		// draw left data points and path
	    path.moveTo(getXPos(0), getYPos(leftDatapoints[0], maxValue));
	    canvas.drawBitmap(leftMarker, getXPos(0)-(5*getScreenDensityRatio()), getYPos(leftDatapoints[0], maxValue), paint);
	    for (int i = 1; i<leftDatapoints.length; i++) {
	        path.lineTo(getXPos(i), getYPos(leftDatapoints[i], maxValue));
	        canvas.drawBitmap(leftMarker, getXPos(i)-10, getYPos(leftDatapoints[i]-1, maxValue), paint);
	    }
	    
		paint.setStyle(Style.STROKE);
		paint.setColor(0xFF666666);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setShadowLayer(2, 1, 1, 0x80000000);
		//int left = getPaddingLeft();
	    //int top = getPaddingTop();
	    //int right = getWidth() - getPaddingRight();
	    //int bottom = getHeight() - getPaddingBottom();
	    //canvas.drawLine(left, top, right, bottom, paint);
		canvas.drawPath(path, paint);
		
		// draw right data points and path
	    path.moveTo(getXPos(0), getYPos(rightDatapoints[0], maxValue));
	    canvas.drawBitmap(rightMarker, getXPos(0)-(5*getScreenDensityRatio()), getYPos(rightDatapoints[0], maxValue), paint);
	
	    for (int i = 1; i<rightDatapoints.length; i++) {
	        path.lineTo(getXPos(i), getYPos(rightDatapoints[i], maxValue));
	        canvas.drawBitmap(rightMarker, getXPos(i)-10, getYPos(rightDatapoints[i]-1, maxValue), paint);
	    }
	    
	    canvas.drawPath(path, paint);
	    
		// draw grid and background
		drawBackground(canvas, maxValue);

	}
	
	private static final float RATIO = 4f / 3f;
	 
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        int maxWidth = (int) (heightWithoutPadding * RATIO);
        int maxHeight = (int) (widthWithoutPadding / RATIO);

        // set the dimensions
        if (widthWithoutPadding > maxWidth) {
            width = maxWidth + getPaddingLeft() + getPaddingRight();
        } else {
            height = maxHeight + getPaddingTop() + getPaddingBottom();
        }

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = getMeasuredWidth();
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = getMeasuredHeight();
        }

        setMeasuredDimension(width, height);
    }
	
	private float getMax(float[] dataPoints) {
		float max = dataPoints[0];
		for (int i=1; i<dataPoints.length; i++) {
			if (dataPoints[i] > max) {
				max = dataPoints[i];
			}
		}
		//return max;
		return 140;
	}
	
	/* helper method */
	private float getYPos(float value, float maxValue) {
	    float height = getHeight() - getPaddingTop() - getPaddingBottom();
	    value = ((value + 20) / maxValue) * height; // scale to view height (added in the 10 to compensate for starting value at -20)
	    //value = height - value; // invert   (add this invert only if the graph is incremental
	    value += getPaddingTop(); // offset for padding
	    return value;
	}
	
	/* helper method */
	 private float getXPos(float value) {
	    float width = getWidth() - getPaddingLeft() - getPaddingRight();
	    float maxValue = leftDatapoints.length - 1;

	    // scale it to the view size
	    value = (value / maxValue) * width;
	    // offset it to adjust for padding
	    value += getPaddingLeft();
	    return value;
	}
	
	public void setChartData(float[] leftDatapoints, float[] rightDatapoints) {
		this.leftDatapoints = leftDatapoints.clone();
		this.rightDatapoints = rightDatapoints.clone();
	}
	
	public void setShowText(boolean showText) {
		invalidate();
		requestLayout();
	}
	
	private void drawBackground(Canvas canvas, float maxValue) {
        int range = getLineDistance(maxValue);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setTextAlign(Align.LEFT);
        paint.setTextSize(16);
        paint.setStrokeWidth(1);
        for (int y = -20; y < maxValue; y += range) {
            final int yPos = (int) getYPos(y, maxValue);

            // turn off anti alias for lines for less fuzzy lines
            paint.setAntiAlias(false);
            canvas.drawLine(getXPos(0), yPos, getWidth()-15, yPos, paint);

            // turn on anti alias again for the text
            paint.setAntiAlias(true);
            if ((y != -20) && (y != 130)) {
            	canvas.drawText(String.valueOf(y), getPaddingLeft(), yPos - 2, paint);
            }
        }
        
        //draw vertical lines
        paint.setAntiAlias(false);
        for (int x = 0; x < 8; x++) {
        	final int xPos = (int) getXPos(x);
        	canvas.drawLine(xPos, getYPos(-20, maxValue), xPos, getHeight()-25, paint);
        	
        	//draw the text for vertical lines
        	paint.setAntiAlias(true);
        	String frequency;
        	
        	switch(x) {
        		case 1:
        			frequency = "250";
        			break;
        		case 2:
        			frequency = "500";
        			break;
        		case 3:
        			frequency = "1000";
        			break;
        		case 4:
        			frequency = "2000";
        			break;
        		case 5:
        			frequency = "4000";
        			break;
        		case 6:
        			frequency = "8000";
        			break;
        		default:
        			frequency = "";
        			break;
        	}
        		
        	canvas.drawText(frequency, xPos, getHeight()-5, paint);
        }
    }
	
	private int getLineDistance(float maxValue) {
        long distance;
        int distanceIndex = 0;
        long distanceMultiplier = 1;
        int numberOfLines = MIN_LINES;

        do {
            distance = DISTANCES[distanceIndex] * distanceMultiplier;
            numberOfLines = (int) FloatMath.ceil(maxValue / distance);

            distanceIndex++;
            if (distanceIndex == DISTANCES.length) {
                distanceIndex = 0;
                distanceMultiplier *= 10;
            }
        } while (numberOfLines < MIN_LINES || numberOfLines > MAX_LINES);

        return (int) distance;
    }

}
