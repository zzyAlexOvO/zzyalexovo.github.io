#ifndef ASSIGNMENT2_SUBMIT_FUNCTION_H
#define ASSIGNMENT2_SUBMIT_FUNCTION_H

typedef struct Stack {
    unsigned char address;
    unsigned char name;
    struct Stack *next;
} Stack;

typedef struct Memory {
    unsigned char address;
    unsigned char value;
    struct Memory *next;
} Memory;

typedef struct Function {
    unsigned label: 3;
    unsigned mod: 3;
    unsigned num: 5;
    struct Operation *operations;
    struct Function *next;
    struct Function *prev;
} Function;

typedef struct Operation {
    unsigned opcode: 3;
    struct Operation *prev;
    struct Operation *next;
    struct Variable *variable1;
    struct Variable *variable2;
} Operation;

typedef struct Variable {
    unsigned char value;
    unsigned char size;
    unsigned valueType: 2;
} Variable;

#endif