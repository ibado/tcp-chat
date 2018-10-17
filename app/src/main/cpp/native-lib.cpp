#include <jni.h>
#include <string>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>
#include <iostream>
#include <fstream>


#include <android/log.h>

struct sockaddr_in serv; //This is our main socket variable.
int fd; //This is the socket file descriptor that will be used to identify the socket

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_bado_ignacio_tcpchat_MainActivity_initClient(JNIEnv *env, jobject instance, jint port) {

    jboolean jresult = JNI_TRUE;

    fd = socket(AF_INET, SOCK_STREAM, 0);

    serv.sin_family = AF_INET;
    serv.sin_port = htons(port);

    int result = inet_pton(AF_INET, "10.0.3.2", &serv.sin_addr); //This binds the client to localhost
    if (result != 1)
        jresult = JNI_FALSE;
    else
        connect(fd, (struct sockaddr *)&serv, sizeof(serv)); //This connects the client to the server.

    // Construct a String
    jstring jstr = env->NewStringUTF("This string comes from JNI");
    // First get the class that contains the method you need to call
    jclass clazz = env->FindClass("com/bado/ignacio/tcpchat/MainActivity");
    // Get the method that you want to call
    jmethodID messageMe = env->GetMethodID(clazz, "executeMe", "(Ljava/lang/String;)V");

    env->CallVoidMethod(instance, messageMe, jstr);

    return jresult;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_bado_ignacio_tcpchat_MainActivity_sendMessage(JNIEnv *env, jobject instance,
                                                             jstring message_) {

    const char* message = env->GetStringUTFChars(message_, 0);

    size_t length = strlen(message);

    send(fd, message, length, 0);

    __android_log_print(ANDROID_LOG_DEBUG, "ibado", "size: %zu", length);

    env->ReleaseStringUTFChars(message_, message);
}