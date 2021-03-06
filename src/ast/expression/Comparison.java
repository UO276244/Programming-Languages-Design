package ast.expression;

import visitor.Visitor;

public class Comparison   extends AbstractExpression{

    private Expression leftExpr;
    private Expression rightExpr;
    private String operand;

    public Comparison(int line, int column, Expression left, Expression right, String operand)
    {
        super(line, column);
        this.leftExpr=left;
        this.rightExpr=right;
        this.operand = operand;
    }


    public Expression getRightExpr() {
        return rightExpr;
    }

    public Expression getLeftExpr() {
        return leftExpr;
    }

    public String getOperand() {
        return operand;
    }

    @Override
    public String toString(){

        return "Comparison: [ Left Expression : " + leftExpr.toString()
                + " - Operand: " + operand
                + " - Right Expression : "+rightExpr.toString() + "]"
                ;

    }

    @Override
    public <TR, TP> TR accept(Visitor<TR, TP> v, TP param) {
        return v.visit(this,param);
    }
}
