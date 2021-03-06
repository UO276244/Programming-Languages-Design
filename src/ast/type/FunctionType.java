package ast.type;

import ast.definition.VariableDefinition;
import ast.node.AstNode;
import visitor.Visitor;

import java.util.List;

public class FunctionType extends AbstractType{
    private Type returningType;
    private List<VariableDefinition> params;
    private int bytesOfParams = 0;

    public FunctionType(int line, int column,Type returningType ,  List<VariableDefinition> params) {
        super(line, column);
        this.returningType = returningType;
        this.params = params;
    }


    public List<VariableDefinition> getParams(){
        return this.params;
    }

    public Type getReturningType(){
        return this.returningType;
    }


    @Override
    public String toString(){
        String st =  "FunctionType: - Returning Type: " + this.returningType +
                " - Parameters: ";

        for(VariableDefinition vars : params){
            st += vars.toString() + ",";
        }


        return st;
    }

    @Override
    public <TR, TP> TR accept(Visitor<TR, TP> v, TP param) {
        return v.visit(this,param);
    }


    @Override
    public Type parenthesis(List<Type> passedAsParams, AstNode node){
        int count = 0;

        if(passedAsParams.size() != params.size()){
            return new ErrorType(node.getLine(),node.getColumn(),"Incorrect number of parameters");
        }

        for(Type t : passedAsParams){
            if (t instanceof ErrorType){
                return t;
            }

            if(!t.equals(params.get(count).getType())){
                return new ErrorType(node.getLine()
                        ,node.getColumn()
                        ,"Parameter's type in position " +
                        count +
                        " is different from type declared in definition. - Expected: " + params.get(count).getType().toString() +
                        " - Received: " + t.toString());
            }
            count++;

        }

       return returningType;
    }

    @Override
    public String typeName() {
        return "FunctionType";
    }

    /**
     *
     * can @return 2 --> integer: memory address aka pointer to function => we are never going to use this
     */
    @Override
    public int numberOfBytes(){

        throw new IllegalStateException("Cannot get number of bytes of a FuntionType");
    }


    public void setBytesOfParams(int offset){
        this.bytesOfParams = offset;
    }

    public int getBytesOfParams(){
        return this.bytesOfParams;
    }
}
