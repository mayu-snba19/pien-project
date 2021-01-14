package pien_project;

//***********************************************************
// Class for basic image processing modules
//***********************************************************

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class BasicImageProcessing {

    //[method]

    //Resize an image (down-sampling)
    //@param
    //input_img: input image
    //ratio: value for the down-sampling magnitude
    //@return
    //down-sampled image
    public Mat resizeImage (Mat input_img, int ratio){

        Mat dst = new Mat();
        int inputWidth = input_img.cols();
        int inputHeight = input_img.rows();
        Imgproc.resize(input_img, dst, new Size ( (int)(inputWidth/ratio), (int)(inputHeight/ratio) ) );

        return dst.clone();
    }


    //Draw detected object region.
    //@param
    //img: input image. object region will be drawn on this image.
    //pos: position of the object estimated with a template matching.
    //template_size: size of the template image (size of the object)
    //@return
    //drawn image
    public Mat drawDetectRegion (Mat img, Point pos, Size drawSize){

        Mat detect_result = img.clone();

        Imgproc.rectangle(detect_result, pos,
            new Point (pos.x + drawSize.width, pos.y + drawSize.height),
            new Scalar (0,0,255), 2);

        return detect_result.clone();
    }


	//------------------------------
	//Impose an image on a region in another image
	//@param dst_img: impose-destination image (input image)
    //@param impose_img: impose image
    //@param pos: position where "impose_img" is imposed
    //@param imposeSize: size of the impose region
    //@return imposed image
	//------------------------------
	public Mat imposeImage(Mat dst_img, Mat impose_img, Point pos, Size imposeSize){

        Mat pasted_img = dst_img.clone();

        //---------------------------
        //Impose "impose_img" on "dst_img"
        //---------------------------
        //Check whether the impose region is out of the region of "dst_img".
        if(pos.x + imposeSize.width < dst_img.cols() &&
            pos.y + imposeSize.height < dst_img.rows() ){

            //set the impose region
            Mat roiImg = pasted_img.submat(
                (int)pos.y, (int)(pos.y + imposeSize.height),
                (int)pos.x, (int)(pos.x + imposeSize.width)
                );

            impose_img.copyTo(roiImg); //impose
        }

        return pasted_img;
	}


	//------------------------------
	//Mosaicing an image
	//@param img: image to be mosaiced
    //@param pos: position of the mosaicing region
    //@param mosaic_size: size of the mosaicing region
    //@return mosaicked image
	//------------------------------
    public Mat mosaicingImage(Mat img, Point pos, Size mosaic_size){

        Mat mosaiced_img = img.clone();

        //---------------------------
        // Mosaic the image
        //---------------------------
        Rect rect = new Rect(pos, mosaic_size);
        Mat trim = new Mat(mosaiced_img, rect);

        //mosaic processing
        Imgproc.resize(trim, trim, new Size(), 0.1, 0.1, Imgproc.INTER_NEAREST);
        Imgproc.resize(trim, trim, new Size(), 10., 10., Imgproc.INTER_NEAREST);

        //---------------------------
        // Impose the mosaiced image
        //---------------------------
        for(int j = 0;j<trim.height();j++)
        {
            for(int i = 0;i<trim.width();i++)
            {
                double [] value = trim.get(j,  i);
                mosaiced_img.put(rect.y+j, rect.x+i, value);
            }
        }
        return mosaiced_img;
	}


    //Reducing noise on an input image
    //[args]
    //- input_img:Mat -> input image
    //[return]
    //- Noise-reduced image
    public Mat doImageFiltering (Mat input_img){

        Mat dst = new Mat ();
        Imgproc.bilateralFilter(input_img, dst, 7, 100., 100.);

        return dst.clone();
    }
}