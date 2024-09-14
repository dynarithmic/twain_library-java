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

public class TW_DEVICEEVENT extends TwainLowLevel
{
    private TW_UINT32  Event = new TW_UINT32();
    private TW_STR255  DeviceName = new TW_STR255();
    private TW_UINT32  BatteryMinutes = new TW_UINT32();
    private TW_INT16   BatteryPercentage = new TW_INT16();
    private TW_INT32   PowerSupply = new TW_INT32();
    private TW_FIX32   XResolution = new TW_FIX32();
    private TW_FIX32   YResolution = new TW_FIX32();
    private TW_UINT32  FlashUsed2 = new TW_UINT32();
    private TW_UINT32  AutomaticCapture = new TW_UINT32();
    private TW_UINT32  TimeBeforeFirstCapture = new TW_UINT32();
    private TW_UINT32  TimeBetweenCaptures = new TW_UINT32();

    public TW_DEVICEEVENT() {}

    public TW_UINT32 getEvent()
    {
        return Event;
    }
    public TW_STR255 getDeviceName()
    {
        return DeviceName;
    }
    public TW_UINT32 getBatteryMinutes()
    {
        return BatteryMinutes;
    }
    public TW_INT16 getBatteryPercentage()
    {
        return BatteryPercentage;
    }
    public TW_INT32 getPowerSupply()
    {
        return PowerSupply;
    }
    public TW_FIX32 getXResolution()
    {
        return XResolution;
    }
    public TW_FIX32 getYResolution()
    {
        return YResolution;
    }
    public TW_UINT32 getFlashUsed2()
    {
        return FlashUsed2;
    }
    public TW_UINT32 getAutomaticCapture()
    {
        return AutomaticCapture;
    }
    public TW_UINT32 getTimeBeforeFirstCapture()
    {
        return TimeBeforeFirstCapture;
    }
    public TW_UINT32 getTimeBetweenCaptures()
    {
        return TimeBetweenCaptures;
    }
    public TW_DEVICEEVENT setEvent(TW_UINT32 event)
    {
        Event = event;
        return this;
    }
    public TW_DEVICEEVENT setDeviceName(TW_STR255 deviceName)
    {
        DeviceName = deviceName;
        return this;
    }
    public TW_DEVICEEVENT setBatteryMinutes(TW_UINT32 batteryMinutes)
    {
        BatteryMinutes = batteryMinutes;
        return this;
    }
    public TW_DEVICEEVENT setBatteryPercentage(TW_INT16 batteryPercentage)
    {
        BatteryPercentage = batteryPercentage;
        return this;
    }
    public TW_DEVICEEVENT setPowerSupply(TW_INT32 powerSupply)
    {
        PowerSupply = powerSupply;
        return this;
    }
    public TW_DEVICEEVENT setXResolution(TW_FIX32 xResolution)
    {
        XResolution = xResolution;
        return this;
    }
    public TW_DEVICEEVENT setYResolution(TW_FIX32 yResolution)
    {
        YResolution = yResolution;
        return this;
    }
    public TW_DEVICEEVENT setFlashUsed2(TW_UINT32 flashUsed2)
    {
        FlashUsed2 = flashUsed2;
        return this;
    }
    public TW_DEVICEEVENT setAutomaticCapture(TW_UINT32 automaticCapture)
    {
        AutomaticCapture = automaticCapture;
        return this;
    }
    public TW_DEVICEEVENT setTimeBeforeFirstCapture(TW_UINT32 timeBeforeFirstCapture)
    {
        TimeBeforeFirstCapture = timeBeforeFirstCapture;
        return this;
    }
    public TW_DEVICEEVENT setTimeBetweenCaptures(TW_UINT32 timeBetweenCaptures)
    {
        TimeBetweenCaptures = timeBetweenCaptures;
        return this;
    }

    public TW_DEVICEEVENT setEvent(long event)
    {
        Event.setValue(event);
        return this;
    }

    public TW_DEVICEEVENT setDeviceName(String deviceName)
    {
        DeviceName.setValue(deviceName);
        return this;
    }

    public TW_DEVICEEVENT setBatteryMinutes(long batteryMinutes)
    {
        BatteryMinutes.setValue(batteryMinutes);
        return this;
    }

    public TW_DEVICEEVENT setBatteryPercentage(short batteryPercentage)
    {
        BatteryPercentage.setValue(batteryPercentage);
        return this;
    }

    public TW_DEVICEEVENT setPowerSupply(int powerSupply)
    {
        PowerSupply.setValue(powerSupply);
        return this;
    }

    public TW_DEVICEEVENT setXResolution(double xResolution)
    {
        XResolution.setValue(xResolution);
        return this;
    }

    public TW_DEVICEEVENT setYResolution(double yResolution)
    {
        YResolution.setValue(yResolution);
        return this;
    }

    public TW_DEVICEEVENT setFlashUsed2(long flashUsed2)
    {
        FlashUsed2.setValue(flashUsed2);
        return this;
    }

    public TW_DEVICEEVENT setAutomaticCapture(long automaticCapture)
    {
        AutomaticCapture.setValue(automaticCapture);
        return this;
    }

    public TW_DEVICEEVENT setTimeBeforeFirstCapture(long timeBeforeFirstCapture)
    {
        TimeBeforeFirstCapture.setValue(timeBeforeFirstCapture);
        return this;
    }

    public TW_DEVICEEVENT setTimeBetweenCaptures(long timeBetweenCaptures)
    {
        TimeBetweenCaptures.setValue(timeBetweenCaptures);
        return this;
    }
}
