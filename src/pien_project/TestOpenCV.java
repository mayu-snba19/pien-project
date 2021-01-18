package pien_project;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

public class TestOpenCV {
	static ImageIO2 iio;
	static BasicImageProcessing bip;
	static int num; //認識できた人数
	static Scanner sc;
    static CascadeClassifier faceDetector;
    static Mat faceImg; //入力画像
    static MatOfRect faceDetections;
    static Rect[] facesArray ;
    static Set<Integer> pienRegisterSet; //ぴえんをつける人を登録するSet
    static String inputFileName;
    static String inputFilePass;
    static String outputFileName;
    static String outputFilePass;

    //準備
	public static void ready() {
        sc = new Scanner(System.in);
        faceDetector= new CascadeClassifier("/usr/local/Cellar/opencv/4.5.1_2/share/opencv4/haarcascades/haarcascade_frontalface_alt.xml");
//      faceDetector= new CascadeClassifier("/usr/local/Cellar/opencv/4.5.0_5/share/opencv4/haarcascades/haarcascade_frontalface_alt.xml");
        iio=new ImageIO2();
        bip=new BasicImageProcessing();
        num=0; //0人で初期化
	}

	public static void input() {
	    System.out.print("顔を検出したいファイル名を入力してください: ");
	    inputFileName = sc.next();
	    System.out.print("出力するファイル名を入力してください: ");
	    outputFileName = sc.next();
	    inputFilePass = "src/image/"+inputFileName;
	    outputFilePass = "src/out-image/"+outputFileName;
      faceImg = iio.readImage(inputFilePass);

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

	//入力
	public static void userInput() {
	    boolean continueFlag = true;
        pienRegisterSet = new HashSet<Integer>();

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
	}

	public static void putImg() {
		//画像を貼り付ける
        Boolean boo=false;
        BufferedImage bufferedImage1=null;
        BufferedImage bufferedImage2=null;

        try {
        	bufferedImage1=ImageIO.read(new File(inputFilePass));
          	bufferedImage2=ImageIO.read(new File("src/image/pien.PNG"));
        }catch(Exception e){
        	e.printStackTrace();
        }
        Graphics graphics1=null;
        for(Integer n:pienRegisterSet) {
        	 try {
        		 if(boo) { //2回目以降
        			bufferedImage1=ImageIO.read(new File(outputFilePass));
             	}
             	BufferedImage bufferedImage3 = new BufferedImage(facesArray[n-1].width, facesArray[n-1].height, BufferedImage.TYPE_INT_ARGB);
             	bufferedImage3.createGraphics().drawImage(bufferedImage2.getScaledInstance(
             			facesArray[n-1].width, facesArray[n-1].height, Image.SCALE_AREA_AVERAGING)
             			  ,0, 0, facesArray[n-1].width, facesArray[n-1].width, null);
     			graphics1 = bufferedImage1.getGraphics();
     			int x = facesArray[n-1].x;
     			int y = facesArray[n-1].y;
     			graphics1.drawImage(bufferedImage3, x, y, null);
     			ImageIO.write(bufferedImage1, "png", new File(outputFilePass));
             }catch(Exception e) {
             	e.printStackTrace();
             }finally {
             }
        	 boo=true;
        }
		graphics1.dispose();
        System.out.println("ぴえん画像を保存しました！");
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
    public static void main (String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ready();
        input();
        detect();
        userInput();
		putImg();
        sc.close();
    }
}
