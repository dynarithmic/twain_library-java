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

import com.dynarithmic.twain.DTwainConstants.AcquireType;
import com.dynarithmic.twain.DTwainConstants.PixelType;
import com.dynarithmic.twain.DTwainConstants.SourceStateAfterAcquire;

public class GeneralOptions
{
    private AcquireType acquireType = defaultAcquireType;
    private int maxPageCount = defaultMaxPageCount;
    private int maxAcquisitions = defaultMaxAcquisitions;
    private SourceStateAfterAcquire sourceStateAfterAcquire = defaultSourceStateAfterAcquire;
    private PixelType pixelType = defaultPixelType;

    public static final AcquireType defaultAcquireType = AcquireType.NATIVEFILE;
    public static final int defaultMaxPageCount = -1;
    public static final int defaultMaxAcquisitions = Integer.MIN_VALUE;
    public static final PixelType defaultPixelType = PixelType.DEFAULT;
    public static final SourceStateAfterAcquire defaultSourceStateAfterAcquire = SourceStateAfterAcquire.OPENED;

    public AcquireType getAcquireType()
    {
        return acquireType;
    }

    public int getMaxPageCount()
    {
        return maxPageCount;
    }

    public int getMaxAcquisitions()
    {
        return maxAcquisitions;
    }

    public SourceStateAfterAcquire getSourceStateAfterAcquire()
    {
        return sourceStateAfterAcquire;
    }

    public GeneralOptions setPixelType(PixelType pixelType)
    {
        this.pixelType = pixelType;
        return this;
    }

    public PixelType getPixelType()
    {
        return pixelType;
    }

    public GeneralOptions setAcquireType(AcquireType acquireType)
    {
        this.acquireType = acquireType;
        return this;
    }

    public GeneralOptions setMaxPageCount(int maxPageCount)
    {
        this.maxPageCount = maxPageCount;
        return this;
    }

    public GeneralOptions setMaxAcquisitions(int maxAcquisitions)
    {
        this.maxAcquisitions = maxAcquisitions;
        return this;
    }

    public GeneralOptions setSourceStateAfterAcquire(SourceStateAfterAcquire sourceStateAfterAcquire)
    {
        this.sourceStateAfterAcquire = sourceStateAfterAcquire;
        return this;
    }


}
