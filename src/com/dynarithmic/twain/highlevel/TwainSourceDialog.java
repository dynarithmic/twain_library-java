package com.dynarithmic.twain.highlevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynarithmic.twain.DTwainConstants;

public class TwainSourceDialog
{
    private boolean enableEnhancedDialog = false;
    private boolean getDefaultSource = false;
    private boolean enableUsingSourceName = false;
    private String sourceName = "";
    private boolean bCenter = false;
    private boolean bCenterScreen = false;
    private boolean bSortNames = false;
    private boolean bHorizScroll = false;
    private boolean bTopmostWindow = false;
    private String sTitle = "TWAIN Select Source";

    private List<String> sIncludeList = new ArrayList<>();
    private List<String> sExcludeList = new ArrayList<>();
    private Map<String, String> sNameMapping = new HashMap<>();

    private String sFontName;
    private int xPos = 0;
    private int yPos = 0;

    public TwainSourceDialog()
    {}

    public TwainSourceDialog  center(boolean bSet) { bCenter = bSet; return this; }
    public TwainSourceDialog  centerScreen(boolean bSet) { bCenterScreen = bSet; return this; }
    public TwainSourceDialog  sortNames(boolean bSet) { bSortNames = bSet; return this; }
    public TwainSourceDialog  topmostWindow(boolean bSet ) { bTopmostWindow = bSet; return this; }
    public TwainSourceDialog  horizontalScroll(boolean bSet) { bHorizScroll = bSet; return this; }
    public TwainSourceDialog  setXYPos(int X, int Y) { xPos = X; yPos = Y; return this; }
    public TwainSourceDialog  setXPos(int X) { xPos = X; return this; }
    public TwainSourceDialog  setYPos(int Y) { yPos = Y; return this; }
    public TwainSourceDialog  setTitle(String title) { sTitle = title; return this; }
    public TwainSourceDialog  setFontName(String sFont) { sFontName = sFont; return this; }
    public TwainSourceDialog  excludeNames(String [] excludedNames) { sExcludeList = Arrays.asList(excludedNames); return this; }
    public TwainSourceDialog  excludeName(String excludedName) { sExcludeList.add(excludedName); return this; }
    public TwainSourceDialog  includeNames(String [] includedNames) { sIncludeList = Arrays.asList(includedNames); return this; }
    public TwainSourceDialog  includeName(String includedName) { sIncludeList.add(includedName); return this; }
    public TwainSourceDialog  mapName(String origName, String newName) { sNameMapping.put(origName, newName); return this; }
    public TwainSourceDialog  mapNames(Map<String, String> nameMap) { sNameMapping = nameMap; return this; }

    public String getTitle() { return sTitle; }
    public String getFontName() { return sFontName; }

    public String [] getExcludedNames() { return sExcludeList.toArray(new String[0]); }
    public String getExcludedNamesAsString()
    {
        return String.join("|",sExcludeList);
    }

    public String [] getIncludedNames() { return sIncludeList.toArray(new String[0]); }
    public String getIncludedNamesAsString()
    {
        return String.join("|", sIncludeList);
    }

    public Map<String, String> getNameMapping() { return sNameMapping; }
    public String getNameMappingAsString()
    {
        List<String> sMapList = new ArrayList<>();
        for (Map.Entry<String, String> entry : sNameMapping.entrySet())
            sMapList.add(entry.getKey() + "=" + entry.getValue());
        return String.join("|",  sMapList);
    }

    public int generateOptions()
    {
        int value = 0;
        if ( bCenter )
            value += DTwainConstants.TwainDialogOptions.CENTER.value();
        if ( bCenterScreen )
            value += DTwainConstants.TwainDialogOptions.CENTER_SCREEN.value();
        if ( bSortNames )
            value += DTwainConstants.TwainDialogOptions.SORTNAMES.value();
        if ( bTopmostWindow )
            value += DTwainConstants.TwainDialogOptions.TOPMOSTWINDOW.value();
        if ( bHorizScroll )
            value += DTwainConstants.TwainDialogOptions.HORIZONTALSCROLL.value();
        if ( !getIncludedNamesAsString().isEmpty() )
            value += DTwainConstants.TwainDialogOptions.USEINCLUDENAMES.value();
        if ( !getExcludedNamesAsString().isEmpty() )
            value += DTwainConstants.TwainDialogOptions.USEEXCLUDENAMES.value();
        if ( !getNameMappingAsString().isEmpty())
            value += DTwainConstants.TwainDialogOptions.USENAMEMAPPING.value();
        return value;
    }

    public boolean isCenter()
    {
        return bCenter;
    }

    public boolean isCenterScreen()
    {
        return bCenterScreen;
    }

    public boolean isSortNames()
    {
        return bSortNames;
    }

    public boolean isHorizontalScroll()
    {
        return bHorizScroll;
    }

    public int getXPos()
    {
        return xPos;
    }

    public int getYPos()
    {
        return yPos;
    }

    public boolean isEnhancedDialogEnabled()
    {
        return enableEnhancedDialog;
    }

    public boolean isGetDefaultSource()
    {
        return getDefaultSource;
    }

    public TwainSourceDialog enableEnhancedDialog(boolean enableEnhancedDialog)
    {
        this.enableEnhancedDialog = enableEnhancedDialog;
        return this;
    }

    public TwainSourceDialog setGetDefaultSource(boolean getDefaultSource)
    {
        this.getDefaultSource = getDefaultSource;
        return this;
    }

    public boolean isSelectUsingSourceName()
    {
        return enableUsingSourceName;
    }

    public String getSourceName()
    {
        return sourceName;
    }

    public TwainSourceDialog enableSelectUsingSourceName(boolean getUsingSourceName)
    {
        this.enableUsingSourceName = getUsingSourceName;
        return this;
    }

    public TwainSourceDialog enableSelectUsingSourceName(boolean getUsingSourceName, String sourceName)
    {
        this.enableUsingSourceName = getUsingSourceName;
        this.sourceName = sourceName;
        return this;
    }

    public TwainSourceDialog setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
        return this;
    }

    public boolean isTopMostWindow()
    {
        return this.bTopmostWindow;
    }
}
