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
