#include <jni.h>
#include <string>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>
#include <iostream>
#include <fstream>

struct sockaddr_in serv; //This is our main socket variable.
int fd; //This is the socket file descriptor that will be used to identify the socket

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_bado_ignacio_tcpchat_MainActivity_initServer(JNIEnv *env, jobject instance) {

    jboolean jresult = JNI_TRUE;

    fd = socket(AF_INET, SOCK_STREAM, 0);

    serv.sin_family = AF_INET;
    serv.sin_port = htons(8096);

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

    const char *message = env->GetStringUTFChars(message_, 0);

    const char *empty_message = "                                ";

    send(fd, empty_message, strlen(message), 0);

    send(fd, message, strlen(message), 0);

    env->ReleaseStringUTFChars(message_, message);
}