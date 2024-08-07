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

import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Dynarithmic Software
 * <p>BarcodeDetectionOptions gets/sets the barcode detection options described by the TWAIN 2.4 specification for
 * any TWAIN-enabled device.<br>
 * If the device does not support the barcode detection option(s), these settings are ignored during the acquisition process.<br>
 *              (see "Bar Code Detection Search Parameters", Chapter 10-3 of the TWAIN 2.4 specification)
 */
public class BarcodeDetectionOptions
{
    private boolean enabled = false;
    private int maxRetries = defaultMaxRetries;
    private int maxSearchPriorities = defaultMaxSearchPriorities;
    private TwainConstants.ICAP_BARCODESEARCHMODE searchMode = defaultSearchMode;
    private int timeOutValue = defaultTimeout;
    private List<ICAP_SUPPORTEDBARCODETYPES> listSearchPriorities = new ArrayList<>();

    public static final ICAP_BARCODESEARCHMODE defaultSearchMode = TwainConstants.ICAP_BARCODESEARCHMODE.TWBD_DEFAULT;
    public static final int defaultTimeout = 0;
    public static final int defaultMaxRetries = 0;
    public static final int defaultMaxSearchPriorities = 0;

    /**
     * @param enabled enable (true) or disable (false) barcode detection.
     * @return The current object.
     * @see #isEnabled()
     */
    public BarcodeDetectionOptions enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    /**
     * @param maxRetries Restricts the number of times a search will be retried if no barcodes are found on each page.
     * @return The current object.
     * @see #getMaxRetries()
     */
    public BarcodeDetectionOptions setMaxRetries(int maxRetries)
    {
        this.maxRetries = maxRetries;
        return this;
    }

    /**
     * @param maxSearchPriorities Sets the maximum number of supported search priorities.
     * @return The current object.
     * @see #getMaxSearchPriorities()
     */
    public BarcodeDetectionOptions setMaxSearchPriorities(int maxSearchPriorities)
    {
        this.maxSearchPriorities = maxSearchPriorities;
        return this;
    }

    /**
     * @param searchMode Restricts bar code searching to certain orientations, or prioritizes on orientation over the other. Value can be one of the following:
     * <ul>
     * <li>ICAP_BARCODESEARCHMODE.TWBD_HORZ (horizontal)</li>
     * <li>ICAP_BARCODESEARCHMODE.TWBD_VERT (vertical)</li>
     * <li>ICAP_BARCODESEARCHMODE.TWBD_HORZVERT (horizontal/vertical)</li>
     * <li>ICAP_BARCODESEARCHMODE.TWBD_VERTHORZ (vertical/horizontal)</li></ul>
     * @return The current object.
     * @see #getSearchMode()
     */
    public BarcodeDetectionOptions setSearchMode(TwainConstants.ICAP_BARCODESEARCHMODE searchMode)
    {
        this.searchMode = searchMode;
        return this;
    }

    /**
     * @param timeOutValue Restricts the total time spent on searching for a bar code on each page
     * @return The current object.
     * @see #getTimeout()
     */
    public BarcodeDetectionOptions setTimeout(int timeOutValue)
    {
        this.timeOutValue = timeOutValue;
        return this;
    }

    /**
     * @param listSearchPriorities A prioritized list of bar code types dictating the order in which bar codes will be sought.
     * @return The current object.
     * @see #getSearchPriorities()
     */
    public BarcodeDetectionOptions setSearchPriorities(List<ICAP_SUPPORTEDBARCODETYPES> listSearchPriorities)
    {
        this.listSearchPriorities = listSearchPriorities;
        return this;
    }

    /**
     * @return true if bar code detection is enabled, otherwise false is returned.
     * @see #enable(boolean)
     */
    public boolean isEnabled() { return this.enabled; }

    /**
     * @return The number of times a search will be retried if no barcodes are found on each page.
     * @see #setMaxRetries(int)
     */
    public int getMaxRetries() { return this.maxRetries; }


    /**
     * @return The maximum number of supported search priorities.
     * @see #getMaxSearchPriorities()
     */
    public int getMaxSearchPriorities() { return this.maxSearchPriorities; }


    /**
     * @return The current bar code search mode
     * @see #setSearchMode(TwainConstants.ICAP_BARCODESEARCHMODE)
     */
    public TwainConstants.ICAP_BARCODESEARCHMODE getSearchMode() { return this.searchMode; }


    /**
     * @return The prioritized list of bar code types dictating the order in which bar codes will be sought.
     * @see #setSearchPriorities(List)
     */
    public List<ICAP_SUPPORTEDBARCODETYPES> getSearchPriorities() { return this.listSearchPriorities; }

    public List<Integer> getSearchPrioritiesAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (ICAP_SUPPORTEDBARCODETYPES sm : this.listSearchPriorities )
        {
            if ( sm != ICAP_SUPPORTEDBARCODETYPES.TWBT_DEFAULT )
                intList.add(sm.ordinal());
        }
        return intList;
    }

    /**
     * @return The total time spent on searching for a bar code on each page.
     * @see #setTimeout(int)
     */
    public int getTimeout() { return this.timeOutValue; }

    static protected final int affectedCaps[] = {   CAPS.ICAP_BARCODEDETECTIONENABLED,
                                                    CAPS.ICAP_SUPPORTEDBARCODETYPES,
                                                    CAPS.ICAP_BARCODEMAXRETRIES,
                                                    CAPS.ICAP_BARCODEMAXSEARCHPRIORITIES,
                                                    CAPS.ICAP_BARCODESEARCHMODE,
                                                    CAPS.ICAP_BARCODESEARCHPRIORITIES,
                                                    CAPS.ICAP_BARCODETIMEOUT};


    /**
     * @return an array of the TWAIN capabilities that the BarcodeDetectionOptions will affect<br>
     *         when TwainSource.acquire() is called.
     */
static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
