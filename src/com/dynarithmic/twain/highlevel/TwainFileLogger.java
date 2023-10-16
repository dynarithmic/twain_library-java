package com.dynarithmic.twain.highlevel;

import java.io.FileWriter;
import java.io.IOException;

import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;

public class TwainFileLogger implements TwainLoggerProc 
{
    FileWriter logFile = null;
    
    public TwainFileLogger()
    {}
    
    public TwainFileLogger(String filename) throws IOException
    {
        logFile = new FileWriter(filename);
    }

    public TwainFileLogger setName(String filename) throws IOException
    {
        logFile = new FileWriter(filename);
        return this;
    }
    
    @Override
    public void Log(String sMsg) throws DTwainJavaAPIException
    {
        try 
        {
            logFile.write(sMsg + "\n");
        } 
        catch (IOException e) 
        {
            throw new DTwainJavaAPIException(e.getMessage());
        }
    }
}
