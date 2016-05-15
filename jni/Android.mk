LOCAL_PATH:=$(call my-dir)
include $(call all-subdir-makefiles)

include $(CLEAR_VARS)
LOCAL_LDLIBS:=-llog
LOCAL_MODULE:= ndk1
LOCAL_SRC_FILES:= Core.cpp

include $(BUILD_SHARED_LIBRARY)