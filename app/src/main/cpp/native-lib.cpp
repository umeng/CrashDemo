#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_umeng_crashdemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    //    std::string hello = "Hello from C++";
    std::string hello = NULL;
    return env->NewStringUTF(hello.c_str());
}
