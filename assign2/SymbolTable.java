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
        LinkedList<String> scope_values = scopes_table.get(scope);
        if(scope_values == null)
        {
            scope_values = new LinkedList<>();
            scope_values.add(id);
            scopes_table.put(scope, scope_values);
        }
        else
        {
            scope_values.add(id);
        }
        Symbol current_sym = new Symbol(type, symbol_type);
        symbols.put(id+scope, current_sym);
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
                            current_sym.getType(), id, current_sym.getSymbol_type());
                });
            });
        }
    }

    boolean checkScope(String id, String scope)
    {
        LinkedList<String> in_scope = scopes_table.get(scope);
        //scope or nothing declared yet.
        if(in_scope == null)
            return false;
        return in_scope.contains(id);
    }


}
