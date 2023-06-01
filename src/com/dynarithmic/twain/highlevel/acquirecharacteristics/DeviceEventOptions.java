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

import com.dynarithmic.twain.lowlevel.TwainConstants.CAPS;
import com.dynarithmic.twain.lowlevel.TwainConstants.CAP_DEVICEEVENT;

public class DeviceEventOptions
{
    private List<CAP_DEVICEEVENT> deviceEvents = new ArrayList<>();
    private boolean enabled = false;

    public DeviceEventOptions setDeviceEvents(List<CAP_DEVICEEVENT> deviceEvents)
    {
        this.deviceEvents = deviceEvents;
        return this;
    }

    public DeviceEventOptions enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public List<CAP_DEVICEEVENT> getDeviceEvents()
    {
        return deviceEvents;
    }

    public List<Integer> getDeviceEventsAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (CAP_DEVICEEVENT event : deviceEvents)
        {
            if (event != CAP_DEVICEEVENT.TWDE_DEFAULT)
                intList.add(event.ordinal());
        }
        return intList;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    static protected final int affectedCaps[] = {CAPS.CAP_DEVICEEVENT};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }

}
