/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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

public class ColorOptions
{
    private boolean enabled = false;
    private double gamma = defaultGamma;
    private ICAP_ICCPROFILE iccProfile = defaultICCProfile;
    private List<ICAP_FILTER> filterValues = new ArrayList<>();
    private ICAP_PLANARCHUNKY planarChunkyValue = defaultPlanarChunkyValue;

    public static final double defaultGamma = Double.MIN_VALUE;
    public static final ICAP_ICCPROFILE defaultICCProfile = ICAP_ICCPROFILE.TWIC_DEFAULT;
    public static final ICAP_PLANARCHUNKY defaultPlanarChunkyValue = ICAP_PLANARCHUNKY.TWPC_DEFAULT;

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public double getGamma()
    {
        return gamma;
    }

    public ICAP_ICCPROFILE getICCProfile()
    {
        return iccProfile;
    }

    public List<ICAP_FILTER> getFilter()
    {
        return filterValues;
    }

    public ICAP_PLANARCHUNKY getPlanarChunky()
    {
        return planarChunkyValue;
    }

    public ColorOptions enableColorManagement(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public ColorOptions setGamma(double gamma)
    {
        this.gamma = gamma;
        return this;
    }

    public ColorOptions setICCProfile(ICAP_ICCPROFILE iccProfile)
    {
        this.iccProfile = iccProfile;
        return this;
    }

    public ColorOptions setFilter(List<ICAP_FILTER> filterValues)
    {
        this.filterValues = filterValues;
        return this;
    }

    public ColorOptions setPlanarChunky(ICAP_PLANARCHUNKY planarChunkyValue)
    {
        this.planarChunkyValue = planarChunkyValue;
        return this;
    }

    static protected final int affectedCaps[] = {
            CAPS.ICAP_COLORMANAGEMENTENABLED,
            CAPS.ICAP_FILTER,
            CAPS.ICAP_GAMMA,
            CAPS.ICAP_ICCPROFILE,
            CAPS.ICAP_PLANARCHUNKY};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }

}
