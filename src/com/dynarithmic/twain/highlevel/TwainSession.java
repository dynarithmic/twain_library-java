/*
    This file is part of the Dynarithmic TWAIN Library (DTWAIN).
    Copyright (c) 2002-2024 Dynarithmic Software.

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

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import com.dynarithmic.twain.DTwainConstants.DSMType;
import com.dynarithmic.twain.DTwainConstants.FileType;
import com.dynarithmic.twain.DTwainConstants.JNIVersion;
import com.dynarithmic.twain.DTwainConstants.SessionStartupMode;
import com.dynarithmic.twain.DTwainGlobalOptions;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.DTwainVersionInfo;
import com.dynarithmic.twain.lowlevel.TW_IDENTITY;
import com.dynarithmic.twain.lowlevel.TwainConstantMapper;

public class TwainSession
{
    private class TwainCallbackInfo
    {
        public TwainSource twainSource;
        public TwainCallback twainCallback;
        public UUID callbackId;
    }
    
    private String dsmPath = "";
    private DTwainJavaAPI dtwainAPI = null;
    private boolean started = false;
    private TwainCharacteristics twainCharacteristics = new TwainCharacteristics();
    private List<TwainSourceInfo> twainSourceCache = new ArrayList<>();
    private static TwainLogger s_TwainLogger = new TwainLogger();
    private DTwainVersionInfo dtwainVersionInfo = new DTwainVersionInfo();
    private boolean enableLogging = false;
    private boolean enableTripletNotification = false;
    private TW_IDENTITY twainSessionId = new TW_IDENTITY();
    private static ConcurrentMap<Long, TwainSource> handleToSourceMap = new ConcurrentHashMap<>();
    private List<Integer> singlePageTypes = new ArrayList<>();
    private List<Integer> multiPageTypes = new ArrayList<>();
    private Map<Integer, SupportedFileTypeInfo> singlepageFileTypeInfoMap = new HashMap<>();
    private Map<Integer, SupportedFileTypeInfo> multipageFileTypeInfoMap = new HashMap<>();
    private Map<TwainSource, TwainCallbackInfo> callbackMap = new HashMap<>();
    
    private static final TwainConstantMapper<JNIVersion> s_map = new TwainConstantMapper<>(JNIVersion.class);

    public TwainSession() throws DTwainJavaAPIException 
    {
        postInitSession();
        if ( twainCharacteristics.isAutoStartSession() )
        try
        {
            start();
        }
        catch (StreamCorruptedException e)
        {
            System.out.println(e);
        }
    }

    public TwainSession(TwainCharacteristics tc) throws DTwainJavaAPIException
    {
        twainCharacteristics = tc;
        postInitSession();
        secondaryInit();
        if ( tc.isAutoStartSession() )
        {
            try
            {
                start();
            }
            catch (StreamCorruptedException e)
            {
                System.out.println(e);
            }
        }
    }

    public TwainSession(SessionStartupMode sMode) throws DTwainJavaAPIException
    {
        this.twainCharacteristics.setAutoStartSession(sMode != SessionStartupMode.NONE);
        postInitSession();
        secondaryInit();
    }

    private void postInitSession() throws DTwainJavaAPIException
    {
       secondaryInit();
    }

    private void secondaryInit() throws DTwainJavaAPIException
    {
    }

    public static int getJNIVersionAsInt(String sVersion) throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException //throws IllegalArgumentException, IllegalAccessException, DTwainJavaAPIException
    {
        if ( sVersion == null || sVersion.isEmpty() )
            return s_map.toInt("jni_32u");
        return s_map.toInt(sVersion);
    }

    public static String getJNIVersionAsString(int version) throws IllegalArgumentException, IllegalAccessException
    {
        return s_map.toString(version);
    }

    public static TwainLogger getLogger()
    {
        return s_TwainLogger;
    }

    public TwainSession enableLogging(boolean enableLogging)
    {
        this.enableLogging = enableLogging;
        return this;
    }
    
    private void enableTripletsInAPI(boolean enableTriplet) throws DTwainJavaAPIException
    {
        if ( this.dtwainAPI != null)
            this.dtwainAPI.DTWAIN_EnableTripletsNotify(enableTriplet?1:0);
    }
    
    public TwainSession enableTripletNotifications(boolean enableTriplet) throws DTwainJavaAPIException
    {
        this.enableTripletNotification = enableTriplet;
        enableTripletsInAPI(enableTriplet);
        return this;
    }
    
    public boolean isEnableTripletNotifications()
    {
        return this.enableTripletNotification;
    }
    
    public TwainCallback registerCallback(TwainSource source, TwainCallback callback)
    {
        if ( !this.callbackMap.containsKey(source))
        {
            UUID newId = UUID.randomUUID();
            callback.setTwainSource(source);
            callback.setTwainSession(this);
            TwainCallbackInfo newInfo = new TwainCallbackInfo();
            newInfo.callbackId = newId;
            newInfo.twainSource = source;
            newInfo.twainCallback = callback;
            callback.activate();
            this.callbackMap.put(source, newInfo);
        }
        else
            this.callbackMap.get(source).twainCallback = callback;
        return callback;
    }

    public boolean isLoggingEnabled()
    {
        return this.enableLogging;
    }

    public void start() throws DTwainJavaAPIException, StreamCorruptedException 
    {
        if (started)
            return;
        if ( dtwainAPI == null )
        {
            twainSourceCache.clear();
            dtwainAPI = new DTwainJavaAPI(DTwainGlobalOptions.getJNIVersion());
            dtwainAPI.DTWAIN_JavaSysInitialize(twainCharacteristics);
            if ( dtwainAPI != null )
                twainSessionId = dtwainAPI.DTWAIN_GetTwainAppID();
            this.dtwainVersionInfo = dtwainAPI.DTWAIN_GetVersionInfo();
            if ( isLoggingEnabled() )
                startLogging();
            long [] allSources = dtwainAPI.DTWAIN_EnumSources();
            for (int i = 0; i < allSources.length; ++i)
            {
                TwainSourceInfo info = dtwainAPI.DTWAIN_GetSourceInfo(allSources[i]);
                twainSourceCache.add(info);
            }
            dsmPath = dtwainAPI.DTWAIN_GetActiveDSMPath();
            dtwainAPI.DTWAIN_EnableMsgNotify(1);

            singlePageTypes.clear();
            multiPageTypes.clear();
            singlepageFileTypeInfoMap.clear();
            multipageFileTypeInfoMap.clear();

            int [] fileTypes = dtwainAPI.DTWAIN_EnumSupportedSinglePageFileTypes();
            for (int i : fileTypes)
                singlePageTypes.add(i);

            fileTypes = dtwainAPI.DTWAIN_EnumSupportedMultiPageFileTypes();
            for (int i : fileTypes)
                multiPageTypes.add(i);
            started = true;

            for ( Integer i : singlePageTypes)
            {
                String sExt = dtwainAPI.DTWAIN_GetFileTypeExtension(i);
                String [] all = sExt.split("\\|");
                List<String> allList = Arrays.asList(all);
                SupportedFileTypeInfo tempInfo = new SupportedFileTypeInfo();
                tempInfo.setName(dtwainAPI.DTWAIN_GetFileTypeName(i)).setType(FileType.from(i));
                for (String s : allList)
                    tempInfo.addExtension(s);
                singlepageFileTypeInfoMap.put(i,  tempInfo);
            }
            for ( Integer i : multiPageTypes)
            {
                String sExt = dtwainAPI.DTWAIN_GetFileTypeExtension(i);
                String [] all = sExt.split("\\|");
                List<String> allList = Arrays.asList(all);
                SupportedFileTypeInfo tempInfo = new SupportedFileTypeInfo();
                tempInfo.setName(dtwainAPI.DTWAIN_GetFileTypeName(i)).setType(FileType.from(i));
                for (String s : allList)
                    tempInfo.addExtension(s);
                multipageFileTypeInfoMap.put(i,  tempInfo);
            }
        }
    }

    public boolean isStarted()
    {
        return started;
    }

    public void stop() throws DTwainJavaAPIException
    {
        if (started)
        {
            if (dtwainAPI != null)
            {
                try
                {
                    while ( dtwainAPI.DTWAIN_IsAcquiring())
                    {
                        TimeUnit.MILLISECONDS.sleep(1);
                    }
                }
                catch (InterruptedException e)
                {
                    throw new DTwainJavaAPIException(e.getMessage());
                }
                dtwainAPI.DTWAIN_SysDestroy();
                s_TwainLogger.setInterface(null);
                started = false;
                dtwainAPI = null;
            }
        }
    }

    public List<TwainSourceInfo> getAllSourceInfo()
    {
        return this.twainSourceCache;
    }

    private TwainSource selectSourceHandler(long sourceHandle) throws DTwainJavaAPIException
    {
        if ( sourceHandle != 0 )
        {
            TwainSource returnedSource = new TwainSource();
            returnedSource.setSourceHandle(sourceHandle);
            returnedSource.setCreated(true);
            returnedSource.setTwainSession(this);
            returnedSource.reloadInfo();
            handleToSourceMap.put(sourceHandle, returnedSource);
            return returnedSource;
        }
        int lastError = dtwainAPI.DTWAIN_GetLastError();
        TwainSource returnedSource = new TwainSource();
        returnedSource.setSourceHandle(0);
        returnedSource.setCreated(false);
        returnedSource.setTwainSession(this);
        returnedSource.setLastError(lastError);
        return returnedSource;
    }

    public TwainSource selectSource() throws DTwainJavaAPIException 
    {
        if ( !isStarted() )
        {
            try
            {
                start();
            }
            catch (StreamCorruptedException e)
            {
                System.out.println(e);
                return null;
            }
        }
        if ( dtwainAPI != null )
        {
            long sourceHandle = TwainSourceSelector.selectSource(dtwainAPI, new TwainSourceDialog().enableEnhancedDialog(false));
            return selectSourceHandler(sourceHandle);
        }
        return null;
    }

    public TwainSource selectSource(TwainSourceDialog sourceSelectorOptions) throws DTwainJavaAPIException 
    {
        if ( !isStarted() )
        {
            try
            {
                start();
            }
            catch (StreamCorruptedException e)
            {
                System.out.println(e);
                return null;
            }
        }
        if ( dtwainAPI != null)
        {
            long sourceHandle = TwainSourceSelector.selectSource(dtwainAPI, sourceSelectorOptions);
            return selectSourceHandler(sourceHandle);
        }
        return new TwainSource();
    }

    public TwainSource selectSource(String sourceName) throws DTwainJavaAPIException
    {
        if ( !isStarted() )
        {
            try
            {
                start();
            }
            catch (StreamCorruptedException e)
            {
                System.out.println(e);
                return null;
            }
        }
        if ( dtwainAPI != null)
        {
            long sourceHandle = TwainSourceSelector.selectSource(dtwainAPI,
                    new TwainSourceDialog().enableSelectUsingSourceName(true,sourceName));
            return selectSourceHandler(sourceHandle);
        }
        return new TwainSource();
    }

    public TwainSource selectDefaultSource() throws DTwainJavaAPIException 
    {
        if ( !isStarted() )
        {
            try
            {
                start();
            }
            catch (StreamCorruptedException e)
            {
                System.out.println(e);
                return null;
            }
        }
        if ( dtwainAPI != null)
        {
            long sourceHandle = TwainSourceSelector.selectSource(dtwainAPI, 0);
            return selectSourceHandler(sourceHandle);
        }
        return new TwainSource();
    }

    public String getDSMPath()
    {
        return dsmPath;
    }

    public TW_IDENTITY getSessionId()
    {
        return this.twainSessionId;
    }

    public TwainCharacteristics getTwainCharacteristics()
    {
        return this.twainCharacteristics;
    }

    public DTwainJavaAPI getAPIHandle()
    {
        return this.dtwainAPI;
    }

    public int getLastError() throws DTwainJavaAPIException
    {
        if ( dtwainAPI != null )
            return dtwainAPI.DTWAIN_GetLastError();
        return 0;
    }

    public String getErrorString(int errorNum) throws DTwainJavaAPIException
    {
        if ( dtwainAPI != null )
            return dtwainAPI.DTWAIN_GetErrorString(errorNum);
        return "";
    }

    private void setupLogging() throws DTwainJavaAPIException
    {
        s_TwainLogger.setInterface(dtwainAPI);
    }

    public void startLogging() throws DTwainJavaAPIException
    {
        if (!s_TwainLogger.isActivated())
        {
            s_TwainLogger.activate(true);
            setupLogging();
            s_TwainLogger.startLogger();
        }
    }

    public void stopLogging() throws DTwainJavaAPIException
    {
        if ( s_TwainLogger.isActivated())
        {
            s_TwainLogger.stopLogger();
            if ( this.started )
                dtwainAPI.DTWAIN_SetTwainLog(0, "");
        }
    }

    public String getShortVersionName()
    {
        return this.dtwainVersionInfo.getShortVersionName();
    }

    public String getLongVersionName()
    {
        return this.dtwainVersionInfo.getLongVersionName();
    }

    public String getDTwainPath()
    {
        return this.dtwainVersionInfo.getExecutionPath();
    }

    public String getVersionCopyright()
    {
        return this.dtwainVersionInfo.getVersionCopyright();
    }
    
    public static TwainSource getTwainSourceFromHandle(long sourceHandle)
    {
        if (handleToSourceMap.containsKey(sourceHandle))
            return handleToSourceMap.get(sourceHandle);
        return null;
    }

    public static boolean removeTwainSource(long sourceHandle)
    {
        TwainSource source = getTwainSourceFromHandle(sourceHandle);
        if ( source != null )
        {
            handleToSourceMap.remove(sourceHandle);
            return true;
        }
        return false;
    }

    public boolean setLanguageResource(String language) throws DTwainJavaAPIException
    {
        String sCurrentLanguage = twainCharacteristics.getResourceLanguage();
        twainCharacteristics.setResourceLanguage(language);
        if ( dtwainAPI != null)
        {
            if (dtwainAPI.DTWAIN_LoadCustomStringResource(language) == 0)
            {
                twainCharacteristics.setResourceLanguage(sCurrentLanguage);
                return false;
            }
        }
        return true;
    }

    public TwainSession setAppInfo(TwainAppInfo info)
    {
        this.twainCharacteristics.setAppInfo(info);
        return this;
    }

    public List<Integer> getSinglePageFileTypes()
    {
        return new ArrayList<>(this.singlePageTypes);
    }

    public List<Integer> getMultiPageFileTypes()
    {
        return new ArrayList<>(this.multiPageTypes);
    }

    public String getPageFileTypeName(int fileType)
    {
        if ( dtwainAPI != null )
            return dtwainAPI.DTWAIN_GetFileTypeName(fileType);
        return "";
    }

    public List<String> getPageFileTypeExtensions(int fileType)
    {
        if ( dtwainAPI != null )
        {
            if ( this.singlepageFileTypeInfoMap.containsKey(fileType))
            {
                return new ArrayList<>(singlepageFileTypeInfoMap.get(fileType).getExtensions());
            }
            else if (this.multipageFileTypeInfoMap.containsKey(fileType))
            {
                return new ArrayList<>(multipageFileTypeInfoMap.get(fileType).getExtensions());
            }
        }
        return new ArrayList<>();
    }

    public SupportedFileTypeInfo getSupportedFileTypeInfo(int fileType)
    {
        if ( dtwainAPI != null)
        {
            if ( this.singlepageFileTypeInfoMap.containsKey(fileType))
                return this.singlepageFileTypeInfoMap.get(fileType);
            else if (this.multipageFileTypeInfoMap.containsKey(fileType))
                return this.multipageFileTypeInfoMap.get(fileType);
        }
        return null;
    }

    public TwainSession setTemporaryDirectory(String sDir) throws DTwainJavaAPIException
    {
        if ( dtwainAPI != null && started)
        {
            int val = dtwainAPI.DTWAIN_SetTempFileDirectory(sDir);
            if ( val != 0 )
                this.twainCharacteristics.setTemporaryDirectory(sDir);
        }
        else
            this.twainCharacteristics.setTemporaryDirectory(sDir);
        return this;
    }
    
    public String getSessionDetails(int indentValue, boolean bRefresh)
    {
        if ( dtwainAPI != null)
        {
            return dtwainAPI.DTWAIN_GetSessionDetails(indentValue, bRefresh);
        }
        return "";
    }
    
    public String getSourceDetails(String sources, int indentValue, boolean bRefresh)
    {
        if ( dtwainAPI != null)
            return dtwainAPI.DTWAIN_GetSourceDetails(sources, indentValue, bRefresh);
        return "";
    }
    
    public TwainSession setDSM(DSMType dsm)
    {
        this.twainCharacteristics.setDSM(dsm);
        return this;
    }
    
    public TwainAppInfo getAppInfo()
    {
        return this.twainCharacteristics.getAppInfo();
    }
    
    private static void logEvent(String logMsg)
    {
        if ( s_TwainLogger.hasProcs() )
            s_TwainLogger.logMessage(logMsg);
    }
    
}
