#!/bin/bash

PATH_TO_NDK=~/Android_New/android-ndk-r19b/
export NDK_PROJECT_PATH=.
${PATH_TO_NDK}ndk-build NDK_APPLICATION_MK=./Application.mk

