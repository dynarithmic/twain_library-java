package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TW_FRAME;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class PagesOptions
{
    private CAP_SEGMENTED segmentedValue = defaultSegmentedValue;
    private int maxFrames = defaultMaxFrames;
    private ICAP_SUPPORTEDSIZES pageSize = defaultPageSize;
    private TW_FRAME frame = new TW_FRAME();

    public static final CAP_SEGMENTED defaultSegmentedValue = CAP_SEGMENTED.TWSG_DEFAULT;
    public static final int defaultMaxFrames = Integer.MIN_VALUE;
    public static final ICAP_SUPPORTEDSIZES defaultPageSize = ICAP_SUPPORTEDSIZES.TWSS_DEFAULT;
    public static final TW_FRAME defaultFrame = new TW_FRAME();

    public CAP_SEGMENTED getSegmentedValue()
    {
        return segmentedValue;
    }

    public int getMaxFrames()
    {
        return maxFrames;
    }

    public ICAP_SUPPORTEDSIZES getPageSize()
    {
        return pageSize;
    }

    public TW_FRAME getFrame()
    {
        return frame;
    }

    public PagesOptions setSegmentedValue(CAP_SEGMENTED segmentedValue)
    {
        this.segmentedValue = segmentedValue;
        return this;
    }

    public PagesOptions setMaxFrames(int maxFrames)
    {
        this.maxFrames = maxFrames;
        return this;
    }

    public PagesOptions setPageSize(ICAP_SUPPORTEDSIZES pageSize)
    {
        this.pageSize = pageSize;
        return this;
    }

    public PagesOptions setFrame(TW_FRAME frame)
    {
        this.frame = frame;
        return this;
    }

    static protected final int affectedCaps[] = {   CAPS.CAP_SEGMENTED,
                                                    CAPS.ICAP_FRAMES,
                                                    CAPS.ICAP_MAXFRAMES,
                                                    CAPS.ICAP_SUPPORTEDSIZES};


    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
