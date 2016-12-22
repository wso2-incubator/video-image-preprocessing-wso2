import com.atul.JavaOpenCV.Imshow;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.Date;

import static org.opencv.core.Core.rectangle;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.resize;


public class PlaneDetector {

    //Creating Cascade classifier to load cascade
    private CascadeClassifier plane_cascade;
    private int detectedCounter =0;
    private int departureCounter=0;
    private int docked = 0;




    public PlaneDetector(){
        //Load cascade classifier
        plane_cascade = new CascadeClassifier ("resources/cascade/plane_50x35.xml");

        if(plane_cascade.empty ()){
            System.out.println("Cascade is not loaded");
        }else {
            System.out.println("Cascade  loading successful");
        }

    }

    public Mat detect(Mat inputFrame,Double frameID) throws Exception{
        //http connection setup
        String type ="application/x-www-from-urlencoded";
        HttpClient httpClient= HttpClientBuilder.create().build();
        HttpPost request= new HttpPost ("http://localhost:9763/endpoints/PlaneRecevier2");
        request.addHeader ("content-type",type);

        Mat mRgba = new Mat ();
        Mat nGray = new Mat();
        Date date= new Date ();
        String planeState=null;

        MatOfRect plane = new MatOfRect ();
        int rectangleCount=0;
        Double rectangleArea=0.0;

        inputFrame.copyTo (mRgba);
        //resize input video if necessary
//        resize (mRgba,mRgba,new Size (720,460),1,1,INTER_NEAREST);

        //convert input RGB frame to Gray scale
        cvtColor (mRgba,nGray,Imgproc.COLOR_BGR2GRAY);
        //Detect planes in frame using detectMultiScale(Mat frame,MatofRect detectedObjects,double scaleFactor,int minNeighbours,int flag,size minSize,size maxSize)
        plane_cascade.detectMultiScale (nGray,plane,1.05,9,0,new Size (240,170),new Size ());


        //draw the rectangle around the detected
//        if(plane.toArray ().length>0) {
        for (Rect rect : plane.toArray ()) {
//            ++detectedCounter;
            rectangleCount=plane.toArray ().length;
            System.out.println("Number of rect - "+plane.toArray ().length+"Area of rectangle - "+rect.area ()+"  Width of rectangle - "+rect.width+"  Hight of rectangle - "+rect.height);
             rectangleArea=rect.area ();
            departureCounter = 0;
            int temp = plane.toArray ().length;
            rectangle (mRgba, new Point (rect.x, rect.y), new Point (rect.x + rect.width, rect.y + rect.height), new Scalar (255, 0, 0));
//            putText (mRgba, Integer.toString (temp), new Point (rect.x, rect.y), 1, 1.0, new Scalar (0, 0, 255));

        }
        //Detect plane state (Arriving)
        if (detectedCounter==0 && rectangleCount==1 && rectangleArea > 60000 ){
            System.out.println ("Arriving");
            planeState="Arriving" ;
            docked = 0;
            detectedCounter=0;

        }

        if (docked < 15 && (rectangleCount>1 | (rectangleArea >40000 && rectangleArea <60000) )){
            docked++;
            System.out.println ("Arriving");
            planeState = "Arriving" ;
        }
        //Detect plane state(Docked)
        if (docked >15| docked==15){
           System.out.println ("Docked");
            planeState = "Docked";
            detectedCounter=1;

        }

//        System.out.println (docked);
//        }
//        System.out.println ("Departure Counter - "+departureCounter+"  Detected Counter - "+detectedCounter);
        //Arrival time
        String time = date.toString ();
        String jsoneString= "{Time:\'"+time+"\',FrameID:\'"+frameID+"\',PlaneDetected:\'"+!plane.empty ()+"\',PlaneState:\'"+planeState+"\'}";
        StringEntity params = new StringEntity (jsoneString);
//        request.setEntity (params);
//        System.out.println (response);
        System.out.println (jsoneString);


//        if(detectedCounter==1){
//            System.out.println("---------------------------------------Detected Time"+date.toString ());
//
//        }
//        if(plane.toArray ().length==0){
//            departureCounter++;
//        }
//        //Departure time
//        if(departureCounter==20 && detectedCounter>0){
//            System.out.println("---------------------------------------Departure time"+ date.toString ());
//            detectedCounter=0;
//        }

        return mRgba;
    }




    public static void main (String[] args) throws Exception{
        //Load OpenCV library
        System.loadLibrary (Core.NATIVE_LIBRARY_NAME);
        Imshow imshow = new Imshow ("Plane Detection");
        PlaneDetector planeDetector = new PlaneDetector ();
        Mat video_image = new Mat ();
        //Load video file
        VideoCapture videoCapture= new VideoCapture ("resources/videos/D1.mp4");

        if (videoCapture.isOpened ()){
            while (true){
                //read frames and assigned to video_image variable
                videoCapture.read (video_image);
//                System.out.println(videoCapture.get (1));
                Double frameID = videoCapture.get (1);

                Thread.sleep (100);
                if (!video_image.empty ()){
                    video_image = planeDetector.detect (video_image,frameID);
                    //display the video, frame by frame
                    imshow.showImage (video_image);
                }else {
                    System.out.println("End of the video");
                }
            }
        }
        //close video capture
        videoCapture.release ();

    }
}
