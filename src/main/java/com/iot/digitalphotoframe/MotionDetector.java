package com.iot.digitalphotoframe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MotionDetector extends ConcurrentCoapResource {

    public static int counter = 0;
    Random random = new Random();
    public static MotionDetectorState motionDetectorState;

    public MotionDetector(String name) {
        super(name);

        motionDetectorState = new MotionDetectorState();
        setObservable(true);
        setObserveType(CoAP.Type.CON);
        getAttributes().setObservable();
        Timer timer = new Timer();
        timer.schedule(new ContiniousTask(), 0, 3000);


    }

    private class ContiniousTask extends TimerTask {
        @Override
        public void run() {
            motionDetectorState.setExist(random.nextBoolean());
            changed();
        }
    }


    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(motionDetectorState);
            exchange.respond(jsonString);
        } catch (Exception ex) {

        }
    }
}
