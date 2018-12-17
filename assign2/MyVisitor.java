import java.util.ArrayList;
import java.util.HashSet;

class MyVisitor implements CalParserVisitor
{
    private SymbolTable symbol_table = new SymbolTable();
    private HashSet<String> called_functions = new HashSet<>();
    private HashSet<String> declared_functions = new HashSet<>();

    //Should really create Error class
    private ArrayList<String> arith_type_error = new ArrayList<>();
    private ArrayList<String> wrong_assignment = new ArrayList<>();
    private ArrayList<String> used_before_declare = new ArrayList<>();
    private ArrayList<String> duplicate_declarations = new ArrayList<>();
    private ArrayList<String> wrong_number_of_param = new ArrayList<>();
    private ArrayList<String> wrong_method_signature = new ArrayList<>();
    private ArrayList<String> all_funcs_declared = new ArrayList<>();
    private String current_scope = "global";

    /*
    * Tests:
     *   Correct number of arguments for func. DONE.
     *   Func return type matches func header. DONE.
     *   Every func called? DONE.
     *   Func declaration for every func called? DONE.
     *   ID declared in scope before use? assignment and func call DONE.
     *   ID declared only once in scope? DONE.
     *   Func only declared once? DONE.
     *   Parameters only used once. DONE.
     *   LHS = RHS var? DONE.
     *   Correct param types for func? DONE.
     *
     *
     *   Arith Op operators var or const?
     *   Bool op check
     *   var written to and read from
    */

    public Object visit(SimpleNode node, Object data)
    {
        throw new RuntimeException("Visit SimpleNode");
    }

    public Object visit(Prog node, Object data)
    {
        symbol_table = (SymbolTable)data;
        node.childrenAccept(this, data);

        semanticReporter();
        return DataType.Prog;
    }

    public Object visit(Var_dec node, Object data)
    {
        node.childrenAccept(this, data);
        return DataType.Var_dec;
    }

    public Object visit(Identifier node, Object data)
    {
        return DataType.Identifier;
    }

    public Object visit(Const_dec node, Object data)
    {
        node.childrenAccept(this, data);
        return DataType.Const_dec;
    }

    public Object visit(Function node, Object data)
    {
        //Are children of this in scope?
        //local variables?
        String func_name = (String)((SimpleNode) node.jjtGetChild(1)).jjtGetValue();
        current_scope = func_name;
        node.childrenAccept(this, data);
        declared_functions.add(func_name);
        current_scope = "global";
        return DataType.Function;
    }

    public Object visit(Ret node, Object data)
    {
        String parent_func_type = (String) ((SimpleNode) node.parent.jjtGetChild(0)).jjtGetValue();
        String parent_func_name = (String) ((SimpleNode) node.parent.jjtGetChild(1)).jjtGetValue();
        //if function returns something, go visit it.
        if(node.jjtGetNumChildren() != 0)
        {
            // Is the function return type the same type as function signature?
            String ret_var_name = (String)((SimpleNode) node.jjtGetChild(0)).jjtGetValue();
            if(declaredBeforeUse(ret_var_name))
            {
                Symbol ret_value = symbol_table.getSymbol(ret_var_name, parent_func_name);
                if(!ret_value.getType().equals(parent_func_type))
                {
                    wrong_assignment.add(ret_var_name + "'s type: " + ret_value.getType()
                            + " does not match return type: " + parent_func_type
                            + " of function: " + parent_func_name);
                }
                node.childrenAccept(this, data);

            }
        }
        else if (!parent_func_type.equals("void"))
        {
            wrong_assignment.add(parent_func_type + " does not match return type: void of function: "
                    + parent_func_name);
        }

        return DataType.Ret;
    }

    public Object visit(Type node, Object data)
    {
        if(node.jjtGetValue().equals("boolean"))
            return DataType.Bool;
        else if (node.jjtGetValue().equals("integer"))
            return DataType.Num;
        else if (node.jjtGetValue().equals("void"))
            return DataType.Void;
        else
            return DataType.TypeUnknown;
    }
    
    public Object visit(Parameter node, Object data)
    {
        //parameters type match the given children??
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Main node, Object data)
    {
        current_scope = "main";
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Ifstate node, Object data)
    {
        //This will be a mess.
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Els node, Object data)
    {
        //Also possibly a mess
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Whil node, Object data)
    {
        //Needed really?
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Assign node, Object data)
    {

        String id_name = (String)((SimpleNode) node.jjtGetChild(0)).jjtGetValue();
        if(declaredBeforeUse(id_name))
        {
            node.childrenAccept(this, data);
            lhsEqualsRhs(id_name, node);
        }
        return data;
    }

    public Object visit(Func_call node, Object data)
    {
        String func_name = (String)((SimpleNode) node.jjtGetChild(0)).jjtGetValue();
        if(declared_functions.contains(func_name))
        {
            //Are right number of arguments supplied?
            int arg_list_length = node.jjtGetChild(1).jjtGetNumChildren();
            //get length of parameters in function signature;
            int func_param_length = symbol_table.getFuncParameters(func_name).size();
            if(arg_list_length != func_param_length)
            {
                wrong_number_of_param.add("Wrong amount of arguments provided to func: "
                        + func_name + ". Required: " + func_param_length + " Given: " + arg_list_length);
            }
            //Create list of functions called.
            called_functions.add(func_name);
        }
        else
        {
            all_funcs_declared.add("Invoked function \"" + func_name + "\" is not declared!");
        }

        node.childrenAccept(this, data);
        return DataType.Func_call;
    }

    public Object visit(Plus node, Object data)
    {
        node.childrenAccept(this, data);
        if(!correctArithTypes(node, data));
            arith_type_error.add("Arith Type error: "  +
                    ((SimpleNode)node.jjtGetChild(0)).jjtGetValue() +
                    " and " + ((SimpleNode)node.jjtGetChild(1)).jjtGetValue() + " in scope:" + current_scope);
        return DataType.Plus;
    }

    public Object visit(Minus node, Object data)
    {
        //integers
        node.childrenAccept(this, data);
        if(!correctArithTypes(node, data));
        arith_type_error.add("Arith Type error: "  +
                ((SimpleNode)node.jjtGetChild(0)).jjtGetValue() +
                " and " + ((SimpleNode)node.jjtGetChild(1)).jjtGetValue() + " in scope:" + current_scope);
       return data;
    }

    public Object visit(Minus_identifier node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Num node, Object data)
    {
        return DataType.Num;
    }

    public Object visit(Bool node, Object data)
    {
        return DataType.Bool;
    }

    public Object visit(And node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Or node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Not node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Eq node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Neq node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Lt node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Lte node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Gt node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Gte node, Object data)
    {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(Arg_list node, Object data)
    {

        node.childrenAccept(this, data);
        //check each argument is declared.

        if(node.children != null)
        {
            ArrayList <Symbol>func_param_list = new ArrayList<>();
            Node arg_list [] = node.children;
            //get function name
            String func_name = (String) ((SimpleNode)node.parent.jjtGetChild(0)).jjtGetValue();
            //retrieve all parameters in func name scope from symbol table.
            if(declared_functions.contains(func_name))
                func_param_list = symbol_table.getFuncParameters(func_name);

            if(!func_param_list.isEmpty())
            {
                for (Node current_arg : arg_list)
                {
                    int i = 0;
                    String argument_name  = (String)((SimpleNode)current_arg).jjtGetValue();
                    //check each child in current scope, if not, then global
                    if(declaredBeforeUse(argument_name))
                    {
                        //Compare each param type vs arg type.
                        Symbol func_arg = (Symbol)func_param_list.get(i);
                        String arg_type = symbol_table.getSymbol(argument_name, current_scope).getType();
                        if(!arg_type.equals(func_arg.getType()))
                        {
                            wrong_method_signature.add(argument_name + ":" + arg_type + " does not match " + func_name
                                    + "'s signature");
                        }
                        func_param_list.remove(0);
                    }
                }
            }


        }

        return DataType.Arg_list;
    }

    private boolean declaredBeforeUse(String parameter_name)
    {

        if(!symbol_table.checkScope(parameter_name, current_scope))
        {
            if(symbol_table.checkScope(parameter_name, "global"))
            {
                return true;
            }
            else {

                used_before_declare.add(parameter_name + " not declared in scope: " +  current_scope + " or Global before use");
                return false;
            }
        }
        else {
            return true;
        }
    }

    void lhsEqualsRhs(String id_name, Node node)
    {

        String dec_type = symbol_table.getSymbol(id_name, current_scope).getType();
        //breaking for param
        String assign_type = node.jjtGetChild(1).toString();

        if (!assign_type.equals("Plus") && !assign_type.equals("Minus"))
        {
            if (!(assign_type.equals("Num") && dec_type.equals("integer"))
                    && !(assign_type.equals("Bool") && dec_type.equals("boolean")))
               wrong_assignment.add(dec_type + ":" + id_name + " given wrong type of: "
                        + assign_type + " in scope: " + current_scope.toUpperCase());
        }

    }

    boolean correctArithTypes(Node node, Object data)
    {
        String arg1 =  "";
        String arg2 =  "";

        if(((node.jjtGetChild(0)).toString().equals("Identifier")))
        {
            String id = (String) ((SimpleNode)node.jjtGetChild(0)).jjtGetValue();
            arg1 = symbol_table.getSymbol(id, current_scope).getType();
        }
        else
        {
            arg1 = node.jjtGetChild(0).toString();
        }
        if(((node.jjtGetChild(1)).toString().equals("Identifier")))
        {
            String id = (String) ((SimpleNode)node.jjtGetChild(1)).jjtGetValue();
            arg2 = symbol_table.getSymbol(id, current_scope).getType();
        }
        else
        {
            arg2 = node.jjtGetChild(1).toString();
        }

        if(arg1.equals("integer") || arg1.equals("Num"))
            if(arg2.equals("integer") || arg2.equals("Num") || arg2.equals("Plus") || arg2.equals("Minus"))
                return true;
        if(arg2.equals("integer") || arg2.equals("Num"))
            if(arg1.equals("integer") || arg1.equals("Num") || arg1.equals("Plus") || arg1.equals("Minus"))
                    return true;

            return false;
    }

    void semanticReporter()
    {
        System.out.println("\n### Semantic Analysis ###\n");
        declared_functions.removeAll(called_functions);
        if(!declared_functions.isEmpty())
        {
            for (String func : declared_functions)
            {
                System.out.println("Function \"" + func + "\" was declared but never used.");
            }
        }
        else
        {
            System.out.println("All declared functions are called.");

        }
        duplicate_declarations = symbol_table.getDuplicateDeclarations();
        if(!arith_type_error.isEmpty())
            arith_type_error.forEach(System.out::println);
        else
            System.out.println("All is Arithmetic correct");
        if(!wrong_assignment.isEmpty())
            wrong_assignment.forEach(System.out::println);
        else
            System.out.println("All assignments are correct type.");
        if(!used_before_declare.isEmpty())
            used_before_declare.forEach(System.out::println);
        else
            System.out.println("All variables are declared before use.");
        if(!duplicate_declarations.isEmpty())
            duplicate_declarations.forEach(System.out::println);
        else
            System.out.println("No duplicate declarations of variables, consts, and functions");
        if(!wrong_number_of_param.isEmpty())
            wrong_number_of_param.forEach(System.out::println);
        else
            System.out.println("All functions called with correct number of args");
        if(!wrong_method_signature.isEmpty())
            wrong_method_signature.forEach(System.out::println);
        else
            System.out.println("All arguments match function parameter types");
        if(!all_funcs_declared.isEmpty())
            all_funcs_declared.forEach(System.out::println);
        else
            System.out.println("All used functions declared");

        System.out.println("\n### Semantic Analysis END ###\n");
    }
}
