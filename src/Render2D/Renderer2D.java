/*

Renderer Keeps an array of bytes which act as a canvas and contains byte representing a pixel's bgr value(blue,green,red)

Constructors
	default():
		canvas width=1000
		canvas height=800
	this(int x,int y):
		sets canvas dimension to (x,y)

Variables
	(private byte[][])  canvas		:	keeps an array of byte and holds the color of the pixel from the grid specific point
	(private int)		width		:	holds the width of the canvas measured on pixels
	(private int)		width		:	holds the height of the canvas measured on pixels

Functions
	static byte[][] toColorBytes(int[] pixels,int width,int height):
		transform a single dimension array into 2 dimension array of bytes
		*for using image textures

	private setCanvasDimensions(int width,int height):
		initialize the dimensions for the canvas array
		*width is multiplied by three to keep values of each color range bgr.

	creategradientGrid(int width,int height):
		initialize a gradient grid for the canvas
		
	drawCircle(int x, int y, int radius,byte color[]):
		draws a circle on the grid
	
	drawGradientCircle(int x, int y, int radius,byte color[]):
		draws a circle on the grid with its color disappearing the further it is from the epicenter
	
	drawTriangle(int[] xpoints, int[] ypoints, byte[] color):
		draws a triangle on the grid requires three xpoints and ypoints
		
	drawQuads(int[] xpoints, int[] ypoints, byte[] color):
		draw a quadrilateral polygon on the grid 
		*assumes your polygon in convex
	
	drawConvexPoly(int[] xpoints, int[] ypoints, byte[] color:
	 	draw an infinitely sided convex polygon.
	 
	drawTexturedPlane(int[] xpoints, int[] ypoints, byte[][] image):
		draws over a quadrilateral plane an image in bytes
*/
package Render2D;

public class Renderer2D {
	private byte canvas[][];
	private int width,height;
	public Renderer2D(int width,int height) {
		setCanvasDimensions(width,height);
	}
	public Renderer2D() {
		setCanvasDimensions(1000,800);
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
	private void setCanvasDimensions(int width,int height) {
		this.canvas=new byte[height][width*3];
		this.width=width;
		this.height=height;
	}
	public void createGradientGrid(int width,int height,byte[] color1,byte[] color2){
		byte output[][]=new byte[height][width*3];
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				output[i][j*3]=(byte)(((float)color1[0]*((float)(i+1)/(float)height))+((float)color2[0]*((float)(j+1)/(float)width)));
				output[i][j*3+1]=(byte)(((float)color1[1]*((float)(i+1)/(float)height))+((float)color2[1]*((float)(j+1)/(float)width)));
				output[i][j*3+2]=(byte)(((float)color1[2]*((float)(i+1)/(float)height))+((float)color2[2]*((float)(j+1)/(float)width)));
			}
		}
		this.width=width;
		this.height=height;
		this.canvas=output;
	}
	public void drawCircle(int x, int y, int radius,byte color[]){
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
	public void drawGradientCircle(int x, int y, int radius,byte color[]){
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
	public void drawTriangle(int xpoints[],int ypoints[],byte[] color) {
		int ySpan=getHighest(ypoints)-getLowest(ypoints)+1;
		List<List<Integer>> xorigins=new List<List<Integer>>();
		for (int i=0;i<ySpan;i++) xorigins.add(new List<Integer>());
		for(int i=0;i<3;i++) {
			int dx=xpoints[(i+1)%3]-xpoints[i];
			int dy=ypoints[(i+1)%3]-ypoints[i];
			int dir=(dy<0) ? -1:1;
			if(dx==0) {
				for(int j=0;j+ypoints[i]!=ypoints[(i+1)%3];j+=dir) {
					xorigins.getValue(j+ypoints[i]-getLowest(ypoints)).add(xpoints[i]);
				}
				continue;
			}
			if(dy==0) {
				xorigins.getValue(ypoints[i]-getLowest(ypoints)).add(xpoints[i]);
				xorigins.getValue(ypoints[i]-getLowest(ypoints)).add(xpoints[(i+1)%3]);
				continue;
			}
			float m=(float)dy/(float)dx;
			float b=(float)ypoints[i]-m*(float)xpoints[i];
			for(int j=0;j+ypoints[i]!=ypoints[(i+1)%3];j+=dir) {
				int x2=(int)Math.round((float)((float)ypoints[i]+j-b)/m);
				xorigins.getValue(j+ypoints[i]-getLowest(ypoints)).add(x2);
			}
		}
		int yorigin=getLowest(ypoints);
		for(int y2=yorigin+1;y2<xorigins.size+yorigin;y2++) {
			int[] arr=List.<Integer>toArray(xorigins.getValue(y2-yorigin));
			int highest=getHighest(arr);
			for(int x2=getLowest(arr)+1;x2<=highest;x2++) {
				if(y2<0||x2<0||y2>=canvas.length||x2>=canvas[0].length/3) continue;
				canvas[y2][x2*3]=((int)canvas[y2][x2*3+2]+(int)color[2]>127)?  127:  (byte)((int)canvas[y2][x2*3+2]+(int)color[2]);
				canvas[y2][x2*3+1]=((int)canvas[y2][x2*3+1]+(int)color[1]>127)? 127: (byte)((int)canvas[y2][x2*3+1]+(int)color[1]);
				canvas[y2][x2*3+2]=((int)canvas[y2][x2*3]+(int)color[0]>127)? 127: (byte)((int)canvas[y2][x2*3]+(int)color[0]);
			}
		}
	}
	public void drawQuads(int xpoints[],int ypoints[],byte color[]) {
		int xpoints2[]= {xpoints[0],xpoints[1],xpoints[2]};
		int ypoints2[]= {ypoints[0],ypoints[1],ypoints[2]};
		int xpoints3[]= {xpoints[2],xpoints[0],xpoints[3]};
		int ypoints3[]= {ypoints[2],ypoints[0],ypoints[3]};
		drawTriangle(xpoints2,ypoints2,color);
		drawTriangle(xpoints3,ypoints3,color);
	}
	public void drawConvexPoly(int xpoints[],int ypoints[],byte color[]) {
		if(xpoints.length!=ypoints.length) return;
		for(int i=0;i<xpoints.length-2;i++) {
			int xpoints2[]= {xpoints[0],xpoints[1+i],xpoints[2+i]};
			int ypoints2[]= {ypoints[0],ypoints[1+i],ypoints[2+i]};
			drawTriangle(xpoints2,ypoints2,color);
		}
	}
	public void drawTexturedPlane(int xpoints[],int ypoints[],byte imagetexture[][]) {
		int graph[][][][]=Mapping.mapPlane(xpoints, ypoints, imagetexture.length, imagetexture[0].length/3);
		for(int j=0;j<graph.length;j++) {
			for(int k=0;k<(int)(graph[0].length);k++) {
				int xpoints2[]= {graph[j][k][0][0],graph[j][k][1][0],graph[j][k][2][0],graph[j][k][3][0]};
				int ypoints2[]= {graph[j][k][0][1],graph[j][k][1][1],graph[j][k][2][1],graph[j][k][3][1]};
				byte color[]={imagetexture[k][j*3],imagetexture[k][(j*3)+1],imagetexture[k][(j*3)+2]};
				drawQuads(xpoints2,ypoints2,color);
			}
		}
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
	public void resetCanvas() {
		setCanvasDimensions(this.width,this.height);
	}
	public byte[][] getCanvas() {
		return canvas;
	}
}
