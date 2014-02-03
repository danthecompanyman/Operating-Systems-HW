Operating Systems HW
====================

This repo contains the evolution of my homework in UP's Operating Systems class over the course of the semester.
The goal of this class is to build an entire self-contained simulation of an OS and its underlying hardware by the end of the semester.

The assembly language for this OS is a simplified version of MIPS created specifically for this class called Pidgin Assembly.

All of this code was done in Eclipse, and I'm using the opportunity to learn and use the GitHub plugin for Eclipse, known as EGit.  Annoyed at it's apparent desire to push up the parent project folder instead of just the contents, but whatever.

Check the list of commits to see each finished homework assignment.

Homework 1
----------
Complete just enough code that the simulation can run a simple program.

Homework 2
----------
Support for four systems calls and three interrupts should be implemented in SOS.java.

* System Calls
  * systemCallCoreDump: Perform a core dump and end the current process.
  * systemCallGetPID: Push the ID of the current process onto the stack.
  * systemCalOutput: Output the topmost value on the stack.
  * systemCallExit: End the current process.

* Interrupts
  * interruptIllegalMemoryAccess: Interrupt the current process due to an illegal memory access.
  * interruptDivideByZero: Interrupt the current process due to an attempt to divide by zero.
  * interruptIllegalInstruction: Interrupt the current process due to an attempted execution of an illegal instruction.

All interrupts end with the destruction of the current process.