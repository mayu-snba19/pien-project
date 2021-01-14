package pien_project;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

public class TestOpenCV {
    public static void main (String [] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        ImageIO iio = new ImageIO();
        BasicImageProcessing bip = new BasicImageProcessing();

        //----------------------------------------
        //DEMO1: resizing and filtering the image
        //----------------------------------------
        Mat read_img = iio.readImage("src/face.jpg");
        Mat small = bip.resizeImage(read_img, 4); //resize the image (1/4-size)
        iio.saveImage("src/small_face.jpg", small);
        Mat filtered = bip.doImageFiltering(small); //image filtering
        iio.saveImage("src/filtered_face.jpg", filtered);


        //----------------------------------------
        //DEMO2: Mosaicing the image
        //Mosaicing the right-half size of the image
        //----------------------------------------
        Mat mosaic = bip.mosaicingImage(small,
            new Point ((small.cols()/2), 0),
            new Size (small.cols()/2,small.rows())
            );
        iio.saveImage("src/mosaiced_tsuda.jpg", mosaic);


        //----------------------------------------
        //DEMO3: Impose an image onto the input image
        //----------------------------------------
        Mat paste = iio.readImage("src/pien.PNG");
        Mat paste_small = bip.resizeImage(paste, 2);
        Mat paste_result = bip.imposeImage(small, paste_small,
           new Point ( (small.cols()-paste_small.cols()-1), (small.rows()-paste_small.rows()-1) ),
           new Size (paste_small.cols(), paste_small.rows())
           );
        iio.saveImage("src/stamped_tsuda.jpg", paste_result);
    }
}
