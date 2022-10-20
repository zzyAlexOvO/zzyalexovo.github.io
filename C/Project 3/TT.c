#include <stdio.h>

int haha(char* a){
    int i = 0;
    char b[50];
    while (*a){
        b[i] = *a;
        a++;
        i++;
        printf("%c",*a);
    }
    b[i] = '\0';
    printf("b is %s; end\n", b);
}
int main(){
    char* a = "haha";
    haha(a);
}