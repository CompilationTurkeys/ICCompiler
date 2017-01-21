package ic.compiler;
   
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java_cup.runtime.*;
import ic.ast.AST_Program;
import ir.mipsgen.MipsGenerator;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		FileReader file_reader = null;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{	
			file_reader = new FileReader(new File(inputFilename));
			
			l = new Lexer(file_reader);
			
			p = new Parser(l);
			
			Symbol parseSymbol = p.parse();
			AST_Program root = (AST_Program) parseSymbol.value;
			SemanticEvaluator semEvaluator = new SemanticEvaluator(root);
			semEvaluator.evaluate();
			
			IRTreeGenerator treeGen = new IRTreeGenerator(root);
			MipsGenerator mipsGen = new MipsGenerator(treeGen.generateIRTree());
			mipsGen.generateCode(outputFilename);
			
    	}
			     
		catch (Exception e)
		{
			System.out.println("failed with message: " + e.getMessage());
			
		}
	}
}


