# Dynarithmic TWAIN Library Java interface
This repositiory contains the new version of the Java Native Interface (JNI) bridge to the Dynarithmic TWAIN Library.  

Note that there is very little documentation to the new Java/JNI bridge.  If you desire to use this early version of the Java/JNI code, here is what you will need to get started:

1. [Version 5.9.3 of the DTWAIN library](https://github.com/dynarithmic/twain_library?tab=readme-ov-file#anchor-dtwain-setup).  (For this release of the Java interface, you should be using [this release version](https://github.com/dynarithmic/twain_library/releases/tag/v5.9.2) of the DTWAIN library).

Choose either the [no_vcruntime or require_vcruntime](https://github.com/dynarithmic/twain_library/tree/master?tab=readme-ov-file#how-do-i-setup-dtwain-library-setup-building-the-application-and-running-the-application-1) version of the DTWAIN library.

From the DTWAIN library, you will need one or more of the dynamic link libraries (dtwain32.dll, dtwain32u.dll, dtwain64.dll, or dtwain64u.dll) available, plus the <a href="https://github.com/dynarithmic/twain_library/tree/master/text_resources" target="_blank">text resources</a> should reside in the same folder as the dtwain DLL.  <br><br><b>When updating to the latest version of the Java interface or DTWAIN's dynamic link libraries, you **must** always use the latest version of the text resource files.</b><br>

2. The JNI dynamic link libraries (DLL's) found in the [32-bit (DTWAINJNI-Binaries-x32.zip)](https://github.com/dynarithmic/twain_library-java/releases/latest/download/DTWAINJNI-Binaries-x32.zip) and [64-bit (DTWAINJNI-Binaries-x64.zip)](https://github.com/dynarithmic/twain_library-java/releases/latest/download/DTWAINJNI-Binaries-x64.zip) zip files.  

5. <a href="https://github.com/dynarithmic/twain_library-java/tree/master/external_jars" target="_blank">The dtwain-java-1.9.8 jar file and miscellaneous third-party libraries</a> must be incorporated into your Java project.  (Note that you must be familiar with adding third-party libraries to your Java project/application within your development environment).
1. The <a href="https://github.com/dynarithmic/twain_library-java/blob/master/JNI_Source" target="_blank">dtwainjni.info</a> file must be accessible by the DLL's mentioned in the previous step.  The **dtwainjni.info** file basically is a bridge between the Java function and class signatures and the C++ translation of those function and class signatures to C++.  Without this file, usage of any of the Java functions that communicate to the JNI layer will throw a Java exception.  The **dtwainjni.info** file must be placed in the same directory as the JNI DLL that will be loaded at runtime.

Make sure you always use the latest version of **dtwainjni.info**.  Since this file can undergo changes between different versions of this library, it is important that you are running the **dtwainjni.info** that matches the version of the Java interface to DTWAIN.

[Description of the latest updates to the Java interface](https://github.com/dynarithmic/twain_library-java/blob/master/updates/updates.txt).

Please note that the DTWAIN DLL's and the JNI DLL's should reside in a location specified by a directory in your system's **PATH** or in a location that can be found by the [Windows search rules](https://learn.microsoft.com/en-us/windows/win32/dlls/dynamic-link-library-search-order).  Unless your JVM's class path is specified as a path in your **PATH** environment variable, do not place the DLL's in your JVM's class path, as Windows will not find them.


----
## Very simple Java application using DTWAIN

Here is an example of the simplest Java application you can create to scan a one page image to a Windows BMP file:

```java
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;

public class TinyApplication
{
    public static void main(String[] args)
    {
        try
        {
            // Open a session
            TwainSession session = new TwainSession();

            // Select a TWAIN data source
            TwainSource source = session.selectSource();
            
            // If a source was selected, it will automatically be opened, ready to be used
            if ( source.isOpened() )
            {
                // acquire to a BMP file to the current working directory, with name "test.bmp"
                source.acquire();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
```
The above example, will save to a file called "test.bmp".  The file will reside in the current working directory, usually the one where your `class` files are located.

The following program sets the name of the file instead of the default `test.bmp`:

```java
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;

public class TinyApplication2
{
    String outputDir = "c:\\test\\out.bmp"; // <-- Change this to something more appropriate for your system
    
    public static void main(String[] args)
    {
        try
        {
            // Open a session
            TwainSession session = new TwainSession();

            // Session will start
            TwainSource source = session.selectSource();
            if ( source.isOpened() )
            {
               // Set the file acquire options. By default, the file will be in BMP format
               source.getAcquireCharacteristics().
                        getFileTransferOptions().
                          setName(outputDir);
                        
                // acquire to a BMP file
                source.acquire();
            }
        }            
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
```


The following program is similar to the previous program, with the only difference being the check for whether the acquisition started successfully or not.

```java
import com.dynarithmic.twain.DTwainConstants.ErrorCode;
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.highlevel.TwainSource.AcquireReturnInfo;

public class TinyApplication3
{
    String outputDir = "c:\\test\\out.bmp"; // <-- Change this to something more appropriate for your system
    public static void main(String[] args)
    {
        try
        {
            // Open a session
            TwainSession session = new TwainSession();

            // Session will start
            TwainSource source = session.selectSource();
            if ( source.isOpened() )
            {
               // Set the file acquire options. By default, the file will be in BMP format
               source.getAcquireCharacteristics().
                        getFileTransferOptions().
                           setName(outputDir);
                        
               // acquire to a BMP file
               AcquireReturnInfo retInfo = source.acquire();
                
               if ( retInfo.getReturnCode() == ErrorCode.ERROR_NONE )
                  System.out.println("Acquisition process started and ended successfully");
               else
                  System.out.println("Acquisition process failed with error: " + retInfo.getReturnCode());
            }
        }            
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
```

The following program sets the file type to a TIFF file, compressed using LZW compression:

```java
import com.dynarithmic.twain.highlevel.TwainSession;
import com.dynarithmic.twain.highlevel.TwainSource;
import com.dynarithmic.twain.DTwainConstants.FileType;

public class TinyApplication3
{
    String outputDir = "c:\\test\\out.tif"; // <-- Change this to something more appropriate for your system
    
    public static void main(String[] args)
    {
        try
        {
            // Open a session
            TwainSession session = new TwainSession();

            // Session will start
            TwainSource source = session.selectSource();
            if ( source.isOpened() )
            {
               // Set the file acquire options.  The file will be in TIFF-LZW format
               source.getAcquireCharacteristics().
                        getFileTransferOptions().
                          setType(FileType.TIFFLZW).
                          setName(outputDir);
                        
                // acquire to a TIFF-LZW file
                source.acquire();
            }
        }            
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
```

There are other examples of error checking, whether a session is successfully opened, processing messages and errors while the scanning is occurring, scanning to a single multipage file, selecting the image file type or acquire raw bitmap data, etc. in the  **com.dtwain.demos** package.


## Setting the JNI version to use

By default, if the Java runtime being used is 32-bit, the 32-bit Unicode version of the JNI DLL's are used.  Similarly, if the Java runtime being used is 64-bit, the 64-bit Unicode version of the JNI DLL's will be used.

If you want to change the JNI version to be used at runtime, the **DTWAINGlobalOptions** class has two static methods named *setJNIVersion* that must be called before a TWAIN session has been started (an instantiation of a **TwainSession** object).  One static method uses an integer, the other uses a string, to set the JNIVersion.

The various settings for the JNI Version are as follows:


| String name | Integer value   | Description                                              |
|-------------|:---------------:|----------------------------------------------------------|
| jni_32      | 0               | Use the 32-bit ANSI JNI DLL (dtwainjni32.dll)            |
| jni_32u     | 1               | Use the 32-bit Unicode JNI DLL (dtwainjni32u.dll)        |
| jni_64      | 2               | Use the 64-bit ANSI JNI DLL (dtwainjni64.dll)            |
| jni_64u     | 3               | Use the 64-bit Unicode JNI DLL (dtwainjni64u.dll)        |
| jni_32d     | 4               | Use the 32-bit debug ANSI JNI DLL (dtwainjni32d.dll)     |
| jni_32ud    | 5               | Use the 32-bit debug Unicode JNI DLL (dtwainjni32ud.dll) |
| jni_64d     | 6               | Use the 64-bit debug ANSI JNI DLL (dtwainjni64d.dll)     |
| jni_64ud    | 7               | Use the 64-bit debug Unicode JNI DLL (dtwainjni64ud.dll) |

<small>Note: The debug libraries (jni_32d, jni_32ud, jni_64d, and jni_64ud) are meant to be used in conjunction with the debug versions of the JNI DLLs.  This is useful if you wish to debug the JNI DLL's alongside the running of your Java application, and not desire the optimizations applied in the release versions of the JNI DLL's.  

Note that this is only useful for programmers who are familiar with debugging C++ code along with a running Java application (for example, from the **Debug** menu in Visual Studio, **Attach** to the running javaw.exe process is just one way to accomplish this).
</small>

A typical program to set the JNI Version could look like this (you can also check the <a href="https://github.com/dynarithmic/twain_library-java/blob/master/src/com/dtwain/demos/SetJNIVersionDemo.java" target="_blank">demo program</a> to see how this is done):

```java
import com.dynarithmic.twain.DTwainGlobalOptions;
//...
public static void main(String [] args)
{
    // sets the JNI Version to use to be the 32-bit ANSI version
    DTwainGlobalOptions.setJNIVersion(JNIVersion.JNI_32); // This is equivalent to 0
    
    // Does exactly the same thing as the line above
    DTwainGlobalOptions.setJNIVersion("jni_32"); 
    
    // should print "0" to the console
    System.out.println("JNI Version used: " + DTwainGlobalOptions.getJNIVersion()); 
    
    // The rest of the program ...
    //...
}
```
Note that the **setJNIVersion** will default to using the Unicode JNI DLL if the integer value or the string passed to **setJNIVersion** is unknown or invalid.  

A **DTwainIncompatibleJNIException** is thrown if **setJNIVersion** is called with a JNI version that does not match the bit-ness of the JVM being run.  For example, if the application is running the 64-bit JVM, and either **JNIVersion.JNI_32** or **JNIVersion.JNI_32U** is used in setJNIVersion, the **DTwainIncompatibleJNIException** is thrown.  The bit-ness of the JVM being run for the application must match one of the JNI types.

Given this, the application is free to use whatever means it deems appropriate if it requires the JNI version to be set at run time.  For example, an application may want to use a resource file or property file to retrieve the JNI version, or in another scenario, the Java application may want to take a command-line argument, denoting the JNI version to use, and use it in the call to **DTwainGlobalOptions**.


----
### Java source code
In the src directory, you will find the following packages:
1. **com.dtwain.demos**  -- various test programs
2. **com.dtwain.demos.fulldemo** -- A full demo program, similar to the DTWDEMO program.
3. **com.dynarithmic.twain** -- The main code that communicates with the JNI layer.  This includes the native methods, which are declared in **DTwainJavaAPI.java**
4. **com.dynarithmic.twain.exceptions** -- The DTWAIN Java exception classes.
5. **com.dynarithmic.twain.highlevel** -- The high level functional interface to the DTWAIN library.  
6. **com.dynarithmic.twain.highlevel.capabilityinterface** -- The high level functional interface to the capabilities of a TWAIN device.  
6. **com.dynarithmic.twain.highlevel.acquireoptions** -- The high level functional interface to the setup of a device before acquiring images.
7. **com.dynarithmic.twain.lowlevel** -- The low level functional interface to the DTWAIN library.  Most of the classes here mimic the classes found in the TWAIN                                                specification, such as TW_USERINTERFACE, TW_DEVICEEVENT, TW_UINT32, TW_STR255, etc.  Useful if you know what you're doing and                                          want to call the low-level TWAIN Data Source Managers directly using DTWAIN_CallDSMProc

----
### Getting started with the demo programs

I would suggest you look at the various programs in the **com.dtwain.demos** package (they are named appropriately).  In particular, start with **com.dtwain.demos.SimpleFileAcquireBMPDemo.java** to see how to acquire to a BMP file (the name of the file is hard-coded, so I suggest you change the code appropriately)

----
### Where is the documentation?
There is very little documentation, so the way to learn to use the library at this present stage is to familiarize yourself with the examples in **com.dtwain.demos** package.  

However the code present in the demos and in the library itself is almost full-featured.  Selecting a TWAIN source, getting, setting, querying the capability information, acquiring to files, image buffers, callbacks, logging, etc.  are all supported.  

----
----

# Building the JNI layer source code

Most users will not need to build the JNI layer source code. Prebuilt binaries are provided with each release and can be used directly from Java applications.

The JNI source code is available for developers who wish to customize or rebuild the JNI DLLs.

## Prerequisites

The following software must be installed:

* Microsoft Visual Studio 2019, 2022, or 2026 with C/C++ development tools
* CMake 3.25 or later
* A Java Development Kit (JDK) containing the JNI headers (`jni.h`)

The JDK installation should be available through the standard `JAVA_HOME` environment variable.

## Building Using Batch Files

The repository contains a set of batch files for common build configurations.

Examples:

```text
build_vs2022-x64-crt-unicode.bat
build_vs2022-x64-nocrt-unicode.bat
build_vs2026-x32-crt-ansi.bat
```

Each batch file automatically:

1. Configures the project using the appropriate CMake preset.
2. Builds the MinSizeRel configuration.
3. Builds the Debug configuration.

## Building Using CMake

Advanced users may invoke CMake directly.

Example:

```text
cmake --preset vs2022-x64-crt-unicode
cmake --build --preset vs2022-x64-crt-unicode-release
cmake --build --preset vs2022-x64-crt-unicode-debug
```

The available presets support:

* Visual Studio 2019, 2022, and 2026
* 32-bit and 64-bit builds
* ANSI and Unicode builds
* CRT and No-CRT runtime options

## Output Files

The generated DLL names follow the traditional JNI DLL naming convention:

```text
dtwainjni32.dll
dtwainjni32u.dll
dtwainjni32d.dll
dtwainjni32ud.dll

dtwainjni64.dll
dtwainjni64u.dll
dtwainjni64d.dll
dtwainjni64ud.dll
```

Corresponding import libraries (`.lib`) and debugging symbols (`.pdb`) are also generated.

## Configuration Options

The build system supports several optional configuration settings, including:

* ANSI or Unicode builds
* CRT or No-CRT runtime libraries
* CRC validation of the dtwainjni.info data file
* Regeneration of the dtwainjni.info data file if CRC is not valid

These options may be modified through CMake presets or by editing the CMake configuration directly.

## Optional
#### DTWAIN Library rebuild
The `dtwain32u.dll`, `dtwain32ud.dll`, `dtwain64u.dll`, and `dtwain64ud.dll` files that are included in the Java library interface are satisfactory without having to rebuild those components.  However if desired (usually for in-depth debugging purposes), these DLL's can also be rebuilt.  The instructions [here](https://github.com/dynarithmic/twain_library_source#rebuild-source) detail rebuilding of the DTWAIN library.

Note that the JNI source code found in the [master repository](https://github.com/dynarithmic/twain_library-java) is compatible with the DTWAIN library created with the DTWAIN source code found in the  [main](https://github.com/dynarithmic/twain_library_source) branch of the source repository.  

For the [JNI development](https://github.com/dynarithmic/twain_library-java/tree/development) branch, you should always choose the DTWAIN source code in the DTWAIN source code [development branch](https://github.com/dynarithmic/twain_library_source/tree/development) if rebuilding the DTWAIN DLL's.

----
----

### To do:

1. Implement more robust buffer transfer.  Currently, the com.dynarithmic.twain.highlevel class **BufferedTransferInfo.java**, does basic buffer transfers (compressionless transfers, and compressed transfers), but the implementation may lack certain features available for buffer transfer (such as file transfers using a memory buffer), and transferring using tiles instead of strips.

2. There is very little DAT_FILESYSTEM support, other than identifying the "cameras" used when obtaining images (in **com.dynarithmic.twain.highlevel.DeviceCameraInfo**)
