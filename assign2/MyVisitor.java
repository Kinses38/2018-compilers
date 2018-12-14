class MyVisitor implements CalParserVisitor
{
    private SymbolTable symbol_table = new SymbolTable();

    public Object visit(SimpleNode node, Object data)
    {
        throw new RuntimeException("Visit SimpleNode");
    }

    public Object visit(ASTProg node, Object data)
    {
        symbol_table = (SymbolTable)data;
        //symbol_table.printSymbolTable();
        //visit all the nodes
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTvar_dec node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTidentifier node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTconst_dec node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTfunction node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTret node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTtype node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTparameter_list node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTparameter node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTmain node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTifstate node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTels node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTwhil node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTassign node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTfunc_call node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTplus node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTminus node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTminus_identifier node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTnum node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTbool node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTand node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTor node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTnot node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTeq node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTneq node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTlt node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTlte node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTgt node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTgte node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
    public Object visit(ASTarg_list node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }
}
