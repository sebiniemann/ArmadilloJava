#------------------------------------------------------------------------------
# Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
# 
# Licensed under the MIT License (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# http://opensource.org/licenses/MIT
# 
# Developers:
#   Sebastian Niemann - Lead developer
#   Daniel Kiechle - Unit testing
#------------------------------------------------------------------------------
cmake_minimum_required(VERSION 2.8 FATAL_ERROR)

set(ARMADILLO_JAVA_MAJOR 4)
set(ARMADILLO_JAVA_MINOR 000)
set(ARMADILLO_JAVA_PATCH 0)

message(STATUS "Configuring test input and result generators for ArmadilloJava ${ARMADILLO_JAVA_MAJOR}.${ARMADILLO_JAVA_MINOR}.${ARMADILLO_JAVA_PATCH}")
message(STATUS "NOTICE: This is written with the system configuration of ArmadilloJava developers in mind and might not compile on your system.")

project(ArmadilloJava CXX)

set(PROJECT_SOURCE_DIR ${PROJECT_BINARY_DIR}/src)
set(EXECUTABLE_OUTPUT_PATH ${PROJECT_BINARY_DIR}/bin)

if(WIN32)
  message(STATUS "")
  message(STATUS "NOTICE: Windows OS detected:")
  message(STATUS "* Assuming the Armadillo C++ include folder to be located in C:\\Development\\Armadillo\\include.")
  message(STATUS "* Assuming the OpenBLAS include folder to be located in C:\\Development\\OpenBLAS\\include.")
  message(STATUS "* Assuming the OpenBLAS library folder to be located in C:\\Development\\OpenBLAS\\lib.")
  message(STATUS "* Assuming the libgfortran library to be located in C:\\Development\\GCC\\bin.")
  
  include_directories(C:\\Development\\Armadillo\\include)
  include_directories(C:\\Development\\OpenBLAS\\include)
  
  link_directories(C:\\Development\\OpenBLAS\\lib)
  link_directories(C:\\Development\\GCC\\bin)
elseif(APPLE)
  message(STATUS "")
  message(STATUS "NOTICE: Apple OS detected:")
elseif(UNIX)
  message(STATUS "")
  message(STATUS "NOTICE: Unix or Unix-like OS detected:")
else()
  message(STATUS "")
  message(FATAL_ERROR "ERROR: Neither Windows, Apple nor Unix/Unix-like OS detected. I would greatly appreciate an e-mail containing some information about this problem.")
endif()

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

include_directories(${PROJECT_BINARY_DIR}/include)

file(GLOB_RECURSE SRC_FILES src/Expected*.cpp src/Input*.cpp)
add_executable(AllExpected src/AllExpected.cpp ${SRC_FILES})
target_link_libraries(AllExpected openblas gfortran)