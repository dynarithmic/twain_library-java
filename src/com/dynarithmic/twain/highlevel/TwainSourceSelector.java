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
package com.dynarithmic.twain.highlevel;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.exceptions.DTwainRuntimeException;

public class TwainSourceSelector
{
    private TwainSourceSelector()
    {}

    public static final int GETDEFAULT = 0;

    static public long selectSource(DTwainJavaAPI theInterface) throws DTwainJavaAPIException
    {
        if (theInterface == null || theInterface.getLibraryHandle() == 0 )
            throw new DTwainRuntimeException(DTwainConstants.ErrorCode.ERROR_NOT_INITIALIZED);
        try
        {
            return theInterface.DTWAIN_SelectSource();
        }
        catch (DTwainJavaAPIException e)
        {
            throw e;
        }
    }

    static public long selectSource(DTwainJavaAPI theInterface, TwainSourceDialog selectOpts) throws DTwainJavaAPIException
    {
        if (theInterface == null || theInterface.getLibraryHandle() == 0 )
            throw new DTwainRuntimeException(DTwainConstants.ErrorCode.ERROR_NOT_INITIALIZED);
        try
        {
            if ( selectOpts.isGetDefaultSource() )
                return theInterface.DTWAIN_SelectDefaultSource();
            else
            if ( !selectOpts.isEnhancedDialogEnabled() && !selectOpts.isSelectUsingSourceName())
                return theInterface.DTWAIN_SelectSource();
            else
            if ( selectOpts.isSelectUsingSourceName())
                return theInterface.DTWAIN_SelectSourceByName(selectOpts.getSourceName());
            return theInterface.DTWAIN_SelectSource2Ex(selectOpts.getTitle(),
                                                             selectOpts.getXPos(),
                                                             selectOpts.getYPos(),
                                                             selectOpts.getIncludedNamesAsString(),
                                                             selectOpts.getExcludedNamesAsString(),
                                                             selectOpts.getNameMappingAsString(),
                                                             selectOpts.generateOptions());
        }
        catch (DTwainJavaAPIException e)
        {
            throw e;
        }
    }

    static public long selectSource(DTwainJavaAPI theInterface, int defaultConstant) throws DTwainJavaAPIException
    {
        if (theInterface == null || theInterface.getLibraryHandle() == 0 )
            throw new DTwainRuntimeException(DTwainConstants.ErrorCode.ERROR_NOT_INITIALIZED);
        try {
            return theInterface.DTWAIN_SelectDefaultSource();
        }
        catch (DTwainJavaAPIException e)
        {
            throw e;
        }
    }

    static public long selectSource(DTwainJavaAPI theInterface, String name) throws DTwainJavaAPIException
    {
        if (theInterface == null || theInterface.getLibraryHandle() == 0 )
            throw new DTwainRuntimeException(DTwainConstants.ErrorCode.ERROR_NOT_INITIALIZED);
        if ( name == null )
            throw new DTwainRuntimeException(DTwainConstants.ErrorCode.ERROR_INVALID_PARAM);
        try
        {
            return theInterface.DTWAIN_SelectSourceByName(name);
        }
        catch (DTwainJavaAPIException e)
        {
            throw e;
        }
    }
}
