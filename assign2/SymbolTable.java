import java.util.*;

class SymbolTable
{

    private LinkedHashMap<String, LinkedList<String>> scopes_table;
    private Hashtable<String, Symbol> symbols;
    private static String SCOPE = "global";

    SymbolTable()
    {
        scopes_table = new LinkedHashMap<>();
        symbols = new Hashtable<>();
        scopes_table.put(SCOPE, new LinkedList<>());
    }

    void put(String id, String type, String symbol_type, String scope)
    {
        LinkedList<String> values_in_scope = scopes_table.get(scope);
        if(values_in_scope == null)
        {
            values_in_scope = new LinkedList<>();
            values_in_scope.add(id);
            scopes_table.put(scope, values_in_scope);
        }
        else
        {
            values_in_scope.add(id);
        }
        Symbol current_sym = new Symbol(type, symbol_type);
        symbols.put(id+scope, current_sym);
    }

    // Get scope, return or bool? Checking if a function is declared in scope or not.
    boolean checkScope(String id, String scope)
    {
        LinkedList<String> in_scope = scopes_table.get(scope);
        //scope or nothing declared yet.
        if(in_scope == null)
            return false;
        return in_scope.contains(id);
    }

    Symbol getSymbol(String id, String scope)
    {
        if(checkScope(id, scope))
            return symbols.get(id + scope);
        else
            return null;
    }

    ArrayList<String> getDuplicateDeclarations()
    {
        HashSet<String> no_dups_allowed = new HashSet<>();
        ArrayList<String> dups = new ArrayList<>();

        scopes_table.forEach((scope, scope_stack) ->
        {
            LinkedList<String> current_scope = scopes_table.get(scope);
            for(String id : current_scope)
            {
                Symbol current_id = symbols.get(id + scope);
                String current_sym_type = current_id.getSymbolType();

                switch (current_sym_type) {
                    case "parameter":
                        if (!no_dups_allowed.add(id + scope))
                            dups.add("Duplicate declarations of "
                                    + current_sym_type.toUpperCase() + ": " + id + " in scope: "
                                    + scope.toUpperCase());
                        break;
                    case "function":
                        if (!no_dups_allowed.add(id + scope))
                            dups.add("Duplicate declarations of "
                                    + current_sym_type.toUpperCase() + ": " + id + " in scope: "
                                    + scope.toUpperCase());
                        break;
                    case "var":
                        if (!no_dups_allowed.add(id + scope))
                            dups.add("Duplicate declarations of "
                                    + current_sym_type.toUpperCase() + ": " + id + " in scope: "
                                    + scope.toUpperCase());
                        break;
                }
            }});
        return dups;
    }

    ArrayList<Symbol> getFuncParameters(String scope)
    {
        ArrayList<Symbol> all_params = new ArrayList<>();
        //return scope stack
        //iterate through function scope where symboltype = param, param++;
        LinkedList <String> current_stack = scopes_table.get(scope);
        for(String id : current_stack)
        {
            Symbol param = symbols.get(id + scope);
            if(param.getSymbolType().equals("parameter"))
                all_params.add(param);
        }
       return all_params;
    }

    void printSymbolTable()
    {
        if(!scopes_table.isEmpty())
        {
            System.out.println("\n### Symbol Table ###");
            scopes_table.forEach((scope, scope_stack) ->
            {
                System.out.println("\nCurrent scope: " + scope + " ");
                System.out.printf("%-10s %-10s %-10s\n", "Type", "ID", "Symbol-Type");
                scope_stack.forEach((id) ->
                {
                    Symbol current_sym = symbols.get((id + scope));
                    System.out.printf("%-10s %-10s %-10s\n",
                            current_sym.getType(), id, current_sym.getSymbolType());
                });
            });
            System.out.println("\n### Symbol Table End ###\n");
        }
    }
}
