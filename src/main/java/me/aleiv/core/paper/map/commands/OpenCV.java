package me.aleiv.core.paper.map.commands;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import us.jcedeno.libs.utils.EpicApi;

public class OpenCV {

    public static void main(String[] args) throws IOException, InterruptedException {
        nu.pattern.OpenCV.loadLocally();

        EpicApi.getPlayerSkin("jcedeno");

        Mat matrix = Imgcodecs
                .imread("~/Documents/repos/personal/squid-game-project/SQUID-GAME/debug/maps/cookie_creeper.png");

    }

    void createSkin(String fileLocation, String maskLocation, String outputLocation) {
        var skin = Imgcodecs.imread(fileLocation);

        var mask = Imgcodecs.imread(maskLocation);

        var output = new Mat();

        Core.bitwise_and(skin, skin, output, mask);

        Imgcodecs.imwrite(outputLocation, output);

    }

}
