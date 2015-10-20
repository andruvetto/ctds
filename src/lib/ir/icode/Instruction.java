package lib.ir.icode;

import lib.ir.ast.*;

/**
 * This class represents a 3-way instruction, useful for generate intermediate code with ICodeVisitor
 * 
 */

public class Instruction {
    private TypeInstruction instruction;
    private Expression op1;
    private Expression op2;
    private Expression res;
    
    public Instruction (TypeInstruction i, Expression op1, Expression op2, Expression res){
        this.instruction = i;
        this.op1 = op1;
        this.op2 = op2;
        this.res = res;
    }
    
    public Instruction (TypeInstruction i, Expression op, Expression res){
        this.instruction = i;
        this.op1 = op;
        this.op2 = null;
        this.res = res;
    }
    
    public Instruction (TypeInstruction i){
        this.instruction = i;
    }
    
    public Instruction (TypeInstruction i, Expression res){
        this.instruction = i;
        this.res = res;
    }
    
    public void setInstruction(TypeInstruction i){
        this.instruction = i;
    }
    
    public TypeInstruction getInstruction(){
        return this.instruction;
    }
    
    public void SetOp1(Expression o){
        this.op1 = o;
    }
    
    public Expression getOp1(){
        return this.op1;
    }
    
    public void SetOp2(Expression o){
        this.op2 = o;
    }
    
    public Expression getOp2(){
        return this.op2;
    }
    
    public void SetRes(Expression o){
        this.res = o;
    }
    
    public Expression getRes(){
        return this.res;
    }
    
    
    @Override
    public String toString() {
        String resp = instruction.toString();
        if (op1 != null) resp += " " + op1;
        if (op2 != null) resp += " " + op2;
        if (res != null) resp += " " + res;
        return resp;
    }
    
}
