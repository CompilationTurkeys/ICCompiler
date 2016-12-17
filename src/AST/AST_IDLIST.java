package AST;

public class AST_IDLIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String head;
	public AST_IDLIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_IDLIST(String head,AST_IDLIST tail)
	{
		this.head = head;
		this.tail = tail;
	}
}