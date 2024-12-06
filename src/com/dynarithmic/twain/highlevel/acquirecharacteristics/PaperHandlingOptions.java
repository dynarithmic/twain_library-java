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

public class PaperHandlingOptions
{
    public enum FeederMode
    {
        FEEDER,
        FEEDER_FLATBED
    }

    public enum ManualDuplexMode
    {
        FACEUPTOPFEED,
        FACEUPBOTTOMFEED,
        FACEDOWNTOPFEED,
        FACEDOWNBOTTOMFEED,
        NONE
    }

    private boolean enableDuplexMode = false;
    private boolean enableAutoFeedMode = true;
    private boolean enableFeederPrepMode = false;
    private boolean enableFeederMode = false;
    private boolean enableAutoFeedNotify = false;
    private int feederWaitTime = defaultFeederWaitTime;
    private CAP_FEEDERALIGNMENT feederAlignment = defaultFeederAlignment;
    private ICAP_FEEDERTYPE feederType = defaultFeederType;
    private CAP_FEEDERORDER feederOrder = defaultFeederOrder;
    private CAP_PAPERHANDLING paperHandling = defaultPaperHandling;
    private List<CAP_FEEDERPOCKET> feederPockets = new ArrayList<>();
    private FeederMode feederMode = defaultFeederMode;
    private ManualDuplexMode manualDuplexMpde = defaultManualDuplexMode;

    public static final int waitInfinite = Integer.MIN_VALUE;
    public static final int noFeederWaitTime = 0;
    public static final int defaultFeederWaitTime = noFeederWaitTime;
    public static final CAP_FEEDERALIGNMENT defaultFeederAlignment = CAP_FEEDERALIGNMENT.TWFA_DEFAULT;
    public static final ICAP_FEEDERTYPE defaultFeederType = ICAP_FEEDERTYPE.TWFE_DEFAULT;
    public static final CAP_FEEDERORDER defaultFeederOrder = CAP_FEEDERORDER.TWFO_DEFAULT;
    public static final FeederMode defaultFeederMode = FeederMode.FEEDER;
    public static final ManualDuplexMode defaultManualDuplexMode = ManualDuplexMode.NONE;
    public static final CAP_PAPERHANDLING defaultPaperHandling = CAP_PAPERHANDLING.TWPH_DEFAULT;

    public boolean isDuplexEnabled()
    {
        return enableDuplexMode;
    }

    public PaperHandlingOptions enableDuplex(boolean enable)
    {
        this.enableDuplexMode = enable;
        return this;
    }

    public boolean isAutoFeedEnabled()
    {
        return enableAutoFeedMode;
    }

    public PaperHandlingOptions enableAutoFeed(boolean enable)
    {
        this.enableAutoFeedMode = enable;
        return this;
    }

    public PaperHandlingOptions enableAutoFeedNotify(boolean enable)
    {
        this.enableAutoFeedNotify = enable;
        return this;
    }

    public boolean isAutoFeedNotifyEnabled()
    {
        return this.enableAutoFeedNotify;
    }

    public boolean isFeederPrepEnabled()
    {
        return enableFeederPrepMode;
    }

    public PaperHandlingOptions enableFeederPrep(boolean enable)
    {
        this.enableFeederPrepMode = enable;
        return this;
    }

    public boolean isFeederEnabled()
    {
        return enableFeederMode;
    }

    public PaperHandlingOptions enableFeeder(boolean enable)
    {
        this.enableFeederMode = enable;
        return this;
    }

    public int getFeederWaitTime()
    {
        return feederWaitTime;
    }

    public PaperHandlingOptions setFeederWaitTime(int feederWaitTime)
    {
        this.feederWaitTime = feederWaitTime;
        return this;
    }

    public CAP_FEEDERALIGNMENT getFeederAlignment()
    {
        return feederAlignment;
    }

    public PaperHandlingOptions  setFeederAlignment(CAP_FEEDERALIGNMENT feederAlignment)
    {
        this.feederAlignment = feederAlignment;
        return this;
    }

    public ICAP_FEEDERTYPE getFeederType()
    {
        return feederType;
    }

    public PaperHandlingOptions  setFeederType(ICAP_FEEDERTYPE feederType)
    {
        this.feederType = feederType;
        return this;
    }
    public CAP_FEEDERORDER getFeederOrder()
    {
        return feederOrder;
    }

    public PaperHandlingOptions  setFeederOrder(CAP_FEEDERORDER feederOrder)
    {
        this.feederOrder = feederOrder;
        return this;
    }

    public List<CAP_FEEDERPOCKET> getFeederPockets()
    {
        return feederPockets;
    }

    public List<Integer> getFeederPocketsAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (CAP_FEEDERPOCKET p : feederPockets)
        {
            if ( p != CAP_FEEDERPOCKET.TWFP_DEFAULT)
                intList.add(p.ordinal());
        }
        return intList;
    }

    public PaperHandlingOptions setFeederPockets(List<CAP_FEEDERPOCKET> feederPockets)
    {
        this.feederPockets = feederPockets;
        return this;
    }

    public FeederMode getFeederMode()
    {
        return feederMode;
    }

    public PaperHandlingOptions  setFeederMode(FeederMode feederMode)
    {
        this.feederMode = feederMode;
        return this;
    }

    public ManualDuplexMode getManualDuplexMode()
    {
        return manualDuplexMpde;
    }

    public PaperHandlingOptions  setManualDuplexMode(ManualDuplexMode manualDuplexMpde)
    {
        this.manualDuplexMpde = manualDuplexMpde;
        return this;
    }

    public PaperHandlingOptions setPaperHandling(CAP_PAPERHANDLING paperHandling)
    {
        this.paperHandling = paperHandling;
        return this;
    }

    public CAP_PAPERHANDLING getPaperHandling()
    {
        return this.paperHandling;
    }

}
