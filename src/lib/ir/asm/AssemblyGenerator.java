package lib.ir.asm;

import java.util.LinkedList;
import lib.ir.ast.Expression;
import lib.ir.ast.IntLiteral;
import lib.ir.ast.Literal;
import lib.ir.ast.Location;
import lib.ir.icode.Instruction;

public class AssemblyGenerator {

    public String result;
    private int bytes = 4; //Default number of bytes
    
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
        res = "movl " + valueOrOffset(instruction.getOp1()) + ", %r10d\n"; 
        res += "movl %r10d, " + valueOrOffset(instruction.getRes()); 
        return res;
    }
    
    private String genReturn(Instruction instruction){
        String res = "";
        if (instruction.getRes()!=null){
            res = "movl " + valueOrOffset(instruction.getRes()) + ", " + "%eax";
        }
        else{
            res = "nop";
        }
        res += "\nleave\nret";
        return res;
    }
    
    private String genMinusInt(Instruction instruction){
        String res;
        res = "movl $0, " + valueOrOffset(instruction.getRes()) + "\n";
        res += "sub " + valueOrOffset(instruction.getOp1()) + ", " + valueOrOffset(instruction.getRes());
        return res;
    }
    
    private String genNegation(Instruction instruction){
        String res;
        res = "movl " + valueOrOffset(instruction.getOp1())  + ", " + valueOrOffset(instruction.getRes()) + "\n"; 
        res += "not " + valueOrOffset(instruction.getRes());
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
           res = "movl " + valueOrOffset(instruction.getRes()) + ", " + register;
        }
        else{
           res = "push " + valueOrOffset(instruction.getRes()); 
        }
        
        return res;
    }
    
    private String genCall(Instruction instruction){
        String res;
        res = "call " + instruction.getOp1() + "\n";
        res += "movl %eax," + valueOrOffset(instruction.getRes());
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