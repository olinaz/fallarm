package edu.npu.fallarm.model;

public class ThreeDvalue {
	protected float x;
	protected float y;
	protected float z;
	
	public ThreeDvalue(float xx, float yy, float zz){
		x = xx;
		y = yy;
		z = zz;		
	}
	public ThreeDvalue(){
		
	}
	
	public int getVectorSum(){
		int result = (int) Math.round(Math.sqrt(x * x + y * y + z * z));
		return result;
	}
	public String toString(){
		return new String("x:" + x + ", y:" + y + ", z:" + z);
	}
}
