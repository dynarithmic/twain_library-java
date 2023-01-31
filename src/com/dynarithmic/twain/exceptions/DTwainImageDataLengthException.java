package com.dynarithmic.twain.exceptions;
public class DTwainImageDataLengthException extends DTwainJavaAPIException
{
     /**
     *
     */
    private static final long serialVersionUID = 1L;
    final String message;

     public DTwainImageDataLengthException(String reason)
     {
         message = reason;
     }

     @Override
     public String getMessage()
     {
         return message;
     }
}