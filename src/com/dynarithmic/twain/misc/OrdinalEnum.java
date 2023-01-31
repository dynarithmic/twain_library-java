package com.dynarithmic.twain.misc;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public interface OrdinalEnum
{
    public int value();

    static <E extends Enum<E>> Map<Integer, E> getValues(Class<E> clzz)
    {
        Map<Integer, E> m = new HashMap<>();
        for(E e : EnumSet.allOf(clzz))
            m.put(((OrdinalEnum)e).value(), e);
        return m;
    }
}
