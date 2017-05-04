cmake_minimum_required(VERSION 3.4.1)

set(OPENSSL_ROOT_DIR /usr/local/opt/libressl)

FIND_PACKAGE(OpenSSL REQUIRED)
find_package(OpenMP)
find_package(JNI REQUIRED)

if (OPENMP_FOUND)
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
    set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
endif()

include_directories(${JNI_INCLUDE_DIRS} ${OPENSSL_INCLUDE_DIR})
add_library(jnipi SHARED com_jiahaowu_balancer_task_TaskPi.c)
add_library(jnibench SHARED com_jiahaowu_balancer_task_TaskBench.c)
target_link_libraries(jnipi m ${OPENSSL_LIBRARIES})
target_link_libraries(jnibench m)