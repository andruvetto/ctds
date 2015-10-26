package lib.ir.ast;

import java.util.ArrayList;
import lib.ir.ASTVisitor;

public class ArrayLocation extends Location {

        private Expression expression;
        private int size;
        private ArrayList<Object> values;

	public ArrayLocation(String id, Expression e) {
		this.id = id;
                expression = e;
		this.size = 0;
                this.values = null;
                this.isArray = true;
	}
        
        public ArrayLocation(String id, Expression e, int n){
                this.id = id;
                expression = e;
                this.size = n;
                this.values = new ArrayList<Object>(n);
                for(Object o : values){
                    o = null;
                }
                this.isArray = true;
        }
	
        public int getSize(){
            return size;
        }
        
        public void setSize(int n){
            size = n;
        }
        
        public ArrayList<Object> getValues(){
            return this.values;
        }
        
        public void setValues(ArrayList<Object> v){
            values = v;
        }
        
        public Object getValueAt(int i){
            return values.get(i);
        }
        
        public Object setValueAt(int i, Object v){
            return values.set(i, v);
        }
        
        public Expression getExpression(){
            return this.expression;
        }

	
	@Override
	public String toString() {
		return "(" + id + " "+this.getOffset() + ")";
	}
        

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}