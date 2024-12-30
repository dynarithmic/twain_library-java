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
package com.dynarithmic.twain.highlevel.acquirecharacteristics;
import java.util.ArrayList;
import java.util.List;

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

/**
 * @author Dynarithmic Software
 * <p>AudibleAlarmOptions gets/sets the audible alarm options described by the TWAIN 2.4 specification for
 * any TWAIN-enabled device.<p>
 * If the device does not support the alarm option(s), these settings are ignored during the acquisition process.<p>
 *              (see "Audible Alarms", Chapter 10-3 of the TWAIN 2.4 specification)
 */
public class AudibleAlarmsOptions
{
    private int alarmVolume = defaultAlarmVolume;
    private List<CAP_ALARMS> alarms = new ArrayList<>();
    static public final int defaultAlarmVolume = Integer.MAX_VALUE;

    /**
     * @return A java List denoting the alarms that are set
     */
    public List<CAP_ALARMS> getAlarms()
    {
        return this.alarms;
    }

    /**
     * @return A java List denoting the alarms that are set as a list of integers
     */
    public List<Integer> getAlarmsAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (CAP_ALARMS alarm : alarms)
        {
            if ( alarm != CAP_ALARMS.TWAL_DEFAULT )
                intList.add(alarm.ordinal());
        }
        return intList;
    }


    /**
     * @param alarms List of alarms to use when acquiring an image
     * @return The current object.
     */
    public AudibleAlarmsOptions setAlarms(List<CAP_ALARMS> alarms)
    {
        this.alarms = alarms;
        return this;
    }

    /**
     * @return The alarm volume
     */
    public int getVolume()
    {
        return this.alarmVolume;
    }

    /**
     * @param volume The volume value
     * @return The current object.
     */
    public AudibleAlarmsOptions setVolume(int volume)
    {
        this.alarmVolume = volume;
        return this;
    }

    static private final int affectedCaps[] = { CAPS.CAP_ALARMS,
                                                CAPS.CAP_ALARMVOLUME};

    /**
     * @return an array of the TWAIN capabilities that the AudibleAlarmsOptions will affect<br>
     *         when TwainSource.acquire() is called.
     */
    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
