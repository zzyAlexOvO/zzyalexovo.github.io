#ifndef SERVER_H
#define SERVER_H
#define MESSAGE_LENGTH 2048
#define ID_LENGTH 256
#define CONTENT_LENGTH 2046
#define SAY_LENGTH 1790
#define GLOBAL_PIPE "gevent"

int connect(char* content);
int say (char* content);
int is_end_with(const char *str1, char *str2);
int client_handler(char* id, char* domain);
int saycont (char* content);
#endif
