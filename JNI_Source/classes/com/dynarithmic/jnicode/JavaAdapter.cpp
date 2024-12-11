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
#include "JavaAdapter.h"

JavaAdapter::JavaAdapter(JNIEnv* jEnv, jobject jObj)
{
    Init(jEnv, jObj);
};


JavaAdapter::~JavaAdapter()
{
    if ( jObject )
        getJNIEnv()->DeleteGlobalRef(jObject);
}

JNIEnv* JavaAdapter::getJNIEnv()
{
    JNIEnv* jEnv = NULL;

    int retVal = pJavaVM->AttachCurrentThread((void**) &jEnv, NULL);
    if(retVal) {
//        printf("AttachCurrentThread error %d", retVal);
    }
    return jEnv;
};

void JavaAdapter::releaseJNIEnv()
{
    int retVal = pJavaVM->DetachCurrentThread();
    if(retVal) {
//        printf("DetachCurrentThread error %d", retVal);
    }
};

void JavaAdapter::Init(JNIEnv* jEnv, jobject jObj)
{
    pJavaVM = NULL;

    int retVal = jEnv->GetJavaVM(&pJavaVM);
    if (retVal) {
  //      printf("GetJavaVM error %d", retVal);
    }
    jObject = jEnv->NewGlobalRef(jObj);
}

void JavaAdapter::Destroy()
{
    JNIEnv *jEnv = getJNIEnv();
    if ( jEnv )
    {
        if ( jObject )
            jEnv->DeleteGlobalRef(jObject);
        releaseJNIEnv();
    }
}

