package com.dynarithmic.twain.exceptions;

import com.dynarithmic.twain.DTwainConstants.ErrorCode;

public class DTwainRuntimeException extends DTwainJavaAPIException
{
    /**
     *
     */
    private static final long serialVersionUID = 3726524279257607844L;
    /**
     *
     */
    String message;
    int errorcode;

    public DTwainRuntimeException(int nCode)
    {
        super("");
        ExceptionImpl(nCode);
    }

    public DTwainRuntimeException(ErrorCode errCode)
    {
        super("");
        ExceptionImpl(errCode.value());
    }

    private void ExceptionImpl(int nCode)
    {
/*      errorcode = nCode;
        // get the error string
        String
        ResourceBundle theBundle = DTwainResourceContainer.getResources();
        String sCode = DTwainConstants.toString(errorcode);
        if (sCode != null)
            message = theBundle.getString(sCode);
        else
            message = "No error code exists";*/
    }

    public String getMessage()
    {
        return message;
    }

    public int getError()
    {
        return errorcode;
    }
}
