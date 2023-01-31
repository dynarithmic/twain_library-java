package com.dynarithmic.twain.highlevel;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.DTwainConstants.LoggingOptions;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
public class TwainLogger
{
    private boolean activated;
    private boolean logTwainMsg;
    private int  m_logFlags = LoggingOptions.USE_CALLBACK.value();
    private String m_logFile = "";
    private DTwainJavaAPI m_interface = null;
    private PrintStream m_utf8Stream = null;
    private OutputStream outputStream = System.out;

    private static final ArrayList<TwainLogger> allLoggers = new ArrayList<>();

    public TwainLogger(OutputStream strm) throws UnsupportedEncodingException
    {
        this();
        this.outputStream = strm;
    }

    public TwainLogger(DTwainJavaAPI iFace, OutputStream strm) throws UnsupportedEncodingException
    {
        this(iFace);
        this.outputStream = strm;
    }

    public TwainLogger() throws UnsupportedEncodingException
    {
        m_utf8Stream = new PrintStream(outputStream, true, "UTF-8");
        allLoggers.add(this);
        activated = false;
        logTwainMsg = false;
    }

    public TwainLogger(DTwainJavaAPI iFace) throws UnsupportedEncodingException
    {
        m_utf8Stream = new PrintStream(outputStream, true, "UTF-8");
        allLoggers.add(this);
        activated = false;
        logTwainMsg = false;
        m_interface = iFace;
    }

    public TwainLogger setOutputStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
        return this;
    }

    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }

    public TwainLogger setInterface(DTwainJavaAPI iFace)
    {
        m_interface = iFace;
        return this;
    }

    public int getLogFlags() { return m_logFlags; }
    public TwainLogger setLogFlags(int flags) { m_logFlags = flags; return this; }

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
            if ( m_logFile != null && !m_logFile.isEmpty())
                m_logFlags = m_logFlags | LoggingOptions.USE_CALLBACK.value() | LoggingOptions.USE_FILE.value();
            else
                m_logFlags = m_logFlags | LoggingOptions.USE_CALLBACK.value();
            try
            {
                if ( !logTwainMsg )
                    m_logFlags &= ~(DTwainConstants.LoggingOptions.SHOW_ISTWAINMSG.value());
                m_interface.DTWAIN_SetTwainLog(m_logFlags, m_logFile);
                m_interface.DTWAIN_EnableMsgNotify(1);
                return true;
            }
            catch (DTwainJavaAPIException e)
            {
                throw e;
            }
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


    public  TwainLogger setLogFileName(String logFile)
    {  m_logFile = logFile; return this; }

    public TwainLogger setLogTwainMsg(boolean bSet)
    {
        logTwainMsg = bSet;
        return this;
    }

    public boolean getLogTwainMsg()
    {
        return logTwainMsg;
    }

    public void activate()
    { activated = true; }

    public void deactivate()
    { activated = false; }

    public boolean isActivated()
    { return activated;  }

    public static void logEvent(String logMsg)
    {
        int nLoggers = allLoggers.size();
        for ( int i = 0; i < nLoggers; ++i )
        {
            TwainLogger theLogger = allLoggers.get(i);
            if (theLogger.activated)
                theLogger.onLogEvent(logMsg);
        }
    }

    public void onLogEvent(String logMsg)
    {  m_utf8Stream.println(logMsg); }
}
