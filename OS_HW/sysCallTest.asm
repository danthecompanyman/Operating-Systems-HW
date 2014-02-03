#This program tests several system calls.
SET R0 0
SET R1 1
SET R2 2
SET R3 3
SET R4 4
PUSH R2     # PID (42) is pushed to the stack
TRAP
PUSH R1
TRAP		# Should output 42
SET R4 9
PUSH R4
PUSH R4		# So there is something on the stack to be printed.
PUSH R4
PUSH R4
SET R4 44
PUSH R4
SET R4 9
PUSH R4
TRAP		# Core dump
PUSH R0
TRAP		# Exits process
