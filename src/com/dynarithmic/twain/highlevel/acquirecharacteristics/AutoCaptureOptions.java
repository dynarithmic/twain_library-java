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

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

/**
 * @author Dynarithmic Software
 * <p>AutoCaptureOptions gets/sets the auto capture options described by the TWAIN 2.4 specification for
 * any TWAIN-enabled device.<br>
 * If the device does not support the auto capture option(s), these settings are ignored during the acquisition process.<br>
 *              (see "Automatic Capture", Chapter 10-3 of the TWAIN 2.4 specification)
 */
public class AutoCaptureOptions
{
    private int numImages = defaultNumImages;
    private int timeBeforeFirstCapture = defaultTimeBeforeFirstCapture;
    private int timeBetweenCaptures = defaultTimeBetweenCaptures;

    static public final int defaultNumImages = Integer.MAX_VALUE;
    static public final int defaultTimeBeforeFirstCapture = 0;
    static public final int defaultTimeBetweenCaptures = 0;

    /**
     * @param numImages The number of images to automatically capture
     * @return The current object.
     * @see #getNumImages()
     */
    public AutoCaptureOptions setNumImages(int numImages)
    {
        this.numImages = numImages;
        return this;
    }

    /**
     * @param timeBeforeFirstCapture Selects the number of milliseconds before the first picture is to be taken<br>
     *                               or the first image is to be scanned.
     * @return The current object.
     * @see #getTimeBeforeFirstCapture()
     */
    public AutoCaptureOptions setTimeBeforeFirstCapture(int timeBeforeFirstCapture)
    {
        this.timeBeforeFirstCapture = timeBeforeFirstCapture;
        return this;
    }

    /**
     * @param timeBetweenCaptures Selects the milliseconds to wait between pictures taken, or images scanned.
     * @return The current object
     * @see #getTimeBetweenCaptures()
     */
    public AutoCaptureOptions setTimeBetweenCaptures(int timeBetweenCaptures)
    {
        this.timeBetweenCaptures = timeBetweenCaptures;
        return this;
    }

    /**
     * @return The number of images to automatically capture
     * @see #setNumImages(int)
     */
    public int getNumImages()
    {
        return numImages;
    }

    /**
     * @return The number of milliseconds before the first picture is to be taken, or the first image to be scanned.
     * @see #setTimeBeforeFirstCapture(int)
     */
    public int getTimeBeforeFirstCapture()
    {
        return timeBeforeFirstCapture;
    }

    /**
     * @return The milliseconds to wait between pictures taken, or images scanned.
     * @see #setTimeBetweenCaptures(int)
     */
    public int getTimeBetweenCaptures()
    {
        return timeBetweenCaptures;
    }

    /**
     * Affected TWAIN capabilities.
     */
    static protected final int affectedCaps[] = {CAPS.CAP_TIMEBEFOREFIRSTCAPTURE,
                                                 CAPS.CAP_TIMEBETWEENCAPTURES,
                                                 CAPS.CAP_AUTOMATICCAPTURE};

    /**
     * @return an array of the TWAIN capabilities that the AutoCaptureOptions will affect<br>
     *         when TwainSource.acquire() is called.
     */
    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
