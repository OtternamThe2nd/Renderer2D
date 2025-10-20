package Testing;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import BMP.BMPWriter;
import Render2D.Renderer2D;

public class Testing {

	public static void main(String[] args) {
		long initTime=System.currentTimeMillis();
		byte images[][][]=new byte[2][][];
		try {
			BufferedImage image1;
			image1=ImageIO.read(new File("C:\\Users\\Justin Bien\\Downloads\\vewwqeyihbaf1.jpeg"));
			int pixels1[]=new int[image1.getHeight()*image1.getWidth()];
			image1.getRGB(0, 0, image1.getWidth(), image1.getHeight(), pixels1, 0, image1.getWidth());
			BufferedImage image2;
			image2=ImageIO.read(new File("C:\\Users\\Justin Bien\\Downloads\\28263ce44d75dd1938c1a25329f14a9f.jpg"));
			int pixels2[]=new int[image2.getHeight()*image2.getWidth()];
			image2.getRGB(0, 0, image2.getWidth(), image2.getHeight(), pixels2, 0, image2.getWidth());
			images[0]=Renderer2D.toColorBytes(pixels1, image1.getWidth(), image1.getHeight());
			images[1]=Renderer2D.toColorBytes(pixels2, image2.getWidth(), image2.getHeight());
		}catch (Exception e) {
			System.out.println(e);
			System.out.println("Image Not Found");
			return;
		}
		Renderer2D renderer=new Renderer2D(10000,8000);
		int xpoints1[]= {1900,700,600,2300};
		int ypoints1[]= {1500,1500,800,800};
		int xpoints2[]= {5000,7000,6000,4500};
		int ypoints2[]= {3000,2300,1300,700};
		renderer.drawTexturedPlane(xpoints1, ypoints1, images[0]);
		renderer.drawTexturedPlane(xpoints2, ypoints2, images[1]);
		BMPWriter writer=new BMPWriter();
		writer.setSign("BM");
		writer.setFileName("Renderer");
		writer.setPixelArray(renderer.getCanvas());
		writer.toFile();
		System.out.println("This Program Took "+(System.currentTimeMillis()-initTime)+" miliseconds");
	}
}
