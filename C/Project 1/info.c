#include "info.h"
#include "mystr.h"
#include "stdlib.h"
#include <stdio.h>

struct Info infoBuilder(const char input[]){
    struct Info info;
    info.secs = 0;
    int i =0;
    int cnt = 0;
    int cut_index = 0;
    char cut[4096];

    while (input[i]){
        if (input[i] == ':'){
            cnt++;
            cut[cut_index] = *"\0";
            if (cnt == 1){
                if(cut_index == 0){
                    info.secs = -1;
                    return info;
                }
                strcpy(info.from,cut);
            }
            else if (cnt == 3){
                if(cut_index == 0){
                    info.secs = -1;
                    return info;
                }
                strcpy(info.to,cut);
            }
            else if (cnt == 5){
                if(cut_index != 2 || atoi(cut) >=24){
                    info.secs = -1;
                    return info;
                }
                info.secs += atoi(cut)*3600;
            }
            else if (cnt == 6){
                if(cut_index != 2 || atoi(cut) >=60){
                    info.secs = -1;
                    return info;
                }
                info.secs += atoi(cut)*60;
            }
            cut_index = 0;
        }
        else{
            cut[cut_index] = input[i];
            cut_index ++;
        }
        i++;
    }
    cut[cut_index] = *"\0";
    if(atoi(cut) >=60){
        info.secs = -1;
        return info;
    }
    info.secs += atoi(cut);
    return info;
}