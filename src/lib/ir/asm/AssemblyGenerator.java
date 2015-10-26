package lib.ir.asm;

import java.util.LinkedList;
import lib.ir.ast.Literal;
import lib.ir.ast.Location;
import lib.ir.icode.Instruction;

public class AssemblyGenerator {

//    public AssemblyGenerator(LinkedList<Instruction> instructions) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
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
            }
            result += "\n";
        }
        
    }


    private String genMethodDecl(Instruction instruction){
        return instruction.getRes() + ":";
    }
    
    private String genLabel(Instruction instruction){
        return "." + instruction.getRes();
    }
    
    private String genAssmnt(Instruction instruction){
        String opType = instruction.getOp1().getClass().getSimpleName();
        int offsetRes = ((Location)instruction.getRes()).getOffset();
        String res;
        if (opType.equals("VarLocation")){
            int offsetOp = ((Location)instruction.getOp1()).getOffset();
            
            res = "mov " + offsetOp + "(%rbp), " + offsetRes + "(%rbp)"; 
        }
        else{
            Object valueOp = ((Literal)instruction.getOp1()).getValue();
            res = "mov " +  "$" + valueOp + ", " + offsetRes + "(%rbp)"; 
        }
        return res;
    }


}