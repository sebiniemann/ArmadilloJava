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

project(ArmadilloJava CXX)

if(WIN32)
  message(STATUS "")
  message(STATUS "NOTICE: Windows OS detected:")
  message(STATUS "* Assuming Armadillo C++ to be located in C:\\Development\\Armadillo.")
  message(STATUS "* Assuming OpenBLAS to be located in C:\\Development\\OpenBLAS.")
  message(STATUS "* Assuming libgfortran to be located in C:\\Development\\GCC\\bin.")
  
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
  message(FATAL_ERROR "ERROR: Neither Windows, Apple or Unix/Unix-like OS detected. I would greatly appreciate an e-mail containing some information about this problem.")
endif()

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

add_executable(generateInput generateInput.cpp)
target_link_libraries(generateInput openblas gfortran)

add_executable(generateExpected generateExpected.cpp)
target_link_libraries(generateExpected openblas gfortran)