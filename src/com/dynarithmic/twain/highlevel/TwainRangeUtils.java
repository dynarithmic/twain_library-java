package com.dynarithmic.twain.highlevel;

import java.util.List;

public class TwainRangeUtils<U>
{
    private TwainRangeUtils() {}

    static private boolean isValidRangeImpl(List<Integer> values)
    {
        int low = values.get(TwainRange.MINPOS);
        int high = values.get(TwainRange.MAXPOS);
        if ( low > high )
            return false;
        return values.get(TwainRange.STEPPOS) >= 0;
    }

    static private boolean isValidRangeImplD(List<Double> values)
    {
        double low = values.get(TwainRange.MINPOS);
        double high = values.get(TwainRange.MAXPOS);
        if ( low > high )
            return false;
        return values.get(TwainRange.STEPPOS) >= 0;
    }

    @SuppressWarnings("unchecked")
    static public <U> boolean isValidRange(List<U> values)
    {
        if ( values.size() != 5 )
            return false;

        if ( values.get(0) instanceof Integer )
            return isValidRangeImpl((List<Integer>)(values));
        return isValidRangeImplD((List<Double>)values);
    }
}
