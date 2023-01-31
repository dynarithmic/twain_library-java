package com.dynarithmic.twain.highlevel.acquirecharacteristics;

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class JobControlOptions
{
    private boolean enabled = false;
    private CAP_JOBCONTROL jobControl = defaultJobControl;

    public static final CAP_JOBCONTROL defaultJobControl = CAP_JOBCONTROL.TWJC_DEFAULT;

    public boolean isEnabled()
    {
        return enabled;
    }

    public CAP_JOBCONTROL getJobControl()
    {
        return jobControl;
    }

    public static CAP_JOBCONTROL getDefaultjobcontrol()
    {
        return defaultJobControl;
    }

    public JobControlOptions enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public JobControlOptions setJobControl(CAP_JOBCONTROL jobControl)
    {
        this.jobControl = jobControl;
        return this;
    }

    static protected final int affectedCaps[] = {CAPS.CAP_JOBCONTROL};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }


}
