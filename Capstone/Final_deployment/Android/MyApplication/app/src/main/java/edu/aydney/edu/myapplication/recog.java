package edu.aydney.edu.myapplication;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.*;

public class recog {
    /**
     * @return
     * @Description
     * @Param uri for image path
     * @Author Hurui YANG
     * @Date 2020.09.05 9:43
     **/
    public static HashMap<String, String> startRecog(String uri) {
        return choose_image(uri);
    }

    private static HashMap<String, String> choose_image(String uri) {
        Mat input_image = imread(uri);
//        System.load("C:\\Users\\26336\\Desktop\\comp3888_t15_06_group04\\MyApplication\\app\\src\\main\\java\\edu\\aydney\\edu\\myapplication\\opencv_java340-x64.dll");
        resize(input_image, input_image, new Size(512, 512), 0, 0, INTER_CUBIC);
        List<MatOfPoint> contours = imageRead(imread(uri));
        return wholeSyting(input_image, contours);
    }

    private static List<MatOfPoint> imageRead(Mat image) {

        Mat input = preprocessImage(image);
        List<MatOfPoint> contours = new ArrayList<>();

        findContours(input, contours, new Mat(), RETR_TREE, CHAIN_APPROX_NONE);
        return contours;
    }

    private static Mat preprocessImage(Mat image) {
        Mat imgReading = new Mat();
        Imgproc.cvtColor(image, imgReading, COLOR_BGR2GRAY);
        resize(imgReading, imgReading, new Size(512, 512), 0, 0, INTER_CUBIC);
        GaussianBlur(imgReading, imgReading, new Size(3, 3), 0);
        Canny(imgReading, imgReading, 40, 120);
        return imgReading;
    }

    private static String coloRecognition(Mat image) {
        String medicine = "Advil";
        Mat img = new Mat();
        cvtColor(image, img, COLOR_BGR2HSV);

//      red
        Mat redImg = new Mat();
        Scalar red_min = new Scalar(0, 128, 10);
        Scalar red_max = new Scalar(30, 255, 255);

        Core.inRange(img, red_min, red_max, redImg);
        Core.bitwise_and(img, img, redImg);
        Imgproc.cvtColor(redImg, redImg, COLOR_HSV2BGR);
        Imgproc.cvtColor(redImg, redImg, COLOR_BGR2GRAY);
        List<MatOfPoint> contour_red = new ArrayList<>();
        Mat hisrarchy = new Mat();
        findContours(redImg, contour_red, hisrarchy, RETR_TREE, CHAIN_APPROX_NONE);
        int count1 = 0;
        for (MatOfPoint matOfPoint : contour_red) {
            Rect ret = boundingRect(matOfPoint);
            if (ret.width > 1 & ret.height > 1) {
                count1++;
            }
        }
        if (count1 > 1) {

            medicine = "Panadol";
        }

//      blue
        Scalar blue_min = new Scalar(60, 128, 46);
        Scalar blue_max = new Scalar(124, 255, 255);
        Mat blueImg = new Mat();
        Core.inRange(img, blue_min, blue_max, blueImg);
        Core.bitwise_and(img, img, blueImg);
        Imgproc.cvtColor(blueImg, blueImg, COLOR_HSV2BGR);
        Imgproc.cvtColor(blueImg, blueImg, COLOR_BGR2GRAY);
        List<MatOfPoint> contour_blue = new ArrayList<>();

        findContours(blueImg, contour_blue, hisrarchy, RETR_TREE, CHAIN_APPROX_NONE);
        int count = 0;
        for (MatOfPoint matOfPoint : contour_blue) {
            Rect ret = boundingRect(matOfPoint);
            if (ret.width > 1 & ret.height > 1) {
                count++;
            }
        }
        if (count > 1) {

            medicine = "Demazin";
        }
        return medicine;
    }

    private static double volumeByEdge(double height, double width) {
        double whole_syringe_size_mm = 35;
        double mm_per_ml = 2.5;
        double tip_mm = 7;
        double plug_mm = 85;
        double mm_per_pixel = whole_syringe_size_mm / width;
        double whole_length = height * mm_per_pixel;
        double medicine_length = whole_length - plug_mm - tip_mm;
        return medicine_length * (1 / mm_per_ml);
    }

    private static HashMap<String, String> wholeSyting(Mat inputImg, List<MatOfPoint> contours) {
//        x_start, y_start, w_s, h_s = cv2.boundingRect(contours[0])
        Rect rect = boundingRect(contours.get(0));
        int x_end = rect.x + rect.width;
        int y_end = rect.y + rect.height;
        int y_start = rect.y;
        int x_start = rect.x;
        for (MatOfPoint mate :
                contours) {
            Rect tmp = boundingRect(mate);

            if (tmp.y < y_start) {
                y_start = tmp.y;
            }
            if (tmp.x < x_start) {
                x_start = tmp.x;
            }
            if (tmp.x + tmp.width > x_end) {
                x_end = tmp.x + tmp.width;
            }
            if (tmp.y + tmp.height > y_end) {
                y_end = tmp.y + tmp.height;
            }

        }
        double whole_height = abs(y_end - y_start);
        double whole_wide = abs(x_end - x_start);
        String volume = String.valueOf(volumeByEdge(whole_height, whole_wide));
        String medicine = coloRecognition(inputImg);
        HashMap<String, String> result = new HashMap<>();
        result.put("medicine", medicine);
        result.put("volume", volume);
        return result;
    }
}