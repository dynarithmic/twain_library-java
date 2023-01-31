package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import java.util.ArrayList;
import java.util.List;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class DoublefeedOptions
{
    private boolean enabled = false;
    private CAP_DOUBLEFEEDDETECTION detectionType = defaultDetectionType;
    private double detectionLength = defaultDetectionLength;
    private CAP_DOUBLEFEEDDETECTIONSENSITIVITY detectionSensitivity = defaultSensitivity;
    private List<CAP_DOUBLEFEEDDETECTIONRESPONSE> detectionResponses = new ArrayList<>();

    public static final double defaultDetectionLength = Double.MAX_VALUE;
    public static final CAP_DOUBLEFEEDDETECTIONSENSITIVITY defaultSensitivity = CAP_DOUBLEFEEDDETECTIONSENSITIVITY.TWUS_DEFAULT;
    public static final CAP_DOUBLEFEEDDETECTION defaultDetectionType = CAP_DOUBLEFEEDDETECTION.TWDF_DEFAULT;

    ////////////////////////// CAP_DOUBLEFEEDDETECTION /////////////////////////////////////
    public DoublefeedOptions enable(boolean bSet)
    {
        this.enabled = bSet;
        return this;
    }

    public DoublefeedOptions setType(CAP_DOUBLEFEEDDETECTION detectionType)
    {
        this.detectionType = detectionType;
        return this;
    }

    public DoublefeedOptions setLength(double detectionLength)
    {
        this.detectionLength = detectionLength;
        return this;
    }

    public DoublefeedOptions setSensitivity(CAP_DOUBLEFEEDDETECTIONSENSITIVITY sensitivity)
    {
        this.detectionSensitivity = sensitivity;
        return this;
    }

    public DoublefeedOptions setResponses(List<CAP_DOUBLEFEEDDETECTIONRESPONSE> responseList)
    {
        this.detectionResponses = responseList;
        return this;
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public CAP_DOUBLEFEEDDETECTION getType()
    {
        return this.detectionType;
    }

    public double getLength()
    {
        return this.detectionLength;
    }

    public CAP_DOUBLEFEEDDETECTIONSENSITIVITY getSensitivity()
    {
        return this.detectionSensitivity;
    }

    public List<CAP_DOUBLEFEEDDETECTIONRESPONSE> getResponses()
    {
        return this.detectionResponses;
    }

    public List<Integer> getResponsesAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (CAP_DOUBLEFEEDDETECTIONRESPONSE response : detectionResponses )
        {
            if (response != CAP_DOUBLEFEEDDETECTIONRESPONSE.TWDP_DEFAULT)
                intList.add(response.ordinal());
        }
        return intList;
    }

    static protected final int affectedCaps[] = { CAPS.CAP_DOUBLEFEEDDETECTIONLENGTH,
                                                  CAPS.CAP_DOUBLEFEEDDETECTIONSENSITIVITY,
                                                  CAPS.CAP_DOUBLEFEEDDETECTIONRESPONSE,
                                                  CAPS.CAP_DOUBLEFEEDDETECTION};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
