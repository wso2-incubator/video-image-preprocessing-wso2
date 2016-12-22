import com.atul.JavaOpenCV.Imshow;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.opencv.core.Core.NATIVE_LIBRARY_NAME;
import static org.opencv.core.Core.rectangle;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.equalizeHist;


class  HunamDetection{
    //creating a CascadeClassifier variable to hold the humanDetection cascade
    private CascadeClassifier human_cascade;
    private final String USER_AGENT = "Mozilla/5.0";


    public HunamDetection(){
        //load cascade location
        human_cascade=new CascadeClassifier ("resources/cascades/hogcascade_pedestrians.xml");
        if(human_cascade.empty ()){
            //if the cascade is not correctly loaded
            System.err.println ("Error-- Cascade is not loaded");
            return;
        }else {
            System.out.println ("Cascade classifier loaded successfully");
        }
    }

    public Mat detect(Mat inputFrame , Double Frame_ID) throws Exception{
        // creating Mat variables to hold the input frames.
        //frameRgb will holed the rgb version of input frame
        Mat frameRgb = new Mat ();
        //frameGray will holed the  gray scaled version of input frame
        Mat frameGray = new Mat ();

        // configuration for connecting to the WSO2 Complex Event processor.
        String type = "application/x-www-form-urlencoded";
        //creating the http client
        HttpClient httpClient= HttpClientBuilder.create ().build ();
        //sending http post request to the CEP
        HttpPost request = new HttpPost("http://172.17.0.1:9763/endpoints/HumanDetection2");
        request.addHeader("content-type", type);

        //generate processing the date and time
        DateFormat df = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        //creating a variable to hold the detected human's location as a rectangle.
        MatOfRect human = new MatOfRect ();

        // copy input frame to variable
        inputFrame.copyTo (frameRgb);
        // convert the input frame from RGB color space to Gray color space and saving to frameGray
        cvtColor(frameRgb,frameGray,COLOR_BGR2GRAY);

        //equalize the histogram of frameGray
        equalizeHist (frameGray,frameGray);

        //detect humans using detectMultiscale method and save the detection to human variable
        // detectMultiScale(Mat frame,MatofRect detectedObjects,double scaleFactor,int minNeighbours,int flag,size minSize,size maxSize)
        human_cascade.detectMultiScale (frameGray,human,1.1,6,0,new Size (),new Size ());

        String dateNw = df.format (date);
        //No of humans in frame.
        int humanCount = human.toArray ().length;

        //creating a jasonString with frame id, time and humancount
        String jsonString = "{Date:'"+dateNw+"',FrameID:'"+Frame_ID+"',HumanCount:'"+humanCount+"'}";
        System.out.println (jsonString);
        // Pass date as a jason to the CEP
        StringEntity params = new StringEntity(jsonString);
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
         // Draw a rectangle around the detected object
        for (Rect rect:human.toArray ()){
            rectangle(frameRgb,new Point (rect.x,rect.y),new Point (rect.x+rect.width,rect.y+rect.height),new Scalar (255,0,0));

        }
        //return the rgb frame to display
        return frameRgb;
    }
}



public class Main {

    public static void main(String[] args) throws Exception {

        //Load OpenCV library
        System.loadLibrary (NATIVE_LIBRARY_NAME);
        Imshow imshow = new Imshow ("HumanDetection");
        HunamDetection hunamDetection=new HunamDetection ();

        Mat video_frame = new Mat ();
        //load video file use '0' to grab the frame from web cam
        VideoCapture videoCapture = new VideoCapture ("resources/videos/ped.mp4");

        if(videoCapture.isOpened ()){
            while (true){
                //read frames and assigned to video_image variable
                videoCapture.read (video_frame);
                //get the frame id
                Double frameID=videoCapture.get (1);

                Thread.sleep (100);
                if(!video_frame.empty ()){
                    video_frame= hunamDetection.detect (video_frame,frameID);

                    //display the processed frame
                    imshow.showImage (video_frame);
                }else {
                    System.out.println("End of the video");
                    break;
                }
            }
        }

        videoCapture.release ();

    }
}
