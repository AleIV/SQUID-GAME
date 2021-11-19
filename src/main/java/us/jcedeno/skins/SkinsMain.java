package us.jcedeno.skins;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SkinsMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        var playersSkinsOptional = SkinToolApi.getAllVariant("civilian");

        if (playersSkinsOptional.isPresent()) {
            var playersSkins = playersSkinsOptional.get();
            playersSkins.forEach(System.out::println);

        } else {
            System.out.println("Empty lol.");
        }
        SkinToolApi.addSkinsToComputeQueue(UUID.fromString("776cc5cd-6db4-41bc-b2a2-dcb5346fac6d"));

    }

}
