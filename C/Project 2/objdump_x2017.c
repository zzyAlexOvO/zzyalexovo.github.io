#include <stdio.h>
#include <stdlib.h>
#include "function.h"
#include "construct.h"
#include "output.h"
#include "objdump_x2017.h"
#include "freeMethod.h"

unsigned char registerBank[8];
unsigned char stack[31];
unsigned char stackPtr = 0;

int main(int argc, char *argv[]) {
    FILE *ptr;
    unsigned char buffer2[256];

    if ((ptr = fopen(argv[1], "rb")) == NULL) {
        printf("Error! opening file");
        // Program exits if the file pointer returns NULL.
        exit(1);
    }
    const size_t fileSize = fread(buffer2, sizeof(unsigned char), 256, ptr);

    struct Function *func = build(buffer2, fileSize);
    struct Function *current = func;
    while (current->prev != NULL) {
        current = current->prev;
        output(current->next);
    }
    output(current);
    freeAll(func);
    fclose(ptr);
}