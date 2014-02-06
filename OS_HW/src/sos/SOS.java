package sos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

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
    public static final int SYSCALL_OPEN     = 3;    /* access a device */
    public static final int SYSCALL_CLOSE    = 4;    /* release a device */
    public static final int SYSCALL_READ     = 5;    /* get input from device */
    public static final int SYSCALL_WRITE    = 6;    /* send output to device */
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
     * The ID of the current process
     */
    private ProcessControlBlock m_currProcess;
    
    /**
     * The list of devices "installed" in the system.
     */
    private Vector<DeviceInfo> m_devices = null;

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
        m_currProcess = new ProcessControlBlock(42);
        m_devices = new Vector<DeviceInfo>();
        
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

    /**
     * registerDevice
     *
     * adds a new device to the list of devices managed by the OS
     *
     * @param dev     the device driver
     * @param id      the id to assign to this device
     * 
     */
    public void registerDevice(Device dev, int id)
    {
        m_devices.add(new DeviceInfo(dev, id));
    }//registerDevice
    
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
                sysCallExit();
                break;
            case SYSCALL_OUTPUT:
                sysCallOutput();
                break;
            case SYSCALL_GETPID:
                sysCallGetPID();
                break;
            case SYSCALL_OPEN:
                sysCallOpen();
                break;
            case SYSCALL_CLOSE:
                sysCallClose();
                break;
            case SYSCALL_READ:
                sysCallRead();
                break;
            case SYSCALL_WRITE:
                sysCallWrite();
                break;
            case SYSCALL_COREDUMP:
                sysCallCoreDump();
                break;
            default:
                return; //This shouldn't happen
        }
    }//systemCall

    /**
     * Perform a core dump and end the current process.
     */
    private void sysCallCoreDump()
    {
        m_CPU.regDump();
        for (int i = 0; i < 3; ++i)
            if (m_CPU.getSP() <= m_CPU.getLIM()) 
                System.out.println(m_CPU.popStack());
        sysCallExit();
    }

    /**
     * Push the ID of the current process onto the stack.
     */
    private void sysCallGetPID()
    {
        m_CPU.pushStack(m_currProcess.processId);
    }

    /**
     * Output the topmost value on the stack.
     */
    private void sysCallOutput()
    {
        System.out.println("\nOUTPUT: " + m_CPU.popStack());
    }

    /**
     * End the current process.
     */
    private void sysCallExit()
    {
        System.exit(0);
    }
    
    /**
     * Register the current process to a new device
     */
    private void sysCallOpen()
    {
        //Get the device ID
        int deviceID = m_CPU.popStack();
        
        //Add the current process to the device to indicate that it's using the device
        findDevice(deviceID).procs.add(m_currProcess);
    }
    
    /**
     * 
     */
    private void sysCallClose()
    {
        //Get the device ID
        int deviceID = m_CPU.popStack();
        
        //Remove the current process from the device to indicate that the device is no longer used
        findDevice(deviceID).procs.remove(m_currProcess);
    }
    
    private void sysCallRead()
    {
        
    }
    
    private void sysCallWrite()
    {
        
    }
    
    /**
     * Helper method to find a device with a given ID in the Vector of DeviceInfos
     * 
     * @param deviceID  The ID of the device to find
     * @return  The DeviceInfo instance matching the given ID
     */
    private DeviceInfo findDevice(int deviceID)
    {
        for (DeviceInfo di : m_devices)
            if (di.getId() == deviceID)
                return di;
        return null;
    }
    
    //======================================================================
    // Inner Classes
    //----------------------------------------------------------------------

    /**
     * class ProcessControlBlock
     *
     * This class contains information about a currently active process.
     */
    private class ProcessControlBlock
    {
        /**
         * a unique id for this process
         */
        private int processId = 0;

        /**
         * constructor
         *
         * @param pid        a process id for the process.  The caller is
         *                   responsible for making sure it is unique.
         */
        public ProcessControlBlock(int pid)
        {
            this.processId = pid;
        }

        /**
         * @return the current process' id
         */
        public int getProcessId()
        {
            return this.processId;
        }
    }//class ProcessControlBlock

    /**
     * class DeviceInfo
     *
     * This class contains information about a device that is currently
     * registered with the system.
     */
    private class DeviceInfo
    {
        /** every device has a unique id */
        private int id;
        /** a reference to the device driver for this device */
        private Device device;
        /** a list of processes that have opened this device */
        private Vector<ProcessControlBlock> procs;

        /**
         * constructor
         *
         * @param d          a reference to the device driver for this device
         * @param initID     the id for this device.  The caller is responsible
         *                   for guaranteeing that this is a unique id.
         */
        public DeviceInfo(Device d, int initID)
        {
            this.id = initID;
            this.device = d;
            d.setId(initID);
            this.procs = new Vector<ProcessControlBlock>();
        }

        /** @return the device's id */
        public int getId()
        {
            return this.id;
        }

        /** @return this device's driver */
        public Device getDevice()
        {
            return this.device;
        }

        /** Register a new process as having opened this device */
        public void addProcess(ProcessControlBlock pi)
        {
            procs.add(pi);
        }
        
        /** Register a process as having closed this device */
        public void removeProcess(ProcessControlBlock pi)
        {
            procs.remove(pi);
        }

        /** Does the given process currently have this device opened? */
        public boolean containsProcess(ProcessControlBlock pi)
        {
            return procs.contains(pi);
        }
        
        /** Is this device currently not opened by any process? */
        public boolean unused()
        {
            return procs.size() == 0;
        }
    }//class DeviceInfo
};//class SOS
