package pien_project;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
class DetectFaceDemo {
  public void run() {
    System.out.println("\nRunning DetectFaceDemo");
    // Create a face detector from the cascade file in the resources
    // directory.
    CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/haarcascade_frontalface_alt.xml").getPath());
    Mat image = Imgcodecs.imread(getClass().getResource("/lena2.jpeg").getPath());
    // Detect faces in the image.
    // MatOfRect is a special container class for Rect.
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    System.out.println(String.format("%sつの顔を取得しました。", faceDetections.toArray().length));
    // Draw a bounding box around each face.
    for (Rect rect : faceDetections.toArray()) {
//    	System.out.println(rect.x);
        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
    }

    int size = faceDetections.toArray().length;

    Rect[] test = new Rect[size];
    for(int i=0 ; i<size ; i++) {
    	test[i] = faceDetections.toArray()[i];
//    	System.out.println(test[i]);
    }

    //ソート
    for(int i=0 ; i<size ; i++) {
    	int j = i;
    	while(j>0 && test[j-1].x > test[j].x) {
            Rect tmp = test[j];
            test[j] = test[j-1];
            test[j-1] = tmp;
            j--;
    	}
    }

    for(int i=0 ; i<size ; i++) {
    	System.out.println(test[i].x);
    }


    // Save the visualized detection.
    String filename = "faceDetection.png";
    System.out.println(String.format("ファイル %s を生成しました。", filename));
    Imgcodecs.imwrite(filename, image);
  }
}
public class Main {
  public static void main(String[] args) {
    // System.out.println("Hello, OpenCV");
    // Load the native library.
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    new DetectFaceDemo().run();
  }
}