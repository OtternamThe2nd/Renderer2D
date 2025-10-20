package BMP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Arrays;

public class BMPWriter {
	public int insize=0;
	public int count=0;
	public String filename="default";
	public byte[] sign=new byte[2],size=new byte[4];
	public byte[] reserve=new byte[4],offset=new byte[4];
	public byte[] infosize=new byte[4],width=new byte[4];
	public byte[] height=new byte[4],planes=new byte[2];
	public byte[] bitppixel=new byte[2],compression=new byte[4];
	public byte[] imageSize=new byte[4],XpixelPerM=new byte[4];
	public byte[] YpixelPerM=new byte[4],colorUsed=new byte[4];
	public byte[] importantColors=new byte[4];
	public byte pixelbytedump[][];
	public byte header[][]= {sign,size,reserve,offset,infosize,width,height,planes,bitppixel,compression,imageSize,XpixelPerM
			,YpixelPerM,colorUsed,importantColors};
	public byte imagebyte[];
	public int pixelsbytesize=0;
	public int headersize=0;
	public int intinfosize=0;
	public BMPWriter() {
		System.out.println(sign[0]);
		planes[0]=(byte)1;
		bitppixel[0]=(byte)24;
		setInfoSize(40);
		setHeaderSize(14);
	}
	public void setHeaderSize(int x) {
		this.headersize=14;
		setSize(headersize+pixelsbytesize+intinfosize);
		setOffset(headersize+intinfosize);
	}
	public void setWidth(int x) {
		for(int i=0;i<4;i++) {
			width[i]=(byte)  (x>>(8*i));
		}
	}
	public void setHeight(int x) {
		for(int i=0;i<4;i++) {
			height[i]=(byte)  (x>>(8*i));
		}
	}
	void setOffset(int x) {
		for(int i=0;i<4;i++) {
			offset[i]=(byte)  (x>>(8*i));
		}
	}
	public void setPixelArray(byte[][] pixelarr) {
		pixelbytedump=pixelarr;
		setImageArray(pixelbytedump);
		setHeight(pixelarr.length);
		setWidth(pixelarr[0].length/3);
	}
	public void setFileName(String str) {
		this.filename=str;
	}
	public byte[] setImageArray(byte[][] pixelarr) {
		int height=pixelarr.length,width=pixelarr[0].length;
		setPixelByteSize(height*(width));
		byte[] padding= {(byte)0};
		byte output[]=new byte[pixelsbytesize];
		int counter=0;
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				output[counter++]=pixelarr[i][j];
			}
		}
		imagebyte=output;
		return output;
	}
	public void setPixelByteSize(int x) {
		this.pixelsbytesize=x;
		for(int i=0;i<4;i++) {
			imageSize[i]=(byte) (x>>(8*i));
		}
		setSize(intinfosize+pixelsbytesize+headersize);
	}
	public void setSize(int in) {
		this.insize=in;
		for(int i=0;i<4;i++) {
			size[i]=(byte) (in>>(8*i));
		}
	}
	public void setInfoSize(int in) {
		intinfosize=in;
		for(int i=0;i<4;i++) {
			infosize[i]=(byte) (in>>(8*i));
		}
		setSize(intinfosize+pixelsbytesize+headersize);
	}
	public void setSign(String sign) {
		int counter=0;
		for(char x:sign.toCharArray()) {
			this.sign[counter++]=(byte)x;
		}
	}
	public byte[] write() {
		byte[] output=new byte[insize];
		int atbyte=0;
		for(byte[] bytes:header) {
			writeTo(output, bytes, atbyte);
			atbyte+=bytes.length;
		}
		writeTo(output,imagebyte,atbyte);
		return output;
	}
	public File toFile() {
		System.out.println(intinfosize+", "+headersize+", "+pixelsbytesize+", "+insize);
		byte[] out=write();
		try {
			File bmp=new File(filename+".bmp");
			while(!bmp.createNewFile()) {
				bmp=new File(filename+count+++".bmp");
			}
			FileOutputStream fileout=new FileOutputStream(bmp);
			fileout.write(out);
			fileout.flush();
			fileout.close();
			return bmp;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	byte[] writeTo(byte[] store,byte[] input,int at) {
		for(byte bit:input) {
			store[at++]=bit;
		}
		return store;
	}
}
