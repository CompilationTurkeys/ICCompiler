   
import java.io.FileReader;
import java.io.PrintWriter;

import AST.AST_PROGRAM;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{

			file_reader = new FileReader(inputFilename);

			file_writer = new PrintWriter(outputFilename);
			
			l = new Lexer(file_reader);
			
			try {
				p = new Parser(l);
				//p.parse();
				Symbol parseSymbol = p.parse(); //Symbol is the object that the CUP returns (need import)
				AST_PROGRAM root = (AST_PROGRAM) parseSymbol.value; //parseSymbol.value is an AST_NODE
				
			}
			catch(Exception e) {
				file_writer.write("FAIL");
				file_writer.close();
				return;
			}

			file_writer.write("OK");
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


