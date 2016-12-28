package ic.compiler;
   
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java_cup.runtime.*;
import ic.ast.AST_Program;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		FileReader file_reader = null;
		PrintWriter file_writer = null;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{	
			file_reader = new FileReader(new File(inputFilename));

			file_writer = new PrintWriter(new File(outputFilename));
			
			l = new Lexer(file_reader);
			
			p = new Parser(l);
			
			Symbol parseSymbol = p.parse();
			AST_Program root = (AST_Program) parseSymbol.value;
			SemanticEvaluator semEvaluator = new SemanticEvaluator(root);
			semEvaluator.evaluate();
			file_writer.write("OK");
    	}
			     
		catch (Exception e)
		{
			file_writer.write("FAIL");
			System.out.println("failed with message: " + e.getMessage());
			
		}
		finally{
			file_writer.close();			
		}
	}
}


