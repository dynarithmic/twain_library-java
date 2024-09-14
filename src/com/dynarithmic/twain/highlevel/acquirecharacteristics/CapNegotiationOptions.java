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

import com.dynarithmic.twain.lowlevel.TwainConstants.CAPS;

/**
 * @author Dynarithmic Software
 * <p>CapNegotiationOptions Gets/sets the extended capabilities that can be queried during image acquisition.<br>
 *              (see "Capability Negotiation Parameters", Chapter 10-3 of the TWAIN 2.4 specification)
 *
 */
public class CapNegotiationOptions
{
    private boolean enabled = false;
    List<Integer> extendedCapList = new ArrayList<>();

    /**
     * @param enable Enables extended capability negotiations.
     * @return The current object.
     * @see #isEnabled()
     */
    public CapNegotiationOptions enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    /**
     * @return true if extended capability negotiation is enabled, otherwise false is returned.
     * @see #enable(boolean)
     */
    public boolean isEnabled()
    {
        return this.enabled;
    }

    /**
     * @param capList  The list of extended capabilities that will be negotiated during image acquisition.<br>
     * @return The current object.
     * @see #getExtendedCaps()
     */
    public CapNegotiationOptions setExtendedCaps(List<Integer> capList)
    {
        this.extendedCapList = capList;
        return this;
    }

    /**
     * @return The list of extended capabilities that will be negotiated during image acquisition.
     * @see #setExtendedCaps(List)
     */
    public List<Integer> getExtendedCaps()
    {
        return this.extendedCapList;
    }

    static protected final int affectedCaps[] = {
            CAPS.CAP_EXTENDEDCAPS};


    /**
     * @return an array of the TWAIN capabilities that the CapNegotiationOptions will affect<br>
     *         when TwainSource.acquire() is called.
     */
static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }

}
