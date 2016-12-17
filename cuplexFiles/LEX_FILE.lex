/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/***************************/
/* AUTHOR: OREN ISH SHALOM */
/***************************/

/*************/
/* USER CODE */
/*************/
   
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/
      
%%
   
/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column
    
/*******************************************************************************/
/* Note that this has to be the EXACT smae name of the class the CUP generates */
/*******************************************************************************/
%cupsym sym
%scanerror Exception

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup
   
/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied letter to letter into the Lexer class code.                */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, String description)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value, String description) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 
%}

%eofval{
	return symbol(sym.EOF, "EOF");
%eofval}
%eofclose

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LINETERMINATOR	= \r|\n|\r\n
WHITESPACE		= {LINETERMINATOR} | [ \t\f]
INTEGER			= 0 | [1-9][0-9]*
IDENTIFIER		= [a-z][a-zA-Z_0-9]*
STR			    = \"([^\\\"]|\\.)*\"
CLASSID 		= [A-Z][a-z_0-9]*
COMMENTS		= "/*"((("*"[^/])?)|[^*])*"*/" | "//".*

   
/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/
   
/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/
   
<YYINITIAL> {

"+"					{ return symbol(sym.PLUS, "+");}
"-"					{ return symbol(sym.MINUS, "-");}
"*"					{ return symbol(sym.TIMES, "*");}
"/"					{ return symbol(sym.DIVIDE, "/");}
"("					{ return symbol(sym.LPAREN, "(");}
")"					{ return symbol(sym.RPAREN, ")");}
"class"				{ return symbol(sym.CLASS, "class");}
"{"					{ return symbol(sym.LBRACE, "{");}
"int"				{ return symbol(sym.INTEGER, "int");}
"["					{ return symbol(sym.LBRACK, "[");}
"]"					{ return symbol(sym.RBRACK, "]");}
";"					{ return symbol(sym.SEMICOLON, ";");}
"="							{ return symbol(sym.ASSIGN, "=");}
","							{ return symbol(sym.COMMA, ",");}
"."							{ return symbol(sym.DOT, ".");}
"=="					{ return symbol(sym.EQUAL, "==");}
"extends"				{ return symbol(sym.EXTENDS, "extends");}
">"						{ return symbol(sym.GT, ">");}
">="						{ return symbol(sym.GTE, ">=");}
"if"						{ return symbol(sym.IF, "if");}
"new"						{ return symbol(sym.NEW, "new") ;}
"<"						{ return symbol(sym.LT, "<");}
"<="						{ return symbol(sym.LTE, "<=");}
"!="						{ return symbol(sym.NEQUAL, "!=");}
"null"						{ return symbol(sym.NULL, "null");}
"}"						{ return symbol(sym.RBRACE, "}");}
"return"						{ return symbol(sym.RETURN, "return");}
"string"						{ return symbol(sym.STRING, "string");}
"void"						{ return symbol(sym.VOID, "void");}
"while"						{ return symbol(sym.WHILE, "while");}



{STR}  				{ return symbol(sym.STR, yytext(), "STR");}
{CLASSID}  			{ return symbol(sym.CLASS_ID, yytext(), "CLASS_ID");}
{INTEGER}			{ return symbol(sym.INT, Integer.valueOf(yytext()), "INTEGER");}   
{WHITESPACE}		{ /* just skip what was found, do nothing */ }
{COMMENTS}			{ /* just skip what was found, do nothing */ }
{IDENTIFIER}		{  return symbol(sym.ID, yytext(), "IDENTIFIER");}

}

[^]                 { throw new Exception("Line " + getLine() + ": Lexical error. Illegal character '" + yytext() + "'"); }