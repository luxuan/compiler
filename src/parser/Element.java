package parser;

import inter.Expr;
import lexer.Lexer;
import lexer.Word;
import tool.Tool;

/** 不成功，则不读取 */
public class Element {
	Word look;   // lookahead tagen，保证每次调用是未使用过的
	private Lexer lex;    // lexical analyzer for this parser
	public Element(Lexer lex){
		this.lex=lex;
	}
	void move() { 
		look = lex.scan(); 
		//System.out.println((int)look.getTag()+"|"+look.getLexme());
	}

	boolean match(char ch) {
		if( look.getTag() == ch ) {
			move();
			return true;
		}else {
			Tool.addError(lex.getLine() , "在字符'"+look.getLexme()+"'处应匹配'"+
					Word.getLexme(ch)+"'("+(int)ch+")");
			return false;
		}
	}
	
	//TODO modify to type,type
	String expressionList(){
		Expr re=expression();
		if(re==null)return null;
		StringBuilder sb=new StringBuilder();
		while(true){
			re.merge();
			sb.append(re.toString());
			if(look.getTag()==Word.COMMA){
				move();	//debugged: add
				re=expression();
				sb.append(Word.COMMA_WORD.getLexme());
			}
			else break;
		}
		return sb.toString();
	}

	Expr expression(){
		Expr re=simpleExpression();
		if(re==null)return null;
		while(look.getTag()==Word.RELOP){
			String tOp=look.getLexme();	//debugged: add
			move();	//debugged: add
			Expr te=simpleExpression();
			if(te!=null){
				re.merge(tOp, te);	//debugged: modify
			}else {
				Tool.addError(lex.getLine(), "relop后须紧跟simple_expression产生式");
			}
		}
		return re;
		   
	}
	Expr simpleExpression(){
		Expr re=null;
		String op=look.getLexme();
		//sign
		if("+".equals(op) || "-".equals(op)){ move(); }
		else op=null;
		   
		re=term();
		if(re==null)return null;
		if("-".equals(op))re.merge("-");
		while(look.getTag()==Word.ADDOP){
			String tOp=look.getLexme();	//debugged: add
			move();	//debugged: add
			re.merge();	//debugged: modify,把正确的加减运算输出
			Expr te=term();
			if(te!=null){
				re.merge(tOp, te);	//debugged: modify
			}else {
				Tool.addError(lex.getLine(), "addop后须紧跟term产生式");
				break;	//debugged: add
			}
		}
		return re;
		   
	}

	Expr term(){
		Expr re=fator();
		if(re==null)return null;
		while(look.getTag()==Word.MULOP){
			String tOp=look.getLexme();	//debugged: add
			move();	//debugged: add
			Expr te=fator();
			if(te!=null){
				re.merge(tOp, te);	//debugged: modify
			}else {
				Tool.addError(lex.getLine(), "No fator after mulop");
				break;	//debugged: add
			}
		}
		return re;
	}
	   
	/** 不成功，则不读取 */
	Expr fator(){
		Expr re=null;
		//System.out.println("debug factor:look="+look.getLexme());
		switch(look.getTag()){
		case Word.ID:
			Word tw=look;
			move();
			if(look.getTag()==Word.LQ){	//id(expressionList)
				move();	//debugged: add
				String code=expressionList();
				match(Word.RQ);
				code=tw.getLexme()+"("+code+")";
				re=new Expr(code);
			}else {	//id
				re=new Expr(tw.getLexme());
			}
			break;
		case Word.NUM:
			re=new Expr(look.getLexme());
			move();
			break;	//debugged: add
		case Word.LQ:
			move();
			re=expression();
			match(Word.RQ);
			break;
		case Word.NOT:
			String notString=look.getLexme();
			move();
			re=fator();
			re.merge(notString);
			break;
		}
		return re;
	}
	   
}
