#include "function.h"
#include "construct.h"
#include <stdlib.h>
#include <stdio.h>

unsigned char cut(unsigned char *ptr, const int fileSize, int *currentIndex, int *currentBit, unsigned char size) {
    unsigned char result;
    if ((fileSize - *currentIndex) < 0) {
        printf("probs stack buffer underflow here\n");
        return 0;
    }
    if (*currentBit + size > 8) {
        unsigned char right = (*(ptr + (fileSize - *currentIndex)) >> *currentBit);
        //printf("right: %d\n",right);
        *currentIndex += 1;
        int lack = size - (8 - *currentBit);
        if ((fileSize - *currentIndex) < 0) {
            printf("probs stack buffer underflow here\n");
            return 0;
        }
        unsigned char left = (*(ptr + (fileSize - (*currentIndex)))) << (8 - lack);
        left = left >> (8 - lack);
        //printf("left: %d\n",left);
        result = (left << (8 - *currentBit)) + right;
        *currentBit = *currentBit + size - 8;
    } else if (*currentBit + size == 8) {
        result = (*(ptr + (fileSize - *currentIndex)) >> *currentBit);
        *currentBit = 0;
        *currentIndex += 1;
    } else {
        result = (*(ptr + (fileSize - *currentIndex)) >> *currentBit);
        result = result << (8 - size);
        result = result >> (8 - size);
        *currentBit += size;
    }
    //printf("cut: %d endIndex %d endBit %d size %d\n",result,*currentIndex,*currentBit,size);
    return result;
}

Variable *extract(unsigned char *ptr, const int fileSize, int *currentIndex, int *currentBit) {
    unsigned char valueType = cut(ptr, fileSize, currentIndex, currentBit, 2);

    short size;
    if ((valueType & 3) == 0) { //1 byte
        size = 8;
    } else if ((valueType & 3) == 01) { //register 3 bits
        size = 3;
    } else if ((valueType & 3) == 2) { //stack 5 bits
        size = 5;
    } else { //pointer 5 bits
        size = 5;
    }

    unsigned char value = cut(ptr, fileSize, currentIndex, currentBit, size);

    Variable *variable = (struct Variable *) malloc(sizeof(struct Variable));
    variable->size = size;
    variable->valueType = valueType;
    variable->value = value;
    //printf("new variable with size %d, value type %d, value %d created\n"
    //     "currentBit is %d, and currentIndex is %d\n",size,valueType,value,*currentBit, *currentIndex);
    return variable;

}

struct Operation *
construct(const unsigned char opcode, unsigned char *ptr, const int fileSize, int *currentIndex, int *currentBit) {
    Operation *op = (Operation *) malloc(sizeof(Operation));
    op->variable1 = NULL;
    op->variable2 = NULL;
    op->opcode = opcode;
    if (opcode == 0 || opcode == 3 || opcode == 4) { //MOV or REF or ADD
        struct Variable *firstVariable = extract(ptr, fileSize, currentIndex, currentBit);
        struct Variable *secondVariable = extract(ptr, fileSize, currentIndex, currentBit);
        op->variable1 = firstVariable;
        op->variable2 = secondVariable;
    } else if (opcode == 1 || opcode == 5 || opcode == 6 || opcode == 7) { //CAL or PRINT or NOT or EQUAL
        struct Variable *firstVariable = extract(ptr, fileSize, currentIndex, currentBit);
        op->variable1 = firstVariable;
    }
    //printf("now op with code: %d created\n", op->opcode);
    return op;
}

struct Function *build(unsigned char *buffer2, int fileSize) {
    int i;
    int currentIndex = 1;
    int currentBit = 0;
    struct Function *func = NULL;
    while (fileSize > currentIndex) {
        if (func != NULL) {
            func->next = (Function *) malloc(sizeof(Function));
            func->next->prev = func;
            func = func->next;
            func->next = NULL;
        } else {
            func = (Function *) malloc(sizeof(Function));
            func->next = NULL;
            func->prev = NULL;
        }

        func->num = cut(buffer2, fileSize, &currentIndex, &currentBit, 5);

        i = 0;


        struct Operation *op = NULL;
        while (func->num > i) {
            unsigned char opcode = cut(buffer2, fileSize, &currentIndex, &currentBit, 3);
            //printf("op: %d\n",opcode);
            if (op == NULL) {
                op = construct(opcode, buffer2, fileSize, &currentIndex, &currentBit);
                op->next = NULL;
                op->prev = NULL;
            } else {
                op->next = construct(opcode, buffer2, fileSize, &currentIndex, &currentBit);
                op->next->prev = op;
                op = op->next;
                op->next = NULL;
            }
            //printf("now op with code: %d created\n", op->opcode);
            i++;
        }
        func->operations = op;
        func->label = cut(buffer2, fileSize, &currentIndex, &currentBit, 3);
        //printf("currentIndex: %d; currentBit: %d\n",currentIndex,currentBit);
    }
    return func;
}

