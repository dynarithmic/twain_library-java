package com.dynarithmic.twain.exceptions;
public class DTwainInitException extends DTwainJavaAPIException
{
     /**
     *
     */
    private static final long serialVersionUID = -4228468040012881124L;
    final String message;

     public DTwainInitException(String reason)
     {
         message = reason;
     }

     @Override
     public String getMessage()
     {
         return message;
     }
}
