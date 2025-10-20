package BMP;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.imageio.ImageIO;

import Render2D.List;
import Render2D.Mapping;

public class Test2 {
	public static void main(String args[]) {
		File file=new File("C:\\Users\\Justin Bien\\Downloads\\28263ce44d75dd1938c1a25329f14a9f.jpg");
		BufferedImage image2=null;
		try {
			if(file.exists()) {
				image2=ImageIO.read(file);
			}
		}catch(Exception E) {
			
		}
		if(image2==null) return;
		int[] pixels=new int[image2.getWidth()*image2.getHeight()];
		image2.getRGB(0,0,image2.getWidth(),image2.getHeight(),pixels,0,image2.getWidth());
		byte image2b[][]=toColorBytes(pixels,image2.getWidth(),image2.getHeight());
		System.out.println(image2b.length);
		System.out.println(image2b[0].length);
		System.out.println();
		BMPWriter bmp=new BMPWriter();
		bmp.setSign("BM");
		byte[] color1={0,0,127};
		byte[] color2= {0,0,0};
		byte[] color3= {0,127,0};
		byte image[][]= {{126,126,126,120,120,100},{120,120,120,126,126,126}};
		byte[][] canvas=createGradientGrid(10000,5000,color2,color2);
		drawGradientCircle(3300,3000,1000,color3,canvas);
		int xpoints[]={100,3100,3000,200};
		int ypoints[]={2,200,2100,2000};
		int xpoints2[]={3000,100,100,3200};
		int ypoints2[]={3000,3500,100,600};
		int planepoints[][]= {{xpoints[0],ypoints[0]},{xpoints[1],ypoints[1]},{xpoints[2],ypoints[2]},{xpoints[3],ypoints[3]}};
		drawTexturedPlane(xpoints2,ypoints2,image2b,canvas);
		bmp.setPixelArray(canvas);
		bmp.toFile();
		System.out.println("done");
	}
	public static byte[][] createGradientGrid(int width,int height,byte[] color1,byte[] color2){
		byte output[][]=new byte[height][width*3];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				output[i][j*3]=(byte)(((float)color1[0]*((float)(i+1)/(float)height))+((float)color2[0]*((float)(j+1)/(float)width)));
				output[i][j*3+1]=(byte)(((float)color1[1]*((float)(i+1)/(float)height))+((float)color2[1]*((float)(j+1)/(float)width)));
				output[i][j*3+2]=(byte)(((float)color1[2]*((float)(i+1)/(float)height))+((float)color2[2]*((float)(j+1)/(float)width)));
			}
		}
		return output;
	}
	public static void drawCircle(int x, int y, int radius,byte color[], byte canvas[][]){
		for(int yorigin=-radius;yorigin<=radius;yorigin++) {
			int xorigin=(int)(Math.sqrt(Math.pow(radius, 2)-Math.pow(yorigin, 2)));
			for(int xstart=-xorigin;xstart<=xorigin;xstart++) {
				int x2=x+xstart;
				int y2=y+yorigin;
				if(x2<0||y2<0||y2>=canvas.length||x2>=canvas[0].length) continue;
				canvas[y2][x2*3]=((int)canvas[y2][x2*3+2]+(int)color[2]>127)?  127:  (byte)((int)canvas[y2][x2*3+2]+(int)color[2]);
				canvas[y2][x2*3+1]=((int)canvas[y2][x2*3+1]+(int)color[1]>127)? 127: (byte)((int)canvas[y2][x2*3+1]+(int)color[1]);
				canvas[y2][x2*3+2]=((int)canvas[y2][x2*3]+(int)color[0]>127)? 127: (byte)((int)canvas[y2][x2*3]+(int)color[0]);
			}
		}
	}
	public static void drawGradientCircle(int x, int y, int radius,byte color[], byte canvas[][]){
		for(int yorigin=-radius;yorigin<=radius;yorigin++) {
			int xorigin=(int)(Math.sqrt(Math.pow(radius, 2)-Math.pow(yorigin, 2)));
			for(int xstart=-xorigin;xstart<=xorigin;xstart++) {
				int x2=x+xstart;
				int y2=y+yorigin;
				float dist=1f-(float)Math.sqrt(Math.pow(xstart, 2)+Math.pow(yorigin, 2))/radius;
				if(x2<0||y2<0||y2>=canvas.length||x2>=canvas[0].length) continue;
				canvas[y2][x2*3]=((float)canvas[y2][x2*3+2]+dist*(float)color[2]>127)?  127:  (byte)((float)canvas[y2][x2*3+2]+dist*(float)color[2]);
				canvas[y2][x2*3+1]=((float)canvas[y2][x2*3+1]+dist*(float)color[1]>127)? 127: (byte)((float)canvas[y2][x2*3+1]+dist*(float)color[1]);
				canvas[y2][x2*3+2]=((float)canvas[y2][x2*3]+dist*(float)color[0]>127)? 127: (byte)((float)canvas[y2][x2*3]+dist*(float)color[0]);
			}
		}
		
	}
	public static void drawTriangle(int xpoints[],int ypoints[],byte[] color, byte canvas[][]) {
		int ySpan=getHighest(ypoints)-getLowest(ypoints);
		List<Integer> xorigins[]=new List[ySpan+1];
		for (int i=0;i<xorigins.length;i++) xorigins[i]=new List<Integer>();
		for(int i=0;i<3;i++) {
			int dx=xpoints[(i+1)%3]-xpoints[i];
			int dy=ypoints[(i+1)%3]-ypoints[i];
			int dir=(dy<0) ? -1:1;
			if(dx==0) {
				for(int j=0;j+ypoints[i]!=ypoints[(i+1)%3];j+=dir) {
					xorigins[j+ypoints[i]-getLowest(ypoints)].add(xpoints[i]);
				}
				continue;
			}
			if(dy==0) {
				xorigins[ypoints[i]-getLowest(ypoints)].add(xpoints[i]);
				xorigins[ypoints[i]-getLowest(ypoints)].add(xpoints[(i+1)%3]);
				continue;
			}
			float m=(float)dy/(float)dx;
			float b=(float)ypoints[i]-m*(float)xpoints[i];
			for(int j=0;j+ypoints[i]!=ypoints[(i+1)%3];j+=dir) {
				int x2=(int)Math.round((float)((float)ypoints[i]+j-b)/m);
				xorigins[j+ypoints[i]-getLowest(ypoints)].add(x2);
			}
		}
		int yorigin=getLowest(ypoints);
		for(int y2=yorigin;y2<xorigins.length+yorigin;y2++) {
			int[] arr=List.<Integer>toArray(xorigins[y2-yorigin]);
			int highest=getHighest(arr);
			for(int x2=getLowest(arr);x2<=highest;x2++) {
				if(y2<0||x2<0||y2>=canvas.length||x2>=canvas[0].length/3) continue;
				canvas[y2][x2*3]=((int)canvas[y2][x2*3+2]+(int)color[2]>127)?  127:  (byte)((int)canvas[y2][x2*3+2]+(int)color[2]);
				canvas[y2][x2*3+1]=((int)canvas[y2][x2*3+1]+(int)color[1]>127)? 127: (byte)((int)canvas[y2][x2*3+1]+(int)color[1]);
				canvas[y2][x2*3+2]=((int)canvas[y2][x2*3]+(int)color[0]>127)? 127: (byte)((int)canvas[y2][x2*3]+(int)color[0]);
			}
		}
	}
	public static void drawConvexPoly(int xpoints[],int ypoints[],byte color[],byte canvas[][]) {
		if(xpoints.length!=ypoints.length) return;
		for(int i=0;i<xpoints.length-2;i++) {
			int xpoints2[]= {xpoints[0],xpoints[1+i],xpoints[2+i]};
			int ypoints2[]= {ypoints[0],ypoints[1+i],ypoints[2+i]};
			drawTriangle(xpoints2,ypoints2,color,canvas);
		}
	}
	public static void drawQuads(int xpoints[],int ypoints[],byte color[],byte canvas[][]) {
		int xpoints2[]= {xpoints[0],xpoints[1],xpoints[2]};
		int ypoints2[]= {ypoints[0],ypoints[1],ypoints[2]};
		int xpoints3[]= {xpoints[2],xpoints[0],xpoints[3]};
		int ypoints3[]= {ypoints[2],ypoints[0],ypoints[3]};
		drawTriangle(xpoints2,ypoints2,color,canvas);
		drawTriangle(xpoints3,ypoints3,color,canvas);
	}
	public static void drawTexturedPlane(int xpoints[],int ypoints[],byte imagetexture[][],byte canvas[][]) {
		int graph[][][][]=Mapping.mapPlane(xpoints, ypoints, imagetexture.length, imagetexture[0].length/3);
		//System.out.println(graph.length);
		for(int j=0;j<graph.length;j++) {
			for(int k=0;k<(int)(graph[0].length);k++) {
				int xpoints2[]= {graph[j][k][0][0],graph[j][k][1][0],graph[j][k][2][0],graph[j][k][3][0]};
				int ypoints2[]= {graph[j][k][0][1],graph[j][k][1][1],graph[j][k][2][1],graph[j][k][3][1]};
				byte color[]={imagetexture[k][j*3],imagetexture[k][(j*3)+1],imagetexture[k][(j*3)+2]};
				drawQuads(xpoints2,ypoints2,color,canvas);
			}
		}
//		float parallelpoints1[][]=new float[imagetexture.length+1][4];
//		float parallelpoints2[][]=new float[imagetexture[0].length/3+1][4];
//		{
//		float dx[]= {-planepoints[0][0]+planepoints[1][0],-planepoints[1][0]+planepoints[2][0],planepoints[2][0]-planepoints[3][0],planepoints[3][0]-planepoints[0][0]};
//		float dy[]= {-planepoints[0][1]+planepoints[1][1],-planepoints[1][1]+planepoints[2][1],planepoints[2][1]-planepoints[3][1],planepoints[3][1]-planepoints[0][1]};
//		System.out.println("D");
//		printArr(dx);
//		printArr(dy);
//		for(int i=0;i<imagetexture.length;i++) {
//			float c=(float)i/(float)imagetexture.length;
//			System.out.println(c);
//			parallelpoints1[i][2]=(float)planepoints[0][0]+(dx[0]*c);
//			parallelpoints1[i][3]=(float)planepoints[0][1]+(dy[0]*c);
//			parallelpoints1[i][0]=(float)planepoints[3][0]+(dx[2]*c);
//			parallelpoints1[i][1]=(float)planepoints[3][1]+(dy[2]*c);
//		}
//		float[] lastpoints={(float)planepoints[1][0],(float)planepoints[1][1],(float)planepoints[2][0],(float)planepoints[2][1]};
//		parallelpoints1[imagetexture.length]= lastpoints;
//		for(int j=0;j<imagetexture[0].length/3;j++) {
//			float c=(float)j/(float)(imagetexture[0].length/3);
//			parallelpoints2[j][0]=(float)planepoints[3][0]+(dx[3]*c);
//			parallelpoints2[j][1]=(float)planepoints[3][1]+(dy[3]*c);
//			parallelpoints2[j][2]=(float)planepoints[0][0]+(dx[1]*c);
//			parallelpoints2[j][3]=(float)planepoints[0][1]+(dy[1]*c);
//		}
//		System.out.println("PPs");
//		print2dArr(parallelpoints1);
//		float lastpoints2[]={(float)planepoints[1][0],(float)planepoints[1][1],(float)planepoints[3][0],(float)planepoints[3][1]};
//		parallelpoints2[imagetexture[0].length/3]= lastpoints2;
//		System.out.println("PPs2");
//		print2dArr(parallelpoints2);
//		}
//		{
//		for(int i=0;i<imagetexture.length;i++) {
//			float dx[]=new float[4];
//			float dy[]=new float[4];
//			dx[1]=parallelpoints1[i][2]-parallelpoints1[i][0];
//			dx[3]=parallelpoints1[i+1][2]-parallelpoints1[i+1][0];
//			dy[1]=parallelpoints1[i][3]-parallelpoints1[i][1];
//			dy[3]=parallelpoints1[i+1][3]-parallelpoints1[i+1][1];
//			for(int j=0;j<imagetexture[0].length/3;j++) {
//				dx[0]=-parallelpoints2[j][2]+parallelpoints2[j][0];
//				dx[2]=-parallelpoints2[j+1][2]+parallelpoints2[j+1][0];
//				dy[0]=parallelpoints2[j][1]-parallelpoints2[j][3];
//				dy[2]=parallelpoints2[j+1][1]-parallelpoints2[j+1][3];
//				System.out.println("D2");
//				printArr(dx);
//				printArr(dy);
//				int xpoints[]=new int[4];
//				int ypoints[]=new int[4];
//				{
//				float dx2[]= {dx[0],dx[3]};
//				float dy2[]= {dy[0],dy[3]};
//				float startpoints[]= {parallelpoints1[i][0],parallelpoints1[i][1],parallelpoints2[j][0],parallelpoints2[j][1]};
//				float ipoints[]=getIntersectionPoints(dx2,dy2,startpoints);
//				xpoints[0]=Math.round(Math.round(ipoints[0]));
//				ypoints[0]=Math.round(Math.round(ipoints[1]));
//				}
//				{
//				float dx2[]= {dx[0],dx[1]};
//				float dy2[]= {dy[0],dy[1]};
//				float startpoints[]= {parallelpoints1[i][0],parallelpoints1[i][1],parallelpoints2[j+1][0],parallelpoints2[j+1][1]};
//				float ipoints[]=getIntersectionPoints(dx2,dy2,startpoints);
//				xpoints[1]=Math.round(Math.round(ipoints[0]));
//				ypoints[1]=Math.round(Math.round(ipoints[1]));
//				}
//				{
//				float dx2[]= {dx[2],dx[1]};
//				float dy2[]= {dy[2],dy[1]};
//				float startpoints[]= {parallelpoints1[i+1][0],parallelpoints1[i+1][1],parallelpoints2[j+1][0],parallelpoints2[j+1][1]};
//				float ipoints[]=getIntersectionPoints(dx2,dy2,startpoints);
//				xpoints[2]=Math.round(Math.round(ipoints[0]));
//				ypoints[2]=Math.round(Math.round(ipoints[1]));
//				}
//				{
//				float dx2[]= {dx[2],dx[3]};
//				float dy2[]= {dy[2],dy[3]};
//				float startpoints[]= {parallelpoints1[i+1][0],parallelpoints1[i+1][1],parallelpoints2[j][0],parallelpoints2[j][1]};
//				float ipoints[]=getIntersectionPoints(dx2,dy2,startpoints);
//				xpoints[3]=Math.round(Math.round(ipoints[0]));
//				ypoints[3]=Math.round(Math.round(ipoints[1]));
//				}
//				System.out.println("Points");
//				printArr(xpoints);
//				printArr(ypoints);
//				byte color[]= {imagetexture[i][j*3],imagetexture[i][j*3+1],imagetexture[i][j*3+2]};
//				drawQuads(xpoints,ypoints,color,canvas);
//			}
//		}
//		}
		
	}
	public static void print2dArr(float[][] arr) {
		for(int i=0;i<arr.length;i++) {
			printArr(arr[i]);
		}
	}
	public static void printArr(float[] arr) {
		for(int i=0;i<arr.length-1;i++) {
			System.out.print(arr[i]+", ");
		}
		System.out.println(arr[arr.length-1]);
	}
	public static void printArr(int[] arr) {
		for(int i=0;i<arr.length-1;i++) {
			System.out.print(arr[i]+", ");
		}
		System.out.println(arr[arr.length-1]);
	}
	public static float[] getIntersectionPoints(float dx[],float dy[],float startpoints[]) {
		if(dx[0]==dx[1]) return null;
		float out[]=new float[2];
		if(dx[0]==0) {
			out[0]=startpoints[0];
			float m2=dy[1]/dx[1];
			float b2=m2*startpoints[2]-startpoints[3];
			out[1]=out[0]*m2+b2;
		}
		float m1=dy[0]/dx[0];
		float b1=m1*startpoints[0]-startpoints[1];
		if(dx[1]==0) {
			out[0]=startpoints[2];
			out[1]=out[0]*m1+b1;
		}
		float m2=dy[1]/dx[1];
		float b2=m2*startpoints[2]-startpoints[3];
		out[0]=(b1-b2)/(m2-m1);
		out[1]=out[0]*m1+b1;
		return out;
	}
	public static int getHighest(int vals[]) {
		int highest=vals[0];
		for(int val :vals) {
			if(val>highest) highest=val;
		}
		return highest;
	}
	public static int getLowest(int vals[]) {
		int lowest=vals[0];
		for(int val:vals) {
			if(val<lowest) lowest=val;
		}
		return lowest;
	}
	public static byte[][] toColorBytes(int[] pixels,int width,int height){
		byte out[][]=new byte[height][width*3];
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				int r=(byte)(int)((((byte)((pixels[(j*width)+i])&0xFF))));
				int g=(byte)(int)((((byte)((pixels[(j*width)+i]>>8)&0xFF))));
				int b=(byte)(int)((((byte)((pixels[(j*width)+i]>>16)&0xFF))));
				//System.out.println(r);
				out[j][i*3]=(byte)(b);
				out[j][i*3+1]=(byte)(g);
				out[j][i*3+2]=(byte)(r);
			}
		}
		return out;
	}
}
