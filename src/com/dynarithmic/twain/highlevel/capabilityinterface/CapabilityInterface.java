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
package com.dynarithmic.twain.highlevel.capabilityinterface;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dynarithmic.twain.DTwainConstants;
import com.dynarithmic.twain.DTwainConstants.*;
import com.dynarithmic.twain.exceptions.DTwainJavaAPIException;
import com.dynarithmic.twain.DTwainJavaAPI;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.lowlevel.TW_FIX32;
import com.dynarithmic.twain.lowlevel.TwainConstants;
import com.dynarithmic.twain.lowlevel.TwainConstants.*;
import com.dynarithmic.twain.lowlevel.TwainObjectUtils;
import com.dynarithmic.twain.highlevel.TwainCapabilityHandler;
import com.dynarithmic.twain.highlevel.TwainFrame;
import com.dynarithmic.twain.highlevel.TwainFrameDouble;
import com.dynarithmic.twain.highlevel.TwainRange;
import com.dynarithmic.twain.highlevel.TwainRangeException;
import com.dynarithmic.twain.highlevel.TwainRangeUtils;

public class CapabilityInterface
{
    public class TwainCapInfo
    {
        private String name;
        private int supportedOps;
        private int dataType;
        private byte[] containerType = new byte[] {-1,-1,-1,-1,-1,-1,-1};

        public TwainCapInfo()
        {
            name = "";
            supportedOps = 0;
            dataType = TWTY.TWTY_INT16;
        }

        public TwainCapInfo(String name, int supportedOps, int dataType)
        {
            this.name = name;
            this.supportedOps = supportedOps;
            this.dataType = dataType;
        }

        public TwainCapInfo(TwainCapInfo rhs)
        {
            this.name = rhs.name;
            this.supportedOps = rhs.supportedOps;
            this.dataType = rhs.dataType;
        }
    }

    public class CapReturnType
    {
        private boolean returnValue = true;
        private int errorCode = 0;
        private int attemptedCap = 0;
        private SetCapOperation attemptedSetOp = null;
        private GetCapOperation attemptedGetOp = null;

        public CapReturnType()
        {
            this.returnValue = true;
            this.errorCode = ErrorCode.ERROR_NONE.value();
            this.attemptedCap = 0;
        }

        public CapReturnType(CapReturnType rhs)
        {
            this.returnValue = rhs.returnValue;
            this.errorCode = rhs.errorCode;
            this.attemptedCap = rhs.attemptedCap;
            if ( rhs.attemptedSetOp != null)
                this.attemptedSetOp = new SetCapOperation(rhs.attemptedSetOp);
            else
                this.attemptedSetOp = null;
            if ( rhs.attemptedGetOp != null)
                this.attemptedGetOp = new GetCapOperation(rhs.attemptedGetOp);
            else
                this.attemptedGetOp = null;
        }

        public CapReturnType(int attemptedCap,
                             GetCapOperation attemptedGetOp,
                             SetCapOperation attemptedSetOp,
                             boolean returnValue,
                             ErrorCode errorCode)
        {
            this(attemptedCap, attemptedGetOp, attemptedSetOp, returnValue, errorCode.value());
        }

        public CapReturnType(int attemptedCap,
                             GetCapOperation attemptedGetOp,
                             SetCapOperation attemptedSetOp,
                             boolean returnValue,
                             int errorCode)
        {
            this.attemptedCap = attemptedCap;
            if ( attemptedGetOp != null )
                this.attemptedGetOp = new GetCapOperation(attemptedGetOp);
            if ( attemptedSetOp != null)
                this.attemptedSetOp = new SetCapOperation(attemptedSetOp);
            this.returnValue = returnValue;
            this.errorCode = errorCode;
        }

        public CapReturnType setErrorCode(ErrorCode errorCode)
        {
            this.errorCode = errorCode.value();
            return this;
        }

        public CapReturnType setReturn(boolean bSet)
        {
            this.returnValue = bSet;
            return this;
        }

        public CapReturnType setAttemptedSetOp(SetCapOperation op)
        {
            this.attemptedSetOp = new SetCapOperation(op);
            return this;
        }

        public CapReturnType setAttemptedGetOp(GetCapOperation op)
        {
            this.attemptedGetOp = new GetCapOperation(op);
            return this;
        }

        public CapReturnType setAttemptedCap(int cap)
        {
            this.attemptedCap = cap;
            return this;
        }

        public SetCapOperation getAttemptedOp()
        {
            return this.attemptedSetOp;
        }

        public int getAttemptedCap()
        {
            return this.attemptedCap;
        }

        public boolean getReturn()
        {
            return this.returnValue;
        }

        public int getErrorCode()
        {
            return errorCode;
        }

        public void setErrorCode(int lastError)
        {
            this.errorCode = lastError;
        }
    }

    public class GetCapOperation
    {
        private int getOperationType = MSG.MSG_GET;
        private int containerType = DTwainConstants.ContainerType.DEFAULT.value();
        private int dataType = TWTY.TWTY_DEFAULT;
        private boolean expandRangeEnabled = false;

        public GetCapOperation()
        {
            this.getOperationType = MSG.MSG_GET;
            this.containerType = DTwainConstants.ContainerType.DEFAULT.value();
            this.dataType = TWTY.TWTY_DEFAULT;
            this.expandRangeEnabled = false;
        }

        public GetCapOperation(GetCapOperation rhs)
        {
            this.getOperationType = rhs.getOperationType;
            this.containerType = rhs.containerType;
            this.dataType = rhs.dataType;
            this.expandRangeEnabled = rhs.expandRangeEnabled;
        }

        public GetCapOperation setOperation(int getOperationType)
        {
            this.getOperationType = getOperationType;
            return this;
        }

        public GetCapOperation enableExpandRange(boolean expandRangeEnabled)
        {
            this.expandRangeEnabled = expandRangeEnabled;
            return this;
        }

        public GetCapOperation setContainerType(int containerType)
        {
            this.containerType = containerType;
            return this;
        }

        public GetCapOperation setDataType(int dataType)
        {
            this.dataType = dataType;
            return this;
        }

        public int getOperation()
        {
            return this.getOperationType;
        }

        public int getContainerType()
        {
            return this.containerType;
        }

        public boolean isExpandRangeEnabled()
        {
            return this.expandRangeEnabled;
        }

        public int getDataType()
        {
            return this.dataType;
        }
    }

    public class SetCapOperation
    {
        private int setOperationType = MSG.MSG_SET;
        private int containerType = TWON.TWON_DEFAULT;
        private int dataType = TWTY.TWTY_DEFAULT;

        public SetCapOperation(SetCapOperation rhs)
        {
            this.setOperationType = rhs.setOperationType;
            this.containerType = rhs.containerType;
            this.dataType = rhs.dataType;
        }

        public SetCapOperation()
        {
            this.setOperationType = MSG.MSG_SET;
            this.containerType = TWON.TWON_DEFAULT;
            this.dataType = TWTY.TWTY_DEFAULT;
        }

        public SetCapOperation setOperation(int setOperationType)
        {
            this.setOperationType = setOperationType;
            return this;
        }

        public SetCapOperation setContainerType(int containerType)
        {
            this.containerType = containerType;
            return this;
        }

        public SetCapOperation setDataType(int dataType)
        {
            this.dataType = dataType;
            return this;
        }

        public int getOperation()
        {
            return this.setOperationType;
        }

        public int getContainerType()
        {
            return this.containerType;
        }

        public int getDataType()
        {
            return this.dataType;
        }
    }

    private Map<Integer, TwainCapInfo> capMap = new HashMap<>();
    private Map<Integer, TwainCapInfo> customCapMap = new HashMap<>();
    private Map<Integer, TwainCapInfo> extendedCapMap= new HashMap<>();
    private Map<Integer, TwainCapInfo> extendedImageCapsMap = new HashMap<>();
    private Map<Integer, List<Object>> capCacheMap = new HashMap<>();
    private Set<Integer> capCacheSet = new HashSet<>();
    private Deque<CapReturnType> capReturnTypeDeque = new ArrayDeque<>();
    private CapReturnType capReturnType = new CapReturnType();
    private TwainSource twainSource = null;
    private DTwainJavaAPI apiHandle = null;
    private int maxErrorBufferSize = 100;

    public CapabilityInterface setMaxErrorBufferSize(int sz)
    {
        if ( sz < maxErrorBufferSize )
        {
            while (capReturnTypeDeque.size() > sz)
                capReturnTypeDeque.removeLast();
        }
        this.maxErrorBufferSize = sz;
        return this;
    }

    public int getMaxErrorBufferSize()
    {
        return this.maxErrorBufferSize;
    }

    public CapReturnType getLastCapError()
    {
        if ( !this.capReturnTypeDeque.isEmpty())
            return this.capReturnTypeDeque.getFirst();
        return new CapReturnType();
    }

    public List<CapReturnType> getAllCapErrors()
    {
        List<CapReturnType> capList = new ArrayList<>();
        for (CapReturnType cr : this.capReturnTypeDeque)
            capList.add(new CapReturnType(cr));
        return capList;
    }

    public GetCapOperation get()
    {
        return new GetCapOperation().setOperation(MSG.MSG_GET);
    }

    public GetCapOperation getCurrent()
    {
        return new GetCapOperation().setOperation(MSG.MSG_GETCURRENT);
    }

    public GetCapOperation getDefault()
    {
        return new GetCapOperation().setOperation(MSG.MSG_GETDEFAULT);
    }

    public GetCapOperation getHelp()
    {
        return new GetCapOperation().setOperation(MSG.MSG_GETHELP);
    }

    public GetCapOperation getLabelEnum()
    {
        return new GetCapOperation().setOperation(MSG.MSG_GETLABELENUM);
    }

    public SetCapOperation set()
    {
        return new SetCapOperation().setOperation(MSG.MSG_SET);
    }

    public SetCapOperation reset()
    {
        return new SetCapOperation().setOperation(MSG.MSG_RESET);
    }

    public SetCapOperation resetAll()
    {
        return new SetCapOperation().setOperation(MSG.MSG_RESETALL);
    }

    public SetCapOperation setConstraintAll()
    {
        return new SetCapOperation().setOperation(MSG.MSG_SETCONSTRAINT);
    }

    public static boolean isIntegralType(Object obj)
    {
        return (obj instanceof Integer) || (obj instanceof Boolean) || (obj instanceof Byte);
    }

    public static boolean isStringType(Object obj)
    {
        return (obj instanceof String);
    }

    public static boolean isFix32Type(Object obj)
    {
        return (obj instanceof Double) || (obj instanceof TW_FIX32);
    }

    public static boolean isFrameType(Object obj)
    {
        return (obj instanceof TwainFrame);
    }

    private void initializeCachedSet()
    {
        Set<Integer> excludedSet = new HashSet<>();
        excludedSet.add(CAPS.CAP_DEVICEONLINE);
        excludedSet.add(CAPS.CAP_DUPLEXENABLED);
        excludedSet.add(CAPS.CAP_ENABLEDSUIONLY);
        excludedSet.add(CAPS.ICAP_BITDEPTH);
        excludedSet.add(CAPS.ICAP_FRAMES);
        excludedSet.add(CAPS.ICAP_XRESOLUTION);
        excludedSet.add(CAPS.ICAP_YRESOLUTION);
        for ( Integer i : excludedSet)
            this.capCacheSet.remove(i);
    }

    private boolean isTwainObject(Object obj)
    {
        return obj instanceof Integer ||
               obj instanceof Double || 
               obj instanceof String ||
               obj instanceof Boolean ||
               obj instanceof TwainFrameDouble;
    }
    
    private void copyToCache(List<Object> lObjects, int capValue)
    {
        List<Object> newList = new ArrayList<>();
        for (Object obj : lObjects)
        {
            if ( isTwainObject(obj) )
                newList.add(obj);
        }
        this.capCacheMap.put(capValue, newList);
    }

    private void copyFromCache(List<Object> lObjects, int capValue)
    {
        lObjects.clear();
        if ( this.capCacheMap.containsKey(capValue))
        {
            List<Object> objectList = this.capCacheMap.get(capValue);
            for (Object obj : objectList)
            {
                if ( isTwainObject(obj) )
                    lObjects.add(obj);
            }
        }
    }

    private CapReturnType addReturnInfo(CapReturnType cr)
    {
        if ( this.capReturnTypeDeque.size() == this.maxErrorBufferSize)
            this.capReturnTypeDeque.removeLast();
        this.capReturnTypeDeque.addFirst(cr);
        return cr;
    }

    private CapReturnType getCapValues(List<Object> lObjects, int cap, GetCapOperation gcType) throws DTwainJavaAPIException
    {
        if ( twainSource == null )
            return addReturnInfo(new CapReturnType(cap, gcType, null, false, DTwainConstants.ErrorCode.ERROR_BAD_SOURCE));
        if (!capMap.isEmpty() && !capMap.containsKey(cap))
            return addReturnInfo(new CapReturnType(cap, gcType, null, false, DTwainConstants.ErrorCode.ERROR_CAP_NO_SUPPORT));

        boolean isCacheFriendly = (gcType.getOperation() == MSG.MSG_GET) &&
                                  this.capCacheSet.contains(cap);

        if (isCacheFriendly && this.capCacheMap.containsKey(cap))
        {
            lObjects.clear();
            copyFromCache(lObjects, cap);
            return addReturnInfo(new CapReturnType(cap, gcType, null, true, DTwainConstants.ErrorCode.ERROR_NONE));
        }

        int getToUse = gcType.getOperation();
        int containerType = gcType.getContainerType();
        int dataType = gcType.getDataType();

        List<Object> lValues = new ArrayList<>();
        boolean retVal = apiHandle.DTWAIN_GetCapValuesEx2(twainSource.getSourceHandle(),
                                                   cap,
                                                   getToUse,
                                                   containerType,
                                                   dataType,
                                                   lValues);
        if ( !retVal )
        {
            int lastError = apiHandle.DTWAIN_GetLastError();
            return addReturnInfo(new CapReturnType(cap, gcType, null, false, lastError));
        }

        copyToCache(lValues, cap);
        copyFromCache(lObjects, cap);
        return addReturnInfo(new CapReturnType(cap, gcType, null, true, DTwainConstants.ErrorCode.ERROR_NONE));
    }

    public List<Object> getCapValues(int cap, GetCapOperation gcType) throws DTwainJavaAPIException
    {
        if ( gcType == null )
            throw new DTwainJavaAPIException("Capability get type cannot be null");
        List<Object> ret = new ArrayList<>();
        this.capReturnType = getCapValues(ret, cap, gcType);
        if ( gcType.isExpandRangeEnabled() )
        {
            int cType = getCapContainerType(cap, gcType);
            if ( cType == DTwainConstants.ContainerType.RANGE.value() && TwainRangeUtils.isValidRange(ret))
            {
                List<Object> ret2 = new ArrayList<>();
                if ( this.getCapDataType(cap) == TWTY.TWTY_FIX32)
                {
                    Double [] dList = ret.toArray(new Double[ret.size()]);
                    List<Double> vals = new TwainRange<Double>(Arrays.asList(dList)).expand();
                    for (int i = 0; i < vals.size(); ++i)
                        ret2.add(vals.get(i));
                    return ret2;
                }
                else
                {
                    Integer [] iList = ret.toArray(new Integer[ret.size()]);
                    List<Integer> vals = new TwainRange<Integer>(Arrays.asList(iList)).expand();
                    for (int i = 0; i < vals.size(); ++i)
                        ret2.add(vals.get(i));
                    return ret2;
                }
            }
        }
        return ret;
    }

    public <T> CapReturnType setCapValues(List<T> values, int cap, SetCapOperation scType) throws DTwainJavaAPIException
    {
        if ( twainSource == null )
            return addReturnInfo(new CapReturnType(cap, null, scType, false, DTwainConstants.ErrorCode.ERROR_BAD_SOURCE));
        if (!capMap.isEmpty() && !capMap.containsKey(cap))
            return addReturnInfo(new CapReturnType(cap, null, scType, false, DTwainConstants.ErrorCode.ERROR_CAP_NO_SUPPORT));
        if ( scType == null )
            throw new DTwainJavaAPIException("SetCapOperation cannot be null");
        int setOpToUse = scType.getOperation();
        List<T> valuesToUse = values;
        if(values == null || values.isEmpty())
        {
            setOpToUse = MSG.MSG_RESET;
            if (values == null )
                valuesToUse = new ArrayList<>();
        }

        @SuppressWarnings("unchecked")
        int retValue = apiHandle.DTWAIN_SetCapValues(twainSource.getSourceHandle(),cap,setOpToUse, (List<Object>) valuesToUse);
        int lastError = DTwainConstants.ErrorCode.ERROR_NONE.value();
        if (retValue == 0)
            lastError = apiHandle.DTWAIN_GetLastError();
        this.capReturnType.setReturn(retValue == 1);
        this.capReturnType.setErrorCode(lastError);
        return addReturnInfo(new CapReturnType(cap, null, scType, capReturnType.getReturn(), capReturnType.getErrorCode()));
    }

    public int getLastError()
    {
        return this.capReturnType.getErrorCode();
    }

    private boolean fillCaps() throws DTwainJavaAPIException
    {
        this.capMap.clear();
        this.customCapMap.clear();
        this.capCacheSet.clear();
        this.extendedCapMap.clear();
        this.extendedImageCapsMap.clear();
        apiHandle = this.twainSource.getTwainSession().getAPIHandle();
        List<Integer> allCaps = this.getSupportedCaps(get());
        for (Integer cap : allCaps)
        {
            String capName = apiHandle.DTWAIN_GetNameFromCap(cap);
            int theOpts = apiHandle.DTWAIN_GetCapOperations(twainSource.getSourceHandle(), cap);
            int theType = apiHandle.DTWAIN_GetCapDataType(twainSource.getSourceHandle(),  cap);
            if ( theOpts != 0 )
                this.capMap.put(cap,  new TwainCapInfo(capName, theOpts, theType));
            else
                this.capMap.put(cap, new TwainCapInfo(capName, -1, theType));
            this.capCacheSet.add(cap);
        }
        this.initializeCachedSet();

        // Get the custom caps
        for (Map.Entry<Integer, TwainCapInfo> entry : this.capMap.entrySet())
        {
            if (entry.getKey() >= TwainConstants.CAPS.CAP_CUSTOMBASE)
                this.customCapMap.put(entry.getKey(), new TwainCapInfo(entry.getValue()));
        }

        // Get the extended caps
        List<Integer> extCaps = this.getExtendedCaps(get());
        Set<Integer> result = allCaps.stream()
                  .distinct()
                  .filter(extCaps::contains)
                  .collect(Collectors.toSet());
        for ( Integer val : result )
        {
            if ( this.capMap.containsKey(val))
                this.extendedCapMap.put(val, new TwainCapInfo(this.capMap.get(val)));
        }

        // Get the extendimgeinfo caps
        List<Integer> extInfo = this.getSupportedExtImageInfo(get());
        for (Integer val : extInfo )
        {
            String name = apiHandle.DTWAIN_GetNameFromCap(val + 1000);
            int theType = apiHandle.DTWAIN_GetCapDataType(twainSource.getSourceHandle(), val + 1000);
            this.extendedImageCapsMap.put(val, new TwainCapInfo(name, TwainConstants.MSG.MSG_GET, theType));
        }
        return this.capMap.size() > 0;
    }

    public int getNumCaps()
    {
        return this.capMap.size();
    }

    public int getNumCustomCaps()
    {
        return this.customCapMap.size();
    }

    public int getNumExtendedCaps()
    {
        return this.extendedCapMap.size();
    }

    public int getNumExtendedImageCaps()
    {
        return this.extendedImageCapsMap.size();
    }

    public List<Integer> getCaps()
    {
        List<Integer> ret = new ArrayList<>();
        for (Map.Entry<Integer, TwainCapInfo> entry : this.capMap.entrySet())
            ret.add(entry.getKey());
        return ret;
    }

    public int getCapDataType(int capValue)
    {
        if (capMap.containsKey(capValue))
            return capMap.get(capValue).dataType;
        return -1;
    }

    public List<Integer> getCapOperations(int capValue)
    {
        int [] allops =
        {
            TwainConstants.TWQC.TWQC_GET              ,
            TwainConstants.TWQC.TWQC_SET              ,
            TwainConstants.TWQC.TWQC_GETDEFAULT       ,
            TwainConstants.TWQC.TWQC_GETCURRENT       ,
            TwainConstants.TWQC.TWQC_RESET            ,
            TwainConstants.TWQC.TWQC_SETCONSTRAINT    ,
            TwainConstants.TWQC.TWQC_GETHELP          ,
            TwainConstants.TWQC.TWQC_GETLABEL         ,
            TwainConstants.TWQC.TWQC_GETLABELENUM     ,
            TwainConstants.TWQC.TWQC_CONSTRAINABLE
        };
        List<Integer> ret = new ArrayList<>();
        if (this.capMap.containsKey(capValue))
        {
            TwainCapInfo capInfo = this.capMap.get(capValue);
            for( int op : allops)
            {
                if ((capInfo.supportedOps & op) > 0)
                    ret.add(op);
            }
        }
        return ret;
    }

    public boolean isCapSupported(int capValue)
    {
        return this.capMap.containsKey(capValue);
    }

    private static <T> boolean equalityComparer(List<T> objList, T value)
    {
        for (Object obj : objList)
        {
            if (obj instanceof Integer)
            {
                if (((Integer)obj).intValue() == ((Integer)value).intValue())
                    return true;
            }
            else
            if (obj instanceof Double)
            {
                if (((Double)obj).doubleValue() == ((Double)value).doubleValue())
                    return true;
            }
            else
            if (obj instanceof String)
            {
                String val1 = (String)obj;
                String val2 = (String)value;
                if ( val1.compareTo(val2) == 0)
                    return true;
            }
        }
        return false;
    }

    private <T> CapReturnType isCapValueSupportedImpl(T testValue, int capToTest) throws DTwainJavaAPIException
    {
        if (twainSource == null)
            return addReturnInfo(new CapReturnType(capToTest, null, null, false, DTwainConstants.ErrorCode.ERROR_BAD_SOURCE));
        if ( !this.capMap.isEmpty() && !capMap.containsKey(capToTest))
            return addReturnInfo(new CapReturnType(capToTest, null, null, false, DTwainConstants.ErrorCode.ERROR_CAP_NO_SUPPORT));

        boolean inCapCache = false;
        List<Object> vList = null;

        boolean isCache = this.capCacheSet.contains(capToTest);
        if (isCache)
        {
            inCapCache = this.capCacheMap.containsKey(capToTest);
            if ( inCapCache )
                vList = this.capCacheMap.get(capToTest);
        }

        if ( !isCache || !inCapCache )
            vList = getCapValues(capToTest, get());
        int cType = getCapContainerType(capToTest, get());
        if ( cType != DTwainConstants.ContainerType.RANGE.value())
        {
            if ( equalityComparer(vList, testValue))
                return addReturnInfo(new CapReturnType(capToTest, null, null, true, 1));
            return addReturnInfo(new CapReturnType(capToTest, null, null, false, DTwainConstants.ErrorCode.ERROR_CAP_NO_SUPPORT));
        }
        else
        {
            boolean bFound = false;
            if ( this.getCapDataType(capToTest) == TWTY.TWTY_FIX32)
            {
                Double [] dList = vList.toArray(new Double[vList.size()]);
                TwainRange<Double> dRange;
                try
                {
                    dRange = new TwainRange<>(Arrays.asList(dList));
                }
                catch (TwainRangeException e)
                {
                    return addReturnInfo(new CapReturnType(capToTest, null, null, false, DTwainConstants.ErrorCode.ERROR_INVALID_RANGE));
                }
                bFound = dRange.valueExists((Double)testValue);
            }
            else
            {
                Integer [] dList = vList.toArray(new Integer[vList.size()]);
                TwainRange<Integer> dRange;
                try
                {
                    dRange = new TwainRange<>(Arrays.asList(dList));
                }
                catch (TwainRangeException e)
                {
                    return addReturnInfo(new CapReturnType(capToTest, null, null, false, DTwainConstants.ErrorCode.ERROR_INVALID_RANGE));
                }
                bFound = dRange.valueExists((Integer)testValue);
            }
            if ( bFound )
                return addReturnInfo(new CapReturnType(capToTest, null, null, bFound, 1));
        }
        return addReturnInfo(new CapReturnType(capToTest, null, null, true, 0));
    }

    public <T> boolean isCapValueSupported(T testValue, int capToTest) throws DTwainJavaAPIException
    {
        CapReturnType ret = isCapValueSupportedImpl(testValue, capToTest);
        if (!ret.getReturn())
            return false;
        if (ret.getErrorCode() == DTwainConstants.ErrorCode.ERROR_NONE.value())
        {
            List<Object> vect = new ArrayList<>();
            GetCapOperation getOp = get();
            getCapValues(vect, capToTest, getOp);
            ret = isCapValueSupportedImpl(testValue, capToTest);
            return ret.getReturn();
        }
        return ret.getReturn();
    }

    public boolean attach(TwainSource ts) throws DTwainJavaAPIException
    {
        if (ts == null)
            throw new DTwainJavaAPIException("Cannot attach a null TwainSource to CapabilityInterface");
        if (ts == this.twainSource )
            return true;
        detach();
        this.twainSource = ts;
        return fillCaps();
    }

    public void detach()
    {
        this.twainSource = null;
        this.capCacheMap.clear();
        this.capCacheSet.clear();
        this.capMap.clear();
        this.customCapMap.clear();
        this.extendedCapMap.clear();
        this.extendedImageCapsMap.clear();
    }

    public int getCapContainerType(int capValue, GetCapOperation gcType)
    {
        boolean isFound = this.capMap.containsKey(capValue);
        if ( this.capMap.size() > 0 && !isFound )
            return DTwainConstants.ContainerType.INVALID.value();
        int getOp = gcType.getOperation();
        int idx = 0;
        switch (getOp)
        {
            case MSG.MSG_GET:
                idx = 0;
                break;
            case MSG.MSG_GETCURRENT:
                idx = 1;
                break;
            case MSG.MSG_GETDEFAULT:
                idx = 2;
                break;
            default:
                idx = 0;
                break;
        }

        TwainCapInfo capInfo = this.capMap.get(capValue);
        if ( capInfo.containerType[idx] != -1)
            return capInfo.containerType[idx];
        int container_type = apiHandle.DTWAIN_GetCapContainer(twainSource.getSourceHandle(), capValue, getOp);
        capInfo.containerType[idx] = (byte)container_type;
        return container_type;
    }

    public static int getCapFromName(String name) throws DTwainJavaAPIException
    {
        try
        {
            return TwainCapabilityHandler.toInt(name);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            throw new DTwainJavaAPIException(e.getMessage());
        }
    }

    public static String getNameFromCap(int capValue)
    {
        return TwainCapabilityHandler.toString(capValue);
    }

    public CapabilityInterface setAPIHandle(DTwainJavaAPI handle)
    {
        apiHandle = handle;
        return this;
    }

    ////////////////////////////////////////////////////////////
    public List<Integer> getAudioXferMech(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ACAP_XFERMECH, gcType));
    }

    public List<String> getAuthor(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_AUTHOR, gcType));
    }

    public List<Integer> getAlarms(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_ALARMS, gcType));
    }

    public List<Integer> getAlarmVolumes(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_ALARMVOLUME, gcType));
    }

    public List<Boolean> getAutoFeed(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_AUTOFEED, gcType));
    }

    public List<Integer> getAutomaticCapture(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_AUTOMATICCAPTURE, gcType));
    }

    public List<Boolean> getAutomaticSenseMedium(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_AUTOMATICSENSEMEDIUM, gcType));
    }

    public List<Boolean> getAutoscan(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_AUTOSCAN, gcType));
    }

    public List<Integer> getBatteryMinutes(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_BATTERYMINUTES, gcType));
    }

    public List<Integer> getBatteryPercentage(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_BATTERYPERCENTAGE, gcType));
    }

    public List<Boolean> getCameraEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CAMERAENABLED, gcType));
    }

    public List<Integer> getCameraOrder(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CAMERAORDER, gcType));
    }

    public List<Boolean> getCameraPreviewUI(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CAMERAPREVIEWUI, gcType));
    }

    public List<Integer> getCameraSide(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CAMERASIDE, gcType));
    }

    public List<Integer> getClearBuffers(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CLEARBUFFERS, gcType));
    }

    public List<String> getCaption(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CAPTION, gcType));
    }

    public List<Boolean> getClearPage(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CLEARPAGE, gcType));
    }

    public List<Boolean> getCustomDSData(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CUSTOMDSDATA, gcType));
    }

    public List<String> getCustomInterfaceGUID(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_CUSTOMINTERFACEGUID, gcType));
    }

    public List<Integer> getDeviceEvent(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DEVICEEVENT, gcType));
    }

    public List<Boolean> getDeviceOnline(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DEVICEONLINE, gcType));
    }

    public List<String> getDeviceTimeDate(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DEVICETIMEDATE, gcType));
    }

    public List<Integer> getDoublefeedDetection(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DOUBLEFEEDDETECTION, gcType));
    }

    public List<Double> getDoublefeedDetectionLength(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DOUBLEFEEDDETECTIONLENGTH, gcType));
    }

    public List<Integer> getDoublefeedDetectionResponse(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DOUBLEFEEDDETECTIONRESPONSE, gcType));
    }

    public List<Integer> getDoublefeedDetectionSensitivity(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DOUBLEFEEDDETECTIONSENSITIVITY, gcType));
    }

    public List<Integer> getDuplex(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DUPLEX, gcType));
    }

    public List<Boolean> getDuplexEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_DUPLEXENABLED, gcType));
    }

    public List<Boolean> getEnableDSUIOnly(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_ENABLEDSUIONLY, gcType));
    }

    public List<Integer> getEndorser(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_ENDORSER, gcType));
    }

    public List<Integer> getExtendedCaps(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_EXTENDEDCAPS, gcType));
    }

    public List<Integer> getFeederAlignment(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_FEEDERALIGNMENT, gcType));
    }

    public List<Boolean> getFeederEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_FEEDERENABLED, gcType));
    }

    public List<Boolean> getFeederLoaded(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_FEEDERLOADED, gcType));
    }

    public List<Integer> getFeederOrder(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_FEEDERORDER, gcType));
    }

    public List<Integer> getFeederPocket(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_FEEDERPOCKET, gcType));
    }

    public List<Boolean> getFeederPrep(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_FEEDERPREP, gcType));
    }

    public List<Boolean> getFeedPage(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_FEEDPAGE, gcType));
    }

    public List<Boolean> getIndicators(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_INDICATORS, gcType));
    }

    public List<Integer> getIndicatorsMode(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_INDICATORSMODE, gcType));
    }

    public List<Integer> getJobControl(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_JOBCONTROL, gcType));
    }

    public List<Integer> getLanguage(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_LANGUAGE, gcType));
    }

    public List<Integer> getMaxBatchBuffers(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_MAXBATCHBUFFERS, gcType));
    }

    public List<Boolean> getMicrEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_MICRENABLED, gcType));
    }

    public List<Boolean> getPaperDetectable(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PAPERDETECTABLE, gcType));
    }

    public List<Integer> getPaperHandling(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PAPERHANDLING, gcType));
    }

    public List<Integer> getPowerSaveTime(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_POWERSAVETIME, gcType));
    }

    public List<Integer> getPowerSupply(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_POWERSUPPLY, gcType));
    }

    public List<Integer> getPrinter(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTER, gcType));
    }

    public List<Integer> getPrinterCharRotation(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERCHARROTATION, gcType));
    }

    public List<Boolean> getPrinterEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERENABLED, gcType));
    }

    public List<Integer> getPrinterFontStyle(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERFONTSTYLE, gcType));
    }

    public List<Integer> getPrinterIndex(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERINDEX, gcType));
    }

    public List<String> getPrinterIndexLeadChar(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERINDEXLEADCHAR, gcType));
    }

    public List<Integer> getPrinterIndexMaxValue(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERINDEXMAXVALUE, gcType));
    }

    public List<Integer> getPrinterIndexNumDigits(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERINDEXNUMDIGITS, gcType));
    }

    public List<Integer> getPrinterIndexStep(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERINDEXSTEP, gcType));
    }

    public List<Integer> getPrinterIndexTrigger(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERINDEXTRIGGER, gcType));
    }

    public List<Integer> getPrinterMode(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERMODE, gcType));
    }

    public List<String> getPrinterString(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERSTRING, gcType));
    }

    public List<String> getPrinterStringPreview(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERSTRINGPREVIEW, gcType));
    }

    public List<String> getPrinterSuffix(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERSUFFIX, gcType));
    }

    public List<Double> getPrinterVerticalOffset(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_PRINTERVERTICALOFFSET, gcType));
    }

    public List<Boolean> getReacquireAllowed(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_REACQUIREALLOWED, gcType));
    }

    public List<Boolean> getRewindPage(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_REWINDPAGE, gcType));
    }

    public List<Integer> getSegmented(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_SEGMENTED, gcType));
    }

    public List<String> getSerialNumber(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_SERIALNUMBER, gcType));
    }

    public List<Integer> getSheetCount(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_SHEETCOUNT, gcType));
    }

    public List<Integer> getSupportedCaps(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_SUPPORTEDCAPS, gcType));
    }

    public List<Integer> getSupportedCapsSegmentUnique(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_SUPPORTEDCAPSSEGMENTUNIQUE, gcType));
    }

    public List<Integer> getSupportedDats(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_SUPPORTEDDATS, gcType));
    }

    public List<Boolean> getThumbnailsEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_THUMBNAILSENABLED, gcType));
    }

    public List<Integer> getTimeBeforeFirstCapture(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_TIMEBEFOREFIRSTCAPTURE, gcType));
    }

    public List<Integer> getTimeBetweenCaptures(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_TIMEBETWEENCAPTURES, gcType));
    }

    public List<String> getTimeDate(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_TIMEDATE, gcType));
    }

    public List<Boolean> getUiControllable(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_UICONTROLLABLE, gcType));
    }

    public List<Integer> getXferCount(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.CAP_XFERCOUNT, gcType));
    }

    public List<Boolean> getAutoBright(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOBRIGHT, gcType));
    }

    public List<Integer> getAutoDiscardBlankPages(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTODISCARDBLANKPAGES, gcType));
    }

    public List<Boolean> getAutomaticBorderDetection(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOMATICBORDERDETECTION, gcType));
    }

    public List<Boolean> getAutomaticColorEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOMATICCOLORENABLED, gcType));
    }

    public List<Integer> getAutomaticColorNonColorPixelType(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOMATICCOLORNONCOLORPIXELTYPE, gcType));
    }

    public List<Boolean> getAutomaticCropUsesFrame(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOMATICCROPUSESFRAME, gcType));
    }

    public List<Boolean> getAutomaticDeskew(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOMATICDESKEW, gcType));
    }

    public List<Boolean> getAutomaticLengthDetection(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOMATICLENGTHDETECTION, gcType));
    }

    public List<Boolean> getAutomaticRotate(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOMATICROTATE, gcType));
    }

    public List<Integer> getAutoSize(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_AUTOSIZE, gcType));
    }

    public List<Boolean> getBarcodeDetectionEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BARCODEDETECTIONENABLED, gcType));
    }

    public List<Integer> getBarcodeMaxRetries(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BARCODEMAXRETRIES, gcType));
    }

    public List<Integer> getBarcodeMaxSearchPriorities(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BARCODEMAXSEARCHPRIORITIES, gcType));
    }

    public List<Integer> getBarcodeSearchMode(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BARCODESEARCHMODE, gcType));
    }

    public List<Integer> getBarcodeSearchPriorities(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BARCODESEARCHPRIORITIES, gcType));
    }

    public List<Integer> getBarcodeTimeout(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BARCODETIMEOUT, gcType));
    }

    public List<Integer> getBitDepth(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BITDEPTH, gcType));
    }

    public List<Integer> getBitDepthReduction(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BITDEPTHREDUCTION, gcType));
    }

    public List<Integer> getBitOrder(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BITORDER, gcType));
    }

    public List<Integer> getBitOrderCodes(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BITORDERCODES, gcType));
    }

    public List<Double> getBrightness(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_BRIGHTNESS, gcType));
    }

    public List<Integer> getCCITTKFactor(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_CCITTKFACTOR, gcType));
    }

    public List<Boolean> getColorManagementEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_COLORMANAGEMENTENABLED, gcType));
    }

    public List<Integer> getCompression(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_COMPRESSION, gcType));
    }

    public List<Double> getContrast(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_CONTRAST, gcType));
    }

    public List<Byte> getCustHalftone(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_CUSTHALFTONE, gcType));
    }

    public List<Double> getExposureTime(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_EXPOSURETIME, gcType));
    }

    public List<Boolean> getExtImageInfo(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_EXTIMAGEINFO, gcType));
    }

    public List<Integer> getFeederType(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_FEEDERTYPE, gcType));
    }

    public List<Integer> getFilmType(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_FILMTYPE, gcType));
    }

    public List<Integer> getFilter(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_FILTER, gcType));
    }

    public List<Integer> getFlashUsed(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_FLASHUSED, gcType));
    }

    public List<Integer> getFlashUsed2(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_FLASHUSED2, gcType));
    }

    public List<Integer> getFlipRotation(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_FLIPROTATION, gcType));
    }

    public List<TwainFrameDouble> getFrames(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_FRAMES, gcType));
    }

    public List<Double> getGamma(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_GAMMA, gcType));
    }

    public List<String> getHalftones(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_HALFTONES, gcType));
    }

    public List<Double> getHighlight(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_HIGHLIGHT, gcType));
    }

    public List<Integer> getICCProfile(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_ICCPROFILE, gcType));
    }

    public List<Integer> getImageDataSet(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_IMAGEDATASET, gcType));
    }

    public List<Integer> getImageFileFormat(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_IMAGEFILEFORMAT, gcType));
    }

    public List<Integer> getImageFilter(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_IMAGEFILTER, gcType));
    }

    public List<Integer> getImageMerge(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_IMAGEMERGE, gcType));
    }

    public List<Double> getImageMergeHeightThreshold(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_IMAGEMERGEHEIGHTTHRESHOLD, gcType));
    }

    public List<Integer> getJpegPixelType(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_JPEGPIXELTYPE, gcType));
    }

    public List<Integer> getJpegQuality(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_JPEGQUALITY, gcType));
    }

    public List<Integer> getJpegSubsampling(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_JPEGSUBSAMPLING, gcType));
    }

    public List<Boolean> getLampState(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_LAMPSTATE, gcType));
    }

    public List<Integer> getLightPath(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_LIGHTPATH, gcType));
    }

    public List<Integer> getLightSource(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_LIGHTSOURCE, gcType));
    }

    public List<Integer> getMaxFrames(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_MAXFRAMES, gcType));
    }

    public List<Double> getMinimumHeight(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_MINIMUMHEIGHT, gcType));
    }

    public List<Double> getMinimumWidth(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_MINIMUMWIDTH, gcType));
    }

    public List<Integer> getMirror(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_MIRROR, gcType));
    }

    public List<Integer> getNoiseFilter(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_NOISEFILTER, gcType));
    }

    public List<Integer> getOrientation(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_ORIENTATION, gcType));
    }

    public List<Integer> getOverscan(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_OVERSCAN, gcType));
    }

    public List<Boolean> getPatchcodeDetectionEnabled(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PATCHCODEDETECTIONENABLED, gcType));
    }

    public List<Integer> getPatchcodeMaxRetries(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PATCHCODEMAXRETRIES, gcType));
    }

    public List<Integer> getPatchcodeMaxSearchPriorities(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PATCHCODEMAXSEARCHPRIORITIES, gcType));
    }

    public List<Integer> getPatchcodeSearchMode(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PATCHCODESEARCHMODE, gcType));
    }

    public List<Integer> getPatchcodeSearchPriorities(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PATCHCODESEARCHPRIORITIES, gcType));
    }

    public List<Integer> getPatchcodeTimeout(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PATCHCODETIMEOUT, gcType));
    }

    public List<Double> getPhysicalHeight(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PHYSICALHEIGHT, gcType));
    }

    public List<Double> getPhysicalWidth(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PHYSICALWIDTH, gcType));
    }

    public List<Integer> getPixelFlavor(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PIXELFLAVOR, gcType));
    }

    public List<Integer> getPixelFlavorCodes(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PIXELFLAVORCODES, gcType));
    }

    public List<Integer> getPixelType(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PIXELTYPE, gcType));
    }

    public List<Integer> getPlanarChunky(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_PLANARCHUNKY, gcType));
    }

    public List<Double> getRotation(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_ROTATION, gcType));
    }

    public List<Double> getShadow(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_SHADOW, gcType));
    }

    public List<Integer> getSupportedBarcodeTypes(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_SUPPORTEDBARCODETYPES, gcType));
    }

    public List<Integer> getSupportedExtImageInfo(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_SUPPORTEDEXTIMAGEINFO, gcType));
    }

    public List<Integer> getSupportedPatchcodeTypes(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_SUPPORTEDPATCHCODETYPES, gcType));
    }

    public List<Integer> getSupportedSizes(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_SUPPORTEDSIZES, gcType));
    }

    public List<Double> getThreshold(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_THRESHOLD, gcType));
    }

    public List<Boolean> getTiles(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_TILES, gcType));
    }

    public List<Integer> getTimefill(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_TIMEFILL, gcType));
    }

    public List<Boolean> getUndefinedImageSize(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_UNDEFINEDIMAGESIZE, gcType));
    }

    public List<Integer> getUnits(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_UNITS, gcType));
    }

    public List<Integer> getImageXferMech(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_XFERMECH, gcType));
    }

    public List<Integer> getXNativeResolution(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_XNATIVERESOLUTION, gcType));
    }

    public List<Double> getXResolution(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_XRESOLUTION, gcType));
    }

    public List<Double> getXScaling(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_XSCALING, gcType));
    }

    public List<Double> getYNativeResolution(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_YNATIVERESOLUTION, gcType));
    }

    public List<Double> getYResolution(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_YRESOLUTION, gcType));
    }

    public List<Double> getYScaling(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_YSCALING, gcType));
    }

    public List<Integer> getZoomFactor(GetCapOperation gcType) throws DTwainJavaAPIException
    {
        return TwainObjectUtils.getObjectListAsType(getCapValues(CAPS.ICAP_ZOOMFACTOR, gcType));
    }
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
    public CapabilityInterface setAlarmVolume(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_ALARMVOLUME, scType);
        return this;
    }

    public CapabilityInterface setAlarms(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_ALARMS, scType);
        return this;
    }

    public CapabilityInterface setAudioXferMech(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_XFERMECH, scType);
        return this;
    }

    public CapabilityInterface setAuthor(List<String> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_AUTHOR, scType);
        return this;
    }

    public CapabilityInterface setAutoBright(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOBRIGHT, scType);
        return this;
    }

    public CapabilityInterface setAutoDiscardBlankPages(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTODISCARDBLANKPAGES, scType);
        return this;
    }

    public CapabilityInterface setAutoSize(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOSIZE, scType);
        return this;
    }

    public CapabilityInterface setAutoFeed(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_AUTOFEED, scType);
        return this;
    }

    public CapabilityInterface setAutomaticBorderDetection(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOMATICBORDERDETECTION, scType);
        return this;
    }

    public CapabilityInterface setAutomaticCapture(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_AUTOMATICCAPTURE, scType);
        return this;
    }

    public CapabilityInterface setAutomaticColorEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOMATICCOLORENABLED, scType);
        return this;
    }

    public CapabilityInterface setAutomaticColorNonColorPixelType(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOMATICCOLORNONCOLORPIXELTYPE, scType);
        return this;
    }

    public CapabilityInterface setAutomaticDeskew(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOMATICDESKEW, scType);
        return this;
    }

    public CapabilityInterface setAutomaticLengthDetection(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOMATICLENGTHDETECTION, scType);
        return this;
    }

    public CapabilityInterface setAutomaticRotate(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_AUTOMATICROTATE, scType);
        return this;
    }

    public CapabilityInterface setAutomaticSenseMedium(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_AUTOMATICSENSEMEDIUM, scType);
        return this;
    }

    public CapabilityInterface setAutoscan(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_AUTOSCAN, scType);
        return this;
    }

    public CapabilityInterface setBarcodeDetectionEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BARCODEDETECTIONENABLED, scType);
        return this;
    }

    public CapabilityInterface setBarcodeMaxRetries(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BARCODEMAXRETRIES, scType);
        return this;
    }

    public CapabilityInterface setBarcodeMaxSearchPriorities(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BARCODEMAXSEARCHPRIORITIES, scType);
        return this;
    }

    public CapabilityInterface setBarcodeSearchMode(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BARCODESEARCHMODE, scType);
        return this;
    }

    public CapabilityInterface setBarcodeSearchPriorities(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BARCODESEARCHPRIORITIES, scType);
        return this;
    }

    public CapabilityInterface setBarcodeTimeout(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BARCODETIMEOUT, scType);
        return this;
    }

    public CapabilityInterface setBitDepth(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BITDEPTH, scType);
        return this;
    }

    public CapabilityInterface setBitDepthReduction(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BITDEPTHREDUCTION, scType);
        return this;
    }

    public CapabilityInterface setBitOrder(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BITORDER, scType);
        return this;
    }

    public CapabilityInterface setBitOrderCodes(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BITORDERCODES, scType);
        return this;
    }

    public CapabilityInterface setBrightness(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_BRIGHTNESS, scType);
        return this;
    }

    public CapabilityInterface setCCITTKFactor(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_CCITTKFACTOR, scType);
        return this;
    }

    public CapabilityInterface setCameraEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_CAMERAENABLED, scType);
        return this;
    }

    public CapabilityInterface setCameraOrder(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_CAMERAORDER, scType);
        return this;
    }

    public CapabilityInterface setCameraSide(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_CAMERASIDE, scType);
        return this;
    }

    public CapabilityInterface setCaption(List<String> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_CAPTION, scType);
        return this;
    }

    public CapabilityInterface setClearPage(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_CLEARPAGE, scType);
        return this;
    }

    public CapabilityInterface setColorManagementEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_COLORMANAGEMENTENABLED, scType);
        return this;
    }

    public CapabilityInterface setCompression(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_COMPRESSION, scType);
        return this;
    }

    public CapabilityInterface setContrast(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_CONTRAST, scType);
        return this;
    }

    public CapabilityInterface setCustHalftone(List<Byte> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_CUSTHALFTONE, scType);
        return this;
    }

    public CapabilityInterface setDeviceEvent(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_DEVICEEVENT, scType);
        return this;
    }

    public CapabilityInterface setDeviceTimeDate(List<String> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_DEVICETIMEDATE, scType);
        return this;
    }

    public CapabilityInterface setDoublefeedDetection(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_DOUBLEFEEDDETECTION, scType);
        return this;
    }

    public CapabilityInterface setDoublefeedDetectionLength(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_DOUBLEFEEDDETECTIONLENGTH, scType);
        return this;
    }

    public CapabilityInterface setDoublefeedDetectionResponse(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_DOUBLEFEEDDETECTIONRESPONSE, scType);
        return this;
    }

    public CapabilityInterface setDoublefeedDetectionSensitivity(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_DOUBLEFEEDDETECTIONSENSITIVITY, scType);
        return this;
    }

    public CapabilityInterface setDuplexEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_DUPLEXENABLED, scType);
        return this;
    }

    public CapabilityInterface setEndorser(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_ENDORSER, scType);
        return this;
    }

    public CapabilityInterface setExposureTime(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_EXPOSURETIME, scType);
        return this;
    }

    public CapabilityInterface setExtImageInfo(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_EXTIMAGEINFO, scType);
        return this;
    }

    public CapabilityInterface setExtendedCaps(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_EXTENDEDCAPS, scType);
        return this;
    }

    public CapabilityInterface setFeedPage(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_FEEDPAGE, scType);
        return this;
    }

    public CapabilityInterface setFeederAlignment(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_FEEDERALIGNMENT, scType);
        return this;
    }

    public CapabilityInterface setFeederEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_FEEDERENABLED, scType);
        return this;
    }

    public CapabilityInterface setFeederOrder(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_FEEDERORDER, scType);
        return this;
    }

    public CapabilityInterface setFeederPocket(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_FEEDERPOCKET, scType);
        return this;
    }

    public CapabilityInterface setFeederPrep(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_FEEDERPREP, scType);
        return this;
    }

    public CapabilityInterface setFeederType(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_FEEDERTYPE, scType);
        return this;
    }

    public CapabilityInterface setFilmType(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_FILMTYPE, scType);
        return this;
    }

    public CapabilityInterface setFilter(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_FILTER, scType);
        return this;
    }

    public CapabilityInterface setFlashUsed(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_FLASHUSED, scType);
        return this;
    }

    public CapabilityInterface setFlashUsed2(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_FLASHUSED2, scType);
        return this;
    }

    public CapabilityInterface setFlipRotation(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_FLIPROTATION, scType);
        return this;
    }

    public CapabilityInterface setFrames(List<TwainFrameDouble> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_FRAMES, scType);
        return this;
    }

    public CapabilityInterface setGamma(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_GAMMA, scType);
        return this;
    }

    public CapabilityInterface setHalftones(List<String> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_HALFTONES, scType);
        return this;
    }

    public CapabilityInterface setHighlight(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_HIGHLIGHT, scType);
        return this;
    }

    public CapabilityInterface setICCProfile(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_ICCPROFILE, scType);
        return this;
    }

    public CapabilityInterface setImageDataSet(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_IMAGEDATASET, scType);
        return this;
    }

    public CapabilityInterface setImageFileFormat(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_IMAGEFILEFORMAT, scType);
        return this;
    }

    public CapabilityInterface setImageFilter(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_IMAGEFILTER, scType);
        return this;
    }

    public CapabilityInterface setImageMerge(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_IMAGEMERGE, scType);
        return this;
    }

    public CapabilityInterface setImageMergeHeightThreshold(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_IMAGEMERGEHEIGHTTHRESHOLD, scType);
        return this;
    }

    public CapabilityInterface setImageXferMech(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_XFERMECH, scType);
        return this;
    }

    public CapabilityInterface setIndicators(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_INDICATORS, scType);
        return this;
    }

    public CapabilityInterface setIndicatorsMode(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_INDICATORSMODE, scType);
        return this;
    }

    public CapabilityInterface setJobControl(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_JOBCONTROL, scType);
        return this;
    }

    public CapabilityInterface setJpegPixelType(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_JPEGPIXELTYPE, scType);
        return this;
    }

    public CapabilityInterface setJpegQuality(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_JPEGQUALITY, scType);
        return this;
    }

    public CapabilityInterface setJpegSubsampling(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_JPEGSUBSAMPLING, scType);
        return this;
    }

    public CapabilityInterface setLampState(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_LAMPSTATE, scType);
        return this;
    }

    public CapabilityInterface setLanguage(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_LANGUAGE, scType);
        return this;
    }

    public CapabilityInterface setLightPath(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_LIGHTPATH, scType);
        return this;
    }

    public CapabilityInterface setLightSource(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_LIGHTSOURCE, scType);
        return this;
    }

    public CapabilityInterface setMaxBatchBuffers(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_MAXBATCHBUFFERS, scType);
        return this;
    }

    public CapabilityInterface setMaxFrames(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_MAXFRAMES, scType);
        return this;
    }

    public CapabilityInterface setMicrEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_MICRENABLED, scType);
        return this;
    }

    public CapabilityInterface setMinimumHeight(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_MINIMUMHEIGHT, scType);
        return this;
    }

    public CapabilityInterface setMinimumWidth(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_MINIMUMWIDTH, scType);
        return this;
    }

    public CapabilityInterface setMirror(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_MIRROR, scType);
        return this;
    }

    public CapabilityInterface setNoiseFilter(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_NOISEFILTER, scType);
        return this;
    }

    public CapabilityInterface setOrientation(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_ORIENTATION, scType);
        return this;
    }

    public CapabilityInterface setOverscan(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_OVERSCAN, scType);
        return this;
    }

    public CapabilityInterface setPaperHandling(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PAPERHANDLING, scType);
        return this;
    }

    public CapabilityInterface setPatchcodeDetectionEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PATCHCODEDETECTIONENABLED, scType);
        return this;
    }

    public CapabilityInterface setPatchcodeMaxRetries(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PATCHCODEMAXRETRIES, scType);
        return this;
    }

    public CapabilityInterface setPatchcodeMaxSearchPriorities(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PATCHCODEMAXSEARCHPRIORITIES, scType);
        return this;
    }

    public CapabilityInterface setPatchcodeSearchMode(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PATCHCODESEARCHMODE, scType);
        return this;
    }

    public CapabilityInterface setPatchcodeSearchPriorities(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PATCHCODESEARCHPRIORITIES, scType);
        return this;
    }

    public CapabilityInterface setPatchcodeTimeout(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PATCHCODETIMEOUT, scType);
        return this;
    }

    public CapabilityInterface setPixelFlavor(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PIXELFLAVOR, scType);
        return this;
    }

    public CapabilityInterface setPixelFlavorCodes(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PIXELFLAVORCODES, scType);
        return this;
    }

    public CapabilityInterface setPixelType(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PIXELTYPE, scType);
        return this;
    }

    public CapabilityInterface setPlanarChunky(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_PLANARCHUNKY, scType);
        return this;
    }

    public CapabilityInterface setPowerSaveTime(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_POWERSAVETIME, scType);
        return this;
    }

    public CapabilityInterface setPrinter(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTER, scType);
        return this;
    }

    public CapabilityInterface setPrinterCharRotation(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERCHARROTATION, scType);
        return this;
    }

    public CapabilityInterface setPrinterEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERENABLED, scType);
        return this;
    }

    public CapabilityInterface setPrinterFontStyle(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERFONTSTYLE, scType);
        return this;
    }

    public CapabilityInterface setPrinterIndex(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERINDEX, scType);
        return this;
    }

    public CapabilityInterface setPrinterIndexLeadChar(List<String> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERINDEXLEADCHAR, scType);
        return this;
    }

    public CapabilityInterface setPrinterIndexMaxValue(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERINDEXMAXVALUE, scType);
        return this;
    }

    public CapabilityInterface setPrinterIndexNumDigits(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERINDEXNUMDIGITS, scType);
        return this;
    }

    public CapabilityInterface setPrinterIndexStep(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERINDEXSTEP, scType);
        return this;
    }

    public CapabilityInterface setPrinterIndexTrigger(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERINDEXTRIGGER, scType);
        return this;
    }

    public CapabilityInterface setPrinterMode(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERMODE, scType);
        return this;
    }

    public CapabilityInterface setPrinterString(List<String> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERSTRING, scType);
        return this;
    }

    public CapabilityInterface setPrinterSuffix(List<String> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERSUFFIX, scType);
        return this;
    }

    public CapabilityInterface setPrinterVerticalOffset(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_PRINTERVERTICALOFFSET, scType);
        return this;
    }

    public CapabilityInterface setRewindPage(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_REWINDPAGE, scType);
        return this;
    }

    public CapabilityInterface setRotation(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_ROTATION, scType);
        return this;
    }

    public CapabilityInterface setSegmented(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_SEGMENTED, scType);
        return this;
    }

    public CapabilityInterface setShadow(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_SHADOW, scType);
        return this;
    }

    public CapabilityInterface setSheetCount(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_SHEETCOUNT, scType);
        return this;
    }

    public CapabilityInterface setSupportedCapsSegmentUnique(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_SUPPORTEDCAPSSEGMENTUNIQUE, scType);
        return this;
    }

    public CapabilityInterface setSupportedSizes(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_SUPPORTEDSIZES, scType);
        return this;
    }

    public CapabilityInterface setThreshold(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_THRESHOLD, scType);
        return this;
    }

    public CapabilityInterface setThumbnailsEnabled(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_THUMBNAILSENABLED, scType);
        return this;
    }

    public CapabilityInterface setTiles(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_TILES, scType);
        return this;
    }

    public CapabilityInterface setTimeBeforeFirstCapture(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_TIMEBEFOREFIRSTCAPTURE, scType);
        return this;
    }

    public CapabilityInterface setTimeBetweenCaptures(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_TIMEBETWEENCAPTURES, scType);
        return this;
    }

    public CapabilityInterface setTimefill(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_TIMEFILL, scType);
        return this;
    }

    public CapabilityInterface setUndefinedImageSize(List<Boolean> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_UNDEFINEDIMAGESIZE, scType);
        return this;
    }

    public CapabilityInterface setUnits(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_UNITS, scType);
        return this;
    }

    public CapabilityInterface setXResolution(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_XRESOLUTION, scType);
        return this;
    }

    public CapabilityInterface setXScaling(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_XSCALING, scType);
        return this;
    }

    public CapabilityInterface setXferCount(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.CAP_XFERCOUNT, scType);
        return this;
    }

    public CapabilityInterface setYResolution(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_YRESOLUTION, scType);
        return this;
    }

    public CapabilityInterface setYScaling(List<Double> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_YSCALING, scType);
        return this;
    }

    public CapabilityInterface setZoomFactor(List<Integer> vals, SetCapOperation scType) throws DTwainJavaAPIException
    {
        setCapValues(vals, CAPS.ICAP_ZOOMFACTOR, scType);
        return this;
    }

    public boolean isAudioXferMechSupported()
    {
        return isCapSupported(CAPS.ACAP_XFERMECH);
    }

    public boolean isAlarmsSupported()
    {
        return isCapSupported(CAPS.CAP_ALARMS);
    }

    public boolean isAlarmVolumeSupported()
    {
        return isCapSupported(CAPS.CAP_ALARMVOLUME);
    }

    public boolean isAuthorSupported()
    {
        return isCapSupported(CAPS.CAP_AUTHOR);
    }

    public boolean isAutoFeedSupported()
    {
        return isCapSupported(CAPS.CAP_AUTOFEED);
    }

    public boolean isAutomaticCaptureSupported()
    {
        return isCapSupported(CAPS.CAP_AUTOMATICCAPTURE);
    }

    public boolean isAutomaticSenseMediumSupported()
    {
        return isCapSupported(CAPS.CAP_AUTOMATICSENSEMEDIUM);
    }

    public boolean isAutoscanSupported()
    {
        return isCapSupported(CAPS.CAP_AUTOSCAN);
    }

    public boolean isBatteryMinutesSupported()
    {
        return isCapSupported(CAPS.CAP_BATTERYMINUTES);
    }

    public boolean isBatteryPercentageSupported()
    {
        return isCapSupported(CAPS.CAP_BATTERYPERCENTAGE);
    }

    public boolean isCameraEnabledSupported()
    {
        return isCapSupported(CAPS.CAP_CAMERAENABLED);
    }

    public boolean isCameraOrderSupported()
    {
        return isCapSupported(CAPS.CAP_CAMERAORDER);
    }

    public boolean isCameraPreviewUISupported()
    {
        return isCapSupported(CAPS.CAP_CAMERAPREVIEWUI);
    }

    public boolean isCameraSideSupported()
    {
        return isCapSupported(CAPS.CAP_CAMERASIDE);
    }

    public boolean isClearBuffersSupported()
    {
        return isCapSupported(CAPS.CAP_CLEARBUFFERS);
    }

    public boolean isCaptionSupported()
    {
        return isCapSupported(CAPS.CAP_CAPTION);
    }

    public boolean isClearPageSupported()
    {
        return isCapSupported(CAPS.CAP_CLEARPAGE);
    }

    public boolean isCustomDSDataSupported()
    {
        return isCapSupported(CAPS.CAP_CUSTOMDSDATA);
    }

    public boolean isCustomInterfaceGUIDSupported()
    {
        return isCapSupported(CAPS.CAP_CUSTOMINTERFACEGUID);
    }

    public boolean isDeviceEventSupported()
    {
        return isCapSupported(CAPS.CAP_DEVICEEVENT);
    }

    public boolean isDeviceOnlineSupported()
    {
        return isCapSupported(CAPS.CAP_DEVICEONLINE);
    }

    public boolean isDeviceTimeDateSupported()
    {
        return isCapSupported(CAPS.CAP_DEVICETIMEDATE);
    }

    public boolean isDoublefeedDetectionSupported()
    {
        return isCapSupported(CAPS.CAP_DOUBLEFEEDDETECTION);
    }

    public boolean isDoublefeedDetectionLengthSupported()
    {
        return isCapSupported(CAPS.CAP_DOUBLEFEEDDETECTIONLENGTH);
    }

    public boolean isDoublefeedDetectionResponseSupported()
    {
        return isCapSupported(CAPS.CAP_DOUBLEFEEDDETECTIONRESPONSE);
    }

    public boolean isDoublefeedDetectionSensitivitySupported()
    {
        return isCapSupported(CAPS.CAP_DOUBLEFEEDDETECTIONSENSITIVITY);
    }

    public boolean isDuplexSupported()
    {
        return isCapSupported(CAPS.CAP_DUPLEX);
    }

    public boolean isDuplexEnabledSupported()
    {
        return isCapSupported(CAPS.CAP_DUPLEXENABLED);
    }

    public boolean isEnableDSUIOnlySupported()
    {
        return isCapSupported(CAPS.CAP_ENABLEDSUIONLY);
    }

    public boolean isEndorserSupported()
    {
        return isCapSupported(CAPS.CAP_ENDORSER);
    }

    public boolean isExtendedCapsSupported()
    {
        return isCapSupported(CAPS.CAP_EXTENDEDCAPS);
    }

    public boolean isFeederAlignmentSupported()
    {
        return isCapSupported(CAPS.CAP_FEEDERALIGNMENT);
    }

    public boolean isFeederEnabledSupported()
    {
        return isCapSupported(CAPS.CAP_FEEDERENABLED);
    }

    public boolean isFeederLoadedSupported()
    {
        return isCapSupported(CAPS.CAP_FEEDERLOADED);
    }

    public boolean isFeederOrderSupported()
    {
        return isCapSupported(CAPS.CAP_FEEDERORDER);
    }

    public boolean isFeederPocketSupported()
    {
        return isCapSupported(CAPS.CAP_FEEDERPOCKET);
    }

    public boolean isFeederPrepSupported()
    {
        return isCapSupported(CAPS.CAP_FEEDERPREP);
    }

    public boolean isFeedPageSupported()
    {
        return isCapSupported(CAPS.CAP_FEEDPAGE);
    }

    public boolean isIndicatorsSupported()
    {
        return isCapSupported(CAPS.CAP_INDICATORS);
    }

    public boolean isIndicatorsModeSupported()
    {
        return isCapSupported(CAPS.CAP_INDICATORSMODE);
    }

    public boolean isJobControlSupported()
    {
        return isCapSupported(CAPS.CAP_JOBCONTROL);
    }

    public boolean isLanguageSupported()
    {
        return isCapSupported(CAPS.CAP_LANGUAGE);
    }

    public boolean isMaxBatchBuffersSupported()
    {
        return isCapSupported(CAPS.CAP_MAXBATCHBUFFERS);
    }

    public boolean isMicrEnabledSupported()
    {
        return isCapSupported(CAPS.CAP_MICRENABLED);
    }

    public boolean isPaperDetectableSupported()
    {
        return isCapSupported(CAPS.CAP_PAPERDETECTABLE);
    }

    public boolean isPaperHandlingSupported()
    {
        return isCapSupported(CAPS.CAP_PAPERHANDLING);
    }

    public boolean isPowerSaveTimeSupported()
    {
        return isCapSupported(CAPS.CAP_POWERSAVETIME);
    }

    public boolean isPowerSupplySupported()
    {
        return isCapSupported(CAPS.CAP_POWERSUPPLY);
    }

    public boolean isPrinterSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTER);
    }

    public boolean isPrinterCharRotationSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERCHARROTATION);
    }

    public boolean isPrinterEnabledSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERENABLED);
    }

    public boolean isPrinterFontStyleSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERFONTSTYLE);
    }

    public boolean isPrinterIndexSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERINDEX);
    }

    public boolean isPrinterIndexLeadCharSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERINDEXLEADCHAR);
    }

    public boolean isPrinterIndexMaxValueSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERINDEXMAXVALUE);
    }

    public boolean isPrinterIndexNumDigitsSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERINDEXNUMDIGITS);
    }

    public boolean isPrinterIndexStepSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERINDEXSTEP);
    }

    public boolean isPrinterIndexTriggerSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERINDEXTRIGGER);
    }

    public boolean isPrinterModeSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERMODE);
    }

    public boolean isPrinterStringSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERSTRING);
    }

    public boolean isPrinterStringPreviewSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERSTRINGPREVIEW);
    }

    public boolean isPrinterSuffixSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERSUFFIX);
    }

    public boolean isPrinterVerticalOffsetSupported()
    {
        return isCapSupported(CAPS.CAP_PRINTERVERTICALOFFSET);
    }

    public boolean isReacquireAllowedSupported()
    {
        return isCapSupported(CAPS.CAP_REACQUIREALLOWED);
    }

    public boolean isRewindPageSupported()
    {
        return isCapSupported(CAPS.CAP_REWINDPAGE);
    }

    public boolean isSegmentedSupported()
    {
        return isCapSupported(CAPS.CAP_SEGMENTED);
    }

    public boolean isSerialNumberSupported()
    {
        return isCapSupported(CAPS.CAP_SERIALNUMBER);
    }

    public boolean isSheetCountSupported()
    {
        return isCapSupported(CAPS.CAP_SHEETCOUNT);
    }

    public boolean isSupportedCapsSupported()
    {
        return isCapSupported(CAPS.CAP_SUPPORTEDCAPS);
    }

    public boolean isSupportedCapsSegmentUniqueSupported()
    {
        return isCapSupported(CAPS.CAP_SUPPORTEDCAPSSEGMENTUNIQUE);
    }

    public boolean isSupportedDatsSupported()
    {
        return isCapSupported(CAPS.CAP_SUPPORTEDDATS);
    }

    public boolean isThumbnailsEnabledSupported()
    {
        return isCapSupported(CAPS.CAP_THUMBNAILSENABLED);
    }

    public boolean isTimeBeforeFirstCaptureSupported()
    {
        return isCapSupported(CAPS.CAP_TIMEBEFOREFIRSTCAPTURE);
    }

    public boolean isTimeBetweenCapturesSupported()
    {
        return isCapSupported(CAPS.CAP_TIMEBETWEENCAPTURES);
    }

    public boolean isTimeDateSupported()
    {
        return isCapSupported(CAPS.CAP_TIMEDATE);
    }

    public boolean isUiControllableSupported()
    {
        return isCapSupported(CAPS.CAP_UICONTROLLABLE);
    }

    public boolean isXferCountSupported()
    {
        return isCapSupported(CAPS.CAP_XFERCOUNT);
    }

    public boolean isAutoBrightSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOBRIGHT);
    }

    public boolean isAutoDiscardBlankPagesSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTODISCARDBLANKPAGES);
    }

    public boolean isAutomaticBorderDetectionSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOMATICBORDERDETECTION);
    }

    public boolean isAutomaticColorEnabledSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOMATICCOLORENABLED);
    }

    public boolean isAutomaticColorNonColorPixelTypeSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOMATICCOLORNONCOLORPIXELTYPE);
    }

    public boolean isAutomaticCropUsesFrameSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOMATICCROPUSESFRAME);
    }

    public boolean isAutomaticDeskewSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOMATICDESKEW);
    }

    public boolean isAutomaticLengthDetectionSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOMATICLENGTHDETECTION);
    }

    public boolean isAutomaticRotateSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOMATICROTATE);
    }

    public boolean isAutoSizeSupported()
    {
        return isCapSupported(CAPS.ICAP_AUTOSIZE);
    }

    public boolean isBarcodeDetectionEnabledSupported()
    {
        return isCapSupported(CAPS.ICAP_BARCODEDETECTIONENABLED);
    }

    public boolean isBarcodeMaxRetriesSupported()
    {
        return isCapSupported(CAPS.ICAP_BARCODEMAXRETRIES);
    }

    public boolean isBarcodeMaxSearchPrioritiesSupported()
    {
        return isCapSupported(CAPS.ICAP_BARCODEMAXSEARCHPRIORITIES);
    }

    public boolean isBarcodeSearchModeSupported()
    {
        return isCapSupported(CAPS.ICAP_BARCODESEARCHMODE);
    }

    public boolean isBarcodeSearchPrioritiesSupported()
    {
        return isCapSupported(CAPS.ICAP_BARCODESEARCHPRIORITIES);
    }

    public boolean isBarcodeTimeoutSupported()
    {
        return isCapSupported(CAPS.ICAP_BARCODETIMEOUT);
    }

    public boolean isBitDepthSupported()
    {
        return isCapSupported(CAPS.ICAP_BITDEPTH);
    }

    public boolean isBitDepthReductionSupported()
    {
        return isCapSupported(CAPS.ICAP_BITDEPTHREDUCTION);
    }

    public boolean isBitOrderSupported()
    {
        return isCapSupported(CAPS.ICAP_BITORDER);
    }

    public boolean isBitOrderCodesSupported()
    {
        return isCapSupported(CAPS.ICAP_BITORDERCODES);
    }

    public boolean isBrightnessSupported()
    {
        return isCapSupported(CAPS.ICAP_BRIGHTNESS);
    }

    public boolean isCCITTKFactorSupported()
    {
        return isCapSupported(CAPS.ICAP_CCITTKFACTOR);
    }

    public boolean isColorManagementEnabledSupported()
    {
        return isCapSupported(CAPS.ICAP_COLORMANAGEMENTENABLED);
    }

    public boolean isCompressionSupported()
    {
        return isCapSupported(CAPS.ICAP_COMPRESSION);
    }

    public boolean isContrastSupported()
    {
        return isCapSupported(CAPS.ICAP_CONTRAST);
    }

    public boolean isCustHalftoneSupported()
    {
        return isCapSupported(CAPS.ICAP_CUSTHALFTONE);
    }

    public boolean isExposureTimeSupported()
    {
        return isCapSupported(CAPS.ICAP_EXPOSURETIME);
    }

    public boolean isExtImageInfoSupported()
    {
        return isCapSupported(CAPS.ICAP_EXTIMAGEINFO);
    }

    public boolean isFeederTypeSupported()
    {
        return isCapSupported(CAPS.ICAP_FEEDERTYPE);
    }

    public boolean isFilmTypeSupported()
    {
        return isCapSupported(CAPS.ICAP_FILMTYPE);
    }

    public boolean isFilterSupported()
    {
        return isCapSupported(CAPS.ICAP_FILTER);
    }

    public boolean isFlashUsedSupported()
    {
        return isCapSupported(CAPS.ICAP_FLASHUSED);
    }

    public boolean isFlashUsed2Supported()
    {
        return isCapSupported(CAPS.ICAP_FLASHUSED2);
    }

    public boolean isFlipRotationSupported()
    {
        return isCapSupported(CAPS.ICAP_FLIPROTATION);
    }

    public boolean isFramesSupported()
    {
        return isCapSupported(CAPS.ICAP_FRAMES);
    }

    public boolean isGammaSupported()
    {
        return isCapSupported(CAPS.ICAP_GAMMA);
    }

    public boolean isHalftonesSupported()
    {
        return isCapSupported(CAPS.ICAP_HALFTONES);
    }

    public boolean isHighlightSupported()
    {
        return isCapSupported(CAPS.ICAP_HIGHLIGHT);
    }

    public boolean isICCProfileSupported()
    {
        return isCapSupported(CAPS.ICAP_ICCPROFILE);
    }

    public boolean isImageDataSetSupported()
    {
        return isCapSupported(CAPS.ICAP_IMAGEDATASET);
    }

    public boolean isImageFileFormatSupported()
    {
        return isCapSupported(CAPS.ICAP_IMAGEFILEFORMAT);
    }

    public boolean isImageFilterSupported()
    {
        return isCapSupported(CAPS.ICAP_IMAGEFILTER);
    }

    public boolean isImageMergeSupported()
    {
        return isCapSupported(CAPS.ICAP_IMAGEMERGE);
    }

    public boolean isImageMergeHeightThresholdSupported()
    {
        return isCapSupported(CAPS.ICAP_IMAGEMERGEHEIGHTTHRESHOLD);
    }

    public boolean isJpegPixelTypeSupported()
    {
        return isCapSupported(CAPS.ICAP_JPEGPIXELTYPE);
    }

    public boolean isJpegQualitySupported()
    {
        return isCapSupported(CAPS.ICAP_JPEGQUALITY);
    }

    public boolean isJpegSubsamplingSupported()
    {
        return isCapSupported(CAPS.ICAP_JPEGSUBSAMPLING);
    }

    public boolean isLampStateSupported()
    {
        return isCapSupported(CAPS.ICAP_LAMPSTATE);
    }

    public boolean isLightPathSupported()
    {
        return isCapSupported(CAPS.ICAP_LIGHTPATH);
    }

    public boolean isLightSourceSupported()
    {
        return isCapSupported(CAPS.ICAP_LIGHTSOURCE);
    }

    public boolean isMaxFramesSupported()
    {
        return isCapSupported(CAPS.ICAP_MAXFRAMES);
    }

    public boolean isMinimumHeightSupported()
    {
        return isCapSupported(CAPS.ICAP_MINIMUMHEIGHT);
    }

    public boolean isMinimumWidthSupported()
    {
        return isCapSupported(CAPS.ICAP_MINIMUMWIDTH);
    }

    public boolean isMirrorSupported()
    {
        return isCapSupported(CAPS.ICAP_MIRROR);
    }

    public boolean isNoiseFilterSupported()
    {
        return isCapSupported(CAPS.ICAP_NOISEFILTER);
    }

    public boolean isOrientationSupported()
    {
        return isCapSupported(CAPS.ICAP_ORIENTATION);
    }

    public boolean isOverscanSupported()
    {
        return isCapSupported(CAPS.ICAP_OVERSCAN);
    }

    public boolean isPatchcodeDetectionEnabledSupported()
    {
        return isCapSupported(CAPS.ICAP_PATCHCODEDETECTIONENABLED);
    }

    public boolean isPatchcodeMaxRetriesSupported()
    {
        return isCapSupported(CAPS.ICAP_PATCHCODEMAXRETRIES);
    }

    public boolean isPatchcodeMaxSearchPrioritiesSupported()
    {
        return isCapSupported(CAPS.ICAP_PATCHCODEMAXSEARCHPRIORITIES);
    }

    public boolean isPatchcodeSearchModeSupported()
    {
        return isCapSupported(CAPS.ICAP_PATCHCODESEARCHMODE);
    }

    public boolean isPatchcodeSearchPrioritiesSupported()
    {
        return isCapSupported(CAPS.ICAP_PATCHCODESEARCHPRIORITIES);
    }

    public boolean isPatchcodeTimeoutSupported()
    {
        return isCapSupported(CAPS.ICAP_PATCHCODETIMEOUT);
    }

    public boolean isPhysicalHeightSupported()
    {
        return isCapSupported(CAPS.ICAP_PHYSICALHEIGHT);
    }

    public boolean isPhysicalWidthSupported()
    {
        return isCapSupported(CAPS.ICAP_PHYSICALWIDTH);
    }

    public boolean isPixelFlavorSupported()
    {
        return isCapSupported(CAPS.ICAP_PIXELFLAVOR);
    }

    public boolean isPixelFlavorCodesSupported()
    {
        return isCapSupported(CAPS.ICAP_PIXELFLAVORCODES);
    }

    public boolean isPixelTypeSupported()
    {
        return isCapSupported(CAPS.ICAP_PIXELTYPE);
    }

    public boolean isPlanarChunkySupported()
    {
        return isCapSupported(CAPS.ICAP_PLANARCHUNKY);
    }

    public boolean isRotationSupported()
    {
        return isCapSupported(CAPS.ICAP_ROTATION);
    }

    public boolean isShadowSupported()
    {
        return isCapSupported(CAPS.ICAP_SHADOW);
    }

    public boolean isSupportedBarcodeTypesSupported()
    {
        return isCapSupported(CAPS.ICAP_SUPPORTEDBARCODETYPES);
    }

    public boolean isSupportedExtImageInfoSupported()
    {
        return isCapSupported(CAPS.ICAP_SUPPORTEDEXTIMAGEINFO);
    }

    public boolean isSupportedPatchcodeTypesSupported()
    {
        return isCapSupported(CAPS.ICAP_SUPPORTEDPATCHCODETYPES);
    }

    public boolean isSupportedSizesSupported()
    {
        return isCapSupported(CAPS.ICAP_SUPPORTEDSIZES);
    }

    public boolean isThresholdSupported()
    {
        return isCapSupported(CAPS.ICAP_THRESHOLD);
    }

    public boolean isTilesSupported()
    {
        return isCapSupported(CAPS.ICAP_TILES);
    }

    public boolean isTimefillSupported()
    {
        return isCapSupported(CAPS.ICAP_TIMEFILL);
    }

    public boolean isUndefinedImageSizeSupported()
    {
        return isCapSupported(CAPS.ICAP_UNDEFINEDIMAGESIZE);
    }

    public boolean isUnitsSupported()
    {
        return isCapSupported(CAPS.ICAP_UNITS);
    }

    public boolean isImageXferMechSupported()
    {
        return isCapSupported(CAPS.ICAP_XFERMECH);
    }

    public boolean isXNativeResolutionSupported()
    {
        return isCapSupported(CAPS.ICAP_XNATIVERESOLUTION);
    }

    public boolean isXResolutionSupported()
    {
        return isCapSupported(CAPS.ICAP_XRESOLUTION);
    }

    public boolean isXScalingSupported()
    {
        return isCapSupported(CAPS.ICAP_XSCALING);
    }

    public boolean isYNativeResolutionSupported()
    {
        return isCapSupported(CAPS.ICAP_YNATIVERESOLUTION);
    }

    public boolean isYResolutionSupported()
    {
        return isCapSupported(CAPS.ICAP_YRESOLUTION);
    }

    public boolean isYScalingSupported()
    {
        return isCapSupported(CAPS.ICAP_YSCALING);
    }

    public boolean isZoomFactorSupported()
    {
        return isCapSupported(CAPS.ICAP_ZOOMFACTOR);
    }
    //----------------------- The set of Value Support functions -------------------
    public boolean isAudioXferMechValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ACAP_XFERMECH);
    }

    public boolean isAlarmsValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_ALARMS);
    }

    public boolean isAlarmVolumeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_ALARMVOLUME);
    }

    public boolean isAuthorValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_AUTHOR);
    }

    public boolean isAutoFeedValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_AUTOFEED);
    }

    public boolean isAutomaticCaptureValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_AUTOMATICCAPTURE);
    }

    public boolean isAutomaticSenseMediumValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_AUTOMATICSENSEMEDIUM);
    }

    public boolean isAutoscanValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_AUTOSCAN);
    }

    public boolean isBatteryMinutesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_BATTERYMINUTES);
    }

    public boolean isBatteryPercentageValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_BATTERYPERCENTAGE);
    }

    public boolean isCameraEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CAMERAENABLED);
    }

    public boolean isCameraOrderValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CAMERAORDER);
    }

    public boolean isCameraPreviewUIValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CAMERAPREVIEWUI);
    }

    public boolean isCameraSideValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CAMERASIDE);
    }

    public boolean isClearBuffersValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CLEARBUFFERS);
    }

    public boolean isCaptionValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CAPTION);
    }

    public boolean isClearPageValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CLEARPAGE);
    }

    public boolean isCustomDSDataValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CUSTOMDSDATA);
    }

    public boolean isCustomInterfaceGUIDValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_CUSTOMINTERFACEGUID);
    }

    public boolean isDeviceEventValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DEVICEEVENT);
    }

    public boolean isDeviceOnlineValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DEVICEONLINE);
    }

    public boolean isDeviceTimeDateValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DEVICETIMEDATE);
    }

    public boolean isDoublefeedDetectionValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DOUBLEFEEDDETECTION);
    }

    public boolean isDoublefeedDetectionLengthValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DOUBLEFEEDDETECTIONLENGTH);
    }

    public boolean isDoublefeedDetectionResponseValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DOUBLEFEEDDETECTIONRESPONSE);
    }

    public boolean isDoublefeedDetectionSensitivityValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DOUBLEFEEDDETECTIONSENSITIVITY);
    }

    public boolean isDuplexValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DUPLEX);
    }

    public boolean isDuplexEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_DUPLEXENABLED);
    }

    public boolean isEnableDSUIOnlyValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_ENABLEDSUIONLY);
    }

    public boolean isEndorserValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_ENDORSER);
    }

    public boolean isExtendedCapsValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_EXTENDEDCAPS);
    }

    public boolean isFeederAlignmentValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_FEEDERALIGNMENT);
    }

    public boolean isFeederEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_FEEDERENABLED);
    }

    public boolean isFeederLoadedValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_FEEDERLOADED);
    }

    public boolean isFeederOrderValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_FEEDERORDER);
    }

    public boolean isFeederPocketValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_FEEDERPOCKET);
    }

    public boolean isFeederPrepValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_FEEDERPREP);
    }

    public boolean isFeedPageValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_FEEDPAGE);
    }

    public boolean isIndicatorsValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_INDICATORS);
    }

    public boolean isIndicatorsModeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_INDICATORSMODE);
    }

    public boolean isJobControlValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_JOBCONTROL);
    }

    public boolean isLanguageValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_LANGUAGE);
    }

    public boolean isMaxBatchBuffersValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_MAXBATCHBUFFERS);
    }

    public boolean isMicrEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_MICRENABLED);
    }

    public boolean isPaperDetectableValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PAPERDETECTABLE);
    }

    public boolean isPaperHandlingValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PAPERHANDLING);
    }

    public boolean isPowerSaveTimeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_POWERSAVETIME);
    }

    public boolean isPowerSupplyValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_POWERSUPPLY);
    }

    public boolean isPrinterValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTER);
    }

    public boolean isPrinterCharRotationValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERCHARROTATION);
    }

    public boolean isPrinterEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERENABLED);
    }

    public boolean isPrinterFontStyleValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERFONTSTYLE);
    }

    public boolean isPrinterIndexValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERINDEX);
    }

    public boolean isPrinterIndexLeadCharValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERINDEXLEADCHAR);
    }

    public boolean isPrinterIndexMaxValueValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERINDEXMAXVALUE);
    }

    public boolean isPrinterIndexNumDigitsValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERINDEXNUMDIGITS);
    }

    public boolean isPrinterIndexStepValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERINDEXSTEP);
    }

    public boolean isPrinterIndexTriggerValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERINDEXTRIGGER);
    }

    public boolean isPrinterModeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERMODE);
    }

    public boolean isPrinterStringValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERSTRING);
    }

    public boolean isPrinterStringPreviewValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERSTRINGPREVIEW);
    }

    public boolean isPrinterSuffixValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERSUFFIX);
    }

    public boolean isPrinterVerticalOffsetValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_PRINTERVERTICALOFFSET);
    }

    public boolean isReacquireAllowedValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_REACQUIREALLOWED);
    }

    public boolean isRewindPageValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_REWINDPAGE);
    }

    public boolean isSegmentedValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_SEGMENTED);
    }

    public boolean isSerialNumberValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_SERIALNUMBER);
    }

    public boolean isSheetCountValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_SHEETCOUNT);
    }

    public boolean isSupportedCapsValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_SUPPORTEDCAPS);
    }

    public boolean isSupportedCapsSegmentUniqueValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_SUPPORTEDCAPSSEGMENTUNIQUE);
    }

    public boolean isSupportedDatsValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_SUPPORTEDDATS);
    }

    public boolean isThumbnailsEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_THUMBNAILSENABLED);
    }

    public boolean isTimeBeforeFirstCaptureValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_TIMEBEFOREFIRSTCAPTURE);
    }

    public boolean isTimeBetweenCapturesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_TIMEBETWEENCAPTURES);
    }

    public boolean isTimeDateValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_TIMEDATE);
    }

    public boolean isUiControllableValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_UICONTROLLABLE);
    }

    public boolean isXferCountValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.CAP_XFERCOUNT);
    }

    public boolean isAutoBrightValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOBRIGHT);
    }

    public boolean isAutoDiscardBlankPagesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTODISCARDBLANKPAGES);
    }

    public boolean isAutomaticBorderDetectionValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOMATICBORDERDETECTION);
    }

    public boolean isAutomaticColorEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOMATICCOLORENABLED);
    }

    public boolean isAutomaticColorNonColorPixelTypeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOMATICCOLORNONCOLORPIXELTYPE);
    }

    public boolean isAutomaticCropUsesFrameValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOMATICCROPUSESFRAME);
    }

    public boolean isAutomaticDeskewValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOMATICDESKEW);
    }

    public boolean isAutomaticLengthDetectionValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOMATICLENGTHDETECTION);
    }

    public boolean isAutomaticRotateValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOMATICROTATE);
    }

    public boolean isAutoSizeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_AUTOSIZE);
    }

    public boolean isBarcodeDetectionEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BARCODEDETECTIONENABLED);
    }

    public boolean isBarcodeMaxRetriesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BARCODEMAXRETRIES);
    }

    public boolean isBarcodeMaxSearchPrioritiesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BARCODEMAXSEARCHPRIORITIES);
    }

    public boolean isBarcodeSearchModeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BARCODESEARCHMODE);
    }

    public boolean isBarcodeSearchPrioritiesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BARCODESEARCHPRIORITIES);
    }

    public boolean isBarcodeTimeoutValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BARCODETIMEOUT);
    }

    public boolean isBitDepthValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BITDEPTH);
    }

    public boolean isBitDepthReductionValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BITDEPTHREDUCTION);
    }

    public boolean isBitOrderValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BITORDER);
    }

    public boolean isBitOrderCodesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BITORDERCODES);
    }

    public boolean isBrightnessValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_BRIGHTNESS);
    }

    public boolean isCCITTKFactorValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_CCITTKFACTOR);
    }

    public boolean isColorManagementEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_COLORMANAGEMENTENABLED);
    }

    public boolean isCompressionValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_COMPRESSION);
    }

    public boolean isContrastValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_CONTRAST);
    }

    public boolean isCustHalftoneValueSupported(Byte capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_CUSTHALFTONE);
    }

    public boolean isExposureTimeValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_EXPOSURETIME);
    }

    public boolean isExtImageInfoValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_EXTIMAGEINFO);
    }

    public boolean isFeederTypeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_FEEDERTYPE);
    }

    public boolean isFilmTypeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_FILMTYPE);
    }

    public boolean isFilterValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_FILTER);
    }

    public boolean isFlashUsedValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_FLASHUSED);
    }

    public boolean isFlashUsed2ValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_FLASHUSED2);
    }

    public boolean isFlipRotationValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_FLIPROTATION);
    }

    public boolean isFramesValueSupported(TwainFrame<Double> capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_FRAMES);
    }

    public boolean isGammaValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_GAMMA);
    }

    public boolean isHalftonesValueSupported(String capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_HALFTONES);
    }

    public boolean isHighlightValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_HIGHLIGHT);
    }

    public boolean isICCProfileValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_ICCPROFILE);
    }

    public boolean isImageDataSetValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_IMAGEDATASET);
    }

    public boolean isImageFileFormatValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_IMAGEFILEFORMAT);
    }

    public boolean isImageFilterValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_IMAGEFILTER);
    }

    public boolean isImageMergeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_IMAGEMERGE);
    }

    public boolean isImageMergeHeightThresholdValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_IMAGEMERGEHEIGHTTHRESHOLD);
    }

    public boolean isJpegPixelTypeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_JPEGPIXELTYPE);
    }

    public boolean isJpegQualityValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_JPEGQUALITY);
    }

    public boolean isJpegSubsamplingValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_JPEGSUBSAMPLING);
    }

    public boolean isLampStateValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_LAMPSTATE);
    }

    public boolean isLightPathValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_LIGHTPATH);
    }

    public boolean isLightSourceValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_LIGHTSOURCE);
    }

    public boolean isMaxFramesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_MAXFRAMES);
    }

    public boolean isMinimumHeightValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_MINIMUMHEIGHT);
    }

    public boolean isMinimumWidthValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_MINIMUMWIDTH);
    }

    public boolean isMirrorValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_MIRROR);
    }

    public boolean isNoiseFilterValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_NOISEFILTER);
    }

    public boolean isOrientationValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_ORIENTATION);
    }

    public boolean isOverscanValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_OVERSCAN);
    }

    public boolean isPatchcodeDetectionEnabledValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PATCHCODEDETECTIONENABLED);
    }

    public boolean isPatchcodeMaxRetriesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PATCHCODEMAXRETRIES);
    }

    public boolean isPatchcodeMaxSearchPrioritiesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PATCHCODEMAXSEARCHPRIORITIES);
    }

    public boolean isPatchcodeSearchModeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PATCHCODESEARCHMODE);
    }

    public boolean isPatchcodeSearchPrioritiesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PATCHCODESEARCHPRIORITIES);
    }

    public boolean isPatchcodeTimeoutValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PATCHCODETIMEOUT);
    }

    public boolean isPhysicalHeightValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PHYSICALHEIGHT);
    }

    public boolean isPhysicalWidthValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PHYSICALWIDTH);
    }

    public boolean isPixelFlavorValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PIXELFLAVOR);
    }

    public boolean isPixelFlavorCodesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PIXELFLAVORCODES);
    }

    public boolean isPixelTypeValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PIXELTYPE);
    }

    public boolean isPlanarChunkyValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_PLANARCHUNKY);
    }

    public boolean isRotationValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_ROTATION);
    }

    public boolean isShadowValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_SHADOW);
    }

    public boolean isSupportedBarcodeTypesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_SUPPORTEDBARCODETYPES);
    }

    public boolean isSupportedExtImageInfoValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_SUPPORTEDEXTIMAGEINFO);
    }

    public boolean isSupportedPatchcodeTypesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_SUPPORTEDPATCHCODETYPES);
    }

    public boolean isSupportedSizesValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_SUPPORTEDSIZES);
    }

    public boolean isThresholdValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_THRESHOLD);
    }

    public boolean isTilesValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_TILES);
    }

    public boolean isTimefillValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_TIMEFILL);
    }

    public boolean isUndefinedImageSizeValueSupported(Boolean capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_UNDEFINEDIMAGESIZE);
    }

    public boolean isUnitsValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_UNITS);
    }

    public boolean isImageXferMechValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_XFERMECH);
    }

    public boolean isXNativeResolutionValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_XNATIVERESOLUTION);
    }

    public boolean isXResolutionValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_XRESOLUTION);
    }

    public boolean isXScalingValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_XSCALING);
    }

    public boolean isYNativeResolutionValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_YNATIVERESOLUTION);
    }

    public boolean isYResolutionValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_YRESOLUTION);
    }

    public boolean isYScalingValueSupported(Double capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_YSCALING);
    }

    public boolean isZoomFactorValueSupported(Integer capValue) throws DTwainJavaAPIException
    {
        return isCapValueSupported(capValue, CAPS.ICAP_ZOOMFACTOR);
    }
}
