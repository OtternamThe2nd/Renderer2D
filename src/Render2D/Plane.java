package Geometry;

public class Plane {
	public static float[] normalize(float x,float y) {
		float m=(float)Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		float[] points={x/m,y/m};
		return points;
	}
	public static float[] pointAngle(float angle) {
		angle=(float) angle/180*(float)Math.PI;
		float points[]=new float[2];
		points[0]=(float) (Math.cos(angle)-Math.sin(angle));
		points[1]=(float) (Math.cos(angle)+Math.sin(angle));
		return points;
	}
}
