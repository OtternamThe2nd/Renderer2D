Project made by Biebi

# To use in your program download only the bin\render2d.jar file then import it in your project

# Note: does not output the image of the file make sure to link it with my BMPWriter
  SampleCode

  Renderer renderer=new Renderer(width,height);
  /* edit renderer canvas here
    renderer.drawTriangle(param,param,param);
    renderer.drawSquare(param,param,param);
  */
  
  BMPWriter writer=new BMPWriter();
	writer.setSign("BM");
	writer.setFileName("TripMongName");
	writer.setPixelArray(renderer.getCanvas());
	writer.toFile();
