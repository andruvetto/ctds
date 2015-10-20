package lib.error;

/**
 * Error class
 * 
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
     
     @Override
    public String toString(){
        return desc + " in line: " + line + " column: " + column;
    }
    
}
