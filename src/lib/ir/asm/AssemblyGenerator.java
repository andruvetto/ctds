package lib.ir.asm;

import java.util.LinkedList;
import lib.ir.ast.ArrayLocation;
import lib.ir.ast.Expression;
import lib.ir.ast.IntLiteral;
import lib.ir.ast.Location;
import lib.ir.ast.Type;
import lib.ir.icode.Instruction;

public class AssemblyGenerator {
    private int numtemp;
    public String result;
    private int bytes = 8; //Default number of bytes
    private int parPushed = 0;//Parameters pushed in stack
    
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
                case MINUSFLOAT:
                    result += genMinusFloat(instruction);
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
                case SUMFLOAT:
                    result += genSumFloat(instruction);
                    break;
                case SUBINT:
                    result += genSubInt(instruction);
                    break;
                case SUBFLOAT:
                    result += genSubFloat(instruction);
                    break;
                case MULTINT:
                    result += genMultInt(instruction);
                    break;
                case MULTFLOAT:
                    result += genMultFloat(instruction);
                    break;    
                case DIVIDEINT:
                    result += genDivideInt(instruction);
                    break;
                case DIVIDEFLOAT:
                    result += genDivideFloat(instruction);
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
                    result += genGtrEq(instruction);
                    break;
                case ARRAYEXCEPTION:
                    result += genArrayException(instruction);
                    break;
                case GLOBALVAR:
                    result += genGlobalVar(instruction);
                    break;
                case GLOBALARRAY:
                    result += genGlobalArray(instruction);
                    break;
            }
            result += "\n";
        }
        
    }

    private String operand(Expression e){
        String typeClass = e.getClass().getSimpleName();
        String res = "";

        if (typeClass.equals("VarLocation") || typeClass.equals("ArrayLocation")){ 
           Location var = (Location)e; 
           
           if (var.isGlobal()){
               res = var.getId() + ("(%rip)");
               return res;
           }
           
           
           
           int offsetOp = var.getOffset();
           if (offsetOp >= 0 && offsetOp <= 7 ){ // Is register!
               if(var.getType().equals(Type.FLOAT)){
                   res = "%xmm" + offsetOp;
               }
               else{  
               switch(offsetOp){
               case 1:
                   res = "%rdi";
                   break;
                case 2:
                   res = "%rsi";
                   break;
                case 3:
                   res = "%rdx";
                   break;
                case 4:
                   res = "%rcx";
                   break;
                case 5:
                   res = "%r8";
                   break;
                case 6:
                   res = "%r9";
                   break;    
                }
               }
           }
           else{ //Is memory
                res = offsetOp + "(%rbp)";
           }
        
        }
        else{ //Is literal
            String value = e.getValue().toString();
            if (e.getType().equals(Type.FLOAT)){
                Float val = Float.valueOf(value);
                value = "" + Float.floatToIntBits(val);
            }
            if (value.equals("true")) value = "1";
            if (value.equals("false")) value = "0"; 
            res = "$" + value;
        }
        return res;
    }
    
    private String genGlobalVar(Instruction instruction){
        String res = "";
        String id = instruction.getRes().getId();
        res += ".comm " + id + "," + bytes + "," + bytes;
        return res;
    }
    
    private String genGlobalArray(Instruction instruction){
        String res = "";
        ArrayLocation array = (ArrayLocation)instruction.getRes();
        String id = array.getId();
        int size = array.getSize();
        res += ".comm " + id + "," + bytes*size + ",32";
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
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n"; 
        res += "movq %r10, " + operand(instruction.getRes()); 
        return res;
    }
    
    private String genReturn(Instruction instruction){
        String res = "";
        if (instruction.getRes()!=null){
            if (instruction.getRes().getType().equals(Type.FLOAT)){
                res = "movq " + operand(instruction.getRes()) + ", " + "%r10\n";
                res += "movq %r10, %xmm0\n";
            }
            else{
                res = "movq " + operand(instruction.getRes()) + ", " + "%rax\n";
            }
        }
        else{
            res = "nop\n";
        }
        res += "leave\n";
        //res += "popq	%rbp\n";
        //res += ".cfi_def_cfa 7, 8\n";
        res += "ret";
        //res += ".cfi_endproc";
        return res;
    }
    
    private String genMinusInt(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "negq %r10\n";
        res += "movq %r10, " + operand(instruction.getRes());
        return res;
    }
    
    private String genMinusFloat(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "movq $0, %r11\n";
        res += "movq %r11, %xmm0\n";
        res += "movq %r10, %xmm1\n";
        res += "subss %xmm1, %xmm0\n";
        res += "movq %xmm0, " + operand(instruction.getRes());
        return res;
    }
    
    private String genNegation(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1())  + ", %r10\n";
        res += "notq %r10\n";
        res += "addq $2, %r10\n";
        res += "movq %r10, " + operand(instruction.getRes());
        return res;
        
    }
    
    private String genPush(Instruction instruction){
        
        String res;
        int parameterNum = (Integer)((IntLiteral)instruction.getOp1()).getValue();
        Expression expr = instruction.getRes();
        String register = "";
        if (expr.getType().equals(Type.FLOAT)){
            if (parameterNum>=0 && parameterNum<=7){
                register = "%xmm" + parameterNum;
                res = "movq " + operand(instruction.getRes()) + ", %r10\n";
                res += "movq %r10, " + register;
            }
            else{
                res = "push " + operand(instruction.getRes());
                parPushed++;
            }
            

        }
        else{
            if (parameterNum>0 && parameterNum<7){
                switch(parameterNum){
                    case 1:
                        register = "%rdi";
                        break;
                    case 2:
                        register = "%rsi";
                        break;
                    case 3:
                        register = "%rdx";
                        break;
                    case 4:
                        register = "%rcx";
                        break;
                    case 5:
                        register = "%r8";
                        break;
                    case 6:
                        register = "%r9";
                        break;    
                }
            res = "movq " + operand(instruction.getRes()) + ", " + register;
            }
            else{
                res = "push " + operand(instruction.getRes());
                parPushed++;
            }
        }
        return res;
    }
    
    private String genCall(Instruction instruction){
        String res;
        if (instruction.getRes().getType().equals(Type.FLOAT)){
            res = "call " + instruction.getOp1() + "\n";
            res += "movq %xmm0," + operand(instruction.getRes());
        }
        else{
            res = "movq $0, %rax\n";
            res += "call " + instruction.getOp1() + "\n";
            res += "movq %rax," + operand(instruction.getRes());
        }
        if (parPushed%2 != 0){
            res += "\naddq $8, %rsp";  
        }
        parPushed = 0;
        
        
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
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "cmpq $0, %r10\n";
        res += "je ." + instruction.getRes();
        res = res.substring(0, res.length()-1);//Elim character ":"
        return res;
    }
    
    private String genInc(Instruction instruction){
        String res;
        res = "incq " + operand(instruction.getRes());
        return res;
    }
    
    private String genSumFloat(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "movq %r10, %xmm0\n";
        res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, %xmm1\n";
        res += "addss %xmm1, %xmm0\n";
        res += "movq %xmm0, " + operand(instruction.getRes());
        return res;
    }
    
    private String genSumInt(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "movq " + operand(instruction.getOp2()) + ", %r11\n";
        res += "addq %r10, %r11\n";
        res += "movq %r11, " + operand(instruction.getRes());
        return res;
    }
    
    private String genSubInt(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "subq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, " + operand(instruction.getRes());
        return res;
    }
    
    private String genSubFloat(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "movq %r10, %xmm0\n";
        res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, %xmm1\n";
        res += "subss %xmm1, %xmm0\n";
        res += "movq %xmm0, " + operand(instruction.getRes());
        return res;
    }
    
    private String genMultInt(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "imulq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, " + operand(instruction.getRes());
        return res;
    }
    
    private String genMultFloat(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "movq %r10, %xmm0\n";
        res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, %xmm1\n";
        res += "mulss %xmm1, %xmm0\n";
        res += "movq %xmm0, " + operand(instruction.getRes());
        return res;
    }
    
    private String genDivideFloat(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "movq %r10, %xmm0\n";
        res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, %xmm1\n";
        res += "divss %xmm1, %xmm0\n";
        res += "movq %xmm0, " + operand(instruction.getRes());
        return res;
    }
    
    private String genDivideInt(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %rax\n";
        res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %rdx, %r11\n"; //Backup edx value
        res += "movq $0, %rdx\n"; 
        res += "idivq %r10\n";//Remainder in rdx, cocient in rax
        res += "movq %r11, %rdx\n"; //Restore edx value
        res += "movq %rax, " + operand(instruction.getRes());
        return res;
    }
    
    private String genMod(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %rax\n";
        res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %rdx, %r11\n"; //Backup edx value
        res += "movq $0, %rdx\n"; 
        res += "idivq %r10\n";//Remainder in rdx, cocient in rax
        res += "movq %rdx, %r10\n";
        res += "movq %r11, %rdx\n"; //Restore edx value
        res += "movq %r10, " + operand(instruction.getRes());
        return res;
    }
    
    private String genAnd(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "andq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, " + operand(instruction.getRes());
        return res;
    }
    
    private String genOr(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "orq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "movq %r10, " + operand(instruction.getRes());
        return res;
    }
    
    private String genArrayAssmnt(Instruction instruction){
        String res;
        Location array = (Location)instruction.getRes();
        res = "movq " + operand(instruction.getOp1()) + ", %r10\n";//operand in r10
        res += "movq " + operand(instruction.getOp2()) + ", %r11\n";//pos in r11
        if (array.isGlobal()){
            res += "movq %r10, " + array.getId() + "(,%r11," + bytes + ")";  
        }
        else{
            res += "negq %r11\n";
            int offset = ((Location)instruction.getRes()).getOffset();
            res += "movq %r10, " + offset + "(%rbp,%r11," + bytes + ")";
        }
        return res;
    }
    
    private String genArrayAccess(Instruction instruction){
        String res;
        res = "movq " + operand(instruction.getOp2()) + ", %r10\n";
        Location array = (Location)instruction.getOp1();
        if (array.isGlobal()){
            res += "movq " + array.getId() + "(,%r10," + bytes + "), %r11\n";
        }
        else{
            res += "negq %r10\n";
            int offset = ((Location)instruction.getOp1()).getOffset();
            res += "movq " + offset + "(%rbp,%r10," + bytes + "), %r11\n";
        }
        res += "movq %r11, " + operand(instruction.getRes());
        return res;
    }
    
    private String genEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".Eq" + idTemp + ":\n";
        res += "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "cmpq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "je .equals" + idTemp + "\n";
        res += "movq $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endEq" + idTemp + "\n"; 
        res += ".equals" + idTemp + ":\n";
        res += "movq $1, " + operand(instruction.getRes()) + "\n";
        res += ".endEq" + idTemp + ":";
        return res;
    }
    
    private String genNotEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".notEq" + idTemp + ":\n";
        res += "movq " + operand(instruction.getOp1()) + ", %r10\n";
        res += "cmpq " + operand(instruction.getOp2()) + ", %r10\n";
        res += "jne .notEquals" + idTemp + "\n";
        res += "movq $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endNotEq" + idTemp + "\n"; 
        res += ".notEquals" + idTemp + ":\n";
        res += "movq $1, " + operand(instruction.getRes()) + "\n";
        res += ".endNotEq" + idTemp + ":";
        return res;
    }
    
    private String genLess(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".less" + idTemp + ":\n";
        res += "movq " + operand(instruction.getOp1()) + ", %r10\n";
        if (instruction.getOp1().getType().equals(Type.FLOAT)){
            res += "movq %r10, %xmm0\n";
            res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "movq %r10, %xmm1\n";
            res += "ucomiss %xmm0, %xmm1\n";
            res += "ja .isLess" + idTemp + "\n"; 
        }
        else{
            res += "cmpq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "jl .isLess" + idTemp + "\n";
        }
        res += "movq $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endLess" + idTemp + "\n"; 
        res += ".isLess" + idTemp + ":\n";
        res += "movq $1, " + operand(instruction.getRes()) + "\n";
        res += ".endLess" + idTemp + ":";
        return res;
    }
    
    private String genLessEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".lessEq" + idTemp + ":\n";
        res += "movq " + operand(instruction.getOp1()) + ", %r10\n";
        if (instruction.getOp1().getType().equals(Type.FLOAT)){
            res += "movq %r10, %xmm0\n";
            res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "movq %r10, %xmm1\n";
            res += "ucomiss %xmm0, %xmm1\n";
            res += "jae .isLessEq" + idTemp + "\n"; 
        }
        else{
            res += "cmpq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "jle .isLessEq" + idTemp + "\n";
        }
        res += "movq $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endLessEq" + idTemp + "\n"; 
        res += ".isLessEq" + idTemp + ":\n";
        res += "movq $1, " + operand(instruction.getRes()) + "\n";
        res += ".endLessEq" + idTemp + ":";
        return res;
    }
    
    private String genGtr(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".gtr" + idTemp + ":\n";
        res += "movq " + operand(instruction.getOp1()) + ", %r10\n";
        if (instruction.getOp1().getType().equals(Type.FLOAT)){
            res += "movq %r10, %xmm0\n";
            res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "movq %r10, %xmm1\n";
            res += "ucomiss %xmm0, %xmm1\n";
            res += "jb .isGtr" + idTemp + "\n"; 
        }
        else{
            res += "cmpq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "jg .isGtr" + idTemp + "\n";
        }
        res += "movq $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endGtr" + idTemp + "\n"; 
        res += ".isGtr" + idTemp + ":\n";
        res += "movq $1, " + operand(instruction.getRes()) + "\n";
        res += ".endGtr" + idTemp + ":";
        return res;
    }
    
    private String genGtrEq(Instruction instruction){
        String res;
        String idTemp = getNextIdTemp();
        res = ".gtrEq" + idTemp + ":\n";
        res += "movq " + operand(instruction.getOp1()) + ", %r10\n";
        if (instruction.getOp1().getType().equals(Type.FLOAT)){
            res += "movq %r10, %xmm0\n";
            res += "movq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "movq %r10, %xmm1\n";
            res += "ucomiss %xmm0, %xmm1\n";
            res += "jbe .isGtrEq" + idTemp + "\n"; 
        }
        else{
            res += "cmpq " + operand(instruction.getOp2()) + ", %r10\n";
            res += "jge .isGtrEq" + idTemp + "\n";
        }
        res += "movq $0, " + operand(instruction.getRes()) + "\n";
        res += "jmp .endGtrEq" + idTemp + "\n"; 
        res += ".isGtrEq" + idTemp + ":\n";
        res += "movq $1, " + operand(instruction.getRes()) + "\n";
        res += ".endGtrEq" + idTemp + ":";
        return res;
    }
    
    private String genArrayException(Instruction instruction){
        String res;
        res = ".ERROR:\n";
        res += ".string	\"Error: Access out of bond in array\"\n";
        res += ".text\n";
        res += ".errorArray:\n";
        res += "movl	$.ERROR, %edi\n";
        res += "movl	$0, %eax\n";
        res += "call	printf\n";
        res += "nop\n";
        res += "popq	%rbp\n";
        res += "ret";
        return res;
    }
}