/* Generated By:JJTree: Do not edit this line. ASTwhil.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTwhil extends SimpleNode {
  public ASTwhil(int id) {
    super(id);
  }

  public ASTwhil(CalParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CalParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=84c6565141b9ec6206d22d58dabc7eee (do not edit this line) */
