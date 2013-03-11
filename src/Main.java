import lexer.Lexer;
import parser.Parser;
import tool.Tool;

public class Main {
	public static void main(String[] args) {
		//TODO 出错后怎么继续运行
		String text=Tool.reader("data/test.txt");
		//System.out.println(text);
		Lexer lex=new Lexer(text);
		Parser parser=new Parser(lex);
		parser.program();
	}
}
