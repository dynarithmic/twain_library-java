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

import com.dynarithmic.twain.lowlevel.TW_FRAME;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class PagesOptions
{
    private CAP_SEGMENTED segmentedValue = defaultSegmentedValue;
    private int maxFrames = defaultMaxFrames;
    private ICAP_SUPPORTEDSIZES pageSize = defaultPageSize;
    private TW_FRAME frame = new TW_FRAME();

    public static final CAP_SEGMENTED defaultSegmentedValue = CAP_SEGMENTED.TWSG_DEFAULT;
    public static final int defaultMaxFrames = Integer.MIN_VALUE;
    public static final ICAP_SUPPORTEDSIZES defaultPageSize = ICAP_SUPPORTEDSIZES.TWSS_DEFAULT;
    public static final TW_FRAME defaultFrame = new TW_FRAME();

    public CAP_SEGMENTED getSegmentedValue()
    {
        return segmentedValue;
    }

    public int getMaxFrames()
    {
        return maxFrames;
    }

    public ICAP_SUPPORTEDSIZES getPageSize()
    {
        return pageSize;
    }

    public TW_FRAME getFrame()
    {
        return frame;
    }

    public PagesOptions setSegmentedValue(CAP_SEGMENTED segmentedValue)
    {
        this.segmentedValue = segmentedValue;
        return this;
    }

    public PagesOptions setMaxFrames(int maxFrames)
    {
        this.maxFrames = maxFrames;
        return this;
    }

    public PagesOptions setPageSize(ICAP_SUPPORTEDSIZES pageSize)
    {
        this.pageSize = pageSize;
        return this;
    }

    public PagesOptions setFrame(TW_FRAME frame)
    {
        this.frame = frame;
        return this;
    }

    static protected final int affectedCaps[] = {   CAPS.CAP_SEGMENTED,
                                                    CAPS.ICAP_FRAMES,
                                                    CAPS.ICAP_MAXFRAMES,
                                                    CAPS.ICAP_SUPPORTEDSIZES};


    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
