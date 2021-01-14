package pien_project;

//***********************************************************
// Class for I/O of image files
//***********************************************************

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


public class ImageIO {

    //[method]

    //Read an input image.
    //@param
    //fileName: file name of the image
    //@return
    //image read
    public Mat readImage (String fileName){
        return Imgcodecs.imread(fileName);
    }

    //Save the processed image as a file (.jpg, .png, etc.).
    //@param
    //fileName: file name of the image
    //image : image to be saved as a file
    public void saveImage(String fileName, Mat image){
        Imgcodecs.imwrite(fileName, image);
    }

    //Rotate an image
    //@param
    //image: image to be rotated
    public Mat rotateImage (Mat image){

        Mat rotate = new Mat();
        Core.rotate(image, rotate, Core.ROTATE_90_COUNTERCLOCKWISE);

        return rotate;
    }
}