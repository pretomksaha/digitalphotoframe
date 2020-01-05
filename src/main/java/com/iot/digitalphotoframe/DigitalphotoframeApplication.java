package com.iot.digitalphotoframe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DigitalphotoframeApplication {
    @Autowired
    public static ResponseData responseData;

    public static void main(String[] args) {


        SpringApplication.run(DigitalphotoframeApplication.class, args);
        responseData = new ResponseData();

        CoapServer coapServer = new CoapServer(8086);
        coapServer.add(new MotionDetector("Picture"));
        coapServer.start();

        CoapServer faceDetectorServer = new CoapServer(8087);
        faceDetectorServer.add(new FaceDetectorSensor("face"));
        faceDetectorServer.start();

        CoapClient coapClient = new CoapClient("coap://localhost:8086/Picture");
        CoapClient faceDetectorClient = new CoapClient("coap://localhost:8087/face");

        CoapObserveRelation relation = coapClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                try {
                    String jsonString = coapResponse.getResponseText();
                    ObjectMapper mapper = new ObjectMapper();
                    MotionDetectorState sensorOneState = mapper.readValue(jsonString, MotionDetectorState.class);

                    responseData.setIsExist(sensorOneState.getExist() + "");

                    if(sensorOneState.getExist() == true) {
                        Thread thread = new Thread() {
                            public void run() {
                                String jsonRequest = "{data:get}";
                                try {
                                    CoapResponse response = faceDetectorClient.post(jsonRequest, MediaTypeRegistry.APPLICATION_JSON);
                                    String responseJson = response.getResponseText();
                                    FaceDetectorResponse faceDetectorResponse = mapper.readValue(responseJson, FaceDetectorResponse.class);

                                    responseData.setPerson(faceDetectorResponse.getPersonImage());

                                } catch (ConnectorException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    } else {
                        responseData.setPerson("");
                    }

                } catch (Exception ex) {

                }


                try {
                    System.out.println("data from coap = " + new ObjectMapper().writeValueAsString(responseData));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });

    }

}
