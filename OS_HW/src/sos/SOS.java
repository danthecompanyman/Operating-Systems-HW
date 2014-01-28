package sos;

/**
 * This class contains the simulated operating system (SOS).  Realistically it
 * would run on the same processor (CPU) that it is managing but instead it uses
 * the real-world processor in order to allow a focus on the essentials of
 * operating system design using a high level programming language.
 */
public class SOS
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
    
    /**
     * Extra space set aside for stack and heap.
     **/
    private int m_stackAndHeapSpace = 50;

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

    //insert method header here
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

    //None yet!
    
    /*======================================================================
     * System Calls
     *----------------------------------------------------------------------
     */
    
    //<insert header comment here>
    public void systemCall()
    {
        //%%%REPLACE THESE LINES WITH APPROPRIATE CODE
        System.out.println("TRAP handled!");
        System.exit(0);
    }//systemCall
};//class SOS
