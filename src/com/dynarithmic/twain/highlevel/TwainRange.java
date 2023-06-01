/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

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
package com.dynarithmic.twain.highlevel;

import java.util.ArrayList;
import java.util.List;

public class TwainRange<T>
{
    private List<T> allValues = new ArrayList<>();
    private boolean isValid = false;
    public static final int MINPOS = 0;
    public static final int MAXPOS = 1;
    public static final int STEPPOS = 2;
    public static final int CURRENTPOS = 3;
    public static final int DEFAULTPOS = 4;
    public static final int RANGESIZE = 5;
    private static final String INVALIDRANGE = "Invalid TwainRange";

    @SuppressWarnings("unchecked")
    private void Init(T dummy)
    {
        for (int i = 0; i < RANGESIZE; ++i)
        {
            if ( dummy instanceof Double)
            {
                List<Double> dList = (List<Double>)(allValues);
                dList.add(0.0);
            }
            else
            if ( dummy instanceof Integer)
            {
                List<Integer> iList = (List<Integer>)(allValues);
                iList.add(0);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void Init(int which, T valueToSet)
    {
        if ( valueToSet instanceof Double)
        {
            List<Double> dList = (List<Double>)(allValues);
            dList.set(which, (Double)valueToSet);
        }
        else
        if ( valueToSet instanceof Integer)
        {
            List<Integer> iList = (List<Integer>)(allValues);
            iList.set(which, (Integer)valueToSet);
        }
    }

    public TwainRange(T dummy)
    {
        for (int i = 0; i < RANGESIZE; ++i)
            Init(dummy);
        isValid = true;
    }

    public TwainRange(List<T> values) throws TwainRangeException
    {
        if ( values.isEmpty())
            throw new TwainRangeException(INVALIDRANGE);

        if ( values.size() != RANGESIZE)
            throw new TwainRangeException(INVALIDRANGE);
        Init(values.get(0));
        Init(MINPOS, values.get(MINPOS));
        Init(MAXPOS, values.get(MAXPOS));
        Init(STEPPOS, values.get(STEPPOS));
        Init(CURRENTPOS, values.get(CURRENTPOS));
        Init(DEFAULTPOS, values.get(DEFAULTPOS));

        if ( !TwainRangeUtils.isValidRange(values) )
            throw new TwainRangeException(INVALIDRANGE);
        isValid = true;
    }

    public TwainRange(T low, T high, T step, T current, T defaultVal) throws TwainRangeException
    {
        for (int i = 0; i < RANGESIZE; ++i)
            Init(low);
        Init(MINPOS, low);
        Init(MAXPOS, high);
        Init(STEPPOS, step);
        Init(CURRENTPOS, current);
        Init(DEFAULTPOS, defaultVal);
        if ( !TwainRangeUtils.isValidRange(allValues) )
            throw new TwainRangeException(INVALIDRANGE);
        this.isValid = true;
    }

    @SuppressWarnings("unchecked")
    public int getExpandCount()
    {
        if ( allValues.isEmpty())
            return 0;
        if ( this.isValid )
        {
            if ( this.allValues.get(0) instanceof Double )
            {
                List<Double> dList = (List<Double>)(allValues);
                return (int)(Math.abs(dList.get(MAXPOS) - dList.get(MINPOS)) / dList.get(STEPPOS) + 1);
            }
            List<Integer> dList = (List<Integer>)(allValues);
            return Math.abs(dList.get(MAXPOS) - dList.get(MINPOS)) / dList.get(STEPPOS) + 1;
        }
        return 0;
    }

    public boolean valueExists(T testValue)
    {
        if (testValue instanceof Double)
        {
            double actualValue = (Double)testValue;
            double lBias = 0.0;
            double aMinValue = (Double)allValues.get(MINPOS);
            double aMaxValue = (Double)allValues.get(MAXPOS);
            if ( actualValue < aMinValue || actualValue > aMaxValue )
                return false;
            double a2Value = (Double)allValues.get(STEPPOS);
            if ( aMinValue != 0.0 )
                lBias = -aMinValue;
            actualValue += lBias;
            double res = actualValue % a2Value;
            if ( res == 0 )
                return true;
        }
        else
        {
            int actualValue = (Integer)testValue;
            int lBias = 0;
            int aMinValue = (Integer)allValues.get(MINPOS);
            int aMaxValue = (Integer)allValues.get(MAXPOS);
            int a2Value = (Integer)allValues.get(STEPPOS);
            if ( actualValue < aMinValue || actualValue > aMaxValue )
                return false;
            if ( aMinValue != 0 )
                lBias = -aMinValue;
            actualValue += lBias;
            int res = actualValue % a2Value;
            if ( res == 0 )
                return true;
        }
        return false;
    }

    public T getMin() { return allValues.get(MINPOS); }
    public T getMax() { return allValues.get(MAXPOS); }
    public T getStep() { return allValues.get(STEPPOS); }
    public T getCurrent() { return allValues.get(CURRENTPOS); }
    public T getDefault() { return allValues.get(DEFAULTPOS); }

    public double getValue(int where)
    {
        if ( allValues.isEmpty())
            return Double.MIN_VALUE;
        if ( allValues.get(0) instanceof Double )
        {
            double a0Value = (Double)(allValues.get(MINPOS));
            double a2Value = (Double)(allValues.get(STEPPOS));
            return a0Value + where * a2Value;
        }
        int a0Value = (Integer)(allValues.get(MINPOS));
        int a2Value = (Integer)(allValues.get(STEPPOS));
        return (int)(a0Value + where * a2Value);
    }

/*      public double getExpandedValue(int nPos) throws DTwainRuntimeException
        {
            if ( nPos < 0 )
                throw new DTwainRuntimeException(DTwainConstants.ErrorCode.ERROR_INVALID_PARAM);
            return minVal + ( step * nPos );
        }

        public double getPosition(double value) throws DTwainRuntimeException
        {
            if ( step == 0 )
                throw new DTwainRuntimeException(DTwainConstants.ErrorCode.ERROR_INVALID_RANGE);
            return (value - minVal) / step;
        }

    }
*/
}
