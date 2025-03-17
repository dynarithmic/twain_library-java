# Dynarithmic TWAIN Library Java interface
This repositiory contains the new version of the Java Native Interface (JNI) bridge to the Dynarithmic TWAIN Library.  

Note that there is very little documentation to the new Java/JNI bridge.  If you desire to use this early version of the Java/JNI code, here is what you will need to get started:

1. <a href="https://github.com/dynarithmic/twain_library/tree/master#how-do-i-get-set-up-using-dtwain" target="_blank">Version 5.6 or higher of the DTWAIN library</a>.  

Choose either the [full_logging or partial_logging](https://github.com/dynarithmic/twain_library?tab=readme-ov-file#how-do-i-get-set-up-using-dtwain) version of the DTWAIN library.

From the DTWAIN library, you will need one or more of the dynamic link libraries (dtwain32.dll, dtwain32u.dll, dtwain64.dll, or dtwain64u.dll) available, plus the <a href="https://github.com/dynarithmic/twain_library/tree/master/text_resources" target="_blank">text resources</a> should reside in the same folder as the dtwain DLL.  <br><br><b>When updating to the latest version of the Java interface, you must always use the latest version of the text resource files.</b><br>

2. The JNI dynamic link libraries in the  32bit and 64bit directories found <a href="https://github.com/dynarithmic/twain_library-java/tree/master/JNI_DLL" target="_blank">here</a>.<br>
5. <a href="https://github.com/dynarithmic/twain_library-java/tree/master/external_jars" target="_blank">The dtwain-java-1.6.jar file and miscellaneous third-party libraries</a> must be incorporated into your Java project.  (Note that you must be familiar with adding third-party libraries to your Java project/application within your development environment).
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
## Rebuilding the JNI layer

The JNI layer (i.e. the **dtwainjnixx.dll** files) is built using **Microsoft Visual Studio 2019**.  The minimum Visual Studio platform used is **Visual Studio 2019**.  The Visual Studio solution file is called **DTWAINJNI_Solution.sln**, and is located <a href="https://github.com/dynarithmic/twain_library-java/tree/master/JNI_Source/classes/com/dynarithmic/jnicode" target="_blank">here</a>.

If you want to build the JNI layer yourself, make sure your C++ compiler setup is able to access the various header files provided by JNI, such as **jni.h**.  

In addition, the following environment variables must be set before building the JNI DLLs:

1) **JDK_INCLUDE_DIR**, which points to the location of the **jni.h** file that comes with the JDK.
2) **DTWAIN_INCLUDE_DIR**, which points to the directory where the base DTWAIN library header files are located.  This is usually where your installation of DTWAIN has placed the <a href="https://github.com/dynarithmic/twain_library/tree/master/c_cpp_includes" target="_blank">c_cpp_include</a> directory.


So for example:  
```batch
SET JDK_INCLUDE_DIR=c:\java\jdk1.8\include
SET DTWAIN_INCLUDE_DIR=c:\dtwain\c_cpp_includes
```
should be issued on the command-line before starting Visual Studio and building your project.


#### <u>Turning on/off dtwainjni.info corruption checking:</u>

By default, the Java native call to the JNI function **DTWAIN_LoadLibrary** will always check for the **dtwainjni.info** file being changed or corrupted.
The only way to turn this checking off is to edit [dtwainjni_config.h](https://github.com/dynarithmic/twain_library-java/blob/master/JNI_Source/classes/com/dynarithmic/jnicode/dtwainjni_config.h) and set the **CONFIG_CHECKCRC**  macro to 0:

`#define CONFIG_CHECKCRC 0`

Once this is set, the JNI DLL's must be rebuilt and then utilized by the Java application.

If you edit the **dtwainjni.info** file, you may need to reset the CRC value.  To do this, you must set the `CONFIG_REFRESHCRC` macro to 1:

`#define CONFIG_REFRESHCRC 1`

After rebuilding the JNI DLL's, you must run your Java application to allow the **dtwainjni.info** file to be rebuilt.  An exception will be thrown to Java, indicating that the current dtwainjni.info file is invalid, and a new file, **dtwainjni_new.info**, was created (it should be created in the same directory where **dtwainjni.info** resides).  You would then rename the **dtwainjni_new.info** to **dtwainjni.info** so that the Java application no longer throws an exception.

Caution:  If you are using JNI DLL's that have the dtwainjni.info checks turned off, or you edit the **dtwainjni.info** file, there is a large risk that the Java code may not work correctly.  It is highly important that you know *exactly* what you are doing in terms of editing the dtwainjni.info file, as this file defines all the method signatures and functions to allow the JNI layer to communicate with Java.

Please note that if you have never built a JNI DLL, I highly recommend that you build a simple one first (Oracle has examples of using JNI) **before** you embark on attempting to build the DTWAIN JNI layer yourself.   There are a few things required (for example, the Oracle JNI header files) before a build can be successful

Having said this, assistance in building the JNI DLL will be minimal, at best.  I suggest you **not** change the C++ code if you are not confident or not familiar with how to interface C++ to Java using JNI.


----

### To do:

1. Implement more robust buffer transfer.  Currently, the com.dynarithmic.twain.highlevel class **BufferedTransferInfo.java**, does basic buffer transfers (compressionless transfers, and compressed transfers), but the implementation may lack certain features available for buffer transfer (such as file transfers using a memory buffer), and transferring using tiles instead of strips.

2. There is very little DAT_FILESYSTEM support, other than identifying the "cameras" used when obtaining images (in **com.dynarithmic.twain.highlevel.DeviceCameraInfo**)
