package ast.type;

import ast.node.AstNode;
import visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Struct extends AbstractType{

    private List<RecordField> fields;

    public Struct(int line, int column) {

        super(line, column);
        this.fields = new ArrayList<>();
    }


    public List<RecordField> getFields(){
        return this.fields;
    }
    public void addField(RecordField field){
        this.fields.add(field);
    }

    public RecordField findField(String name){
            for(RecordField rf : fields){
                if(rf.getName().equals(name)){
                    return rf;
                }
            }

          throw new IllegalStateException("Non-existing field named " + name + " in struct.");
    }


    @Override
    public String toString(){
        String st = "Struct: - Fields: [";

        for(RecordField rf : fields){
            st += " - " + rf.toString();
        }

        st += "]";
        return st;
    }


    @Override
    public <TR, TP> TR accept(Visitor<TR, TP> v, TP param) {
        return v.visit(this,param);
    }

    @Override
    public Type dot(String fieldName, AstNode node){

        for(RecordField rf : fields){
            if(rf.getName().equals(fieldName)){
                return rf.getType();
            }
        }

        return new ErrorType(node.getLine(),node.getColumn(),"Non existing field name in struct.");

    }

    @Override
    public String typeName() {
        return "Struct";
    }

    @Override
    public int numberOfBytes(){
        int total = 0;
        for(RecordField rf : fields){
            total += rf.getType().numberOfBytes();
        }

        return total;
    }
}
