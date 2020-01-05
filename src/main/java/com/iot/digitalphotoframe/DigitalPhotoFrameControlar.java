package com.iot.digitalphotoframe;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.io.InputStream;

@Controller

@RequestMapping("/iot/")
public class DigitalPhotoFrameControlar {


    @GetMapping("/home")
    public String home() {
        return "dpf";
    }

    @GetMapping("get-data")
    @ResponseBody
    public ResponseData getData() {
        return DigitalphotoframeApplication.responseData;
    }

}
