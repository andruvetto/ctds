package lib.ir.ast;

import lib.ir.ASTVisitor;

public abstract class AST {
	private int lineNumber;
	private int colNumber;
        protected Type type;
        protected String id;
        protected Object value;
        
        public Object getValue(){
            return this.value;
        }
        
        public void setValue(Object value){
            this.value = value;
        }
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void setLineNumber(int ln) {
		lineNumber = ln;
	}
	
	public int getColumnNumber() {
		return colNumber;
	}
	
	public void setColumnNumber(int cn) {
		colNumber = cn;
	}
        
        public Type getType(){
            return this.type;
        }
        
        public void setType(Type t){
            this.type = t;
        }
        
        public String getId(){
            return this.id;
        }
        
        public void setId(String id){
            this.id = id;
        }
	
	public abstract <T> T accept(ASTVisitor<T> v);
}
