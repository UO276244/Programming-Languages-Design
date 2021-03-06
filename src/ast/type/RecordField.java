package ast.type;

import ast.node.AstNode;
import visitor.Visitor;

public class RecordField implements AstNode {

    private String name;
    private Type type;
    private int line;
    private int column;
    private int offset;

    public RecordField(int line, int column,String name,Type type){

        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    public String getName(){
        return this.name;
    }

    public Type getType(){
        return this.type;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public int getColumn() {
        return this.column;
    }




    @Override
    public String toString(){
        return "RecordField - Type: [" + this.type + "]"+
                " - Name: " + this.name ;
    }

    @Override
    public <TR, TP> TR accept(Visitor<TR, TP> v, TP param) {
        return v.visit(this,param);
    }


    public int getOffset(){
        return this.offset;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }

}
