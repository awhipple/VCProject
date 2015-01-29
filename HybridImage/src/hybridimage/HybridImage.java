package hybridimage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

enum ResultType {
    ShowFirst, ShowSecond, ShowSecondLP, ShowResult;
}

enum ImageAddType {
    Add, AddWeighted, BitwiseAnd;
}

class HybridImageDemo {
  public void run() {
     
    final int USE_EXAMPLE = 2;  
    //final int USE_EXAMPLE = 4;
    
    ResultType resultType;
    //resultType = ResultType.ShowFirst;
    //resultType = ResultType.ShowSecond;
    //resultType = ResultType.ShowSecondLP;
    resultType = ResultType.ShowResult;
    
    final int   LP_GAUSSIAN_SIZE = 0,
                LP_STD_DEV = 5;
    
    final int   HP_GAUSSIAN_SIZE = 0,
                HP_STD_DEV = 7;
    final boolean HP_FLIP = false;
    final int HP_BRIGHTEN = 120;
    
    ImageAddType imageAddType;
    //imageAddType = ImageAddType.Add;
    imageAddType = ImageAddType.AddWeighted;
    //imageAddType = ImageAddType.BitwiseAnd;
    
    String[] fileNames = new String[] {
      "/resources/images/dog2.jpg", "/resources/images/cat2.jpg",
      "/resources/images/dog3.png", "/resources/images/cat3.png",
      "/resources/images/marilyn.bmp", "/resources/images/einstein.bmp",
      "/resources/images/grassCat.jpg", "/resources/images/grassDog.jpg",
      "/resources/images/elephant.png", "/resources/images/leopard.png"
    };
  
    String image1FileName = fileNames[USE_EXAMPLE*2];
    String image2FileName = fileNames[USE_EXAMPLE*2+1];
    
    Mat firstImage = loadImage(image1FileName);
    Mat secondImage = loadImage(image2FileName);
    
    Mat secondImageTemp = new Mat();
    Mat result = new Mat();
        
    Imgproc.GaussianBlur(firstImage, firstImage, new Size(LP_GAUSSIAN_SIZE, LP_GAUSSIAN_SIZE), LP_STD_DEV);
        
    Imgproc.GaussianBlur(secondImage, secondImageTemp, new Size(HP_GAUSSIAN_SIZE, HP_GAUSSIAN_SIZE), HP_STD_DEV, HP_STD_DEV);
    Core.subtract(secondImage, secondImageTemp, secondImage);
    if(HP_FLIP) Core.bitwise_not(secondImage, secondImage);
    Core.add(secondImage, new Scalar(HP_BRIGHTEN, HP_BRIGHTEN, HP_BRIGHTEN), secondImage);
    
    switch(imageAddType) {
        case Add:           Core.add(firstImage, secondImage, result);
                            break;
        case AddWeighted:   Core.addWeighted(firstImage, .5, secondImage, .5, 25, result);
                            break;
        case BitwiseAnd:    Core.bitwise_and(firstImage, secondImage, result);
                            break;
    }
    
    
    String filename = "result.png";
    System.out.println(String.format("Writing %s", filename));
    switch(resultType) {
        case ShowFirst:     Highgui.imwrite(filename, firstImage);
                            break;
        case ShowSecond:    Highgui.imwrite(filename, secondImage);
                            break;
        case ShowSecondLP:  Highgui.imwrite(filename, secondImageTemp);
                            break;
        case ShowResult:    Highgui.imwrite(filename, result);
                            break;
    }
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
