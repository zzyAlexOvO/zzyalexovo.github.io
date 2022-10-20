CC=gcc
CFLAGS=-fsanitize=address -Wvla -Wall -Werror -g -std=gnu11 -lasan

timetable: timetable.c
	$(CC) $(CFLAGS) $^ mystr.c info.c -o $@

test: timetable
	bash test.sh

clean:
	rm timetable
	rm info
	rm mystr
