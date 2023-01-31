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
