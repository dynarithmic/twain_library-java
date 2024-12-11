/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2025 Dynarithmic Software.

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynarithmic.twain.DTwainConstants;

public class TwainSourceSelectorOptions
{
    private boolean enableEnhancedDialog = false;
    private boolean getDefaultSource = false;
    private boolean enableUsingSourceName = false;
    private String sourceName = "";
    private boolean bCenter = false;
    private boolean bCenterScreen = false;
    private boolean bSortNames = false;
    private boolean bHorizScroll = false;
    private String sTitle = "TWAIN Select Source";

    private List<String> sIncludeList = new ArrayList<>();
    private List<String> sExcludeList = new ArrayList<>();
    private Map<String, String> sNameMapping = new HashMap<>();

    private String sFontName;
    private int xPos = 0;
    private int yPos = 0;

    public TwainSourceSelectorOptions()
    {}

    public TwainSourceSelectorOptions  center(boolean bSet) { bCenter = bSet; return this; }
    public TwainSourceSelectorOptions  centerScreen(boolean bSet) { bCenterScreen = bSet; return this; }
    public TwainSourceSelectorOptions  sortNames(boolean bSet) { bSortNames = bSet; return this; }
    public TwainSourceSelectorOptions  horizontalScroll(boolean bSet) { bHorizScroll = bSet; return this; }
    public TwainSourceSelectorOptions  setXYPos(int X, int Y) { xPos = X; yPos = Y; return this; }
    public TwainSourceSelectorOptions  setXPos(int X) { xPos = X; return this; }
    public TwainSourceSelectorOptions  setYPos(int Y) { yPos = Y; return this; }
    public TwainSourceSelectorOptions  setTitle(String title) { sTitle = title; return this; }
    public TwainSourceSelectorOptions  setFontName(String sFont) { sFontName = sFont; return this; }
    public TwainSourceSelectorOptions  excludeNames(String [] excludedNames) { sExcludeList = Arrays.asList(excludedNames); return this; }
    public TwainSourceSelectorOptions  excludeName(String excludedName) { sExcludeList.add(excludedName); return this; }
    public TwainSourceSelectorOptions  includeNames(String [] includedNames) { sIncludeList = Arrays.asList(includedNames); return this; }
    public TwainSourceSelectorOptions  includeName(String includedName) { sIncludeList.add(includedName); return this; }
    public TwainSourceSelectorOptions  mapName(String origName, String newName) { sNameMapping.put(origName, newName); return this; }
    public TwainSourceSelectorOptions  mapNames(Map<String, String> nameMap) { sNameMapping = nameMap; return this; }

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

    public TwainSourceSelectorOptions enableEnhancedDialog(boolean enableEnhancedDialog)
    {
        this.enableEnhancedDialog = enableEnhancedDialog;
        return this;
    }

    public TwainSourceSelectorOptions setGetDefaultSource(boolean getDefaultSource)
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

    public TwainSourceSelectorOptions enableSelectUsingSourceName(boolean getUsingSourceName)
    {
        this.enableUsingSourceName = getUsingSourceName;
        return this;
    }

    public TwainSourceSelectorOptions enableSelectUsingSourceName(boolean getUsingSourceName, String sourceName)
    {
        this.enableUsingSourceName = getUsingSourceName;
        this.sourceName = sourceName;
        return this;
    }

    public TwainSourceSelectorOptions setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
        return this;
    }
}
