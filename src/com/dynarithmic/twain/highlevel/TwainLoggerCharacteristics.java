package com.dynarithmic.twain.highlevel;

import java.io.UnsupportedEncodingException;
import com.dynarithmic.twain.DTwainConstants.LoggingOptions;

public class TwainLoggerCharacteristics
{
    public static class LoggingDestination
    {
        private LoggingDestination() {}
        public static final int TOFILE = LoggingOptions.USE_FILE.value();
        public static final int TOCONSOLE = LoggingOptions.USE_CALLBACK.value();
        public static final int TOALL = TOFILE + TOCONSOLE;
        public static final int TOCUSTOM = 0;
    }

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

    private int logDestination = LoggingDestination.TOCONSOLE;
    private LoggerVerbosity logVerbosity = LoggerVerbosity.MEDIUM;
    private String fileName = "";
    private boolean enabled = false;
    private TwainLogger twainLogger = null;
    private int [] verbose_settings = new int [7];
    private int customLoggingOptions = 0;

    public TwainLoggerCharacteristics() throws UnsupportedEncodingException
    {
        this.twainLogger = new TwainLogger();
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

    public TwainLoggerCharacteristics setCustomLoggingOptions(int customLoggingOptions)
    {
        this.customLoggingOptions = customLoggingOptions;
        return this;
    }

    public int getCustomLoggingOptions()
    {
        return this.customLoggingOptions;
    }

    public int getVerbosityFlags()
    {
        if ( logVerbosity == LoggerVerbosity.CUSTOM )
            return getCustomLoggingOptions() & (0x0000FFFF);
        return verbose_settings[logVerbosity.ordinal()];
    }

    public TwainLoggerCharacteristics enable(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public TwainLoggerCharacteristics setDestination( int destination )
    {
        this.logDestination = destination;
        return this;
    }

    public int getDestination()
    {
        return this.logDestination;
    }

    public TwainLoggerCharacteristics setVerbosity(LoggerVerbosity verbosity)
    {
        this.logVerbosity = verbosity;
        return this;
    }

    public LoggerVerbosity getVerbosity()
    {
        return this.logVerbosity;
    }

    public TwainLoggerCharacteristics setFileName(String fileName)
    {
        this.fileName = fileName;
        return this;
    }

    public String getFileName()
    {
        return this.fileName;
    }

    public TwainLogger getInternalLogger()
    {
        return this.twainLogger;
    }
}
