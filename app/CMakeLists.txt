# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)

set(OpenCV_DIR "../openCVLibrary344/src/sdk/native/libs")


add_library(opencv_344_android SHARED IMPORTED)

set_target_properties(opencv_344_android PROPERTIES IMPORTED_LOCATION
                       ${OpenCV_DIR}/${ANDROID_ABI}/libopencv_java3.so)

#include_directories(O:/OpenCV-android-sdk/sdk/native/include/)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

#find_library( # Sets the name of the path variable.
#              log-lib
#
#              # Specifies the name of the NDK library that
#              # you want CMake to locate.
#              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

#target_link_libraries( native-lib
#                    lib_opencv
#                    ${log-lib} )
#
#find_package(OpenCV REQUIRED)
#message(STATUS "OpenCV libraries: ${OpenCV_LIBS}")
#target_link_libraries(native-lib lib_opencv ${OpenCV_LIBS})