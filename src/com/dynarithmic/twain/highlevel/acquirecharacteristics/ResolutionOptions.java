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

import com.dynarithmic.twain.lowlevel.TwainConstants;

public class ResolutionOptions
{
    private double xResolution = defaultXResolution;
    private double yResolution = defaultYResolution;

    public static final double defaultXResolution = Double.MAX_VALUE;
    public static final double defaultYResolution = Double.MAX_VALUE;

    public ResolutionOptions setXResolution(double xResolution)
    {
        this.xResolution = xResolution;
        return this;
    }

    public ResolutionOptions setYResolution(double yResolution)
    {
        this.yResolution = yResolution;
        return this;
    }

    public double getXResolution()
    {
        return this.xResolution;
    }

    public double getYResolution()
    {
        return this.yResolution;
    }

    static protected final int affectedCaps[] = { TwainConstants.CAPS.ICAP_XRESOLUTION,
                                                  TwainConstants.CAPS.ICAP_YRESOLUTION};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
