#This program is designed to test performing a factorial
#The number to take the factorial of is located in R0

SET R0 5		#number to take factorial of
COPY R1 R0		#value
SET R2 1		#limit
SET R3 1		#code

BLT R3 R0 loop	#check if value is equal to or less than 1
SET R0 1		#if so set value to 1
BRANCH end		#jump to end

:loop
SUB R1 R1 R2	#perform factorial
MUL R0 R0 R1
BNE R1 R2 loop

:end
PUSH R0
PUSH R3
TRAP
