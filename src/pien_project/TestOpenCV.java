package pien_project;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;


public class TestOpenCV {
	static ImageIO iio;
	static BasicImageProcessing bip;
	static int num; //認識できた人数
	static Scanner sc;
    static CascadeClassifier faceDetector;
    static Mat faceImg; //入力画像
    static Mat pienImg; //ぴえん画像
    static MatOfRect faceDetections;
    static Rect[] facesArray ;

    //準備
	public static void ready() {
        sc = new Scanner(System.in);
        faceDetector= new CascadeClassifier("/usr/local/Cellar/opencv/4.5.1_2/share/opencv4/haarcascades/haarcascade_frontalface_alt.xml");
        iio=new ImageIO();
        bip=new BasicImageProcessing();
        faceImg = iio.readImage("src/image/three.jpeg");
        pienImg=iio.readImage("src/image/pien.PNG");
        num=0; //0人で初期化
	}

	//顔を認識
	public static void detect() {
		faceDetections = new MatOfRect();
		// ファイルから顔を認識する
	    faceDetector.detectMultiScale(faceImg, faceDetections);
	    num = faceDetections.toArray().length;
        facesArray= new Rect[num];
	    sort();
    }

	//x座標が小さい順にソート
	public static void sort() {
		//x座標順に並び替え
	    if(num == 0) {
	    	System.out.println("顔を検出できませんでした。他のファイルで試してみてください。");
	    	System.exit(1);
	    }else {
	        System.out.println(String.format("%sつの顔を検出しました。", faceDetections.toArray().length));
	        for(int i=0 ; i<num ; i++) {
	        	facesArray[i] = faceDetections.toArray()[i];
	        }
	        for(int i=0 ; i<num ; i++) {
	        	int j = i;
	        	while(j>0 && facesArray[j-1].x > facesArray[j].x) {
	                Rect tmp = facesArray[j];
	                facesArray[j] = facesArray[j-1];
	                facesArray[j-1] = tmp;
	                j--;
	        	}
	        }
	    }
	}

	//貼り付け
	public static void putImg() {
	    boolean continueFlag = true;
        Set<Integer> pienRegisterSet = new HashSet<Integer>();

        while(continueFlag) {
            String n;
            String YorN;
            System.out.print("左から何番目の人にぴえんをつけますか？: ");
            //何番目にぴえんをつけるか
            n = sc.next();
            if(isInt(n)) {
            	int nn = Integer.parseInt(n);
            	if(nn > num || nn < 1) {
            		System.out.println("【エラー】数値 " + n + " は指定できません。1から" + num + "の間で指定してください。");
            		continue;
            	}
            	pienRegisterSet.add(nn);
            	boolean YorNFlag  =true;
            	while(YorNFlag) {
            		System.out.print("他の人にもぴえんをつけますか？ [yes/no]: ");
            		YorN = sc.next();
            		if(YorN.equals("yes")) {
            			YorNFlag = false;
            		}else if(YorN.equals("no")) {
            			YorNFlag = false;
            			continueFlag = false;
            		}else {
            			System.out.println("【エラー】yes か no で入力してください。");
            		}
            	}
            }else {
            	System.out.println("【エラー】int型の数値を入力してください。");
            }
        }


        //setに登録された顔に四角をつける
//        for(Integer n : pienRegisterSet) {
//            Imgproc.rectangle(faceImg, new Point(facesArray[n-1].x, facesArray[n-1].y), new Point(facesArray[n-1].x + facesArray[n-1].width, facesArray[n-1].y +facesArray[n-1].height), new Scalar(0, 255, 0));
//        }

        //画像を貼り付ける
        Mat paste_result=null;

        int cnt=0;

        for(Integer n:pienRegisterSet) {
        	if(cnt==0) {//初回
        		double max=Math.max(facesArray[n-1].width, facesArray[n-1].height);
            	double scope=(double)pienImg.cols()/max;

            	Mat resizePien = bip.resizeImage(pienImg,scope); //resize the image (1/4-size)
            	//ぴえんを適切なサイズに変更する
            	System.out.println(facesArray[n-1].height+" "+facesArray[n-1].width);

            	paste_result=bip.imposeImage(faceImg,resizePien,
            		new Point((facesArray[n-1].x),(facesArray[n-1].y)),
            		new Size(resizePien.cols(),resizePien.rows())
            	);
            	iio.saveImage("src/out-image/stamped.jpg",paste_result);
        	}else { //2回目以降
        		double max=Math.max(facesArray[n-1].width, facesArray[n-1].height);
            	double scope=(double)pienImg.cols()/max;

            	Mat resizePien = bip.resizeImage(pienImg,scope); //resize the image (1/4-size)
            	//ぴえんを適切なサイズに変更する
            	System.out.println(facesArray[n-1].height+" "+facesArray[n-1].width);
            	faceImg=iio.readImage("src/out-image/stamped.jpg");

            	paste_result=bip.imposeImage(faceImg,resizePien,
            		new Point((facesArray[n-1].x),(facesArray[n-1].y)),
            		new Size(resizePien.cols(),resizePien.rows())
            	);
            	iio.saveImage("src/out-image/stamped.jpg",paste_result);

        	}
        	cnt++;
        }
	}


	  //入力された値がintかどうかを判定するメソッド
	  private static boolean isInt(String n) {
		  boolean intFlag = true;
	      for(int i=0 ; i<n.length() ; i++) {
	      	if(Character.isDigit(n.charAt(i))) {
	      		continue;
	      	}else {
	      		intFlag = false;
	      		break;
	      	}
	      }
	      return intFlag;
	  }

	//メインメソッド
    public static void main (String [] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        iio = new ImageIO();
        bip = new BasicImageProcessing();
        ready();
        detect();
        putImg();
        sc.close();
    }
}
