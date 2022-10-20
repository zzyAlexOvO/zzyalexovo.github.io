//store the information of one row in timetable
struct Info{
    char from[100];
    char to[100];
    int secs;
}extern Info;

//build a Info object from a string of format "from::to::12:23:32"
struct Info infoBuilder(const char input[]);