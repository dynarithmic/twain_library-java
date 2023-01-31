package com.dynarithmic.twain.exceptions;
public class DTwainWrongThreadException extends DTwainJavaAPIException
{
    private static final long serialVersionUID = 7405951376251874086L;
    String message;

     public DTwainWrongThreadException(String reason)
     {
         super(reason);
     }
}