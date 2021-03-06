package codegenerator;

import ast.definition.VariableDefinition;
import ast.expression.ArrayAccess;
import ast.expression.FieldAccess;
import ast.expression.Variable;
import ast.type.Integer;
import ast.type.Struct;
import ast.type.Void;
import visitor.AbstractVisitor;

public class AddressCGVisitor extends AbstractCodeGeneratorVisitor<Void,Object> {

    private ValueCGVisitor value;
    private CodeGenerator cg;

    public ValueCGVisitor getValue() {
        return value;
    }

    public void setValue(ValueCGVisitor visitor){
        this.value = visitor;
    }

    public AddressCGVisitor(CodeGenerator cg){
        this.cg = cg;
    }


    /** REMEMBER: depending on scope of variable, address will be
     * different, global (scope 0) uses directly the offset,
     * if its local (negative offset ) or param (positive offset) calculates
     * address using BP
     *
     * address[[Variable : expression --> ID]]() =
     *
     *         if(expression.definition.scope == 0){ //GLOBAL
     *             <pusha > expression.definition.offset
     *         }else{ //LOCAL OR PARAM
     *
     *             <push BP>
     *             <pushi > expression.type.suffix()
     *             <addi >
     *
     *         }
     *
     *
     * @param v
     * @param param
     * @return
     */
    @Override
    public Void visit(Variable v, Object param){

        if(v.getDefinition().getScope() == 0){

            cg.pushAddress( ((VariableDefinition)v.getDefinition()).getOffset() );

        }else{

            cg.pushBP();
            cg.push( ((VariableDefinition)v.getDefinition()).getOffset() );
            cg.add(Integer.getInstance());

        }

        return null;
    }

    /**
     *
     * address[[ArrayAccess : exp1 --> exp2 exp3]]() =
     *          address[[ exp2 ]]
     *          value[[ exp3 ]]
     *          <pushi> exp1.type.numberOfBytes()
     *          <muli>
     *          <addi>
     *
     * @param a
     * @param param
     * @return
     */
    @Override
    public Void visit(ArrayAccess a, Object param){

        a.getLeftExpr().accept(this,param);

        a.getRightExpr().accept(this.value,param);

        cg.push(a.getType().numberOfBytes());

        cg.mul(Integer.getInstance());

        cg.add(Integer.getInstance());


        return null;
    }

    /**
     *  1 - Memory address of the record is computed
     *  2 - Offset of the field is obtained
     *  3 - Field's address is the sum of previous values
     *
     * address[[FieldAccess : exp1 --> exp2 ID]]() =
     *          address[[exp2]]
     *          <pushi > exp2.type.find(ID).offset
     *          <addi>
     *
     * @param fa
     * @param param
     * @return
     */
    @Override
    public Void visit(FieldAccess fa, Object param){

        fa.getExpression().accept(this,param);
        Struct struct = (Struct) fa.getExpression().getType();
        cg.push(struct.findField(fa.getFieldName()).getOffset());

        cg.add(Integer.getInstance());

        return null;
    }
}
