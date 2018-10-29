#include <jni.h>
#include <string>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>
#include <iostream>
#include <fstream>
#include <unistd.h>


#include <android/log.h>

struct sockaddr_in serv; //This is our main socket variable.
int fd; //This is the socket file descriptor that will be used to identify the socket

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_bado_ignacio_tcpchat_MainActivity_initClient(JNIEnv *env, jobject instance, jint port) {

    jboolean jresult = JNI_TRUE;

    fd = socket(AF_INET, SOCK_STREAM, 0);

    char buffer[1024];

    serv.sin_family = AF_INET;
    serv.sin_port = htons(port);

    int result = inet_pton(AF_INET, "10.0.3.2", &serv.sin_addr); //This binds the client to localhost
    if (result != 1)
        jresult = JNI_FALSE;
    else
        connect(fd, (struct sockaddr *)&serv, sizeof(serv)); //This connects the client to the server.

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

extern "C"
JNIEXPORT jstring JNICALL
Java_com_bado_ignacio_tcpchat_MainActivity_getMessage(JNIEnv *env, jobject instance) {

    char result[100] = "";

    ssize_t num;

    //fgets(result, 99, stdin);
    /*if ((send(fd, result, strlen(result), 0)) == -1) {
        fprintf(stderr, "Failure Sending Message\n");
        close(fd);
        exit(1);
    } else {
        num = recv(fd, result, sizeof(result), 0);
        if (num <= 0) {
            __android_log_print(ANDROID_LOG_DEBUG, "ibado", "Either Connection Closed or Error\n");
        }
    }*/
    num = recv(fd, result, sizeof(result), 0);
    if (num <= 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "ibado", "Either Connection Closed or Error\n");
    } else {
        __android_log_print(ANDROID_LOG_DEBUG, "ibado", "the message was sent properly\n");
    }

    return env->NewStringUTF(result);
}