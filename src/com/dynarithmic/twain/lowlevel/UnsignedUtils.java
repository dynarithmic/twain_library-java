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
package com.dynarithmic.twain.lowlevel;

import java.math.BigInteger;

public class UnsignedUtils
{
    private UnsignedUtils()
    {}

    private static final String[] TEXT = {"Valid range is [0, ",
                                          "], inclusive",
                                          "Value (",
                                          ") is outside the valid range of a ",
                                          "-bit unsigned integer.  "};

    private static String composeSuffix(long value)
    {
        return TEXT[0] + value + TEXT[1];
    }

    private static String composeSuffix(String value)
    {
        return TEXT[0] + value + TEXT[1];
    }

    private static String composeMainMessage(long value, int numBits)
    {
        return TEXT[2] + value + TEXT[3] + numBits + TEXT[4];
    }

    private static String composeMainMessage(BigInteger value, int numBits)
    {
        return TEXT[2] + value + TEXT[3] + numBits + TEXT[4];
    }

    private static final BigInteger b64 = new BigInteger("18446744073709551615");

    public static void checkUnsigned64(BigInteger value) throws IllegalArgumentException
    {
        if ( !isUnsigned64(value))
        {
            throw new IllegalArgumentException(composeMainMessage(value, 64) +
                                               composeSuffix(b64.toString()));
        }
    }

    public static void checkUnsigned32(long value) throws IllegalArgumentException
    {
        if ( !isUnsigned32(value))
        {
            throw new IllegalArgumentException(composeMainMessage(value, 32) +
                    composeSuffix(Integer.toUnsignedLong(0x00000000FFFFFFFF)));
        }
    }

    public static void checkUnsigned16(long value) throws IllegalArgumentException
    {
        if ( !isUnsigned16(value))
        {
            throw new IllegalArgumentException(composeMainMessage(value, 16) +
                    composeSuffix(Integer.toUnsignedLong(0x000000000000FFFF)));
        }
    }

    public static void checkUnsigned8(long value) throws IllegalArgumentException
    {
        if ( !isUnsigned8(value))
        {
            throw new IllegalArgumentException(composeMainMessage(value, 8) +
                    composeSuffix(Integer.toUnsignedLong(0x00000000000000FF)));
        }
    }

    public static void checkUnsigned64(long value) throws IllegalArgumentException
    {
        checkUnsigned64(new BigInteger(value + ""));
    }

    public static void checkUnsigned64(int value) throws IllegalArgumentException
    {
        checkUnsigned64((long)value);
    }

    public static void checkUnsigned64(short value) throws IllegalArgumentException
    {
        checkUnsigned64((long)value);
    }

    public static void checkUnsigned64(byte value) throws IllegalArgumentException
    {
        checkUnsigned64((long)value);
    }

    public static void checkUnsigned32(int value) throws IllegalArgumentException
    {
        checkUnsigned32((long)value);
    }

    public static void checkUnsigned32(short value) throws IllegalArgumentException
    {
        checkUnsigned32((long)value);
    }

    public static void checkUnsigned32(byte value) throws IllegalArgumentException
    {
        checkUnsigned32((long)value);
    }

    public static void checkUnsigned16(int value) throws IllegalArgumentException
    {
        checkUnsigned16((long)value);
    }

    public static void checkUnsigned16(short value) throws IllegalArgumentException
    {
        checkUnsigned16((long)value);
    }

    public static void checkUnsigned16(byte value) throws IllegalArgumentException
    {
        checkUnsigned16((long)value);
    }

    public static void checkUnsigned8(int value) throws IllegalArgumentException
    {
        checkUnsigned8((long)value);
    }

    public static void checkUnsigned8(short value) throws IllegalArgumentException
    {
        checkUnsigned8((long)value);
    }

    public static void checkUnsigned8(byte value) throws IllegalArgumentException
    {
        checkUnsigned8((long)value);
    }

    public static boolean isUnsigned64(BigInteger value)
    {
        return !(value.intValue() < 0 || value.compareTo(b64) > 0);
    }

    public static boolean isUnsigned64(long value)
    {
        return isUnsigned64(new BigInteger(value + ""));
    }

    public static boolean isUnsigned64(int value)
    {
        return isUnsigned64((long)value);
    }

    public static boolean isUnsigned64(short value)
    {
        return isUnsigned64((long) value);
    }

    public static boolean isUnsigned64(byte value)
    {
        return isUnsigned64((long) value);
    }


    public static boolean isUnsigned32(long value)
    {
        return !( value < 0 || value > Integer.toUnsignedLong(0x00000000FFFFFFFF));
    }

    public static boolean isUnsigned16(long value)
    {
        return !( value < 0 || value > Integer.toUnsignedLong(0x0000FFFF));
    }

    public static boolean isUnsigned8(long value)
    {
        return !( value < 0 || value > Integer.toUnsignedLong(0x000000FF));
    }

    public static boolean isUnsigned32(int value)
    {
        return isUnsigned32((long)value);
    }

    public static boolean isUnsigned32(short value)
    {
        return isUnsigned32((long)value);
    }

    public static boolean isUnsigned32(byte value)
    {
        return isUnsigned32((long)value);
    }

    public static boolean isUnsigned16(int value)
    {
        return !isUnsigned16((long)value);
    }

    public static boolean isUnsigned16(short value)
    {
        return isUnsigned16((long) value);
    }

    public static boolean isUnsigned16(byte value)
    {
        return isUnsigned16((long) value);
    }

    public static boolean isUnsigned8(int value)
    {
        return isUnsigned8((long)value);
    }

    public static boolean isUnsigned8(short value)
    {
        return isUnsigned8((long) value);
    }

    public static boolean isUnsigned8(byte value)
    {
        return isUnsigned8((long) value);
    }
}
