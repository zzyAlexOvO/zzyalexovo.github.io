#include <stdio.h>
#include "function.h"
#include "output.h"
#include <stdlib.h>
#include "objdump_x2017.h"
#include "construct.h"
#include "freeMethod.h"

struct Pointer *pointerHead = NULL;
struct Stack *stkHead = NULL;

unsigned char trackStk(unsigned char value) {
    unsigned char cnt = 0;
    if (stkHead == NULL) {
        stkHead = (struct Stack *) malloc(sizeof(struct Stack));
        stkHead->name = value;
        stkHead->next = NULL;
        return cnt;
    } else {
        struct Stack *current = stkHead;
        while (current->next != NULL) {
            if (current->name == value) {
                return cnt;
            } else {
                current = current->next;
                cnt++;
            }
        }
        if (current->name == value) {
            return cnt;
        } else {
            struct Stack *newStack = (struct Stack *) malloc(sizeof(struct Stack));
            current->next = newStack;
            newStack->name = value;
            newStack->next = NULL;
            return cnt + 1;
        }
    }
}

void outputValue(struct Variable *var) {
    if (var->valueType == 0) {
        printf("VAL");
        printf(" %d", var->value);
    } else if (var->valueType == 1) {
        printf("REG");
        printf(" %d", var->value);
    } else if (var->valueType == 2) {
        printf("STK");
        printf(" %c", trackStk(var->value) + 65);
    } else if (var->valueType == 3) {
        printf("PTR");
        printf(" %c", trackStk(var->value) + 65);
    }
}

void outputOP(struct Operation *op) {
    if (op->opcode == 0) {
        printf("    MOV ");
        outputValue(op->variable1);
        printf(" ");
        outputValue(op->variable2);
    } else if (op->opcode == 2) {
        printf("    RET");
    } else if (op->opcode == 3) {
        printf("    REF ");
        outputValue(op->variable1);
        printf(" ");
        outputValue(op->variable2);
    } else if (op->opcode == 4) {
        printf("    ADD ");
        outputValue(op->variable1);
        printf(" ");
        outputValue(op->variable2);
    } else if (op->opcode == 1) {
        printf("    CAL ");
        outputValue(op->variable1);
    } else if (op->opcode == 5) {
        printf("    PRINT ");
        outputValue(op->variable1);
    } else if (op->opcode == 6) {
        printf("    NOT ");
        outputValue(op->variable1);
    } else if (op->opcode == 7) {
        printf("    EQU ");
        outputValue(op->variable1);
    }
    printf("\n");
}

void output(struct Function *func) {
    printf("FUNC LABEL %d\n", func->label);
    int i = func->num;
    struct Operation *ptr = func->operations;
    while (i > 1) {
        outputOP(ptr);
        ptr = ptr->prev;
        i--;
    }
    outputOP(ptr);
    freeStk(stkHead);
    stkHead = NULL;
}