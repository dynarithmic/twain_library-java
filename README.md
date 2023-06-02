# Dynarithmic TWAIN Library Java interface
This repositiory contains an alpha version of the new Java Native Interface (JNI) bridge to the Dynarithmic TWAIN Library.  This Java/JNI bridge will eventually replace the old version that is currently available in the main <a href="https://github.com/dynarithmic/twain_library/tree/master/language_bindings_and_examples/Java" target="_blank">twain_library repository.</a>

Note that there is very little documentation to the new Java/JNI bridge.  If you desire to use this early version of the Java/JNI code, here is what you will need to get started:

1. <a href="https://github.com/dynarithmic/twain_library/tree/master/binaries" target="_blank">Version 5.3.0.5 or higher of the DTWAIN library</a>.  
From the DTWAIN library, you will need one or more of the dynamic link libraries (dtwain32.dll, dtwain32u.dll, dtwain64.dll, or dtwain64u.dll) available, plus the <a href="https://github.com/dynarithmic/twain_library/tree/master/text_resources" target="_blank">text resources</a> should reside in the same folder as the dtwain DLL.

2. The JNI dynamic link libraries in the  32bit and 64bit directories found <a href="https://github.com/dynarithmic/twain_library-java/tree/master/JNI_DLL" target="_blank">here</a>.
5. <a href="https://github.com/dynarithmic/twain_library-java/tree/master/external_jars" target="_blank">The dtwain-java-1.0.jar file and miscellaneous third-party libraries</a> must be incorporated into your Java project.  (Note that you must be familiar with adding third-party libraries to your Java project/application within your development environment).
1. The <a href="https://github.com/dynarithmic/twain_library-java/blob/master/JNI_Source" target="_blank">dtwainjni.info</a> file must be accessible by the DLL's mentioned in the previous step.  The **dtwainjni.info** file basically is a bridge between the Java function and class signatures and the C++ translation of those function and class signatures to C++.  Without this file, usage of any of the Java functions that communicate to the JNI layer will throw a Java exception.  The **dtwainjni.info** file must be placed in the same directory as the JNI DLL that will be loaded at runtime.
----

## Setting the JNI version to use

By default, the 32-bit Unicode version of the JNI DLL's are used.  This means that by default, the Java application will be using the 32-bit TWAIN system (meaning that you can access 32-bit TWAIN devices).

If you require access to 64-bit TWAIN devices, on startup your application must use the 64-bit JNI DLLs.  To do this, the **DTWAINGlobalOptions** class has two static methods named *setJNIVersion* that must be called before a TWAIN session has been started (an instantiation of a **TwainSession** object).  One static method uses an integer, the other uses a string, to set the JNIVersion.

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
    // sets the JNI Version to use to be the 32-bit Unicode version
    DTwainGlobalOptions.setJNIVersion(JNIVersion.JNI_32U); // This is equivalent to 1
    
    // Does exactly the same thing as the line above
    DTwainGlobalOptions.setJNIVersion("jni_32u"); 
    
    // should print "1" to the console
    System.out.println("JNI Version used: " + DTwainGlobalOptions.getJNIVersion()); 
    
    // The rest of the program ...
    //...
}
```
Note that the **setJNIVersion** will default to using the 32-bit Unicode JNI DLL's if the integer value or the string passed to **setJNIVersion** is unknown or invalid.  

Given this, the application is free to use whatever means it deems appropriate if it requires the JNI version to be set at run time.  For example, and application may want to use a resource file or property file to retrieve the JNI version, or in another scenario, the Java application may want to take a command-line argument, denoting the JNI version to use, and use it in the call to **DTwainGlobalOptions**.


----
### Java source code
In the src directory, you will find the following packages:
1. **com.dtwain.demos**  -- various test programs
2. **com.dtwain.demos.fulldemo** -- A full demo program, similar to the DTWDEMO program.
3. **com.dynarithmic.twain** -- The main code that communicates with the JNI layer.  This includes the native methods, which are declared in **DTwainJavaAPI.java**
4. **com.dynarithmic.twain.exceptions** -- The DTWAIN Java exception classes.
5. **com.dynarithmic.twain.highlevel** -- The high level functional interface to the DTWAIN library.  
6. **com.dynarithmic.twain.highlevel.capbilityinterface** -- The high level functional interface to the capabilities of a TWAIN device.  
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
### Rebuilding the JNI layer

The JNI layer (i.e. the **dtwainjnixx.dll** files) is built using **Microsoft Visual Studio 2019**.  The minimum Visual Studio platform used is **Visual Studio 2019**.  The Visual Studio solution file is called **DTWAINJNI_Solution.sln**, and is located <a href="https://github.com/dynarithmic/twain_library-java/tree/master/JNI_Source/classes/com/dynarithmic/jnicode" target="_blank">here</a>.

If you are daring to do a build of the JNI layer yourself, make sure your C++ compiler setup is able to access the various header files provided by JNI, such as **jni.h**.  

In addition, the following environment variables must be set before building the JNI DLLs:

1) **JDK_INCLUDE_DIR**, which points to the location of the **jni.h** file that comes with the JDK.
2) **DTWAIN_INCLUDE_DIR**, which points to the directory where the base DTWAIN library header files are located.  This is usually where your installation of DTWAIN has placed the <a href="https://github.com/dynarithmic/twain_library/tree/master/c_cpp_includes" target="_blank">c_cpp_include</a> directory.
3) **DTWAIN_LIBRARY_DIR**, which points to the location of the DTWAIN import library files:

```plaintext
dtwain32.lib
dtwain32u.lib
dtwain32ud.lib
dtwain32d.lib
dtwain64.lib
dtwain64u.lib
dtwain64ud.lib
dtwain64d.lib
```
These files are included in the <a href="https://github.com/dynarithmic/twain_library/blob/master/binaries/32bit/release_libraries.zip" target="_blank">32-bit release libraries</a> and the <a href="https://github.com/dynarithmic/twain_library/blob/master/binaries/64bit/release_libraries.zip" target="_blank">64-bit release libraries.</a>

(Note that the libraries that end with the letter **'d'** (for example **dtwain32d.lib**) are the debug libraries that are available in the <a href="https://github.com/dynarithmic/twain_library_source/tree/main/binaries" target="_blank">twain_library_source repository</a>).

So for example:  
```batch
SET JDK_INCLUDE_DIR=c:\java\jdk1.8\include
SET DTWAIN_INCLUDE_DIR=c:\dtwain\c_cpp_includes
SET DTWAIN_LIBRARY_DIR=c:\dtwain\libs
```
should be issued on the command-line before starting Visual Studio and building your project.

Please note that if you have never built a JNI DLL, I highly recommend that you build a simple one first (Oracle has examples of using JNI) **before** you embark on attempting to build the DTWAIN JNI layer yourself.   There are a few things required (for example, the Oracle JNI header files) before a build can be successful

Having said this, assistance in building the JNI DLL will be minimal, at best.  I suggest you **not** change the C++ code if you are not confident or not familiar with how to interface C++ to Java using JNI.


----

### To do:

1. Fully implement DTWAIN_GetExtendedImageInfo in the Java layer.  This will allow devices that support this operation to query more information once the scanned page has been obtained.  Currently this functionality is partially done using the com.dynarithmic.highlevel.ExtendedImageInfo class, but usage of this is stubbed out in the C++ JNI layer.
2. Implement more robust buffer transfer.  Currently, the com.dynarithmic.twain.highlevel class **BufferedTransferInfo.java**, does basic buffer transfers (compressionless transfers, and compressed transfers), but the implementation may lack certain features available for buffer transfer (such as file transfers using a memory buffer), and transferring using tiles instead of strips.
3. There is very little DAT_FILESYSTEM support, other than identifying the "cameras" used when obtaining images (in **com.dynarithmic.twain.highlevel.DeviceCameraInfo**)
4. As mentioned in 1., add the extended image capabilities to com.dynarithmic.twain.highlevel.capabilityinterface
