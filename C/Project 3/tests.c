#include "virtual_alloc.h"
#include "virtual_sbrk.h"
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <unistd.h>

void * virtual_heap = NULL;

void * virtual_sbrk(int32_t increment) {
    // Your implementation here (for your testing only)
    virtual_heap = (void *)realloc(virtual_heap,sizeof(uint8_t)*increment);
    return virtual_heap;
}

int compareResult(char* file,char* msg){
    char fn[50];
    int i = 0;
    while (*file){
        fn[i] = *file;
        file++;
        i++;
    }
    fn[i] = '\0';
    FILE* output = fopen("OwnTests/temp.txt","r");
    FILE* expect = fopen(fn,"r");
    char optChar = fgetc(output);
    char eptChar = fgetc(expect);
    while (optChar == eptChar){
        if (optChar == EOF && optChar == EOF){
            fclose(output);
            fclose(expect);
            printf("%s succeed\n",msg);
            return 0;
        }
        else if (optChar == EOF || optChar == EOF){
            fclose(output);
            fclose(expect);
            printf("%s failed\n",msg);
            return 1;
        }else{
            optChar = fgetc(output);
            eptChar = fgetc(expect);
        }
    }
    fclose(output);
    fclose(expect);
    printf("%s failed\n",msg);
    return 1;
}
int basicTest(){
    virtual_heap = (void *)malloc(1);
    init_allocator(virtual_heap,3,1);
    virtual_malloc(virtual_heap,4);
    FILE* fp = freopen("OwnTests/temp.txt","w+",stdout);
    virtual_info(virtual_heap);
    fclose(fp);
    FILE* result = freopen("OwnTests/result.txt","a",stdout);
    char* file = "OwnTests/basic.out";
    int ans = compareResult(file,"basicTest");
    fclose(result);
    return ans;
}

int secBasicTest(){
    virtual_heap = (void *)malloc(1);
    init_allocator(virtual_heap,4,1);
    virtual_malloc(virtual_heap,7);
    virtual_malloc(virtual_heap,4);
    FILE* fp = freopen("OwnTests/temp.txt","w+",stdout);
    virtual_info(virtual_heap);
    fclose(fp);
    FILE* result = freopen("OwnTests/result.txt","a",stdout);
    char* file = "OwnTests/secBasic.out";
    int ans = compareResult(file,"secBasicTest");
    fclose(result);
    return ans;
}

int smallBlockTest(){
    virtual_heap = (void *)malloc(1);
    init_allocator(virtual_heap,4,2);
    virtual_malloc(virtual_heap,7);
    virtual_malloc(virtual_heap,1);
    FILE* fp = freopen("OwnTests/temp.txt","w+",stdout);
    virtual_info(virtual_heap);
    fclose(fp);
    FILE* result = freopen("OwnTests/result.txt","a",stdout);
    char* file = "OwnTests/smallBlock.out";
    int ans = compareResult(file,"smallBlockTest");
    fclose(result);
    return ans;
}

int reallocTest(){
    virtual_heap = (void *)malloc(1);
    init_allocator(virtual_heap,4,2);
    void* ptr = virtual_malloc(virtual_heap,7);
    *(uint8_t*)ptr = 1;
    *(uint8_t*)(ptr+1) = 3;
    void* secPtr = virtual_realloc(virtual_heap,ptr,3);
    if (*(uint8_t*)secPtr == 1 && *(uint8_t*)(secPtr+1) == 3){
        FILE* fp = freopen("OwnTests/temp.txt","w+",stdout);
        virtual_info(virtual_heap);
        fclose(fp);
        FILE* result = freopen("OwnTests/result.txt","a",stdout);
        char* file = "OwnTests/realloc.out";
        int ans = compareResult(file,"reallocTest");
        fclose(result);
        return ans;
    }else{
        printf("reallocTest failed with storing correct value\n");
        return 1;
    }
}

int hugeTest(){
    virtual_heap = (void *)malloc(1);
    init_allocator(virtual_heap,23,10);
    void* ptr = virtual_malloc(virtual_heap,15);
    void* secPtr = virtual_malloc(virtual_heap,1024);
    void* thirdPtr = virtual_malloc(virtual_heap,4194303);
     thirdPtr = virtual_realloc(virtual_heap,thirdPtr,65536);
    void* fourthPtr = virtual_malloc(virtual_heap,2097152);
    *(uint8_t*)(fourthPtr+2097151) = 8;
     fourthPtr = virtual_realloc(virtual_heap,fourthPtr,4194303);
    if (*(uint8_t*)(fourthPtr+2097151) == 8
    && ptr != NULL && secPtr != NULL && thirdPtr != NULL && fourthPtr != NULL){
        FILE* fp = freopen("OwnTests/temp.txt","w+",stdout);
        virtual_info(virtual_heap);
        fclose(fp);
        FILE* result = freopen("OwnTests/result.txt","a",stdout);
        char* file = "OwnTests/huge.out";
        int ans = compareResult(file,"hugeTest");
        fclose(result);
        return ans;
    }else{
        printf("hugeTest failed with storing correct value\n");
        return 1;
    }
}
int MaxNodeTest(){
    virtual_heap = (void *)malloc(1);
    init_allocator(virtual_heap,23,10);
    int i = (int)pow(2,13);
    while (i>0){
        void* ptr = virtual_malloc(virtual_heap,1);
        if (ptr == NULL){
            FILE* result = freopen("OwnTests/result.txt","a",stdout);
            printf("MaxNodeTest failed\n");
            fclose(result);
            return 1;
        }else{
            i--;
        }
    }
    FILE* fp = freopen("OwnTests/temp.txt","w+",stdout);
    virtual_info(virtual_heap);
    fclose(fp);
    FILE* result = freopen("OwnTests/result.txt","a",stdout);
    int ans = compareResult("MaxNode.out","MaxNodeTest");
    fclose(result);
    return ans;
}

int MaxNodeAndFreeTest(){
    virtual_heap = (void *)malloc(1);
    init_allocator(virtual_heap,23,10);
    int i = (int)pow(2,13);
    void* list[8192];
    while (i>0){
        void* ptr = virtual_malloc(virtual_heap,1);
        list[i-1] = ptr;
        if (ptr == NULL){
            FILE* result = freopen("OwnTests/result.txt","a",stdout);
            printf("MaxNodeTest failed\n");
            fclose(result);
            return 1;
        }else{
            i--;
        }
    }
    FILE* fp = freopen("OwnTests/temp.txt","w+",stdout);
    virtual_info(virtual_heap);
    i=0;
    while (i<8192){
        virtual_free(virtual_heap,list[i]);
        i++;
    }
    virtual_info(virtual_heap);
    fclose(fp);
    FILE* result = freopen("OwnTests/result.txt","a",stdout);
    int ans = compareResult("MaxNodeAndFree.out","MaxNodeAndFreeTest");
    fclose(result);
    return ans;
}

int main() {
    basicTest();
    secBasicTest();
    smallBlockTest();
    reallocTest();
    hugeTest();
    MaxNodeAndFreeTest();
    MaxNodeTest();
}
