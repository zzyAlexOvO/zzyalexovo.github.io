#include "function.h"
#include "freeMethod.h"
#include <stdio.h>
#include <stdlib.h>

void freeOp(struct Operation *op) {
    if (op->opcode == 0 || op->opcode == 3 || op->opcode == 4) {
        free(op->variable1);
        free(op->variable2);
    } else if (op->opcode == 2) {}
    else {
        free(op->variable1);
    }
    free(op);
}

void freeFunc(struct Function *function) {
    struct Operation *op = function->operations;
    while (op->prev != NULL) {
        op = op->prev;
        freeOp(op->next);
    }
    freeOp(op);
    free(function);
}

void freeStk(struct Stack *stkHead) {
    if (stkHead != NULL) {
        struct Stack *current = stkHead;
        while (current->next != NULL) {
            struct Stack *temp = current->next;
            free(current);
            current = temp;
        }
        free(current);
    }
}

void freeStkWithoutHead(struct Stack *stkHead) {
    if (stkHead->next != NULL) {
        struct Stack *current = stkHead->next;
        while (current->next != NULL) {
            struct Stack *temp = current->next;
            free(current);
            current = temp;
        }
        free(current);
    }
}

void freeAll(struct Function *func) {
    while (func->prev != NULL) {
        func = func->prev;
        freeFunc(func->next);
    }
    freeFunc(func);
}