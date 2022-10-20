#include <stdio.h>
#include <stdlib.h>
#include "function.h"
#include "construct.h"
#include "freeMethod.h"
#include <stdio.h>
#include <stdlib.h>

unsigned char registerBank[4];
struct Function *func = NULL;
struct Memory *memo = NULL;

//within the stack frame, find one matched name
struct Stack *getStk(unsigned char name, struct Stack *stkHead) {
    if (stkHead->next == NULL) {
        return NULL;
    }
    struct Stack *cur = stkHead->next;
    while (cur->next != NULL) {
        if (cur->name == name) {
            return cur;
        }
        cur = cur->next;
    }
    if (cur->name == name) {
        return cur;
    }
    return NULL;
}

//add a new stack at the end of the stack
struct Stack *addStk(unsigned char value, struct Stack *stkHead) {
    struct Stack *current = stkHead;
    if (current->next == NULL) {
        struct Stack *newStack = (struct Stack *) malloc(sizeof(struct Stack));
        current->next = newStack;
        newStack->name = value;
        newStack->next = NULL;
        newStack->address = 0;
        return newStack;
    }
    while (current->next != NULL) {
        current = current->next;
    }
    struct Stack *newStack = (struct Stack *) malloc(sizeof(struct Stack));
    current->next = newStack;
    newStack->name = value;
    newStack->next = NULL;
    newStack->address = 0;
    return newStack;
}

//find the value stored in specified address
unsigned char findMemo(unsigned char address) {
    struct Memory *current = memo;
    if (current == NULL) {
        printf("Empty Memo\n");
        exit(1);
    } else {
        while (current->next != NULL) {
            if (current->address == address) {
                //printf("address: %d, value: %d\n",address,current->value);
                return current->value;
            }
            current = current->next;
        }
        if (current->address == address) {
            return current->value;
        } else {
            printf("Not found in Memo(%d)\n", address);
            exit(1);
        }
    }
}

unsigned char allocMemo() {
    unsigned char cnt = 1;
    if (memo == NULL) {
        return 1;
    } else {
        struct Memory *current = memo;
        while (current->next != NULL) {
            cnt++;
            current = current->next;
        }
        return cnt + 1;
    }
}

//change the value stored in specified address, if not found, assign the address at the top of memo
void setMemo(unsigned char address, unsigned char value) {
    if (address == 0) {
        printf("address 0 has been accepted!\n");
        exit(1);
    }
    if (memo == NULL) {
        memo = (struct Memory *) malloc(sizeof(struct Memory));
        memo->address = address;
        memo->value = value;
        memo->next = NULL;
    } else {
        struct Memory *current = memo;
        while (current->next != NULL) {
            if (current->address == address) {
                current->value = value;
            }
            current = current->next;
        }
        if (current->address == address) {
            current->value = value;
        } else {
            current->next = (struct Memory *) malloc(sizeof(struct Memory));
            current->next->address = address;
            current->next->value = value;
            current->next->next = NULL;
        }
    }
}

unsigned char retrieve(struct Variable *var, struct Stack *stkHead) {
    if (var->valueType == 0) {
        return var->value;
    } else if (var->valueType == 1) {
        return registerBank[var->value];
    } else if (var->valueType == 2) {
        struct Stack *ptr = getStk(var->value, stkHead);
        if (ptr == NULL) {
            printf("Non-existed stack\n");
            exit(1);
        }
        return findMemo(ptr->address);;
    } else {
        struct Stack *ptr = getStk(var->value, stkHead);
        if (ptr == NULL) {
            printf("Non-existed pointer\n");
            exit(1);
        }
        //printf("symbol: %d, address: %d\n", ptr->name, ptr->address);
        return findMemo(findMemo(ptr->address));
    }
}

void print(struct Variable *var, struct Stack *stkHead) {
    printf("%d\n", (unsigned int) retrieve(var, stkHead));
}

void equ(struct Variable *var) {
    if (var->valueType != 1) {
        printf("EQU recieved non-register!");
        exit(1);
    } else {
        if (var->value == 0) {
            registerBank[var->value] = 1;
        } else {
            registerBank[var->value] = 0;
        }
    }
}

void not(struct Variable *var) {
    if (var->valueType != 1) {
        printf("EQU recieved non-register!");
        exit(1);
    } else {
        registerBank[var->value] = ~registerBank[var->value];
    }
}

void ref(struct Variable *var1, struct Variable *var2, struct Stack *stkHead) {
    if (var2->valueType != 2) {
        printf("Second variable is not a stack\n");
        exit(1);
    } else if (var1->valueType == 3) {
        struct Stack *ptr = getStk(var1->value, stkHead);
        if (ptr == NULL) {
            ptr = addStk(var1->value, stkHead);
            ptr->address = allocMemo();
        }
        setMemo(ptr->address, getStk(var2->value, stkHead)->address);
    } else if (var1->valueType == 2) {
        struct Stack *ptr = getStk(var1->value, stkHead);
        if (ptr == NULL) {
            ptr = addStk(var1->value, stkHead);
            ptr->address = allocMemo();
        }
        setMemo(ptr->address, getStk(var2->value, stkHead)->address);
    } else if (var1->valueType == 1) {
        registerBank[var1->value] = getStk(var2->value, stkHead)->address;
    } else {
        printf("First variable is not a pointer or stack\n");
        exit(1);
    }
}

void add(struct Variable *var1, struct Variable *var2, struct Stack *stkHead) {
    if (var1->valueType != 1 || var2->valueType != 1) {
        printf("at least one variable is not a register!\n");
        exit(1);
    } else {
        registerBank[var1->value] = retrieve(var1, stkHead) + retrieve(var2, stkHead);
    }
}

void mov(struct Variable *var1, struct Variable *var2, struct Stack *stkHead) {
    if (var1->valueType == 1) {
        registerBank[var1->value] = retrieve(var2, stkHead);
        //printf("value at REG %d has been set to %d\n", var1->value, retrieve(var2, stkHead));
    } else if (var1->valueType == 2) {
        struct Stack *stk = getStk(var1->value, stkHead);
        if (stk == NULL) {
            stk = addStk(var1->value, stkHead);
            stk->address = allocMemo();
        }
        setMemo(stk->address, retrieve(var2, stkHead));
        //printf("value at STK %d has been set to %d\n", stk->address, retrieve(var2, stkHead));
    } else if (var1->valueType == 3) {
        struct Stack *stk = getStk(var1->value, stkHead);
        if (stk == NULL) {
            stk = addStk(var1->value, stkHead);
            stk->address = allocMemo();
            unsigned char address2 = allocMemo();
            setMemo(stk->address, address2);
        }
        setMemo(findMemo(stk->address), retrieve(var2, stkHead));
    } else {
        printf("Unknown issue happened when move value");
        exit(1);
    }
}

int runOp(struct Operation *op, struct Stack *stkHead) {
    //printf("run %d\n",op->opcode);
    if (op->opcode == 0) {
        mov(op->variable1, op->variable2, stkHead);
    } else if (op->opcode == 1) {
        return op->variable1->value;
    } else if (op->opcode == 2) {
        return -1;
    } else if (op->opcode == 3) {
        ref(op->variable1, op->variable2, stkHead);
    } else if (op->opcode == 4) {
        add(op->variable1, op->variable2, stkHead);
    } else if (op->opcode == 5) {
        print(op->variable1, stkHead);
    } else if (op->opcode == 6) {
        not(op->variable1);
    } else if (op->opcode == 7) {
        equ(op->variable1);
    }
    return 0;
}

void runFunc(unsigned char label) {
    struct Function *temp = func;
    while (temp->prev != NULL) {
        if (temp->label == label) {
            break;
        }
        temp = temp->prev;
    }
    if (temp->label == label) {
        struct Stack stkHead = {0, 0, NULL};
        struct Operation *op = temp->operations;
        while (1) {
            int it = runOp(op, &stkHead);
            if (it == -1) {
                freeStkWithoutHead(&stkHead);
                break;
            }
            if (it == 0) {
                op = op->prev;
            } else {
                //printf("once");
                runFunc(it);
                op = op->prev;
            }
        }
    } else {
        printf("function not found(%d)\n", label);
        exit(1);
    }
}

int main(int argc, char *argv[]) {
    FILE *file;
    unsigned char buffer2[256];

    if ((file = fopen(argv[1], "rb")) == NULL) {
        printf("Error! opening file");
        // Program exits if the file pointer returns NULL.
        exit(1);
    }
    const size_t fileSize = fread(buffer2, sizeof(unsigned char), 256, file);

    func = build(buffer2, fileSize);
    runFunc(0);
    freeAll(func);
}

