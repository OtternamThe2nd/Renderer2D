package Render2D;
public class Mapping {
	static public int[][][][] mapPlane(int xpoints[],int ypoints[],float width,float height){
		int out[][][][]=new int[(int)height][(int)width][4][2];
		for(float i=0;i<height;i++) {
			float t1=((i)/height);
			float t2=((i+1)/height);
			float x11=(1f-t1)*xpoints[0]+t1*xpoints[3];
			float y11=(1f-t1)*ypoints[0]+t1*ypoints[3];
			float x21=(1f-t1)*xpoints[1]+t1*xpoints[2];
			float y21=(1f-t1)*ypoints[1]+t1*ypoints[2];
			float x12=(1f-t2)*xpoints[0]+t2*xpoints[3];
			float y12=(1f-t2)*ypoints[0]+t2*ypoints[3];
			float x22=(1f-t2)*xpoints[1]+t2*xpoints[2];
			float y22=(1f-t2)*ypoints[1]+t2*ypoints[2];
			for(float j=0;j<width;j++) {
				t1=((j)/width);
				t2=((j+1)/width);
				float x31=(1f-t1)*xpoints[0]+(t1)*xpoints[1];
				float y31=(1f-t1)*ypoints[0]+(t1)*ypoints[1];
				float x41=(1f-t1)*xpoints[3]+(t1)*xpoints[2];
				float y41=(1f-t1)*ypoints[3]+(t1)*ypoints[2];
				float x32=(1f-t2)*xpoints[0]+(t2)*xpoints[1];
				float y32=(1f-t2)*ypoints[0]+(t2)*ypoints[1];
				float x42=(1f-t2)*xpoints[3]+(t2)*xpoints[2];
				float y42=(1f-t2)*ypoints[3]+(t2)*ypoints[2];
				float[] xs1= {x11,x21,x31,x41};
				float[] ys1= {y11,y21,y31,y41};
				out[(int)i][(int)j][0]=intersect(xs1,ys1);
				float[] xs2= {x11,x21,x32,x42};
				float[] ys2= {y11,y21,y32,y42};
				out[(int)i][(int)j][1]=intersect(xs2,ys2);
				float[] xs3= {x12,x22,x32,x42};
				float[] ys3= {y12,y22,y32,y42};
				out[(int)i][(int)j][2]=intersect(xs3,ys3);
				float[] xs4= {x12,x22,x31,x41};
				float[] ys4= {y12,y22,y31,y41};
				out[(int)i][(int)j][3]=intersect(xs4,ys4);
			}
		}
		return out;
	}
	static public int[] intersect(float xpoints[],float ypoints[]) {
		float dx1=xpoints[1]-xpoints[0];
		float dy1=ypoints[1]-ypoints[0];
		float x=0,y=0;
		float m1=0, b1=0;
		if(dx1==0) {
			x=xpoints[0];
		}else {
		m1=dy1/dx1;
		b1=((-xpoints[0]*m1+ypoints[0]));
		}
		float dx2=xpoints[3]-xpoints[2];
		float dy2=ypoints[3]-ypoints[2];
		float m2=0,b2=0;
		if(dx2==0) {
			x= xpoints[2];
		}else {
			m2=dy2/dx2;
			b2=((float)(-xpoints[2]*m2+ypoints[2]));
			if(dx1!=0) x=((b2-b1)/(m1-m2));
		}
		if(dx1!=0) y=(m1*x+b1);
		else y=(m2*x+b2);
		//System.out.println(m1+", "+m2+", "+b1+", "+b2);
		//System.out.println(points[0]+", "+points[1]);
		//System.out.println(m1+", "+b1+", "+m2+", "+b2);
		int points[]= {Math.round(x),Math.round(y)};
		return points;
	}
	public static void output(float[] in) {
		for(int i=0;i<in.length-1;i++) {
			System.out.print(in[i]+", ");
		}
		System.out.println(in[in.length-1]);
	}
	public static void output(float in[][]) {
		for(int i=0;i<in.length;i++) {
			output(in[i]);
		}
	}
	public static void output(float in[][][]) {
		for(int i=0;i<in.length;i++) {
			output(in[i]);
		}
	}
	public static void output(float in[][][][]) {
		for(int i=0;i<in.length;i++) {
			output(in[i]);
		}
	}
	public static void output(int[] in) {
		for(int i=0;i<in.length-1;i++) {
			System.out.print(in[i]+", ");
		}
		System.out.println(in[in.length-1]);
	}
	public static void output(int in[][]) {
		for(int i=0;i<in.length;i++) {
			output(in[i]);
		}
	}
	public static void output(int in[][][]) {
		for(int i=0;i<in.length;i++) {
			output(in[i]);
		}
	}
	public static void output(int in[][][][]) {
		for(int i=0;i<in.length;i++) {
			output(in[i]);
		}
	}
}
