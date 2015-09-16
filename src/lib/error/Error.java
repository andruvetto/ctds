/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author daniel
 */
public class Error {
    private final String desc;
    private final int line;
    private final int column;
    
    public Error(int l, int c, String d){
        desc = d;
        line = l;
        column = c;
    }
    
    public String getDesc(){
        return desc;
    }
    
     public int getLine(){
        return line;
    }
     
     public int getColumn(){
        return column;
    }
    
}
