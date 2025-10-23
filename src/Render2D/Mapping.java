package Render2D;

public class Mapping {
	static public int[][][][] mapPlane(int xpoints[],int ypoints[],int width,int height){
		//float parallelxpoints[][]=new float[width][4];
		//float parallelypoints[][]=new float[height][4];
		int out[][][][]=new int[height][width][4][2];
		for(float i=0;i<height;i++) {
			float dx1=(xpoints[1]-xpoints[0]);
			float dy1=(ypoints[1]-ypoints[0]);
			float dx2=(xpoints[2]-xpoints[3]);
			float dy2=(ypoints[2]-ypoints[3]);
			float x11=xpoints[0]+(dx1*(i/(float)height));
			float y11=ypoints[0]+(dy1*(i/(float)height));
			float x21=xpoints[3]+(dx2*(i/(float)height));
			float y21=ypoints[3]+(dy2*(i/(float)height));
			float x12=xpoints[0]+(dx1*((float)(i+(float)1)/(float)height));
			float y12=ypoints[0]+(dy1*((float)(i+(float)1)/(float)height));
			float x22=xpoints[3]+(dx2*((float)(i+(float)1)/(float)height));
			float y22=ypoints[3]+(dy2*((float)(i+(float)1)/(float)height));
			for(float j=0;j<width;j++) {
				float dx3=(xpoints[3]-xpoints[0]);
				float dy3=(ypoints[3]-ypoints[0]);
				float dx4=(xpoints[2]-xpoints[1]);
				float dy4=(ypoints[2]-ypoints[1]);
				float x31=(float)xpoints[0]+(dx3*(j/(float)width));
				float y31=(float)ypoints[0]+(dy3*(j/(float)width));
				float x41=(float)xpoints[1]+(dx4*(j/(float)width));
				float y41=(float)ypoints[1]+(dy4*(j/(float)width));
				float x32=(float)xpoints[0]+(dx3*((float)(j+(float)1)/(float)width));
				float y32=(float)ypoints[0]+(dy3*((float)(j+(float)1)/(float)width));
				float x42=(float)xpoints[1]+(dx4*((float)(j+(float)1)/(float)width));
				float y42=(float)ypoints[1]+(dy4*((float)(j+(float)1)/(float)width));
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
		int points[]= {Math.round(x),Math.round(y)};
		return points;
	}
}
