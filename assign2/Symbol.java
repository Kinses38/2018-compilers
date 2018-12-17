class Symbol
{
    private String type;
    private String symbol_type;

    Symbol()
    {
        this.type = "";
        this.symbol_type = "";
    }

    Symbol(String type, String symbol_type)
    {
        this.type = type;
        this.symbol_type = symbol_type;
    }

    String getType()
    {
        return this.type;
    }

    String getSymbolType()
    {
        return this.symbol_type;
    }
}
