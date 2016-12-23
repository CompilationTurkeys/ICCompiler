package ic.ast;

public enum BinaryOpTypes {
	PLUS,
	DIVIDE,
	GT,
	GTE,
	EQUALS,
	LT,
	LTE,
	NEQUALS,
	TIMES,
	MINUS;
	
	
	public String getOpDescreption(){
		switch (this){
		case PLUS:
			return "addition";
		case MINUS:
			return "substraction";
		case DIVIDE:
			return "division";
		case TIMES:
			return "multiplication";
		case GT:
			return "greater than";
		case LT:
			return "less than";
		case NEQUALS:
			return "not equals";
		case GTE:
			return "greater than or equal";
		case LTE:
			return "less than or equal";
		case EQUALS:
			return "equals";
		default:
			return null;
		}
	}
}
