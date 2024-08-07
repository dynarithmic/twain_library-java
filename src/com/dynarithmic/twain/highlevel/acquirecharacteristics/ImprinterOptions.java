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

import com.dynarithmic.twain.lowlevel.TwainConstants.*;

public class ImprinterOptions
{
    private boolean enabled = false;
    private List<CAP_PRINTER> printersToUse     = new ArrayList<>();
    private List<CAP_PRINTERFONTSTYLE> fontStyles   = new ArrayList<>();
    private List<CAP_PRINTERINDEXTRIGGER> indexTriggers = new ArrayList<>();
    private List<String>  printerStrings = new ArrayList<>();
    private String suffixString = defaultSuffixString;
    private int printerIndex = defaultIndex;
    private int printerMaxIndex = defaultMaxIndex;
    private int charRotation = defaultCharRotation;
    private int numDigits = defaultNumDigits;
    private int indexStep = defaultIndexStep;
    private String leadChar = defaultLeadChar;
    private CAP_PRINTERMODE stringMode = defaultStringMode;
    private double verticalOffset = defaultVerticalOffset;

    public static final String defaultSuffixString = "";
    public static final int defaultIndex = Integer.MAX_VALUE;
    public static final int defaultMaxIndex = Integer.MIN_VALUE;
    public static final int defaultCharRotation = Integer.MAX_VALUE;
    public static final int defaultNumDigits = Integer.MIN_VALUE;
    public static final int defaultIndexStep = Integer.MIN_VALUE;
    public static final String defaultLeadChar = "";
    public static final CAP_PRINTERMODE defaultStringMode = CAP_PRINTERMODE.TWPM_DEFAULT;
    public static final double defaultVerticalOffset = Double.MAX_VALUE;

    public ImprinterOptions enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public ImprinterOptions setPrintersToUse(List<CAP_PRINTER> printerToUse)
    {
        this.printersToUse = printerToUse;
        return this;
    }

    public ImprinterOptions setFontStyles(List<CAP_PRINTERFONTSTYLE> fontStyles)
    {
        this.fontStyles = fontStyles;
        return this;
    }

    public ImprinterOptions setStrings(List<String> printerStrings)
    {
        this.printerStrings = printerStrings;
        return this;
    }

    public ImprinterOptions setSuffixString(String suffixString)
    {
        this.suffixString = suffixString;
        return this;
    }

    public ImprinterOptions setIndex(int printerIndex)
    {
        this.printerIndex = printerIndex;
        return this;
    }

    public ImprinterOptions setMaxIndex(int printerMaxIndex)
    {
        this.printerMaxIndex = printerMaxIndex;
        return this;
    }

    public ImprinterOptions setCharRotation(int charRotation)
    {
        this.charRotation = charRotation;
        return this;
    }

    public ImprinterOptions setNumDigits(int numDigits)
    {
        this.numDigits = numDigits;
        return this;
    }

    public ImprinterOptions setIndexStep(int indexStep)
    {
        this.indexStep = indexStep;
        return this;
    }

    public ImprinterOptions setLeadChar(String leadChar)
    {
        this.leadChar = leadChar;
        return this;
    }

    public ImprinterOptions setStringMode(CAP_PRINTERMODE stringMode)
    {
        this.stringMode = stringMode;
        return this;
    }

    public ImprinterOptions setVerticalOffset(double verticalOffset)
    {
        this.verticalOffset = verticalOffset;
        return this;
    }

    public ImprinterOptions setIndexTriggers(List<CAP_PRINTERINDEXTRIGGER> indexTriggers)
    {
        this.indexTriggers = indexTriggers;
        return this;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public List<CAP_PRINTER> getPrintersToUse()
    {
        return printersToUse;
    }

    public List<CAP_PRINTERFONTSTYLE> getFontStyles()
    {
        return fontStyles;
    }

    public List<Integer> getFontStylesAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (CAP_PRINTERFONTSTYLE font : this.fontStyles)
        {
            if (font != CAP_PRINTERFONTSTYLE.TWPF_DEFAULT)
                intList.add(font.ordinal());
        }
        return intList;
    }

    public List<String> getStrings()
    {
        return printerStrings;
    }

    public String getSuffixString()
    {
        return suffixString;
    }

    public int getIndex()
    {
        return printerIndex;
    }

    public int getMaxIndex()
    {
        return printerMaxIndex;
    }

    public int getCharRotation()
    {
        return charRotation;
    }

    public int getNumDigits()
    {
        return numDigits;
    }

    public int getIndexStep()
    {
        return indexStep;
    }

    public String getLeadChar()
    {
        return leadChar;
    }

    public CAP_PRINTERMODE getStringMode()
    {
        return stringMode;
    }

    public List<CAP_PRINTERINDEXTRIGGER> getIndexTriggers()
    {
        return this.indexTriggers;
    }

    public List<Integer> getIndexTriggersAsInt()
    {
        List<Integer> intList = new ArrayList<>();
        for (CAP_PRINTERINDEXTRIGGER trigger : this.indexTriggers)
        {
            if ( trigger != CAP_PRINTERINDEXTRIGGER.TWCT_DEFAULT)
                intList.add(trigger.ordinal());
        }
        return intList;
    }

    public double getVerticalOffset()
    {
        return verticalOffset;
    }

    static protected final int affectedCaps[] = { CAPS.CAP_ENDORSER,
                                                  CAPS.CAP_PRINTER,
                                                  CAPS.CAP_PRINTERCHARROTATION,
                                                  CAPS.CAP_PRINTERENABLED,
                                                  CAPS.CAP_PRINTERFONTSTYLE,
                                                  CAPS.CAP_PRINTERINDEX,
                                                  CAPS.CAP_PRINTERINDEXLEADCHAR,
                                                  CAPS.CAP_PRINTERINDEXMAXVALUE,
                                                  CAPS.CAP_PRINTERINDEXNUMDIGITS,
                                                  CAPS.CAP_PRINTERINDEXSTEP,
                                                  CAPS.CAP_PRINTERINDEXTRIGGER,
                                                  CAPS.CAP_PRINTERMODE,
                                                  CAPS.CAP_PRINTERSTRING,
                                                  CAPS.CAP_PRINTERSUFFIX,
                                                  CAPS.CAP_PRINTERVERTICALOFFSET};

    static public final int [] getAffectedCaps()
    {
        return affectedCaps;
    }
}
