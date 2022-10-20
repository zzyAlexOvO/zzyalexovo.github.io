#ifndef ASSIGNMENT2_SUBMIT_CONSTRUCT_H
#define ASSIGNMENT2_SUBMIT_CONSTRUCT_H

unsigned char cut(unsigned char *ptr, const int fileSize, int *currentIndex, int *currentBit, unsigned char size);

struct Variable *extract(unsigned char *ptr, const int fileSize, int *currentIndex, int *currentBit);

struct Operation *
construct(const unsigned char opcode, unsigned char *ptr, const int fileSize, int *currentIndex, int *currentBit);

struct Function *build(unsigned char *buffer2, int fileSize);

#endif //ASSIGNMENT2_SUBMIT_CONSTRUCT_H
