package com.dynarithmic.twain.lowlevel;

public class TW_RANGE extends TwainLowLevel
{
    private TW_UINT16 ItemType = new TW_UINT16();
    private TW_UINT32 MinValue = new TW_UINT32();
    private TW_UINT32 MaxValue = new TW_UINT32();
    private TW_UINT32 StepSize = new TW_UINT32();
    private TW_UINT32 DefaultValue = new TW_UINT32();
    private TW_UINT32 CurrentValue = new TW_UINT32();

    public TW_RANGE()
    {
    }

    public TW_UINT16 getItemType()
    {
        return ItemType;
    }

    public TW_UINT32 getMinValue()
    {
        return MinValue;
    }

    public TW_UINT32 getMaxValue()
    {
        return MaxValue;
    }

    public TW_UINT32 getStepSize()
    {
        return StepSize;
    }

    public TW_UINT32 getDefaultValue()
    {
        return DefaultValue;
    }

    public TW_UINT32 getCurrentValue()
    {
        return CurrentValue;
    }

    public TW_RANGE setItemType(TW_UINT16 itemType)
    {
        ItemType = itemType;
        return this;
    }

    public TW_RANGE setMinValue(TW_UINT32 minValue)
    {
        MinValue = minValue;
        return this;
    }

    public TW_RANGE setMaxValue(TW_UINT32 maxValue)
    {
        MaxValue = maxValue;
        return this;
    }

    public TW_RANGE setStepSize(TW_UINT32 stepSize)
    {
        StepSize = stepSize;
        return this;
    }

    public TW_RANGE setDefaultValue(TW_UINT32 defaultValue)
    {
        DefaultValue = defaultValue;
        return this;
    }

    public TW_RANGE setCurrentValue(TW_UINT32 currentValue)
    {
        CurrentValue = currentValue;
        return this;
    }

    public TW_RANGE setItemType(int itemType)
    {
        ItemType.setValue(itemType);
        return this;
    }

    public TW_RANGE setMinValue(long minValue)
    {
        MinValue.setValue(minValue);
        return this;
    }

    public TW_RANGE setMaxValue(long maxValue)
    {
        MaxValue.setValue(maxValue);
        return this;
    }

    public TW_RANGE setStepSize(long stepSize)
    {
        StepSize.setValue(stepSize);
        return this;
    }

    public TW_RANGE setDefaultValue(long defaultValue)
    {
        DefaultValue.setValue(defaultValue);
        return this;
    }

    public TW_RANGE setCurrentValue(long currentValue)
    {
        CurrentValue.setValue(currentValue);
        return this;
    }

}
