#!/bin/bash
./objdump_x2017 tests/complex.x2017 | diff - tests/complex.asm
./objdump_x2017 tests/Move_Around.x2017 | diff - tests/Move_Around.asm
./objdump_x2017 tests/Print_Reg.x2017 | diff - tests/Print_Reg.asm
./objdump_x2017 tests/print_value.x2017 | diff - tests/print_value.asm
./objdump_x2017 tests/Ref_Ptr.x2017 | diff - tests/Ref_Ptr.asm
./objdump_x2017 tests/Ref_Stk.x2017 | diff - tests/Ref_Stk.asm
./objdump_x2017 tests/Stk_Print.x2017 | diff - tests/Stk_Print.asm
./objdump_x2017 tests/Stk_to_Pointer.x2017 | diff - tests/Stk_to_Pointer.asm
./objdump_x2017 tests/Test_Add.x2017 | diff - tests/Test_Add.asm
./objdump_x2017 tests/Test_Equ.x2017 | diff - tests/Test_Equ.asm
./objdump_x2017 tests/Test_Not.x2017 | diff - tests/Test_Not.asm
./objdump_x2017 tests/Two_Function.x2017 | diff - tests/Two_Function.asm
./objdump_x2017 tests/Two_Function_Arranged.x2017 | diff - tests/Two_Function_Arranged.asm
./objdump_x2017 tests/sample_program.x2017 | diff - tests/sample_program.asm

./vm_x2017 tests/complex.x2017 | diff - tests/complex.out
./vm_x2017 tests/Move_Around.x2017 | diff - tests/Move_Around.out
./vm_x2017 tests/Print_Reg.x2017 | diff - tests/Print_Reg.out
./vm_x2017 tests/print_value.x2017 | diff - tests/print_value.out
./vm_x2017 tests/Ref_Ptr.x2017 | diff - tests/Ref_Ptr.out
./vm_x2017 tests/Ref_Stk.x2017 | diff - tests/Ref_Stk.out
./vm_x2017 tests/Stk_Print.x2017 | diff - tests/Stk_Print.out
./vm_x2017 tests/Stk_to_Pointer.x2017 | diff - tests/Stk_to_Pointer.out
./vm_x2017 tests/Test_Add.x2017 | diff - tests/Test_Add.out
./vm_x2017 tests/Test_Equ.x2017 | diff - tests/Test_Equ.out
./vm_x2017 tests/Test_Not.x2017 | diff - tests/Test_Not.out
./vm_x2017 tests/Two_Function.x2017 | diff - tests/Two_Function.out
./vm_x2017 tests/Two_Function_Arranged.x2017 | diff - tests/Two_Function_Arranged.out
# Trigger all your test cases with this script