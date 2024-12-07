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

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class DeviceParamsOptions
{
    private double exposureTime = defaultExposureTime;
    private ICAP_FLASHUSED2 flashUsed = defaultFlashUsed;
    private ICAP_IMAGEFILTER imageFilter = defaultImageFilter;
    private ICAP_LIGHTPATH lightPath = defaultLightPath;
    private ICAP_FILMTYPE filmType = defaultFilmType;
    private ICAP_LIGHTSOURCE lightSource = defaultLightSource;
    private ICAP_NOISEFILTER noiseFilter = defaultNoiseFilter;
    private ICAP_OVERSCAN overscan = defaultOverscan;
    private ICAP_UNITS unitValue = defaultUnit;
    private int zoomFactor = defaultZoomFactor;

    public static final double defaultExposureTime = Double.MAX_VALUE;
    public static final ICAP_FLASHUSED2 defaultFlashUsed = ICAP_FLASHUSED2.TWFL_DEFAULT;
    public static final ICAP_IMAGEFILTER defaultImageFilter = ICAP_IMAGEFILTER.TWIF_NONE;
    public static final ICAP_LIGHTPATH  defaultLightPath = ICAP_LIGHTPATH.TWLP_REFLECTIVE;
    public static final ICAP_FILMTYPE   defaultFilmType = ICAP_FILMTYPE.TWFM_POSITIVE;
    public static final ICAP_LIGHTSOURCE defaultLightSource = ICAP_LIGHTSOURCE.TWLS_DEFAULT;
    public static final ICAP_NOISEFILTER defaultNoiseFilter = ICAP_NOISEFILTER.TWNF_DEFAULT;
    public static final ICAP_OVERSCAN defaultOverscan = ICAP_OVERSCAN.TWOV_NONE;
    public static final ICAP_UNITS defaultUnit = ICAP_UNITS.TWUN_INCHES;
    public static final int defaultZoomFactor = 0;

    public DeviceParamsOptions setExposureTime(double exposureTime)
    {
        this.exposureTime = exposureTime;
        return this;
    }

    public DeviceParamsOptions setFlashUsed(ICAP_FLASHUSED2 flashused)
    {
        this.flashUsed = flashused;
        return this;
    }

    public DeviceParamsOptions setImageFilter(ICAP_IMAGEFILTER imageFilter)
    {
        this.imageFilter = imageFilter;
        return this;
    }

    public DeviceParamsOptions setLightPath(ICAP_LIGHTPATH lightPath)
    {
        this.lightPath = lightPath;
        return this;
    }

    public DeviceParamsOptions setFilmType(ICAP_FILMTYPE filmType)
    {
        this.filmType = filmType;
        return this;
    }

    public DeviceParamsOptions setLightSource(ICAP_LIGHTSOURCE lightSource)
    {
        this.lightSource = lightSource;
        return this;
    }

    public DeviceParamsOptions setNoiseFilter(ICAP_NOISEFILTER noiseFilter)
    {
        this.noiseFilter = noiseFilter;
        return this;
    }

    public DeviceParamsOptions setOverscan(ICAP_OVERSCAN overscan)
    {
        this.overscan = overscan;
        return this;
    }

    public DeviceParamsOptions setUnit(ICAP_UNITS unit)
    {
        this.unitValue = unit;
        return this;
    }

    public DeviceParamsOptions setZoomFactor(int zoomFactor)
    {
        this.zoomFactor = zoomFactor;
        return this;
    }

    public double getExposureTime()
    {
        return this.exposureTime;
    }

    public ICAP_FLASHUSED2 getFlashUsed()
    {
        return this.flashUsed;
    }

    public ICAP_IMAGEFILTER getImageFilter()
    {
        return this.imageFilter;
    }

    public ICAP_LIGHTPATH getLightPath()
    {
        return this.lightPath;
    }

    public ICAP_FILMTYPE getFilmType()
    {
        return this.filmType;
    }

    public ICAP_LIGHTSOURCE getLightSource()
    {
        return this.lightSource;
    }

    public ICAP_NOISEFILTER getNoiseFilter()
    {
        return this.noiseFilter;
    }

    public ICAP_OVERSCAN getOverscan()
    {
        return this.overscan;
    }

    public ICAP_UNITS getUnit()
    {
        return this.unitValue;
    }

    public int getZoomFactor()
    {
        return this.zoomFactor;
    }

    static protected final int affectedCaps[] = {
                    CAPS.ICAP_EXPOSURETIME,
                    CAPS.ICAP_FLASHUSED2,
                    CAPS.ICAP_IMAGEFILTER,
                    CAPS.ICAP_LIGHTPATH,
                    CAPS.ICAP_FILMTYPE,
                    CAPS.ICAP_LIGHTSOURCE,
                    CAPS.ICAP_NOISEFILTER,
                    CAPS.ICAP_OVERSCAN,
                    CAPS.ICAP_UNITS,
                    CAPS.ICAP_ZOOMFACTOR};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
