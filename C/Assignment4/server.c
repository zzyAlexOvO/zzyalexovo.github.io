#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <stdint.h>
#include<dirent.h>
#include "server.h"

char identifier[ID_LENGTH];
char domain[CONTENT_LENGTH - ID_LENGTH];

int connect(char* content){
    printf("%s now being forked\n",content);
    int pid = fork();
    if(pid < 0){
        printf("fork failed\n");
        exit(1);
    }else if (pid == 0){
        strcpy(identifier,content);
        strcpy(domain,content+ID_LENGTH);
        printf("accessing %s returns: %d\n",domain,access(domain, F_OK));
        if (access(domain, F_OK) !=0){
            int a = mkdir(domain, S_IRWXU | S_IRWXG);
            printf("mkdir returns: %d",a);
        }
        chdir(domain);
        client_handler(identifier,domain);
    }else{
        return 0;
    }
    return 0;
}
int is_end_with(const char *str1, char *str2){
    if(str1 == NULL || str2 == NULL)
        return -1;
    int len1 = strlen(str1);
    int len2 = strlen(str2);
    if((len1 < len2) ||  (len1 == 0 || len2 == 0))
        return -1;
    while(len2 >= 1)
    {
        if(str2[len2 - 1] != str1[len1 - 1])
            return 0;
        len2--;
        len1--;
    }
    return 1;
}
int say (char* content){
    DIR *dp;
    struct dirent *dirp;
    char dirname[CONTENT_LENGTH - ID_LENGTH];
    memset(dirname,0,CONTENT_LENGTH - ID_LENGTH);
    strcpy(dirname,domain);
    if((dp = opendir(dirname)) == NULL) {
        printf("Can't open %s\n", dirname);
        exit(1);
    }
    char msg[MESSAGE_LENGTH];
    memset(msg,0,MESSAGE_LENGTH);
    msg[0] = 0b00000000;
    msg[1] = 0b00000001;
    strcat(msg+2,identifier);
    strcat(msg+2+ID_LENGTH,content);
    char self[sizeof(identifier)+3];
    memset(self,0, sizeof(self));
    strcpy(self,identifier);
    strcat(self,"_RD");
    while((dirp = readdir(dp)) != NULL) {
        if(dirp->d_type == 8 && is_end_with(dirp->d_name,"_RD") && (strcmp(self,dirp->d_name) != 0)) {
            int fd = open(dirp->d_name,O_WRONLY);
            write(fd,msg,MESSAGE_LENGTH);
            close(fd);
        }
    }
    closedir(dp);
    return 0;
}

int client_handler(char* id, char* domain){
    printf("a handler for %s has been created\n",id);
    char pipe1[ID_LENGTH];
    char pipe2[ID_LENGTH];
    strcpy(pipe1,id);
    strcat(pipe1,"_WR");
    mkfifo(pipe1,0666);
    strcpy(pipe2,id);
    strcat(pipe2,"_RD");
    mkfifo(pipe2,0666);
    while (1){
        int fd = open(pipe1,O_RDONLY);
        char buf[MESSAGE_LENGTH];
        read(fd,buf,MESSAGE_LENGTH);
        if (*(uint8_t*)(buf+1) == 1){//SAY
            FILE* log = fopen("log.txt","a");
            fprintf(log,"say");
            fclose(log);
            say(buf+2);
        }else if (*(uint8_t*)(buf+1) == 2){//SAYCONT
            saycont(buf+2);
        }
    }
    return 1;
}
int saycont (char* content){
    DIR *dp;
    struct dirent *dirp;
    char dirname[CONTENT_LENGTH - ID_LENGTH];
    strcpy(dirname,domain);
    if((dp = opendir(dirname)) == NULL) {
        printf("Can't open %s\n", dirname);
    }
    char msg[MESSAGE_LENGTH];
    msg[0] = 0b00000000;
    msg[1] = 0b00000100;
    msg[2] = *"\0";
    strcat(msg+2,identifier);
    strcat(msg+2+ID_LENGTH,content);
    char self[sizeof(domain)+3];
    strcpy(self,domain);
    strcat(self,"_RD");
    while((dirp = readdir(dp)) != NULL) {
        if(dirp->d_type == 8 && is_end_with(dirp->d_name,"_RD") && (strcmp(self,dirp->d_name) != 0)) {
            int fd = open(dirp->d_name,O_WRONLY);
            write(fd,msg,MESSAGE_LENGTH);
            close(fd);
        }
    }
    return 0;
}
int main(void){
    memset(identifier,0,ID_LENGTH);
    memset(domain,0,CONTENT_LENGTH - ID_LENGTH);
    mkfifo(GLOBAL_PIPE,0666);
    int globalEvent[2];
    char r_buf[2048];
    if(pipe(globalEvent)==-1){
        perror("pipe failed\n");
        exit(1);
    }
    while (1){
        printf("global server is waiting\n");
        int fd = open(GLOBAL_PIPE,O_RDONLY);
        read(fd,r_buf,MESSAGE_LENGTH);
        close(fd);
        int type = *(uint8_t*)(r_buf+1);
        printf("type detected %d\n",type);
        if(type == 0){ // CONNECT
            printf("%s is being created\n",r_buf+2);
            connect(r_buf+2);
        }
    }
}