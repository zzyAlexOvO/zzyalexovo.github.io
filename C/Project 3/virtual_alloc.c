#include "virtual_alloc.h"
#include "virtual_sbrk.h"
#include <stdio.h>
#include <math.h>
#include <stdlib.h>

void init_allocator(void * heapstart, uint8_t initial_size, uint8_t min_size) {
    int size = pow(2,initial_size);
    int already = heapstart-virtual_sbrk(0);
    int nodeNum = pow(2,initial_size-min_size);
    virtual_sbrk(already);
    void* newStart = virtual_sbrk(size + nodeNum + 6);
    if(newStart == (void*)(-1)){
        printf("Cannot allocate this much memory!");
        exit(1);}
    if (initial_size >= 127){
        printf("initial_size is too big\n");
        exit(1);
    }
    //printf("heaostart: %d, virtual break: %d",heapstart,virtual_sbrk(0));
    *(uint8_t*)newStart = initial_size;
    *(uint8_t*)(newStart+1) = min_size;
    //int8_t cur = initial_size;
    *(int32_t*)(newStart+2) = 1;
    *(int8_t*)(newStart+6) = initial_size;
    //printf("the initial size is %d, the min size is %d, current size is %d, the first variable is %d\n",*(uint8_t*)heapstart,*(uint8_t*)(heapstart+1),*(uint8_t*)(heapstart+2),*(uint8_t*)(heapstart+3));
    // Your code here
}

void * virtual_malloc(void * heapstart, uint32_t size) {
    int8_t max = *(uint8_t*)heapstart;
    int8_t min = *(uint8_t*)(heapstart + 1);
    int convertedSize = min;
    while (pow(2,convertedSize)<size){
        convertedSize++;
    }
    uint32_t stock = *(uint32_t*)(heapstart + 2);
    //printf("stock is %d\n",stock);
    int i = 1;
    int temp = 0;
    //find if there is unallocated block with the same size
    while (i <= stock){
        int8_t block = *(int8_t*)(heapstart + 5 + i);
        if(block == convertedSize){
            *(int8_t*)(heapstart + 5 + i) = -(*(int8_t*)(heapstart + 5 + i));
            return (void *)(heapstart + 6 + (int)pow(2,max-min) + temp);
        }
        temp += pow(2,abs(block));
        i++;
    }
    //find the left-most block and split it
    int n = 1;
    while ((n+convertedSize)<=max) {
        temp = 0;
        i = 1;
        while (i <= stock) {
            int8_t block = *(int8_t * )(heapstart + 5 + i);
            if (block == convertedSize + n) {
                //printf("a block of converted size %d has been found at index %d\n",block,i);
                *(uint32_t*)(heapstart + 2) += n;
                int cnt = 0;
                while (stock - cnt > i){
                    //printf("a value originally stored at %d has been moved to %d\n",(stock + n - cnt),(stock - cnt));
                    *(int8_t * )(heapstart + 5 + stock + n - cnt) = *(int8_t * )(heapstart + 5 + stock - cnt);
                    cnt++;
                }
                *(int8_t * )(heapstart + 5 + i) = -convertedSize;
                //printf("value at %d has been assigned %d\n",i,(-convertedSize));
                cnt = 0;
                while (cnt < n){
                    *(int8_t * )(heapstart + 5 + i + cnt+1) = convertedSize+cnt;
                    //printf("value at %d has been assigned %d\n",i+cnt+1,convertedSize+cnt);
                    cnt++;
                }
                return (void *) (heapstart + 6 + (int) pow(2, max - min) + temp);
            }
            temp += pow(2, abs(block));
            i++;
        }
        n++;
    }
    return NULL;
}

int virtual_free(void * heapstart, void * ptr) {
    int max = *(int8_t *)heapstart;
    int min = *(int8_t *)(heapstart+1);
    int size = *(int32_t *)(heapstart+2);
    void* pointer = heapstart+6+(int)pow(2,max-min);
    int i = 1;
    while(i<=size){
        //printf("when i = %d, ptr is %d, pointer is %d\n",i,ptr,pointer);
        if(pointer == ptr){
            int temp = abs(*(int8_t *)(heapstart+5+i));
            int buddy_right = 0;
            int buddy_left = 0;
            //printf("pointer is found with size of %d",temp);
            while (1) {
                int left = 1;
                int right = 1;
                while (temp == abs(*(int8_t * )(heapstart + 5 + i - left-buddy_left)) && (i - left-buddy_left >0)) {
                    left++;
                }
                while (temp == abs(*(int8_t * )(heapstart + 5 + i + right+buddy_right))) {
                    right++;
                }
                //printf("left is %d; right is %d\n",left,right);
                if (left == 1 && right == 1) {
                    break;
                } else if (left % 2 == 1) {
                    //printf("entered left\n");
                    if (temp == *(int8_t * )(heapstart + 6 + i+buddy_right)) {
                        temp++;
                        buddy_right++;
                    }else{
                        break;
                    }
                } else if (right % 2 == 1) {
                    //printf("entered right\n");
                    if (temp == *(int8_t * )(heapstart + 4 + i-buddy_left)) {
                        temp++;
                        buddy_left++;
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }
            *(int8_t * )(heapstart + 5 + i - buddy_left) = temp;
            int n =1;
            while (i +buddy_right+n <= size) {
                *(int8_t * )(heapstart + 5 + i - buddy_left+n) = *(int8_t * )(heapstart + 5 + i +buddy_right+n);
                n++;
            }
            *(int32_t *)(heapstart+2) = *(int32_t *)(heapstart+2)-buddy_left-buddy_right;
            return 0;
        }
        pointer += (int)pow(2,abs(*(int8_t *)(heapstart+5+i)));
        i++;
    }
    // Your code here
    return 1;
}

void * virtual_realloc(void * heapstart, void * ptr, uint32_t size) {
    if(size == 0){
        virtual_free(heapstart,ptr);
        return NULL;
    }else if (ptr == NULL){
        return virtual_malloc(heapstart,size);
    }else{
        //int max = *(int8_t *)heapstart;
        //int min = *(int8_t *)(heapstart+1);
        uint32_t stock = *(uint32_t *)(heapstart+2);
        int8_t stash[stock];
        int i = 1;
        while (i <= stock){
            stash[i-1] = *(int8_t *)(heapstart+5+i);
            i++;
        }
        virtual_free(heapstart,ptr);
        void* new_ptr = virtual_malloc(heapstart,size);
        if (new_ptr == NULL){
            *(uint32_t *)(heapstart+2) = stock;
            i = 1;
            while (i <= stock){
                *(int8_t *)(heapstart+5+i) = stash[i-1];
                i++;
            }
            return NULL;
        }else if (new_ptr == ptr){
            return new_ptr;
        }else{
            int max = *(int8_t *)heapstart;
            int min = *(int8_t *)(heapstart+1);
            int bag = *(uint32_t *)(heapstart+2);
            void* pointer = heapstart+6+(int)pow(2,max-min);
            //printf("max: %d; min: %d; bag: %d; heapstart:%d ; pointer:%d; ptr: %d\n",max,min,bag,heapstart,pointer,ptr);
            //printf("bag: {%d, %d, %d}",stash[0],stash[1],stash[2]);
            int i = 0;
            int temp;
            while(i<bag){
                if(pointer == ptr){
                    temp = abs(stash[i]);
                    break;
                }
                pointer += (int)pow(2,abs(stash[i]));
                i++;
            }
            i = 0;
            while(i<pow(2,temp)){
                //printf("%d has been assigned to %d, where temp = %d\n",i, *(uint8_t*)(new_ptr+i),temp);
                *(uint8_t*)(new_ptr+i) = *(uint8_t*)(ptr+i);
                i++;
            }
            return new_ptr;
        }
//*/
    }
    // Your code here
    return NULL;
}

void virtual_info(void * heapstart) {
    uint32_t size = *(uint32_t*)(heapstart + 2);
    int i = 1;
    while (i <= size){
        int8_t block = *(int8_t*)(heapstart + 5 + i);
        if(block < 0){
            int a = pow(2,abs(block));
            printf("allocated %d\n",a);
        }else{
            int a = pow(2,block);
            printf("free %d\n", a);
        }
        i++;
    }
    // Your code here
}


