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
package com.dynarithmic.twain.highlevel;
import java.util.ArrayList;

import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.DTwainConstants.LoggingOptions;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;

public class TwainLogger
{
    private boolean activated;
    private boolean logTwainMsg;
    private int  m_logFlags = LoggingOptions.USE_CALLBACK.value();
    private DTwainJavaAPI m_interface = null;
    private int [] verbose_settings = new int [7];
    private LoggerVerbosity logVerbosity = LoggerVerbosity.MEDIUM;

    private ArrayList<TwainLoggerProc> allLoggers = new ArrayList<>();

    public enum LoggerVerbosity
    {
        NONE,     // No logging
        MINIMAL,  // Call stack, parameter values passed and return values
        MODERATE, // MINIMAL + decode message sent to and from TWAIN DSM/Source
        MEDIUM,   // MODERATE + decode message sent to and from TWAIN DSM/Source + TW_MEMREF
        HIGH,     // MEDIUM + decode message sent to and from TWAIN DSM/Source + TW_MEMREF + TW_EVENT
        MAXIMUM,  // HIGH + low-level TWAIN activity
        CUSTOM    // Custom setting
    }
    
    public TwainLogger()
    {
        activated = false;
        logTwainMsg = false;
        verbose_settings[0] = 0;
        verbose_settings[1] = LoggingOptions.SHOW_CALLSTACK.value() | LoggingOptions.DECODE_BITMAP.value();
        verbose_settings[2] = verbose_settings[1] |
                              LoggingOptions.DECODE_DEST.value() |
                              LoggingOptions.DECODE_SOURCE.value();
        verbose_settings[3] = verbose_settings[2] | LoggingOptions.DECODE_TWMEMREF.value();
        verbose_settings[4] = verbose_settings[3] | LoggingOptions.DECODE_TWEVENT.value() ;
        verbose_settings[5] = LoggingOptions.LOGALL.value();
        verbose_settings[6] = LoggerVerbosity.CUSTOM.ordinal();
    }

    public int getVerbosityFlags()
    {
        return verbose_settings[logVerbosity.ordinal()];
    }
    
    public TwainLogger setVerbosity(LoggerVerbosity verbosity)
    {
        this.logVerbosity = verbosity;
        return this;
    }
    public boolean isActivated()
    {
        return activated;
    }
    
    public TwainLogger activate(boolean activated)
    {
        this.activated = activated;
        return this;
    }
    
    public TwainLogger addLogger(TwainLoggerProc proc)
    {
        allLoggers.add(proc);
        return this;
    }
    
    public TwainLogger clearLoggers()
    {
        allLoggers.clear();
        return this;
    }
    
    public TwainLogger removeLogger(TwainLoggerProc proc)
    {
        allLoggers.remove(proc);
        return this;
    }
    
    public TwainLogger setInterface(DTwainJavaAPI iFace)
    {
        m_interface = iFace;
        return this;
    }

    private void startLoggerHelper() throws DTwainJavaAPIException
    {
        if ( m_interface == null )
        {
            throw new DTwainJavaAPIException("DTwainJavaAPI instance cannot be null");
        }
        try {
            startLoggerInternal();
            activated = true;
        }
        catch (DTwainJavaAPIException e)
        {
            throw e;
        }
    }

    private boolean startLoggerInternal() throws DTwainJavaAPIException
    {
        if ( m_interface != null )
        {
            int verbosity = verbose_settings[logVerbosity.ordinal()] + LoggingOptions.USE_CALLBACK.value();
            int total_verbosity = (int)verbosity;
            m_interface.DTWAIN_SetTwainLog(total_verbosity, "");
            m_interface.DTWAIN_EnableMsgNotify(1);
            return true;
        }
        return false;
    }

    public void startLogger(DTwainJavaAPI iFace) throws DTwainJavaAPIException
    {
        m_interface = iFace;
        startLoggerHelper();
    }

    public void startLogger() throws DTwainJavaAPIException
    {
        startLoggerHelper();
    }
    public void stopLogger()
    {
        activated = false;
    }

    public TwainLogger setLogTwainMsg(boolean bSet)
    {
        logTwainMsg = bSet;
        return this;
    }

    public boolean hasProcs()
    {
        return allLoggers.size() > 0;
    }

    public void logMessage(String msg)
    {
        for (TwainLoggerProc proc : allLoggers)
        {
            try
            {
                proc.Log(msg);
            }
            catch (Exception e) 
            {
            }
        }
    }
}
