package com.iot.digitalphotoframe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.Random;
import java.util.Timer;

public class FaceDetectorSensor extends ConcurrentCoapResource {

    public static int counter = 0;
    Random random = new Random();
    public static String[] persons = {"pretom", "nahin", "limon", "chandrima", "akib", "akifa", "sehrish", "rony"};

    public FaceDetectorSensor(String name) {
        super(name);
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(persons);
            exchange.respond(jsonString);
        } catch (Exception ex) {

        }
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        exchange.accept();

            try {
                ObjectMapper mapper = new ObjectMapper();

                Random randomGenerator = new Random();
                int randomInt = randomGenerator.nextInt(8);
                String person = persons[randomInt];

                FaceDetectorResponse faceDetectorResponse = new FaceDetectorResponse();
                faceDetectorResponse.setPersonImage(person);
                String jsonString = mapper.writeValueAsString(faceDetectorResponse);

                exchange.respond(CoAP.ResponseCode.CREATED, jsonString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

}
