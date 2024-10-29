package com.dynarithmic.twain.exceptions;

public class DTwainIncompatibleJNIException extends DTwainJavaAPIException 
{
     private static String message = "Conflict in 32/64 bit versions between the Java runtime and DTWAIN JNI layer (bitness does not match)\nEither set the proper runtime environment (32-bit, 64-bit) in your Java runtime or " +
                       "call com.dynarithmic.twain.DTwainGlobalOptions.setJNIVersion() to set the proper JNI version to use (32-bit, 64-bit) to match the Java runtime being used";

     public DTwainIncompatibleJNIException()
     {
         super(message);
     }
}
