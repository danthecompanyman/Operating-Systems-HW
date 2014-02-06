# This program tests the system call handler routines 
# to verify that they detect possible errors appropriately
# (relating to the devices)

# Open the keyboard device
SET R0 0             # keyboard device
PUSH R0              # push arg on stack
SET R0 3             # OPEN System Call Number
PUSH R0              # Push syscall id on stack
TRAP                 # It's a TRAP

# Check success code
POP R1               # Grab return code
SET R2 0             # 0 is success
BNE R1 R2 end        # goto end on error

# Open the console device
SET R0 1             # Console device
PUSH R0              # Push arg onto stack
SET R0 3             # OPEN syscall number
PUSH R0              # Push syscall on stack
TRAP                 # Open the console

# Check success code
POP R1               # Get return code from stack
SET R2 0             # 0 is success
BNE R1 R2 end        # Verify success

# Attempt to open a device that doesn't exit
SET R0 3             # Invalid device
PUSH R0
SET R0 3             # Attempt to open the device
PUSH R0
TRAP

# Check success code
POP R1               # Pop and check for success
SET R2 0
BNE R1 R2 end

# Attempt to open a device that is already opened
SET R0 0             # Try to open the keyboard device
PUSH R0
SET R0 3             # OPEN syscall number
PUSH R0
TRAP                 # Call it

# Check success code
POP R0
SET R1 0
BNE R0 R1 end

# Close the keyboard
SET R0 0             # Keyboard device number
PUSH R0              # Push arg to stack
SET R1 4             # CLOSE system call number
PUSH R1
TRAP

# Check success code
POP R0
SET R1 0
BNE R0 R1 end

# Attempt to read from the keyboard after it has been closed
SET R0 0
PUSH R0
PUSH R0
SET R0 5
PUSH R0
TRAP

# Check for failure
POP R3
SET R0 0
BNE R0 R3 end

# No need to retrieve value from keyboard (reading should fail)

# Open the keyboard back up
SET R0 0
PUSH R0
SET R0 3
PUSH R0
TRAP

# Check success
POP R0
SET R1 0
BNE R0 R1 end

# Attempt to write to the keyboard
SET R0 0
PUSH R0
PUSH R0
SET R2 5
PUSH R2
SET R0 6
PUSH R0
TRAP

# Check for success
POP R0
SET R1 0
BNE R0 R1 end

# Attempt to read from the console
SET R0 1
PUSH R0
PUSH R0
SET R0 5
PUSH R0
TRAP

# Check for success
POP R0
SET R1 0
BNE R0 R1 end

# Write to the console
SET R0 1
PUSH R0
PUSH R0
SET R1 0
PUSH R1
SET R2 6
PUSH R2
TRAP

# Close console
SET R0 1
PUSH R0
SET R0 4
PUSH R4
TRAP

# Check for success
POP R0
SET R1 0
BNE R0 R1 end

# Close keyboard
SET R0 0
PUSH R0
SET R0 4
PUSH R4
TRAP


# Perform an exit system call
:end
SET R0 0             # Push exit syscall id to
PUSH R0              # stack and TRAP
TRAP


