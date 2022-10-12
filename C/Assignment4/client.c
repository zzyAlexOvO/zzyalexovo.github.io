#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <stdint.h>
#include<dirent.h>
#include "client.h"

int init_client(char* identifier, char* domain){
    char id[256];
    memset(id,0,256);
    char dom[1790];
    memset(dom,0,1790);
    strcpy(id,identifier);
    strcpy(dom,domain);
    char msg[2048];
    msg[0] = 0b00000000;
    msg[1] = 0b00000000;
    memcpy(msg+2,id,256);
    memcpy(msg+258,dom,1790);
    printf("id %s,domain %s is waiting\n",id,dom);
    int fd = open("gevent",O_WRONLY);
    write(fd,msg,2048);
    close(fd);
    printf("waited end\n");
    return 0;
}
int says(char* identifier, char* domain, char* content){
    unsigned char path[2048];
    memset(path,0,2048);
    strcpy(path,identifier);
    strcat(path,"_WR");
    unsigned char msg[2048];
    memset(msg,0,2048);
    msg[0] = 0b00000000;
    msg[1] = 0b00000001;
    strcat(msg+2,content);
    int fd = open(path,O_WRONLY);
    write(fd,msg,2048);
    close(fd);
    printf("%s says %s\n",path,msg+2);
    return 0;
}
int receive(char* identifier, char* domain){
    char path[2048];
    memset(path,0,2048);
    strcpy(path,domain);
    strcat(path,"/");
    strcat(path,identifier);
    strcat(path,"_RD");
    char buffer[2048];
    memset(buffer,0,2048);
    int fd = open(path,O_RDONLY);
    read(fd,buffer,2048);
    close(fd);
    printf("%s heard ",path);
    printf("%s says ",buffer+2);
    printf("%s\n",buffer+258);
    return 0;
}
