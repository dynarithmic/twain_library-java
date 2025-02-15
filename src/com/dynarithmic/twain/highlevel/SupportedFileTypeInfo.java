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
import java.util.List;

import com.dynarithmic.twain.DTwainConstants.FileType;

public class SupportedFileTypeInfo
{
    FileType filetype;
    String filetypename;
    List<String> fileExtensions = new ArrayList<>();

    public FileType getType() { return filetype; }
    public String getName() { return filetypename; }
    public SupportedFileTypeInfo setName(String name) { filetypename = name; return this;}
    public SupportedFileTypeInfo setType(FileType f) { this.filetype = f; return this; }
    public SupportedFileTypeInfo addExtension(String ext) { fileExtensions.add(ext); return this;}
    public List<String> getExtensions()
    {
        List<String> tempList = new ArrayList<>(fileExtensions);
        return tempList;
    }
}
