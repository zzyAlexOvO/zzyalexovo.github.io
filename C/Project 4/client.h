#ifndef ASSIGNMENT4_CLIENT_H
#define ASSIGNMENT4_CLIENT_H

int init_client(char* identifier, char* domain);
int says(char* identifier, char* domain, char* content);
int receive(char* identifier, char* domain);

#endif //ASSIGNMENT4_CLIENT_H
