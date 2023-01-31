package com.dynarithmic.twain.highlevel;
import com.dynarithmic.twain.exceptions.DTwainInitException;

public class TwainRangeException extends DTwainInitException
{
    /**
     *
     */
    private static final long serialVersionUID = 5559342018098617794L;

    public TwainRangeException(String reason)
    {
        super(reason);
    }
}
