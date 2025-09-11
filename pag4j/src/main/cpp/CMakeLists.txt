cmake_minimum_required(VERSION 3.16)
project(pag4j)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# https://github.com/microsoft/STL/issues/4730
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -D_DISABLE_CONSTEXPR_MUTEX_CONSTRUCTOR")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -D_DISABLE_CONSTEXPR_MUTEX_CONSTRUCTOR")

set(GRADLE_ROOT_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../..)

set(PAG_BUILD_SHARED OFF)
set(PAG_BUILD_FRAMEWORK OFF)
add_subdirectory(${GRADLE_ROOT_DIR}/libpag ${CMAKE_CURRENT_BINARY_DIR}/libpag)

find_package(Java COMPONENTS Runtime Development)
if(NOT Java_FOUND)
    message(FATAL_ERROR "PAG4J requires Java to build!")
    return()
endif()

# Handle JNI discovery for Android cross-compilation
if (ANDROID)
    # When cross-compiling for Android, we need to find JNI on the host system
    # but CMake's FindJNI module gets confused by the Android toolchain

    # Get JAVA_HOME from environment or use the found Java installation
    if (DEFINED ENV{JAVA_HOME})
        set(JAVA_HOME $ENV{JAVA_HOME})
    else ()
        get_filename_component(JAVA_HOME ${Java_JAVA_EXECUTABLE} DIRECTORY)
        get_filename_component(JAVA_HOME ${JAVA_HOME} DIRECTORY)
    endif ()

    message(STATUS "Using JAVA_HOME: ${JAVA_HOME}")

    # Manually set JNI paths for Android cross-compilation
    set(JNI_INCLUDE_DIRS
            ${JAVA_HOME}/include
            ${JAVA_HOME}/include/darwin  # macOS-specific
    )

    # For Android builds, we only need the include directories, not the libraries
    # The JNI libraries will be provided by the Android runtime
    set(JNI_LIBRARIES "")  # Empty for Android builds

    # Verify the include directories exist
    foreach (include_dir ${JNI_INCLUDE_DIRS})
        if (NOT EXISTS ${include_dir})
            message(WARNING "JNI include directory not found: ${include_dir}")
        endif ()
    endforeach ()

    # Check for jni.h specifically
    if (NOT EXISTS ${JAVA_HOME}/include/jni.h)
        message(FATAL_ERROR "jni.h not found in ${JAVA_HOME}/include/")
    endif ()

    message(STATUS "JNI include directories: ${JNI_INCLUDE_DIRS}")

else ()
    # For non-Android builds, use the standard FindJNI module
    find_package(JNI REQUIRED)
endif ()

file(GLOB PAG4J_SOURCES
    ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp
)

add_library(pag4j SHARED
    ${PAG4J_SOURCES}
)

target_include_directories(pag4j PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${JNI_INCLUDE_DIRS}
    ${CMAKE_CURRENT_SOURCE_DIR}/include/
)

target_link_libraries(pag4j
    ${JNI_LIBRARIES}
    pag
)

set_target_properties(pag4j PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}
)

if(WIN32)
    file(REMOVE ${CMAKE_CURRENT_BINARY_DIR}/libEGL.dll)
    file(COPY ${GRADLE_ROOT_DIR}/libpag/third_party/tgfx/vendor/angle/win/x64/libEGL.dll DESTINATION ${CMAKE_CURRENT_BINARY_DIR})
    file(REMOVE ${CMAKE_CURRENT_BINARY_DIR}/libGLESv2.dll)
    file(COPY ${GRADLE_ROOT_DIR}/libpag/third_party/tgfx/vendor/angle/win/x64/libGLESv2.dll DESTINATION ${CMAKE_CURRENT_BINARY_DIR})
endif()

if (APPLE)
    # Determine Mac architecture
    if (CMAKE_SYSTEM_PROCESSOR MATCHES "arm64|aarch64")
        set(MAC_ARCH "arm64")
    else ()
        set(MAC_ARCH "x64")
    endif ()

    file(REMOVE ${CMAKE_CURRENT_BINARY_DIR}/libEGL.dylib)
    file(COPY ${GRADLE_ROOT_DIR}/libpag/third_party/tgfx/vendor/swiftshader/mac/${MAC_ARCH}/libEGL.dylib DESTINATION ${CMAKE_CURRENT_BINARY_DIR})
    file(REMOVE ${CMAKE_CURRENT_BINARY_DIR}/libGLESv2.dylib)
    file(COPY ${GRADLE_ROOT_DIR}/libpag/third_party/tgfx/vendor/swiftshader/mac/${MAC_ARCH}/libGLESv2.dylib DESTINATION ${CMAKE_CURRENT_BINARY_DIR})
endif ()
