/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
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
