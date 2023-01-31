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
