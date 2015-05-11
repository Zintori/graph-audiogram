package com.zintori.graphchartlibrary;

public class DataModel {
	
	private float[] leftDatapoints = new float[8];
    private float[] rightDatapoints = new float[8];
    
	public DataModel(float[] left, float[] right) {
		System.arraycopy(left, 0, leftDatapoints, 0, left.length);
	    System.arraycopy(right, 0, rightDatapoints, 0, right.length);
	    if (left.length < leftDatapoints.length) {
	    	for (int i=left.length; i<leftDatapoints.length; i++) {
	    		leftDatapoints[i] = -1;
	    	}
	    }
	    if (right.length < rightDatapoints.length) {
	    	for (int i=right.length; i<rightDatapoints.length; i++) {
	    		rightDatapoints[i] = -1;
	    	}
	    }
	}
	
	/* initialize the data points with some values, for testing */
	
	public DataModel() {
		float[] left = { (float) 15, (float) 10, (float) 15, (float) 15, (float) 20, (float) 20, (float) 15, (float) 15};
	    float[] right = { (float) 35, (float) 40, (float) 40, (float) 60, (float) 65, (float) 70, (float) 90, (float) 100};
	    System.arraycopy(left, 0, leftDatapoints, 0, left.length);
	    System.arraycopy(right, 0, rightDatapoints, 0, right.length);
	}
	
	public float[] getLeftDataPoints() {
		return leftDatapoints;
	}
	
	public float[] getRightDataPoints() {
		return rightDatapoints;
	}
}
