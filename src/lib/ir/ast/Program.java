/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ast;

import ir.ASTVisitor;
import java.util.List;

/**
 *
 * @author daniel
 */
public class Program extends AST{
    private List<ClassDecl> classes;
    
    public Program(List<ClassDecl> clist){
        this.classes = clist;        
    }
    
    @Override
    public String toString() {
        String res = "";
        for (ClassDecl c: classes){
            res = res + c.toString();
        }
        return res;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
