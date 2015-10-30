package lib.ir.asm;

import java.util.LinkedList;
import lib.ir.ast.Expression;
import lib.ir.ast.IntLiteral;
import lib.ir.ast.Location;
import lib.ir.icode.Instruction;

public class AssemblyGenerator {
    private int numtemp;
    public String result;
    private int bytes = 4; //Default number of bytes
    
    private String getNextIdTemp(){
        numtemp++;
        return "" + numtemp;
    }
    
    public AssemblyGenerator(LinkedList<Instruction> instructions){
        result = "";
        numtemp = 0;
        while(!instructions.isEmpty()){
            Instruction instruction = instructions.pop();
            switch (instruction.getInstruction()){
                case METHODDECL:
                    result += genMethodDecl(instruction);
                    break;
                case LABEL:
                    result += genLabel(instruction);
                    break;
                case ASSMNT:
                    result += genAssmnt(instruction);
                    break;
                case RETURN:
                    result += genReturn(instruction);
                    break;
                case MINUSINT:
                    result += genMinusInt(instruction);
                    break;
                case NEGATION:
                    result += genNegation(instruction);
                    break;
                case PUSH:
                    result += genPush(instruction);
                    break;
                case CALL:
                    result += genCall(instruction);
                    break;
                case JUMP:
                    result += genJump(instruction);
                    break;
                case JUMPFALSE:
                    result += genJumpFalse(instruction);
                    break;
                case INC:
                    result += genInc(instruction);
                    break;
                case SUMINT:
                    result += genSumInt(instruction);
                    break;
                case SUBINT:
                    result += genSubInt(instruction);
                    break;
                case MULTINT:
                    result += genMultInt(instruction);
                    break;
                case DIVIDEINT:
                    result += genDivideInt(instruction);
                    break;
                case MOD:
                    result += genMod(instruction);
                    break;
                case AND:
                    result += genAnd(instruction);
                    break;
                case OR:
                    result += genOr(instruction);
                    break;
                case ARRAYASSMNT:
                    result += genArrayAssmnt(instruction);
                    break;
                case ARRAYACCESS:
                    result += genArrayAccess(instruction);
                    break;
                case EQ:
                    result += genEq(instruction);
                    break;
                case NOT_EQ:
                    result += genNotEq(instruction);
                    break;
                case LESS:
                    result += genLess(instruction);
                    break;
                case LESS_EQ:
                    result += genLessEq(instruction);
                    break;
                case GTR:
                    result += genGtr(instruction);
                    break;
                case GTR_EQ:
                    result += genGtr(instruction);
                    break;
            }
            result += "\n";
        }
        
    }
    
    private String operand(Expression e){
        String type = e.getClass().getSimpleName();
        String res = "";
        if (type.equals("VarLocation")){ 
           int offsetOp = ((Location)e).getOffset();
           if (offsetOp >= 1 && offsetOp <= 7 ){ // Is register!
               switch(offsetOp){
               case 1:
                   res = "%edi";
                   break;
                case 2:
                   res = "%esi";
                   break;
                case 3:
                   res = "%edx";
                   break;
                case 4:
                   res = "%ecx";
                   break;
                case 5:
                   res = "%r8d";
                   break;
                case 6:
                   res = "%r9d";
                   break;    
                }
           }
           else{ //Is memory
                res = offsetOp + "(%rbp)";
           }
        }
        else{ //Is literal
            String value = e.getValue().toString();
            if (value.equals("true")) value = "1";
            if (value.equals("false")) value = "0";    
            res = "$" + value;
        }
        return res;
    }


    private String genMethodDecl(Instruction instruction){
        String res;
        res = instruction.getRes() + ":\n";
        res += "enter $" + instruction.getOp1() + ",$0";
        return res;
    }
    
    private String genLabel(Instruction instruction){
        return "." + instruction.getRes();
    }
    
    private String genAssmnt(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n"; 
        res += "movl %r10d, " + operand(instruction.getRes()); 
        return res;
    }
    
    private String genReturn(Instruction instruction){
        String res = "";
        if (instruction.getRes()!=null){
            res = "movl " + operand(instruction.getRes()) + ", " + "%eax";
        }
        else{
            res = "nop";
        }
        res += "\nleave\nret";
        return res;
    }
    
    private String genMinusInt(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "negl %r10d\n";
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
    }
    
    private String genNegation(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1())  + ", %r10d\n";
        res += "notl %r10d\n";
        res += "add $2, %r10d\n";
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
        
    }
    
    private String genPush(Instruction instruction){
        String res;
        int parameterNum = (Integer)((IntLiteral)instruction.getOp1()).getValue();
        if (parameterNum>0 && parameterNum<7){
           String register = "";
           switch(parameterNum){
               case 1:
                   register = "%edi";
                   break;
                case 2:
                   register = "%esi";
                   break;
                case 3:
                   register = "%edx";
                   break;
                case 4:
                   register = "%ecx";
                   break;
                case 5:
                   register = "%r8d";
                   break;
                case 6:
                   register = "%r9d";
                   break;    
           }
           res = "movl " + operand(instruction.getRes()) + ", " + register;
        }
        else{
           res = "push " + operand(instruction.getRes()); 
        }
        
        return res;
    }
    
    private String genCall(Instruction instruction){
        String res;
        res = "call " + instruction.getOp1() + "\n";
        res += "movl %eax," + operand(instruction.getRes());
        return res;
    }
    
    private String genJump(Instruction instruction){
        String res;
        res = "jmp ." + instruction.getRes();
        res = res.substring(0, res.length()-1);//Elim character ":"
        return res;
    }
    
    private String genJumpFalse(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "cmpl $0, %r10d\n";
        res += "je ." + instruction.getRes();
        res = res.substring(0, res.length()-1);//Elim character ":"
        return res;
    }
    
    private String genInc(Instruction instruction){
        String res;
        res = "incl " + operand(instruction.getRes());
        return res;
    }
    
    private String genSumInt(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "addl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
    }
    
    private String genSubInt(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "subl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
    }
    
    private String genMultInt(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "imull " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
    }
    
    private String genDivideInt(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %eax\n";
        res += "movl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "movl %edx, %r11d\n"; //Backup edx value
        res += "movl $0, %edx\n"; 
        res += "idivl %r10d\n";//Remainder in edx, cocient in eax
        res += "movl %r11d, %edx\n"; //Restore edx value
        res += "movl %eax, " + operand(instruction.getRes());
        return res;
    }
    
    private String genMod(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %eax\n";
        res += "movl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "movl %edx, %r11d\n"; //Backup edx value
        res += "movl $0, %edx\n"; 
        res += "idivl %r10d\n";//Remainder in edx, cocient in eax
        res += "movl %edx, %r10d\n";
        res += "movl %r11d, %edx\n"; //Restore edx value
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
    }
    
    private String genAnd(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "andl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
    }
    
    private String genOr(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "orl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "movl %r10d, " + operand(instruction.getRes());
        return res;
    }
    
    private String genArrayAssmnt(Instruction instruction){
        String res;
        res = "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "mov " + operand(instruction.getOp2()) + ", %r11\n";
        res += "neg %r11\n";
        int offset = ((Location)instruction.getRes()).getOffset();
        res += "movl %r10d, " + offset + "(%rbp,%r11," + bytes + ")";
        return res;
        //TODO IMPLEMENTS EXCEPTION OUT OF RANGE
    }
    
    private String genArrayAccess(Instruction instruction){
        String res;
        res = "mov " + operand(instruction.getOp2()) + ", %r10\n";
        res += "neg %r10\n";
        int offset = ((Location)instruction.getOp1()).getOffset();
        res += "movl " + offset + "(%rbp,%r10," + bytes + "), %r11d\n";
        res += "movl %r11d, " + operand(instruction.getRes());
        return res;
        //TODO IMPLEMENTS EXCEPTION OUT OF RANGE
    }
    
    private String genEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".Eq" + idTemp + ":\n";
        res += "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "cmpl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "je .equals" + idTemp + "\n";
        res += "movl $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endEq" + idTemp + "\n"; 
        res += ".equals" + idTemp + ":\n";
        res += "movl $1, " + operand(instruction.getRes()) + "\n";
        res += ".endEq" + idTemp + ":";
        return res;
    }
    
    private String genNotEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".notEq" + idTemp + ":\n";
        res += "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "cmpl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "jne .notEquals" + idTemp + "\n";
        res += "movl $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endNotEq" + idTemp + "\n"; 
        res += ".notEquals" + idTemp + ":\n";
        res += "movl $1, " + operand(instruction.getRes()) + "\n";
        res += ".endNotEq" + idTemp + ":";
        return res;
    }
    
    private String genLess(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".less" + idTemp + ":\n";
        res += "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "cmpl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "jl .isLess" + idTemp + "\n";
        res += "movl $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endLess" + idTemp + "\n"; 
        res += ".isLess" + idTemp + ":\n";
        res += "movl $1, " + operand(instruction.getRes()) + "\n";
        res += ".endLess" + idTemp + ":";
        return res;
    }
    
    private String genLessEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".lessEq" + idTemp + ":\n";
        res += "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "cmpl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "jle .isLessEq" + idTemp + "\n";
        res += "movl $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endLessEq" + idTemp + "\n"; 
        res += ".isLessEq" + idTemp + ":\n";
        res += "movl $1, " + operand(instruction.getRes()) + "\n";
        res += ".endLessEq" + idTemp + ":";
        return res;
    }
    
    private String genGtr(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".gtr" + idTemp + ":\n";
        res += "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "cmpl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "jg .isGtr" + idTemp + "\n";
        res += "movl $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endGtr" + idTemp + "\n"; 
        res += ".isGtr" + idTemp + ":\n";
        res += "movl $1, " + operand(instruction.getRes()) + "\n";
        res += ".endGtr" + idTemp + ":";
        return res;
    }
    
    private String genGtrEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".gtrEq" + idTemp + ":\n";
        res += "movl " + operand(instruction.getOp1()) + ", %r10d\n";
        res += "cmpl " + operand(instruction.getOp2()) + ", %r10d\n";
        res += "jge .isGtrEq" + idTemp + "\n";
        res += "movl $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endGtrEq" + idTemp + "\n"; 
        res += ".isGtrEq" + idTemp + ":\n";
        res += "movl $1, " + operand(instruction.getRes()) + "\n";
        res += ".endGtrEq" + idTemp + ":";
        return res;
    }


}