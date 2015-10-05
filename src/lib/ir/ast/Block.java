package lib.ir.ast;

import java.util.List;
import lib.ir.ASTVisitor;
import java.util.LinkedList;

public class Block extends Statement {
	private List<FieldDecl> fields;
        private List<Statement> statements;
	private int blockId;
	
	public Block(int bId) {
		statements = new LinkedList<Statement>();
                fields = new LinkedList<FieldDecl>();
		blockId = bId;
	}
	
	public Block(int bId, List<Statement> s, List<FieldDecl> fd) {
		blockId = bId;
		statements = s;
                fields = fd;
	}
        
	public void addStatement(Statement s) {
		this.statements.add(s);
	}
        
        public void addField(FieldDecl s) {
		this.fields.add(s);
	}
		
	public List<Statement> getStatements() {
		return this.statements;
	} 
        
        public List<FieldDecl> getFields() {
		return this.fields;
	} 
		
	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	@Override
	public String toString() {
            String rtn = "";
	    for (FieldDecl f: fields) {
			rtn += f.toString() + '\n';
		}
            if (rtn.isEmpty()) rtn += '\n';
	    for (Statement s: statements) {
			rtn += s.toString() + '\n';
		}
            if (rtn.length() > 0) return rtn.substring(0, rtn.length() - 1); // remove last new line char
            return rtn; 
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
	
}
