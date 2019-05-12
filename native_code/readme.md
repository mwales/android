# How to build standalone Android applications

## What didn't work

I tried using the following guide first:
https://software.intel.com/en-us/articles/building-an-android-command-line-application-using-the-ndk-build-tools

I think I have done something similar to these steps in the past, and had
success, but it didn't work for me this time.  The files in this repo
that are Android.mk and Application.mk were for this method. I execute them
using the build.sh script in this directory.

This technique would build an ELF I thought would work, but when I tried to
execute them on the device, they would never work.  I would get errors about
the ELF magic numbers, kind of indicating they were built for the wrong
architecture, but they were for ARM 32-bit according to the file command.

## What did work (as tested on the ARM Android emulator)

Find the make-standalone-toolchain.sh in the Android NDK directory.  Run it. 
It takes a while, and then drops a .tar.bz2 in /tmp/ndk-username/arm-linux-androideabi.tar.bz2

Grab this, save this, cherish this.  Extract it.

Now you can just call clang and clang++ cross-compilers that this tool extracts
for you, and it will just make ELFs that run in the emulator.

'''
../ndk/arm-linux-androideabi/bin/armv7a-linux-androideabi21-clang++ main.cpp
'''

The C++ ELF I created needed the C++ shared object.  We found the
libc++_shared.so for our architecture, and pushed the library to the same
directory we running executables in (/data/local/tmp on Android).  To run a C++
executable, just do the following

'''
LD_LIBRARY_PATH=./ ./a.out
'''

The path of the library we needed to copy to our emualtor device:

arm-linux-androideabi/sysroot/usr/lib/arm-linux-androideabi/libc++_shared.so

This even worked with a STL vector
