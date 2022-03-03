package ast.definition;

import ast.node.AstNode;
import ast.type.Type;

public interface Definition extends AstNode {

    String getName();
    Type getType();
}