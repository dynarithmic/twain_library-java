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
package com.dynarithmic.twain.lowlevel;

import com.dynarithmic.twain.lowlevel.TwainConstants.DG;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.javatuples.Triplet;
import com.dynarithmic.twain.lowlevel.TwainConstants.DAT;
import com.dynarithmic.twain.lowlevel.TwainConstants.MSG;

public class TwainTriplet
{
    private static final TwainConstantMapper<DG> DGToStringMap =
            new TwainConstantMapper<>(TwainConstants.DG.class);
    private static final TwainConstantMapper<DAT> DATToStringMap =
            new TwainConstantMapper<>(TwainConstants.DAT.class);
    private static final TwainConstantMapper<MSG> MSGToStringMap =
            new TwainConstantMapper<>(TwainConstants.MSG.class);

    private static final LinkedHashMap<Triplet<Long, Integer, Integer>, TwainLowLevel> TripletMap =  new LinkedHashMap<>();
    static
    {
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_GET), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_GETCURRENT), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_GETDEFAULT), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_GETHELP), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_GETLABEL), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_GETLABELENUM), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_QUERYSUPPORT), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_RESET), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_SET), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CAPABILITY, TwainConstants.MSG.MSG_SETCONSTRAINT), new TW_CAPABILITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CUSTOMDSDATA, TwainConstants.MSG.MSG_GET), new TW_CUSTOMDSDATA());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_CUSTOMDSDATA, TwainConstants.MSG.MSG_SET), new TW_CUSTOMDSDATA());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_DEVICEEVENT, TwainConstants.MSG.MSG_GET), new TW_DEVICEEVENT());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_AUTOMATICCAPTUREDIRECTORY), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_CHANGEDIRECTORY), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_COPY), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_CREATEDIRECTORY), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_DELETE), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_FORMATMEDIA), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_GETCLOSE), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_GETFIRSTFILE), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_GETINFO), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_GETNEXTFILE), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_FILESYSTEM, TwainConstants.MSG.MSG_RENAME), new TW_FILESYSTEM());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_EVENT, TwainConstants.MSG.MSG_PROCESSEVENT), new TW_EVENT());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_METRICS, TwainConstants.MSG.MSG_GET), new TW_METRICS());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_PASSTHRU, TwainConstants.MSG.MSG_PASSTHRU), new TW_PASSTHRU());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_PENDINGXFERS, TwainConstants.MSG.MSG_ENDXFER), new TW_PENDINGXFERS());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_PENDINGXFERS, TwainConstants.MSG.MSG_GET), new TW_PENDINGXFERS());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_PENDINGXFERS, TwainConstants.MSG.MSG_RESET), new TW_PENDINGXFERS());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_PENDINGXFERS, TwainConstants.MSG.MSG_STOPFEEDER), new TW_PENDINGXFERS());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_SETUPFILEXFER, TwainConstants.MSG.MSG_GET), new TW_SETUPFILEXFER());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_SETUPFILEXFER, TwainConstants.MSG.MSG_GETDEFAULT), new TW_SETUPFILEXFER());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_SETUPFILEXFER, TwainConstants.MSG.MSG_RESET), new TW_SETUPFILEXFER());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_SETUPFILEXFER, TwainConstants.MSG.MSG_SET), new TW_SETUPFILEXFER());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_SETUPMEMXFER, TwainConstants.MSG.MSG_GET), new TW_SETUPMEMXFER());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_STATUS, TwainConstants.MSG.MSG_GET), new TW_STATUS());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_STATUSUTF8, TwainConstants.MSG.MSG_GET), new TW_STATUSUTF8());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_TWAINDIRECT, TwainConstants.MSG.MSG_SETTASK), new TW_TWAINDIRECT());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_USERINTERFACE, TwainConstants.MSG.MSG_DISABLEDS), new TW_USERINTERFACE());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_USERINTERFACE, TwainConstants.MSG.MSG_ENABLEDS), new TW_USERINTERFACE());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_USERINTERFACE, TwainConstants.MSG.MSG_ENABLEDSUIONLY), new TW_USERINTERFACE());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_XFERGROUP, TwainConstants.MSG.MSG_GET), new TW_UINT32());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_XFERGROUP, TwainConstants.MSG.MSG_SET), new TW_UINT32());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_IDENTITY, TwainConstants.MSG.MSG_CLOSEDS), new TW_IDENTITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_IDENTITY, TwainConstants.MSG.MSG_GETDEFAULT), new TW_IDENTITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_IDENTITY, TwainConstants.MSG.MSG_GETFIRST), new TW_IDENTITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_IDENTITY, TwainConstants.MSG.MSG_GETNEXT), new TW_IDENTITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_IDENTITY, TwainConstants.MSG.MSG_OPENDS), new TW_IDENTITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_IDENTITY, TwainConstants.MSG.MSG_SET), new TW_IDENTITY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_NULL, TwainConstants.MSG.MSG_CLOSEDSOK), new TW_NULL());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_NULL, TwainConstants.MSG.MSG_CLOSEDSREQ), new TW_NULL());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_NULL, TwainConstants.MSG.MSG_DEVICEEVENT), new TW_NULL());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_CONTROL, TwainConstants.DAT.DAT_NULL, TwainConstants.MSG.MSG_XFERREADY), new TW_NULL());

        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_CIECOLOR, TwainConstants.MSG.MSG_GET), new TW_CIECOLOR());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_EXTIMAGEINFO, TwainConstants.MSG.MSG_GET), new TW_EXTIMAGEINFO());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_GRAYRESPONSE, TwainConstants.MSG.MSG_RESET), new TW_RESPONSETYPE());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_GRAYRESPONSE, TwainConstants.MSG.MSG_SET), new TW_RESPONSETYPE());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_ICCPROFILE, TwainConstants.MSG.MSG_GET), new TW_MEMORY());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGEFILEXFER, TwainConstants.MSG.MSG_GET), new TW_NULL());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGEINFO, TwainConstants.MSG.MSG_GET), new TW_IMAGEINFO());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGELAYOUT, TwainConstants.MSG.MSG_GET), new TW_IMAGELAYOUT());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGELAYOUT, TwainConstants.MSG.MSG_GETDEFAULT), new TW_IMAGELAYOUT());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGELAYOUT, TwainConstants.MSG.MSG_RESET), new TW_IMAGELAYOUT());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGELAYOUT, TwainConstants.MSG.MSG_SET), new TW_IMAGELAYOUT());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGEMEMFILEXFER, TwainConstants.MSG.MSG_GET), new TW_IMAGEMEMXFER());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGEMEMXFER, TwainConstants.MSG.MSG_GET), new TW_IMAGEMEMXFER());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_IMAGENATIVEXFER, TwainConstants.MSG.MSG_GET), new TW_HANDLE());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_JPEGCOMPRESSION, TwainConstants.MSG.MSG_GET), new TW_JPEGCOMPRESSION());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_JPEGCOMPRESSION, TwainConstants.MSG.MSG_GETDEFAULT), new TW_JPEGCOMPRESSION());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_JPEGCOMPRESSION, TwainConstants.MSG.MSG_RESET), new TW_JPEGCOMPRESSION());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_JPEGCOMPRESSION, TwainConstants.MSG.MSG_SET), new TW_JPEGCOMPRESSION());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_PALETTE8, TwainConstants.MSG.MSG_GET), new TW_PALETTE8());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_PALETTE8, TwainConstants.MSG.MSG_GETDEFAULT), new TW_PALETTE8());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_PALETTE8, TwainConstants.MSG.MSG_RESET), new TW_PALETTE8());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_PALETTE8, TwainConstants.MSG.MSG_SET), new TW_PALETTE8());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_RGBRESPONSE, TwainConstants.MSG.MSG_RESET), new TW_RESPONSETYPE());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_IMAGE, TwainConstants.DAT.DAT_RGBRESPONSE, TwainConstants.MSG.MSG_SET), new TW_RESPONSETYPE());

        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_AUDIO, TwainConstants.DAT.DAT_AUDIOFILEXFER, TwainConstants.MSG.MSG_GET), new TW_NULL());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_AUDIO, TwainConstants.DAT.DAT_AUDIOINFO, TwainConstants.MSG.MSG_GET), new TW_AUDIOINFO());
        TripletMap.put(new Triplet<>((long) TwainConstants.DG.DG_AUDIO, TwainConstants.DAT.DAT_AUDIONATIVEXFER, TwainConstants.MSG.MSG_GET), new TW_HANDLE());
    }

    public static TwainLowLevel createObjectFromTriplet(long DG, int DAT, int MSG) throws ClassNotFoundException,
                                                                            NoSuchMethodException,
                                                                            SecurityException,
                                                                            InstantiationException,
                                                                            IllegalAccessException,
                                                                            IllegalArgumentException,
                                                                            InvocationTargetException
    {
        Triplet<Long, Integer, Integer> trip = new Triplet<>(DG, DAT, MSG);
        if (TripletMap.containsKey( trip ))
        {
            Class<?> c = Class.forName(TripletMap.get(trip).getClass().getName());
            Constructor<?> cons = c.getConstructor();
            return (TwainLowLevel)cons.newInstance();
        }
        return null;
    }

    public static Map<Triplet<Long, Integer, Integer>, TwainLowLevel> getTripletMap()
    {
        return TripletMap;
    }

    public static String getDGName(int val)
    {
        try
        {
            return DGToStringMap.toString(val);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            throw new IllegalArgumentException("" + val + " is not a valid or known TWAIN DG name");
        }
    }

    public static String getDATName(int val)
    {
        try
        {
            return DATToStringMap.toString(val);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            throw new IllegalArgumentException("" + val + " is not a valid or known TWAIN DAT name");
        }
    }

    public static String getMSGName(int val)
    {
        try
        {
            return MSGToStringMap.toString(val);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            throw new IllegalArgumentException("" + val + " is not a valid or known TWAIN MSG name");
        }
    }

    TW_IDENTITY originID = new TW_IDENTITY();
    TW_IDENTITY destinationID = new TW_IDENTITY();
    TW_UINT32  dg = new TW_UINT32();
    TW_UINT16  dat  = new TW_UINT16();
    TW_UINT16  msg = new TW_UINT16();
    TwainLowLevel tripletData = new TW_NULL();

    public TW_IDENTITY getOriginID()
    {
        return originID;
    }

    public TwainTriplet setOriginID(TW_IDENTITY originID)
    {
        this.originID = originID;
        return this;
    }

    public TW_IDENTITY getDestinationID()
    {
        return destinationID;
    }

    public TwainTriplet setDestinationID(TW_IDENTITY destinationID)
    {
        this.destinationID = destinationID;
        return this;
    }

    public TW_UINT32 getDG()
    {
        return dg;
    }

    public TwainTriplet setDG(TW_UINT32 dg)
    {
        this.dg = dg;
        return this;
    }

    public TwainTriplet setDG(long dG)
    {
        dg.setValue(dG);
        return this;
    }

    public TW_UINT16 getDAT()
    {
        return dat;
    }

    public TwainTriplet setDAT(TW_UINT16 datData)
    {
        dat = datData;
        return this;
    }

    public TwainTriplet setDAT(int datData)
    {
        dat.setValue(datData);
        return this;
    }

    public TW_UINT16 getMSG()
    {
        return msg;
    }

    public TwainTriplet setMSG(TW_UINT16 msgData)
    {
        msg = msgData;
        return this;
    }

    public TwainTriplet setMSG(int msgData)
    {
        msg.setValue(msgData);
        return this;
    }

    public TwainLowLevel getData()
    {
        return tripletData;
    }

    public TwainTriplet setData(TwainLowLevel data)
    {
        tripletData = data;
        return this;
    }

}
