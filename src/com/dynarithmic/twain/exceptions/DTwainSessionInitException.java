package com.dynarithmic.twain.exceptions;
public class DTwainSessionInitException extends Exception
{
     /**
     *
     */
    private static final long serialVersionUID = 8495031996046172326L;
    String message;

     public DTwainSessionInitException(String reason)
     {
         message = reason;
     }

     public String getMessage()
     {
         return message;
     }
}