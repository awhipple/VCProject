package hybridimage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;

class HybridImageDemo {
  public void run() {
     
    final int USE_EXAMPLE = 1;  
    
    String[] fileNames = new String[] {
      "/resources/images/dog2.jpg", "/resources/images/cat2.jpg",
      "/resources/images/dog3.png", "/resources/images/cat3.png"
    };
  
    String image1FileName = fileNames[USE_EXAMPLE*2];
    String image2FileName = fileNames[USE_EXAMPLE*2+1];
    
    Mat firstImage = loadImage(image1FileName);
    Mat secondImage = loadImage(image2FileName);
    
    Imgproc.GaussianBlur(firstImage, firstImage, new Size(133, 133), 5);
    
    Imgproc.Laplacian(secondImage, secondImage, 16, 3, 1, 0);
    Core.bitwise_not(secondImage, secondImage);
    
    Core.bitwise_and(firstImage, secondImage, firstImage);
    
    String filename = "result.png";
    System.out.println(String.format("Writing %s", filename));
    Highgui.imwrite(filename, firstImage);
  }
  
  private Mat loadImage(String fileName) {
    String inputImageFileName = getClass().getResource(fileName).getPath();
    if(inputImageFileName.startsWith("/", 0)) {
        inputImageFileName = inputImageFileName.replaceFirst("/", "");
    }
    return Highgui.imread(inputImageFileName);
  }
}

public class HybridImage {
    public static void main(String[] args) {
        System.out.println("Hello, OpenCV");

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HybridImageDemo().run();
    }
}
