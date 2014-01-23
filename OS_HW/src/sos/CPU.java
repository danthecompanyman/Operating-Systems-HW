package sos;

/**
 * This class is the centerpiece of a simulation of the essential hardware of a
 * microcomputer.  This includes a processor chip, RAM and I/O devices.  It is
 * designed to demonstrate a simulated operating system (SOS).
 *
 * @see RAM
 * @see SOS
 * @see Program
 * @see Sim
 */
public class CPU
{
    //======================================================================
    //Constants
    //----------------------------------------------------------------------

    //These constants define the instructions available on the chip
    public static final int SET    = 0;    /* set value of reg */
    public static final int ADD    = 1;    // put reg1 + reg2 into reg3
    public static final int SUB    = 2;    // put reg1 - reg2 into reg3
    public static final int MUL    = 3;    // put reg1 * reg2 into reg3
    public static final int DIV    = 4;    // put reg1 / reg2 into reg3
    public static final int COPY   = 5;    // copy reg1 to reg2
    public static final int BRANCH = 6;    // goto address in reg
    public static final int BNE    = 7;    // branch if not equal
    public static final int BLT    = 8;    // branch if less than
    public static final int POP    = 9;    // load value from stack
    public static final int PUSH   = 10;   // save value to stack
    public static final int LOAD   = 11;   // load value from heap
    public static final int SAVE   = 12;   // save value to heap
    public static final int TRAP   = 15;   // system call
    
    //These constants define the indexes to each register
    public static final int R0   = 0;     // general purpose registers
    public static final int R1   = 1;
    public static final int R2   = 2;
    public static final int R3   = 3;
    public static final int R4   = 4;
    public static final int PC   = 5;     // program counter
    public static final int SP   = 6;     // stack pointer
    public static final int BASE = 7;     // bottom of currently accessible RAM
    public static final int LIM  = 8;     // top of accessible RAM
    public static final int NUMREG = 9;   // number of registers

    //Misc constants
    public static final int NUMGENREG = PC; // the number of general registers
    public static final int INSTRSIZE = 4;  // number of ints in a single instr + args.  (Set to a fixed value for simplicity.)

    //======================================================================
    //Member variables
    //----------------------------------------------------------------------
    /**
     * specifies whether the CPU should output details of its work
     **/
    private boolean m_verbose = true;

    /**
     * This array contains all the registers on the "chip".
     **/
    private int m_registers[];

    /**
     * A pointer to the RAM used by this CPU
     *
     * @see RAM
     **/
    private RAM m_RAM = null;

    //======================================================================
    //Methods
    //----------------------------------------------------------------------

    /**
     * CPU ctor
     *
     * Initializes all member variables.
     */
    public CPU(RAM ram)
    {
        m_registers = new int[NUMREG]; //All values are zero by default
        m_RAM = ram;
    }//CPU ctor

    /**
     * getPC
     *
     * @return the value of the program counter
     */
    public int getPC()
    {
        return m_registers[PC];
    }

    /**
     * getSP
     *
     * @return the value of the stack pointer
     */
    public int getSP()
    {
        return m_registers[SP];
    }

    /**
     * getBASE
     *
     * @return the value of the base register
     */
    public int getBASE()
    {
        return m_registers[BASE];
    }

    /**
     * getLIMIT
     *
     * @return the value of the limit register
     */
    public int getLIM()
    {
        return m_registers[LIM];
    }

    /**
     * getRegisters
     *
     * @return the registers
     */
    public int[] getRegisters()
    {
        return m_registers;
    }

    /**
     * setPC
     *
     * @param v the new value of the program counter
     */
    public void setPC(int v)
    {
        m_registers[PC] = v;
    }

    /**
     * setSP
     *
     * @param v the new value of the stack pointer
     */
    public void setSP(int v)
    {
        m_registers[SP] = v;
    }

    /**
     * setBASE
     *
     * @param v the new value of the base register
     */
    public void setBASE(int v)
    {
        m_registers[BASE] = v;
    }

    /**
     * setLIM
     *
     * @param v the new value of the limit register
     */
    public void setLIM(int v)
    {
        m_registers[LIM] = v;
    }

    /**
     * regDump
     *
     * Prints the values of the registers.  Useful for debugging.
     */
    private void regDump()
    {
        for(int i = 0; i < NUMGENREG; i++)
            System.out.print("r" + i + "=" + m_registers[i] + " ");
        System.out.print("PC=" + m_registers[PC] + " ");
        System.out.print("SP=" + m_registers[SP] + " ");
        System.out.print("BASE=" + m_registers[BASE] + " ");
        System.out.print("LIM=" + m_registers[LIM] + " ");
        System.out.println("");
    }//regDump

    /**
     * printIntr
     *
     * Prints a given instruction in a user readable format.  Useful for
     * debugging.
     *
     * @param instr the current instruction
     */
    public void printInstr(int[] instr)
    {
        switch(instr[0])
        {
            case SET:
                System.out.println("SET R" + instr[1] + " = " + instr[2]);
                break;
            case ADD:
                System.out.println("ADD R" + instr[1] + " = R" + instr[2] + " + R" + instr[3]);
                break;
            case SUB:
                System.out.println("SUB R" + instr[1] + " = R" + instr[2] + " - R" + instr[3]);
                break;
            case MUL:
                System.out.println("MUL R" + instr[1] + " = R" + instr[2] + " * R" + instr[3]);
                break;
            case DIV:
                System.out.println("DIV R" + instr[1] + " = R" + instr[2] + " / R" + instr[3]);
                break;
            case COPY:
                System.out.println("COPY R" + instr[1] + " = R" + instr[2]);
                break;
            case BRANCH:
                System.out.println("BRANCH @" + instr[1]);
                break;
            case BNE:
                System.out.println("BNE (R" + instr[1] + " != R" + instr[2] + ") @" + instr[3]);
                break;
            case BLT:
                System.out.println("BLT (R" + instr[1] + " < R" + instr[2] + ") @" + instr[3]);
                break;
            case POP:
                System.out.println("POP R" + instr[1]);
                break;
            case PUSH:
                System.out.println("PUSH R" + instr[1]);
                break;
            case LOAD:
                System.out.println("LOAD R" + instr[1] + " <-- @R" + instr[2]);
                break;
            case SAVE:
                System.out.println("SAVE R" + instr[1] + " --> @R" + instr[2]);
                break;
            case TRAP:
                System.out.print("TRAP ");
                break;
            default:        // should never be reached
                System.out.println("?? ");
                break;          
        }//switch
    }//printInstr

    /**
     * run
     * 
     * Run the simulated program
     */
    public void run()
    {
        //Loop until the program ends
        while (true)
        {
            //Fetch the next instruction from RAM using the CPU register
            int instr[] = m_RAM.fetch(getPC());
            
            //If printing in verbose mode then call the two debugging methods
            if (m_verbose == true) 
            {
                regDump();
                printInstr(instr);
            }
            
            //Decode and execute the instruction.
            //If we run into a TRAP, end the program.
            switch(instr[0])
            {
                case SET:
                    m_registers[instr[1]] = instr[2];
                    break;
                case ADD:
                    m_registers[instr[1]] = m_registers[instr[2]] + m_registers[instr[3]];
                    break;
                case SUB:
                    m_registers[instr[1]] = m_registers[instr[2]] - m_registers[instr[3]];
                    break;
                case MUL:
                    m_registers[instr[1]] = m_registers[instr[2]] * m_registers[instr[3]];
                    break;
                case DIV:
                    m_registers[instr[1]] = m_registers[instr[2]] / m_registers[instr[3]];
                    break;
                case COPY:
                    m_registers[instr[1]] = m_registers[instr[2]];
                    break;
                case BRANCH:
                    setPC(instr[1] + getBASE() - instr.length);
                    break;
                case BNE:
                    if (m_registers[instr[1]] != m_registers[instr[2]])
                        setPC(instr[3] + getBASE() - instr.length);
                    break;
                case BLT:
                    if (m_registers[instr[1]] < m_registers[instr[2]])
                        setPC(instr[3] + getBASE() - instr.length);
                    break;
                case POP:
                    popStack(instr[1]);
                    break;
                case PUSH:
                    pushStack(m_registers[instr[1]]);
                    break;
                case LOAD:
                    if (validateRAMLoc(m_registers[instr[2]]))
                        m_registers[instr[1]] = m_RAM.read(m_registers[instr[2]]);
                    break;
                case SAVE:
                    if (validateRAMLoc(m_registers[instr[2]]))
                        m_RAM.write(m_registers[instr[2]], m_registers[instr[1]]);
                    break;
                case TRAP:
                    return;
                default:        // should never be reached
                    System.out.println("?? ");
                    break;          
            }//switch
            
            //Advance the PC register for the next instruction.
            setPC(getPC() + instr.length);
            
            if(!validateRAMLoc(getPC()))
                return;
        }
    }//run
    
    /**
     * Pop a value off the stack if there's anything to pop off.
     * 
     * @param val   The index of the register to pop the value to.
     */
    public void popStack(int index)
    {
        if (getSP() < m_RAM.getSize())
        {
            m_registers[index] = m_RAM.read(getSP());
            setSP(getSP() + 1);
        }
        else
            System.out.println("ERROR: Nothing to pop off the stack.");
    }//popStack
    
    /**
     * pushStack
     * 
     * Push a value onto the stack if we aren't overriding 
     * a program's allocated space.
     * 
     * @param val   The value to push onto the stack.
     */
    public void pushStack(int val)
    {
        if (getSP() > getLIM())
        {
            setSP(getSP() - 1);
            m_RAM.write(getSP(), val);
        }
        else
            System.out.println("ERROR: Cannot push anything more to the stack.");
    }//pushStack
    
    /**
     * validateRAMLoc
     * 
     * Check that a given RAM location is a location that we can actually access
     * 
     * @param ramLoc    The RAM location to check
     * @return          True if the RAM location is accessible.  False otherwise.
     */
    public boolean validateRAMLoc(int ramLoc)
    {
        if(ramLoc >= getBASE() && ramLoc <= getLIM())
            return true;
        else
            System.out.println("ERROR: Trying to touch RAM outside of allocated space.");
            return false;
    }//validateRAMLoc
};//class CPU
