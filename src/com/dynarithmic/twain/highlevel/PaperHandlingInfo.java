/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

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
package com.dynarithmic.twain.highlevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.GetCapOperation;
import com.dynarithmic.twain.highlevel.capabilityinterface.CapabilityInterface.SetCapOperation;
import com.dynarithmic.twain.lowlevel.TwainConstants;

public class PaperHandlingInfo
{
    boolean autoFeed = false;
    boolean clearPage = false;
    boolean duplexEnabled = false;
    TwainConstants.CAP_DUPLEX duplex = TwainConstants.CAP_DUPLEX.TWDX_NONE;
    List<Integer> feederAlignment = new ArrayList<>();
    boolean feederEnabled = false;
    List<Integer> feederOrder = new ArrayList<>();
    boolean feederLoaded = false;
    List<Integer> feederPocket = new ArrayList<>();
    boolean feederPrep = false;
    boolean feedPage = false;
    boolean paperDetectable = false;
    List<Integer> paperHandling = new ArrayList<>();
    boolean reacquireAllowed = false;
    boolean rewindPage = false;
    boolean feederSupported = false;
    List<Integer> feederType = new ArrayList<>();
    public static final int FEEDERAVAILABLE = 1;
    public static final int FLATBEDAVAILABLE = 2;
    private int pageFeederMechanisms = 0;

    public int getPageFeederMechanisms()
    {
        return this.pageFeederMechanisms;
    }

    void Reset()
    {
        autoFeed = false;
        duplex = TwainConstants.CAP_DUPLEX.TWDX_NONE;
        clearPage = false;
        duplexEnabled = false;
        feederAlignment = new ArrayList<>();
        feederEnabled = false;
        feederOrder = new ArrayList<>();
        feederLoaded = false;
        feederPocket = new ArrayList<>();
        feederPrep = false;
        feedPage = false;
        paperDetectable = false;
        paperHandling = new ArrayList<>();
        reacquireAllowed = false;
        rewindPage = false;
        feederSupported = false;
        pageFeederMechanisms = 0;
    }

    public boolean isFeederSupported()
    {
        return this.feederSupported;
    }

    public boolean isFeederLoaded(TwainSource ts) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = ts.getCapabilityInterface();
        List<Boolean> vals = ci.getFeederLoaded(ci.get());
        if ( vals.isEmpty())
            return false;
        return vals.get(0);
    }

    public boolean getInfo(TwainSource ts) throws DTwainJavaAPIException
    {
        CapabilityInterface ci = ts.getCapabilityInterface();
        GetCapOperation op = ci.get();
        List<Boolean> vals;
        pageFeederMechanisms = 0;

        vals = ci.getAutoFeed(op);
        if ( !vals.isEmpty() )
            this.autoFeed = vals.get(0).booleanValue();

        vals = ci.getClearPage(op);
        if ( !vals.isEmpty() )
            this.clearPage = vals.get(0).booleanValue();

        vals = ci.getDuplexEnabled(op);
        if ( !vals.isEmpty() )
            this.duplexEnabled = vals.get(0).booleanValue();

        this.feederAlignment = ci.getFeederAlignment(op);

        vals = ci.getFeederEnabled(op);
        if ( !vals.isEmpty() )
            this.feederEnabled = vals.get(0).booleanValue();

        this.feederOrder = ci.getFeederOrder(op);

        vals = ci.getFeederLoaded(op);
        if ( !vals.isEmpty() )
            this.feederLoaded = vals.get(0).booleanValue();

        this.feederPocket = ci.getFeederPocket(op);

        vals = ci.getFeederPrep(op);
        if ( !vals.isEmpty() )
            this.feederPrep = vals.get(0).booleanValue();

        vals = ci.getFeedPage(op);
        if ( !vals.isEmpty() )
            this.feedPage = vals.get(0).booleanValue();

        vals = ci.getPaperDetectable(op);
        if ( !vals.isEmpty() )
            this.paperDetectable = vals.get(0).booleanValue();

        this.paperHandling = ci.getPaperHandling(op);

        vals = ci.getReacquireAllowed(op);
        if ( !vals.isEmpty() )
            this.reacquireAllowed = vals.get(0).booleanValue();

        vals = ci.getRewindPage(op);
        if ( !vals.isEmpty() )
            this.rewindPage = vals.get(0).booleanValue();

        this.feederType = ci.getFeederType(op);

        if ( ci.isDuplexSupported())
            this.duplex = TwainConstants.CAP_DUPLEX.values()[ci.getDuplex(op).get(0)];
        if ( !ci.isFeederEnabledSupported())
            this.feederSupported = false;
        else
        {
            List<Boolean> tempVal = ci.getFeederEnabled(ci.getCurrent());
            if ( !tempVal.isEmpty())
            {
                if ( tempVal.get(0) )
                    this.feederSupported = true;
                else
                {
                    ci.setFeederEnabled(Arrays.asList(true), ci.set());
                    if ( ci.getLastError() == DTwainConstants.ErrorCode.ERROR_NONE.ordinal())
                    {
                        ci.setFeederEnabled(Arrays.asList(tempVal.get(0)), ci.set());
                        this.feederSupported = true;
                    }
                    else
                        this.feederSupported = false;
                }
            }
        }

        pageFeederMechanisms += feederSupported?FEEDERAVAILABLE:0;
        if ( feederSupported )
        {
            List<Boolean> tempVal = ci.getFeederEnabled(ci.getCurrent());
            SetCapOperation co = ci.set();
            ci.setFeederEnabled(Arrays.asList(false), co);
            if ( ci.getLastCapError().getErrorCode() == DTwainConstants.ErrorCode.ERROR_NONE.value())
                this.pageFeederMechanisms += PaperHandlingInfo.FLATBEDAVAILABLE;
            ci.setFeederEnabled(tempVal, co);
        }
        return true;
    }

    public boolean isFeederOnly()
    {
        return this.pageFeederMechanisms == FEEDERAVAILABLE;
    }
}
