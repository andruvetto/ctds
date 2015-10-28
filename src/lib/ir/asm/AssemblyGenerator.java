package lib.ir.asm;

import java.util.LinkedList;
import lib.ir.ast.Expression;
import lib.ir.ast.Literal;
import lib.ir.ast.Location;
import lib.ir.icode.Instruction;

public class AssemblyGenerator {

    public String result;
    
    public AssemblyGenerator(LinkedList<Instruction> instructions){
        result = "";
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
            }
            result += "\n";
        }
        
    }
    
    private String valueOrOffset(Expression e){
        String type = e.getClass().getSimpleName();
        String res;
        if (type.equals("VarLocation")){
           int offsetOp = ((Location)e).getOffset();
           res = offsetOp + "(%rbp)"; 
        }
        else{
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
        res += "enter " + instruction.getOp1() + ",0";
        return res;
    }
    
    private String genLabel(Instruction instruction){
        return "." + instruction.getRes();
    }
    
    private String genAssmnt(Instruction instruction){
        return "mov " + valueOrOffset(instruction.getOp1()) + ", " + valueOrOffset(instruction.getRes());
    }
    
    private String genReturn(Instruction instruction){
        String res = "";
        if (instruction.getRes()!=null){
            res = "mov " + valueOrOffset(instruction.getRes()) + ", " + "%eax";
        }
        else{
            res = "mov $1, %eax";
        }
        res += "\nleave\nreturn";
        return res;
    }
    
    private String genMinusInt(Instruction instruction){
        String res;
        res = "mov $0, " + valueOrOffset(instruction.getRes()) + "\n";
        res += "sub " + valueOrOffset(instruction.getOp1()) + ", " + valueOrOffset(instruction.getRes());
        return res;
    }
    
    private String genNegation(Instruction instruction){
        String res;
        res = "mov " + valueOrOffset(instruction.getOp1())  + ", " + valueOrOffset(instruction.getRes()) + "\n"; 
        res += "neg " + valueOrOffset(instruction.getRes());
        return res;
        
    }
    
    private String genPush(Instruction instruction){
        String res;
        res = "push " + valueOrOffset(instruction.getRes());
        return res;
    }
    
    private String genCall(Instruction instruction){
        String res;
        res = "call " + instruction.getOp1() + "\n";
        res += "mov %eax," + valueOrOffset(instruction.getRes());
        return res;
    }
    
    private String genJump(Instruction instruction){
        String res;
        res = "jmp ." + instruction.getRes();
        return res;
    }
    
    private String genJumpFalse(Instruction instruction){
        String res;
        res = "cmp $0, " + valueOrOffset(instruction.getOp1()) + "\n";
        res += "je ." + instruction.getRes();
        return res;
    }
    
    private String genInc(Instruction instruction){
        String res;
        res = "inc " + valueOrOffset(instruction.getRes());
        return res;
    }


}