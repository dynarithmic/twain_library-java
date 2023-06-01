/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2023 Dynarithmic Software.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    DYNARITHMIC SOFTWARE. DYNARITHMIC SOFTWARE DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS.

 */
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
