import java.util.*;

class SymbolTable
{

    private Hashtable<String, LinkedList<String>> sym_tab;
    private Hashtable<String, String> types;
    private Hashtable<String, String> values;

    private static String SCOPE = "global";

    SymbolTable()
    {
        sym_tab = new Hashtable<>();
        types = new Hashtable<>();
        values = new Hashtable<>();

        sym_tab.put(SCOPE, new LinkedList<>());
    }

    void put(String id, String type, String value, String scope)
    {
        LinkedList<String> tmp = sym_tab.get(scope);
        if(tmp == null)
        {
            tmp = new LinkedList<>();
            tmp.add(id);
            sym_tab.put(scope, tmp);
        }
        else
        {
            tmp.addFirst(id);
        }
        types.put(id + scope, type);
        values.put(id+ scope, value);
    }

    void printSymbolTable()
    {
        sym_tab.forEach((scope, scope_stack) ->
        {
            System.out.println("Current scope: " + scope + " ");
            scope_stack.forEach((id) ->
            {
                String type = types.get(id + scope);
                String value = values.get(id + scope);
                System.out.printf("%-10s %-10s %-10s\n", type, id, value);
            });
        });
    }

    boolean checkScope(String id, String scope)
    {
        LinkedList<String> in_scope = sym_tab.get(scope);
        //scope or nothing declared yet.
        if(in_scope == null)
            return false;
        return in_scope.contains(id);
    }


}
