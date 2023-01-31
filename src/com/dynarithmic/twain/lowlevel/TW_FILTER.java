package com.dynarithmic.twain.lowlevel;

public class TW_FILTER extends TwainLowLevel
{
    private TW_UINT32 Size;
    private TW_UINT32 DescriptorCount;
    private TW_UINT32 MaxDescriptorCount;
    private TW_UINT32 Condition;
    private TW_HANDLE hDescriptors;

    public TW_FILTER() {}

    public TW_UINT32 getSize()
    {
        return Size;
    }
    public TW_UINT32 getDescriptorCount()
    {
        return DescriptorCount;
    }
    public TW_UINT32 getMaxDescriptorCount()
    {
        return MaxDescriptorCount;
    }
    public TW_UINT32 getCondition()
    {
        return Condition;
    }
    public TW_HANDLE gethDescriptors()
    {
        return hDescriptors;
    }
    public TW_FILTER setSize(TW_UINT32 size)
    {
        Size = size;
        return this;
    }
    public TW_FILTER setDescriptorCount(TW_UINT32 descriptorCount)
    {
        DescriptorCount = descriptorCount;
        return this;
    }
    public TW_FILTER setMaxDescriptorCount(TW_UINT32 maxDescriptorCount)
    {
        MaxDescriptorCount = maxDescriptorCount;
        return this;
    }
    public TW_FILTER setCondition(TW_UINT32 condition)
    {
        Condition = condition;
        return this;
    }
    public TW_FILTER sethDescriptors(TW_HANDLE hDescriptors)
    {
        this.hDescriptors = hDescriptors;
        return this;
    }

    public TW_FILTER setSize(long size)
    {
        Size.setValue(size);
        return this;
    }

    public TW_FILTER setDescriptorCount(long descriptorCount)
    {
        DescriptorCount.setValue(descriptorCount);
        return this;
    }

    public TW_FILTER setMaxDescriptorCount(long maxDescriptorCount)
    {
        MaxDescriptorCount.setValue(maxDescriptorCount);
        return this;
    }

    public TW_FILTER setCondition(long condition)
    {
        Condition.setValue(condition);
        return this;
    }

    public TW_FILTER sethDescriptors(long hDescriptors)
    {
        this.hDescriptors.setValue(hDescriptors);
        return this;
    }
}
