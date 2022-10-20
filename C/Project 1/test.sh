#!/bin/bash
cat tests/single_station1.in | xargs -a tests/single_station1.args ./timetable | diff - tests/single_station1.out
cat tests/Argv_missed.in | xargs -a tests/Argv_missed.args ./timetable | diff - tests/Argv_missed.out
cat tests/invalid_time.in | xargs -a tests/invalid_time.args ./timetable | diff - tests/invalid_time.out
cat tests/no_matching.in | xargs -a tests/no_matching.args ./timetable | diff - tests/no_matching.out
cat tests/over_day.in | xargs -a tests/over_day.args ./timetable | diff - tests/over_day.out
cat tests/several_stations.in | xargs -a tests/several_stations.args ./timetable | diff - tests/several_stations.out
cat tests/space_between_stations.in | xargs -a tests/space_between_stations.args ./timetable | diff - tests/space_between_stations.out
cat tests/Accept_exact_same_time.in | xargs -a tests/Accept_exact_same_time.args ./timetable | diff - tests/Accept_exact_same_time.out

# Trigger all your test cases with this script
