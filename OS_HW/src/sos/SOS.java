package sos;

import java.util.Arrays;

/**
 * This class contains the simulated operating system (SOS).  Realistically it
 * would run on the same processor (CPU) that it is managing but instead it uses
 * the real-world processor in order to allow a focus on the essentials of
 * operating system design using a high level programming language.
 */
public class SOS implements CPU.TrapHandler
{
    //======================================================================
    //Constants
    //----------------------------------------------------------------------

    //These constants define the system calls this OS can currently handle
    public static final int SYSCALL_EXIT     = 0;    /* exit the current program */
    public static final int SYSCALL_OUTPUT   = 1;    /* outputs a number */
    public static final int SYSCALL_GETPID   = 2;    /* get current process id */
    public static final int SYSCALL_COREDUMP = 9;    /* print process state and exit */
    
    //======================================================================
    //Member variables
    //----------------------------------------------------------------------

    /**
     * This flag causes the SOS to print lots of potentially helpful status messages
     **/
    public static final boolean m_verbose = false;
    
    /**
     * The CPU the operating system is managing.
     **/
    private CPU m_CPU = null;
    
    /**
     * The RAM attached to the CPU.
     **/
    private RAM m_RAM = null;

    /*======================================================================
     * Constructors & Debugging
     *----------------------------------------------------------------------
     */
    
    /**
     * The constructor does nothing special
     */
    public SOS(CPU c, RAM r)
    {
        //Init member list
        m_CPU = c;
        m_RAM = r;
        
        m_CPU.registerTrapHandler(this);
    }//SOS ctor
    
    /**
     * Does a System.out.print as long as m_verbose is true
     **/
    public static void debugPrint(String s)
    {
        if (m_verbose)
            System.out.print(s);
    }
    
    /**
     * Does a System.out.println as long as m_verbose is true
     **/
    public static void debugPrintln(String s)
    {
        if (m_verbose)
            System.out.println(s);
    }
    
    /*======================================================================
     * Memory Block Management Methods
     *----------------------------------------------------------------------
     */

    //None yet!
    
    /*======================================================================
     * Device Management Methods
     *----------------------------------------------------------------------
     */

    //None yet!
    
    /*======================================================================
     * Process Management Methods
     *----------------------------------------------------------------------
     */

    //None yet!
    
    /*======================================================================
     * Program Management Methods
     *----------------------------------------------------------------------
     */

    /**
     * Create a process out of the given program and allocate the necessary space
     * for that program in our RAM.
     * 
     * @param prog      - The program to create the process for.
     * @param allocSize - The amount of memory to allocate for the program.
     */
    public void createProcess(Program prog, int allocSize)
    {
        //Compile the program into an array of int.
        int[] compiledProg = prog.export();
        
        //Set the BASE and LIMIT registers based upon the allocation size given.
        m_CPU.setBASE(0);
        //Honor the value of the allocSize parameter given and leave space for a stack and heap.
        m_CPU.setLIM(m_CPU.getBASE() + allocSize);
        
        //Copy the given program into the simulated RAM. 
        for(int i = 0; i < compiledProg.length; i++)
            m_RAM.write(i + m_CPU.getBASE(), compiledProg[i]);
        
        //Set the PC at the beginning of the program. 
        m_CPU.setPC(m_CPU.getBASE());
        //Set the SP to the opposite side of the RAM.
        m_CPU.setSP(m_CPU.getLIM() - 1);
    }//createProcess
        
    /*======================================================================
     * Interrupt Handlers
     *----------------------------------------------------------------------
     */

    /**
     * Interrupt the current process due to an illegal memory access and end the process.
     * 
     * @param addr - The illegal address that is trying to be accessed
     */
    @Override
    public void interruptIllegalMemoryAccess(int addr)
    {
        System.out.println("\nERROR: Illegal Memory Access at Address " + addr);
        System.exit(0);
    }

    /**
     * Interrupt the current process due to an attempt to divide by zero and end the process.
     */
    @Override
    public void interruptDivideByZero()
    {
        System.out.println("\nERROR: Division by Zero");
        System.exit(0);
    }

    /**
     * Interrupt the current process due to the attempted execution of an illegal instruction
     * and end the process.
     * 
     * @param instr - The illegal instruction that is trying to execute.
     */
    @Override
    public void interruptIllegalInstruction(int[] instr)
    {
        System.out.println("\nERROR: Illegal Instruction Attempted: " + Arrays.toString(instr));
        System.exit(0);
    }
    
    /*======================================================================
     * System Calls
     *----------------------------------------------------------------------
     */
    
    /**
     * Execute a system call based on the most recent value on the stack.
     */
    public void systemCall()
    {
        switch (m_CPU.popStack())
        {
            case SYSCALL_EXIT:
                systemCallExit();
                break;
            case SYSCALL_OUTPUT:
                systemCallOutput();
                break;
            case SYSCALL_GETPID:
                systemCallGetPID();
                break;
            case SYSCALL_COREDUMP:
                systemCallCoreDump();
                break;
            default:
                return; //This shouldn't happen
        }
    }//systemCall

    /**
     * Perform a core dump and end the current process.
     */
    private void systemCallCoreDump()
    {
        m_CPU.regDump();
        for (int i = 0; i < 3; ++i)
            if (m_CPU.getSP() <= m_CPU.getLIM()) 
                System.out.println(m_CPU.popStack());
        systemCallExit();
    }

    /**
     * Push the ID of the current process onto the stack.
     */
    private void systemCallGetPID()
    {
        m_CPU.pushStack(42);
    }

    /**
     * Output the topmost value on the stack.
     */
    private void systemCallOutput()
    {
        System.out.println("\nOUTPUT: " + m_CPU.popStack());
    }

    /**
     * End the current process.
     */
    private void systemCallExit()
    {
        System.exit(0);
    }
};//class SOS
