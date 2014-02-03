# This test file is designed to generate an illegal memory access
SET R1 99999
SAVE R1 R1
SET R0 0
PUSH R0 
TRAP