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


        Mat read_img = iio.readImage("src/image/face.jpg");
        Mat small = bip.resizeImage(read_img, 4); //resize the image (1/4-size)
//        iio.saveImage("src/mid-image/small_face.jpg", small);

        Mat paste = iio.readImage("src/image/pien.PNG");
        Mat paste_small = bip.resizeImage(paste, 2);
        Mat paste_result = bip.imposeImage(small, paste_small,
           new Point ( (small.cols()-paste_small.cols()-1), (small.rows()-paste_small.rows()-1) ),
           new Size (paste_small.cols(), paste_small.rows())
           );
        iio.saveImage("src/out-image/stamped.jpg", paste_result);
    }
}
