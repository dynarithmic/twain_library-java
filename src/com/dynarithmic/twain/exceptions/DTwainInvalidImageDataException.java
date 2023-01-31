package com.dynarithmic.twain.exceptions;
public class DTwainInvalidImageDataException extends DTwainJavaAPIException
{
     /**
     *
     */
    private static final long serialVersionUID = -865684313225030687L;
    final String message;

     public DTwainInvalidImageDataException(String reason)
     {
         message = reason;
     }

     public String getMessage()
     {
         return message;
     }
}