package com.iot.digitalphotoframe;

import java.util.Date;

public class MotionDetectorState {

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    private Boolean isExist;

    private Date dateTOShow;
}
