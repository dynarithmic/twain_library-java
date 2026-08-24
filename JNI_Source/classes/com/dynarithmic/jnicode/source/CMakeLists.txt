cmake_minimum_required(VERSION 3.25.1)

if(POLICY CMP0144)
    cmake_policy(SET CMP0144 NEW)
endif()

if(POLICY CMP0167)
    cmake_policy(SET CMP0167 OLD)
endif()

project(dtwainjni VERSION 1.9.7 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

option(DTWAINJNI_BUILD_UNICODE
       "Build DTWAIN JNI DLL as Unicode instead of ANSI"
       ON)

option(DTWAINJNI_USE_DYNAMIC_CRT
       "Build DTWAIN JNI using the dynamic Visual C++ runtime DLL"
       ON)

if(CMAKE_SIZEOF_VOID_P EQUAL 8)
    set(DTWAINJNI_BITNESS "64")
else()
    set(DTWAINJNI_BITNESS "32")
endif()

if(DTWAINJNI_BUILD_UNICODE)
    set(DTWAINJNI_CHAR_SUFFIX "u")
else()
    set(DTWAINJNI_CHAR_SUFFIX "")
endif()

option(DTWAINJNI_CONFIG_CHECKCRC
       "Perform CRC check on dtwainjni.info data file"
       ON)

option(DTWAINJNI_CONFIG_REFRESHCRC
       "Create a refreshed dtwainjni.info named dtwainjni_new.info with updated CRC"
       OFF)

if(DTWAINJNI_CONFIG_CHECKCRC)
    set(DTWAINJNI_CONFIG_CHECKCRC_VALUE 1)
else()
    set(DTWAINJNI_CONFIG_CHECKCRC_VALUE 0)
endif()

if(DTWAINJNI_CONFIG_REFRESHCRC)
    set(DTWAINJNI_CONFIG_REFRESHCRC_VALUE 1)
else()
    set(DTWAINJNI_CONFIG_REFRESHCRC_VALUE 0)
endif()


set(DTWAINJNI_GENERATED_DIR
    "${PROJECT_BINARY_DIR}/generated")

file(MAKE_DIRECTORY "${DTWAINJNI_GENERATED_DIR}")

configure_file(
    "${PROJECT_SOURCE_DIR}/dtwainjni_config.h.in"
    "${DTWAINJNI_GENERATED_DIR}/dtwainjni_config.h"
    @ONLY
)

find_package(JNI REQUIRED)

set(HEADER_FILES ${PROJECT_SOURCE_DIR}/c_cpp_includes/
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/capconst.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwain.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwainc.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwaindefs.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwainx.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwainx2.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwain_filetypes.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwain_library_selector.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwain_standard_defs.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwain_version.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwpdft.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwstrfn.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/dtwtype.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/twain.h
  ${PROJECT_SOURCE_DIR}/c_cpp_includes/winconst.h
  ${PROJECT_SOURCE_DIR}/h/CRCCheck.h
  ${PROJECT_SOURCE_DIR}/h/DSMCaller.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINFunctionCaller.h
  ${PROJECT_SOURCE_DIR}/h/DTwainJavaAPI.h
  ${DTWAINJNI_GENERATED_DIR}/dtwainjni_config.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINJNI_VerInfo.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINJNI_Version.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINJNIGlobalNames.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINJNI_VerInfo.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINJNI_Version.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINJNIGlobalNames.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINJNIGlobals.h
  ${PROJECT_SOURCE_DIR}/h/DTWAINRAII.h
  ${PROJECT_SOURCE_DIR}/h/ExtendedImageInfo.h
  ${PROJECT_SOURCE_DIR}/h/ExtendedImageInfo_Types.h
  ${PROJECT_SOURCE_DIR}/h/get_dll_version.h
  ${PROJECT_SOURCE_DIR}/h/JavaAdapter.h
  ${PROJECT_SOURCE_DIR}/h/JavaArrayList.h
  ${PROJECT_SOURCE_DIR}/h/JavaArrayTraits.h
  ${PROJECT_SOURCE_DIR}/h/javaobjectcaller.h
  ${PROJECT_SOURCE_DIR}/h/resource.h
  ${PROJECT_SOURCE_DIR}/h/StringDefs.h
  ${PROJECT_SOURCE_DIR}/h/UTFCharsHandler.h
)

set(MAIN_SOURCES
  ${PROJECT_SOURCE_DIR}/cpp/CRCCheck.cpp
  ${PROJECT_SOURCE_DIR}/cpp/DTWAINJavaAPI.cpp
  ${PROJECT_SOURCE_DIR}/rc/dtwainjni.rc
  ${PROJECT_SOURCE_DIR}/cpp/DTWAINJNIGlobals.cpp
  ${PROJECT_SOURCE_DIR}/cpp/dtwimpl.cpp
  ${PROJECT_SOURCE_DIR}/cpp/get_dll_version.cpp
  ${PROJECT_SOURCE_DIR}/cpp/ExtendedImageInfo.cpp
  ${PROJECT_SOURCE_DIR}/cpp/JavaAdapter.cpp
  ${PROJECT_SOURCE_DIR}/cpp/JavaArrayTraits.cpp
  ${PROJECT_SOURCE_DIR}/cpp/javaobjectcaller.cpp
  ${PROJECT_SOURCE_DIR}/cpp/dtwimpl.cpp
)

set(DTWAINJNI_RESOURCE_FILES
    ${PROJECT_SOURCE_DIR}/rc/dtwainjni.rc
)

add_library(dtwainjni SHARED
    ${HEADER_FILES}
    ${MAIN_SOURCES}
)

target_include_directories(dtwainjni PRIVATE
   "${DTWAINJNI_GENERATED_DIR}"
    ${PROJECT_SOURCE_DIR}/h
    ${PROJECT_SOURCE_DIR}/c_cpp_includes
    ${JNI_INCLUDE_DIRS}
)

if(MSVC)
    target_compile_definitions(dtwainjni PRIVATE
        WIN32
        _WINDOWS
        _USRDLL
        NOMINMAX
        USING_DTWAIN_LOADLIBRARY

        $<$<BOOL:${DTWAINJNI_BUILD_UNICODE}>:UNICODE>
        $<$<BOOL:${DTWAINJNI_BUILD_UNICODE}>:_UNICODE>
        $<$<NOT:$<BOOL:${DTWAINJNI_BUILD_UNICODE}>>:_MBCS>
    )
endif()

if(MSVC)
    target_compile_options(dtwainjni PRIVATE
        $<$<CONFIG:MinSizeRel>:/Zi>
    )

    target_compile_options(dtwainjni PRIVATE
        $<$<CONFIG:Debug>:/Zi>
    )

    target_link_options(dtwainjni PRIVATE
        $<$<CONFIG:MinSizeRel>:/DEBUG>
        $<$<CONFIG:MinSizeRel>:/OPT:REF>
        $<$<CONFIG:MinSizeRel>:/OPT:ICF>
    )

    target_link_options(dtwainjni PRIVATE
        $<$<CONFIG:Debug>:/DEBUG>
    )

endif()


if(MSVC)
    if(DTWAINJNI_USE_DYNAMIC_CRT)
        set_property(TARGET dtwainjni PROPERTY
            MSVC_RUNTIME_LIBRARY
            "$<$<CONFIG:Debug>:MultiThreadedDebug>$<$<NOT:$<CONFIG:Debug>>:MultiThreadedDLL>"
        )
    else()
        set_property(TARGET dtwainjni PROPERTY
            MSVC_RUNTIME_LIBRARY
            "$<$<CONFIG:Debug>:MultiThreadedDebug>$<$<NOT:$<CONFIG:Debug>>:MultiThreaded>"
        )
    endif()
endif()

set_target_properties(dtwainjni PROPERTIES
    OUTPUT_NAME_DEBUG
        "dtwainjni${DTWAINJNI_BITNESS}${DTWAINJNI_CHAR_SUFFIX}d"

    OUTPUT_NAME_RELEASE
        "dtwainjni${DTWAINJNI_BITNESS}${DTWAINJNI_CHAR_SUFFIX}"

    OUTPUT_NAME_MINSIZEREL
        "dtwainjni${DTWAINJNI_BITNESS}${DTWAINJNI_CHAR_SUFFIX}"

    OUTPUT_NAME_RELWITHDEBINFO
        "dtwainjni${DTWAINJNI_BITNESS}${DTWAINJNI_CHAR_SUFFIX}"
)

set_target_properties(dtwainjni PROPERTIES
    PDB_NAME_DEBUG
        "dtwainjni${DTWAINJNI_BITNESS}${DTWAINJNI_CHAR_SUFFIX}d"

    PDB_NAME_MINSIZEREL
        "dtwainjni${DTWAINJNI_BITNESS}${DTWAINJNI_CHAR_SUFFIX}"
)

target_link_libraries(dtwainjni PRIVATE Version)

source_group("Header Files\\Generated" FILES
    "${DTWAINJNI_GENERATED_DIR}/dtwainjni_config.h"
)


