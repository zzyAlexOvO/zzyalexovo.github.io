#include <stdio.h>
#include <ctype.h>
#include <assert.h>
#include <stdlib.h>
#include "info.h"
#include "mystr.h"

#define MAX_DAYTIME_IN_SECS h'h'h86400;//time of 24 hours in secs, which cannot be achieved

//format second-only time back to normal "xx:xx:xx" and output
void time_transfer(const int secs){
    int hh = secs/3600;
    int mm = (secs - hh*3600)/60;
    int ss = (secs - hh*3600 - mm*60);
    printf("%02d:%02d:%02d\n",hh,mm,ss);
}

//transfer time of format "xx:xx:xx" into only seconds containing
int secs_generater(char orig[]){
    int hh = atoi(orig);
    //check hour is between 0 to 24
    assert(hh<24 && hh>=0);
    char temp[3];
    temp[2] = *"\0";
    temp[0] = orig[3];
    temp[1] = orig[4];
    int mm = atoi(temp);
    //check minute is between 0 to 60
    assert(mm<60 && mm>=0);
    temp[0] = orig[6];
    temp[1] = orig[7];
    int ss = atoi(temp);
    //check second is between 0 to 60
    assert(ss<60 && ss>=0);
    int secs = 3600*hh + 60*mm + ss;
    return secs;
}

int main(int argc, char *argv[])
{
    //pre-check the number of arguments
    if (argc != 4){
        printf("Please provide <source> <destination> <time> as command line arguments\n");
        exit(0);
    }

    //format time from argv
    char *time = argv[3];
    int secs = secs_generater(time);


    int closest = MAX_DAYTIME_IN_SECS;//the closest time found
    int earliest = MAX_DAYTIME_IN_SECS;//the time of the first train daily found
    char input[4096];

    //keep accepting input and save the time that is closer than closest or earlier than earliest under the condition of having same destination and start
    while(fgets(input, 4096, stdin) != NULL) {
        struct Info info = infoBuilder(input);
        //abandon invalid info
        if (info.secs == -1){
            continue;
        }
        if ((strcmp(argv[1],info.from)==0) && (strcmp(argv[2],info.to)==0)){
            if(info.secs >= secs && info.secs < closest){
                closest = info.secs;
            }
            else if (info.secs<earliest){
                earliest = info.secs;
            }
        }
    }

    //generate output
    if (closest < 86400){
        printf("The next train to %s from %s departs at ",argv[2], argv[1]);
        time_transfer(closest);
    }
    else if (earliest < 86400){
        printf("The next train to %s from %s departs at ",argv[2], argv[1]);
        time_transfer(earliest);
    }
    else{
        printf("No suitable trains can be found\n");
    }

}