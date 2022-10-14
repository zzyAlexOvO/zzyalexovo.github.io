#include "server.h"
#include "client.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <stdint.h>
#include <dirent.h>

int main(void){
    int pid = fork();
    if (pid == 0){
        char *args[]={"./server",NULL};
        execv("./server",args);
    }
    else if (pid < 0){
        printf("fork failed\n");
    }else {
        init_client("Alex", "/home/alex/文档/comp2017/A4/domain");
        init_client("Ariel","/home/alex/文档/comp2017/A4/domain");
        says("Alex","/home/alex/文档/comp2017/A4/domain","SB!");
        receive("Ariel","/home/alex/文档/comp2017/A4/domain");
    }
}