CC=gcc
CFLAGS=-fsanitize=address -Wvla -Wall -Werror -g -std=gnu11 -lasan -lm

# fill in all your make rules

vm_x2017:
	$(CC) $(CFLAGS) $^ vm_x2017.c construct.c freeMethod.c -o $@

objdump_x2017: 
	$(CC) $(CFLAGS) $^ objdump_x2017.c construct.c output.c freeMethod.c -o $@
tests:
	echo "tests"

run_tests:
	bash test.sh

clean:
	echo "clean"

