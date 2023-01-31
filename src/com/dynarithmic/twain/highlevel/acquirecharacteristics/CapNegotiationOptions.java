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
