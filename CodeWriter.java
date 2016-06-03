import java.io.BufferedWriter;
import java.io.FileWriter;


/**
 * Translates VM commands into Hack assembly code.
 */
public class CodeWriter {

	private static final String ADD = "add", SUB = "sub", NEG = "neg", EQ = "eq", GT = "gt",
			LT = "lt", AND = "and", OR = "or", NOT = "not";
	private static final String ARGUMENT = "argument", LOCAL = "local", THIS = "this", THAT = "that", 
			POINTER = "pointer", STATIC = "static", CONSTANT = "constant", TEMP = "temp";
	private static final String THIS_LABEL = "THIS", THAT_LABEL = "THAT";
	private static final String INC_SP = "@SP\nM=M+1\n";
	private static final String DEC_SP = "@SP\nM=M-1\n";
	private static final String JUMP_LABEL = "JUMP_";
	
	
	private static long counter = 0; 
	private BufferedWriter writer;
	private String fileName;
	
	/*
	 * Returns the current label according to the given index.
	 */
	private static String getCounterLabel(long index) {
		return JUMP_LABEL + index;
	}
	
	/*
	 * Returns the current label according to the given segment.
	 */
	private static String getSegmentlabel(String segment) {
		String label = "";
		switch (segment) {
			case LOCAL:
				label = "LCL";
				break;
			case ARGUMENT:
				label = "ARG";
				break;
			case THIS:
				label = THIS_LABEL;
				break;
			case THAT:
				label = THAT_LABEL;
				break;
			case TEMP:
				label = "5";
				break;
		}
		return label;
	}
	
	/*
	 * Writes the asm code of the given binary command. 
	 */
	private static String binaryOpCode(String command) {
		String operation = "";
		switch(command) {
			case ADD:
				operation = "+";
				break;
			case SUB:
				operation = "-";
				break;
			case AND:
				operation = "&";
				break;
			case OR:
				operation = "|";
				break;
		}
		return DEC_SP + "A=M\nD=M\n@SP\nA=M-1\nM=M" + operation + "D\n";
	}
	
	/*
	 * Writes the asm code of the given unary command.
	 */
	private static String unaryOpCode(String command) {
		String operation = "";
		switch(command) {
			case NEG:
				operation = "-";
				break;
			case NOT:
				operation = "!";
				break;
		}
		return "@SP\nA=M-1\nM=" + operation + "M\n";  
	}

	/*
	 * Writes the asm code of the given jump command.
	 */
	private static String boolOpCode(String command) {
		++counter;
		String jump = "";
		switch(command) {
		case EQ:
			jump = "JEQ"; 
			break;
		case GT:
			jump = "JGT"; 
			break;
		case LT:
			jump = "JLT";
			break;
		}
		return DEC_SP + "A=M\nD=M\n@SP\nA=M-1\nD=M-D\nM=-1\n@" + getCounterLabel(counter) + 
				"\nD;" + jump + "\n@SP\nA=M-1\nM=0\n(" + getCounterLabel(counter) + ")\n"; 
	}
	
	/*
	 * Writes the asm code for the push command.
	 */
	private static String pushCode(String segment, int index, String fileName) {
		String buffer = "";
		switch (segment) {
			case ARGUMENT:
			case LOCAL:
			case THIS:
			case THAT:
			case TEMP:
				String addr = (segment.equals(TEMP)) ? "A" : "M";
				buffer = "@" + index + "\nD=A\n@" + getSegmentlabel(segment) + "\nA=" + addr + "+D\nD=M\n"; 
				break;
			case POINTER:
				String entry = (index == 0) ? THIS_LABEL : THAT_LABEL;
				buffer = "@" + entry + "\nD=M\n";
				break;
			case STATIC:
				buffer = "@" + fileName + "." + index + "\nD=M\n";
				break;
			case CONSTANT:
				if (index >= 0) {
					buffer = "@" + index + "\nD=A\n";
				}
				else {
					buffer = "@" + -index + "\nD=-A\n";
				}
				break;
		}
		return buffer + "@SP\nA=M\nM=D\n" + INC_SP;
	}
	
	/*
	 * Writes the asm code for the pop command.
	 */
	private static String popCode(String segment, int index, String fileName) {
		String buffer = "";
		String bufferMid = DEC_SP + "A=M\nD=M\n";
		String bufferSuff = "@R13\nM=D\n" + bufferMid + "@R13\nA=M\nM=D\n"; 
		switch (segment) {
			case ARGUMENT:
			case LOCAL:
			case THIS:
			case THAT:
			case TEMP:
				String addr = (segment.equals(TEMP)) ? "A" : "M";
				buffer = "@" + index + "\nD=A\n" + "@" + getSegmentlabel(segment) + "\nD=" + addr + "+D\n" + bufferSuff;
				break;
			case POINTER:
				String entry = (index == 0) ? THIS_LABEL : THAT_LABEL;
				buffer = "@" + entry +"\nD=A\n" + bufferSuff;
				break;
			case STATIC:
				buffer = bufferMid + "@" + fileName + "." + index + "\nM=D\n"; ;
				break;
		}
		return buffer;
	}
	
	
	/**
	 * The constructor.
	 * @param fileName the file name of the output asm file. 
	 */
	public CodeWriter(String fileName) {
		try {
			this.writer = new BufferedWriter(new FileWriter(fileName));
		}
		catch(Exception e) {
			System.exit(1);
		}
	}
	
	/**
	 * Sets a new file name.
	 */
	public void setFileName(String fileName) {
		fileName = fileName.substring(0, fileName.lastIndexOf(VMtranslator.VM_SUFF));
		this.fileName = fileName;
	}
	
	/**
	 * Writes the assembly code that is the translation of the given arithmetic command.
	 */
	public void writeArithmetic(String command) {
		
		String buffer = "";
		switch (command) {
			case ADD:
			case SUB:
			case AND:
			case OR:
				buffer = binaryOpCode(command);
				break;
			case NEG:
			case NOT:
				buffer = unaryOpCode(command);
				break;
			case EQ:
			case GT:
			case LT:
				buffer = boolOpCode(command);
				break;
		}
		try {
			this.writer.write(buffer);
		} catch (Exception e) {
			System.exit(1);
		}
	}
	
	/**
	 * Writes the assembly code that is translation of the given command, 
	 * where command is either C_PUSH or C_POP.
	 */
	public void writePushPop(CommandType commandType, String segment, int index) {
		
		String buffer = "";
		switch (commandType) {
			case C_PUSH:
				buffer = pushCode(segment, index, this.fileName);
				break;			
			case C_POP:
				buffer = popCode(segment, index, this.fileName);
				break;
			default:
				break;
		}
		try {
			this.writer.write(buffer);
		} catch (Exception e) {
			System.exit(1);
		}
	}
	
	/**
	 * Closes the output file.
	 */
	public void close() {
		try {
			this.writer.close();
		}
		catch (Exception e) {
			System.exit(1);
		}
	}
	
}
