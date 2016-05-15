################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../jni/Core.cpp \
../jni/native.cpp 

OBJS += \
./jni/Core.o \
./jni/native.o 

CPP_DEPS += \
./jni/Core.d \
./jni/native.d 


# Each subdirectory must supply rules for building sources it contributes
jni/%.o: ../jni/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I/usr/local/android-ndk-r9d/platforms/android-19/arch-arm/usr/include -I/usr/local/android-ndk-r9d/sources -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


