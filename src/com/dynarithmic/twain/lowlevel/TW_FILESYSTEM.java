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
package com.dynarithmic.twain.lowlevel;

public class TW_FILESYSTEM extends TwainLowLevel
{
    private TW_STR255  InputName = new TW_STR255();
    private TW_STR255  OutputName = new TW_STR255();
    private TW_MEMREF  Context = new TW_MEMREF();
    private TW_INT32   RecursiveOrSubdirectories = new TW_INT32();
    private TW_UINT32  FileTypeOrFileSystemType = new TW_UINT32();
    private TW_UINT32  Size = new TW_UINT32();
    private TW_STR32   CreateTimeDate = new TW_STR32();
    private TW_STR32   ModifiedTimeDate = new TW_STR32();
    private TW_UINT32  FreeSpace = new TW_UINT32();
    private TW_INT32   NewImageSize = new TW_INT32();
    private TW_UINT32  NumberOfFiles = new TW_UINT32();
    private TW_UINT32  NumberOfSnippets = new TW_UINT32();
    private TW_UINT32  DeviceGroupMask = new TW_UINT32();
    private TW_INT8[] Reserved = new TW_INT8[508];

    public TW_FILESYSTEM() {}

    public TW_STR255 getInputName()
    {
        return InputName;
    }

    public TW_STR255 getOutputName()
    {
        return OutputName;
    }

    public TW_MEMREF getContext()
    {
        return Context;
    }

    public TW_INT32 getRecursiveOrSubdirectories()
    {
        return RecursiveOrSubdirectories;
    }

    public TW_UINT32 getFileTypeOrFileSystemType()
    {
        return FileTypeOrFileSystemType;
    }

    public TW_UINT32 getSize()
    {
        return Size;
    }

    public TW_STR32 getCreateTimeDate()
    {
        return CreateTimeDate;
    }

    public TW_STR32 getModifiedTimeDate()
    {
        return ModifiedTimeDate;
    }

    public TW_UINT32 getFreeSpace()
    {
        return FreeSpace;
    }

    public TW_INT32 getNewImageSize()
    {
        return NewImageSize;
    }

    public TW_UINT32 getNumberOfFiles()
    {
        return NumberOfFiles;
    }

    public TW_UINT32 getNumberOfSnippets()
    {
        return NumberOfSnippets;
    }

    public TW_UINT32 getDeviceGroupMask()
    {
        return DeviceGroupMask;
    }

    public TW_INT8[] getReserved()
    {
        return Reserved;
    }

    public TW_FILESYSTEM setInputName(TW_STR255 inputName)
    {
        InputName = inputName;
        return this;
    }

    public TW_FILESYSTEM setOutputName(TW_STR255 outputName)
    {
        OutputName = outputName;
        return this;
    }

    public TW_FILESYSTEM setContext(TW_MEMREF context)
    {
        Context = context;
        return this;
    }

    public TW_FILESYSTEM setRecursiveOrSubdirectories(TW_INT32 recursiveOrSubdirectories)
    {
        RecursiveOrSubdirectories = recursiveOrSubdirectories;
        return this;
    }

    public TW_FILESYSTEM setFileTypeOrFileSystemType(TW_UINT32 fileTypeOrFileSystemType)
    {
        FileTypeOrFileSystemType = fileTypeOrFileSystemType;
        return this;
    }

    public TW_FILESYSTEM setSize(TW_UINT32 size)
    {
        Size = size;
        return this;
    }

    public TW_FILESYSTEM setCreateTimeDate(TW_STR32 createTimeDate)
    {
        CreateTimeDate = createTimeDate;
        return this;
    }

    public TW_FILESYSTEM setModifiedTimeDate(TW_STR32 modifiedTimeDate)
    {
        ModifiedTimeDate = modifiedTimeDate;
        return this;
    }

    public TW_FILESYSTEM setFreeSpace(TW_UINT32 freeSpace)
    {
        FreeSpace = freeSpace;
        return this;
    }

    public TW_FILESYSTEM setNewImageSize(TW_INT32 newImageSize)
    {
        NewImageSize = newImageSize;
        return this;
    }

    public TW_FILESYSTEM setNumberOfFiles(TW_UINT32 numberOfFiles)
    {
        NumberOfFiles = numberOfFiles;
        return this;
    }

    public TW_FILESYSTEM setNumberOfSnippets(TW_UINT32 numberOfSnippets)
    {
        NumberOfSnippets = numberOfSnippets;
        return this;
    }

    public TW_FILESYSTEM setDeviceGroupMask(TW_UINT32 deviceGroupMask)
    {
        DeviceGroupMask = deviceGroupMask;
        return this;
    }

    public TW_FILESYSTEM setReserved(TW_INT8[] reserved)
    {
        Reserved = reserved;
        return this;
    }

    public TW_FILESYSTEM setInputName(String inputName)
    {
        InputName.setValue(inputName);
        return this;
    }

    public TW_FILESYSTEM setOutputName(String outputName)
    {
        OutputName.setValue(outputName);
        return this;
    }

    public TW_FILESYSTEM setRecursiveOrSubdirectories(int recursiveOrSubdirectories)
    {
        RecursiveOrSubdirectories.setValue(recursiveOrSubdirectories);
        return this;
    }

    public TW_FILESYSTEM setRecursiveOrSubdirectories(TW_BOOL recursiveOrSubdirectories)
    {
        RecursiveOrSubdirectories.setValue(recursiveOrSubdirectories.getValue()?1:0);
        return this;
    }

    public TW_FILESYSTEM setRecursiveOrSubdirectories(boolean recursiveOrSubdirectories)
    {
        RecursiveOrSubdirectories.setValue(recursiveOrSubdirectories?1:0);
        return this;
    }

    public TW_FILESYSTEM setFileTypeOrFileSystemType(long fileTypeOrFileSystemType)
    {
        FileTypeOrFileSystemType.setValue(fileTypeOrFileSystemType);
        return this;
    }

    public TW_FILESYSTEM setFileTypeOrFileSystemType(int fileTypeOrFileSystemType)
    {
        FileTypeOrFileSystemType.setValue(fileTypeOrFileSystemType);
        return this;
    }

    public TW_FILESYSTEM setSize(long size)
    {
        Size.setValue(size);
        return this;
    }

    public TW_FILESYSTEM setCreateTimeDate(String createTimeDate)
    {
        CreateTimeDate.setValue(createTimeDate);
        return this;
    }

    public TW_FILESYSTEM setModifiedTimeDate(String modifiedTimeDate)
    {
        ModifiedTimeDate.setValue(modifiedTimeDate);
        return this;
    }

    public TW_FILESYSTEM setFreeSpace(long freeSpace)
    {
        FreeSpace.setValue(freeSpace);
        return this;
    }

    public TW_FILESYSTEM setNewImageSize(int newImageSize)
    {
        NewImageSize.setValue(newImageSize);
        return this;
    }

    public TW_FILESYSTEM setNumberOfFiles(long numberOfFiles)
    {
        NumberOfFiles.setValue(numberOfFiles);
        return this;
    }

    public TW_FILESYSTEM setNumberOfSnippets(long numberOfSnippets)
    {
        NumberOfSnippets.setValue(numberOfSnippets);
        return this;
    }

    public TW_FILESYSTEM setDeviceGroupMask(long deviceGroupMask)
    {
        DeviceGroupMask.setValue(deviceGroupMask);
        return this;
    }

    public native TW_FILESYSTEM fakeFunc(TW_UINT8[] vals);
}
