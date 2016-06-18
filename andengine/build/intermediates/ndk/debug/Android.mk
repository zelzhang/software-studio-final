LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := andengine_shared
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	/Users/ryan/AndroidStudioProjects/OpenFlappyBird-master/andengine/src/main/jni/Android.mk \
	/Users/ryan/AndroidStudioProjects/OpenFlappyBird-master/andengine/src/main/jni/Application.mk \
	/Users/ryan/AndroidStudioProjects/OpenFlappyBird-master/andengine/src/main/jni/build.sh \
	/Users/ryan/AndroidStudioProjects/OpenFlappyBird-master/andengine/src/main/jni/src/BufferUtils.cpp \
	/Users/ryan/AndroidStudioProjects/OpenFlappyBird-master/andengine/src/main/jni/src/GLES20Fix.c \

LOCAL_C_INCLUDES += /Users/ryan/AndroidStudioProjects/OpenFlappyBird-master/andengine/src/main/jni
LOCAL_C_INCLUDES += /Users/ryan/AndroidStudioProjects/OpenFlappyBird-master/andengine/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
