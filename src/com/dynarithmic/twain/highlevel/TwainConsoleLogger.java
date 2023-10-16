package com.dynarithmic.twain.highlevel;

public class TwainConsoleLogger implements TwainLoggerProc 
{
    @Override
    public void Log(String sMsg) throws Exception 
    {
        System.out.println(sMsg);
    }
}
