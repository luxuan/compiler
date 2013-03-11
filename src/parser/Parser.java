package parser;

import inter.Expr;
import inter.MiddleCode;
import lexer.*;

public class Parser extends Element {

	private MiddleCode middleCode;

	public Parser(Lexer lex) { 
		super(lex); 
		this.middleCode=new MiddleCode();
		Expr.middleCode=this.middleCode;
		super.move(); 
	}


	public void program(){
		match(Word.PROGRAM);
		match(Word.ID);
		match(Word.LQ);
		identifierList();
		match(Word.RQ);
		
		declarations();
		subprogramDeclarations();
		compoundStatement();
		
		System.out.println(this.middleCode.toString());	//打印中间代码
	}
	
	boolean identifierList(){
		while(true){
			if(!match(Word.ID))return false;
			if(look.getTag()!=Word.COMMA)break;
			move();
		}
		return true;
	}
	
	boolean declarations(){
		while(look.getTag()==Word.VAR){
			move();
			if(!identifierList())return false;
			if(!match(Word.ASSIGN)) return false;
			if(!type())return false;
			if(!match(Word.SEMIC))return false;
		}
		return true;
	}
	boolean type(){
		char tag=look.getTag();
		if(tag==Word.ARRAY){
			move();
			if(match(Word.LS) && match(Word.NUM) && match(Word.DOT) &&
					match(Word.DOT) && match(Word.NUM) && match(Word.RS) 
					&& match(Word.OF) && match(Word.TYPE)){
				return true;
			}
		}else if(tag==Word.TYPE){
			move();
			return true;
		}
		return false;
	}
	
	boolean subprogramDeclarations(){
		//head
		char tag=look.getTag();
		if(tag==Word.FUNCTION || tag==Word.PROCEDURE){
			move();
			if(!match(Word.ID))return false;
			
			//arguments
			if(look.getTag()==Word.LQ){
				move();
				//paramenter_list
				while(true){
					if(!identifierList())return false;
					if(!match(Word.ASSIGN)) return false;
					if(!type())return false;
					if(look.getTag()!=Word.SEMIC)break;
					move();
				}
				if(!match(Word.RQ))return false;
			}
			
			if(look.getTag()==Word.ASSIGN){
				move();
				if(!match(Word.TYPE))return false;
			}
			if(!match(Word.SEMIC))return false;
		}
		return declarations()&&compoundStatement();
	}
	
	/** compound_statement,ooptional_statements,statement_list */
	boolean compoundStatement(){
		if(look.getTag()!=Word.BEGIN)return false;	//debugged: modify
		move();
		
		//ooptional_statements
		if(look.getTag()==Word.END){ move();return true; }	//debugged: modify
		//statement_list
		while(true){
			if(!statement())return false;
			if(look.getTag()!=Word.SEMIC)break;
			move();
		}
		return match(Word.END);	//debugged:modify
	}
	   
	boolean statement(){
		Expr expr=null;
		String code=null;
		switch(look.getTag()){
		case Word.ID:	//variable,procedure_statement
			Word tw=look;
			move();
			if(look.getTag()==Word.LQ){	//procedure_statement: id(expression)
				move();	//debugged: add
				code=expressionList();
				match(Word.RQ);
				code=tw.getLexme()+Word.LQ_WORD.getLexme()+code+Word.RQ_WORD.getLexme();
				this.middleCode.addCode(code);
			}else {	//variable: id[expression],id
				if(look.getTag()==Word.LS){
					move();	//debugged: add
					Expr te=expression();
					te.merge();
					match(Word.RS);
					expr=new Expr(tw.getLexme()+Word.LS_WORD.getLexme()+te.toString()+Word.RS_WORD.getLexme());
				}else {
					expr=new Expr(tw.getLexme());
				}
				code=expr.toString()+look.getLexme();
				if(!match(Word.ASSIGNOP))return false;	//debugged: modify
				expr=expression();
				code+=expr.toString();
				this.middleCode.addCode(code);
			}
			break;
		case Word.IF:
			move();	//debugged: add
			expr=expression();
			if(!match(Word.THEN))return false;	//debugged: modify
			int ifLine=this.middleCode.getLine();
			this.middleCode.addCode("if "+expr.toString()+" goto "+(ifLine+2));
			this.middleCode.addCode("");	//暂时不能确定else起始位置，后面修改
			if(!statement())return false;	//if成立部分
			this.middleCode.addCode("");	//暂时不能确定else结束位置，后面修改
			int elseLine=this.middleCode.getLine();
			this.middleCode.set(ifLine+1, "goto "+elseLine);	//修改else入口
			   
			if(!match(Word.ELSE))return false;	//debugged: modify
			if(!statement())return false;
			//修改if结束跳转
			this.middleCode.set(elseLine-1, "goto "+this.middleCode.getLine());
			break;
		case Word.WHILE:
			move();	//debugged: add
			expr=expression();
			if(!match(Word.DO)) return false;	//debugged: modify
			int whileLine=this.middleCode.getLine();
			this.middleCode.addCode("if "+expr.toString()+" goto "+(whileLine+2));
			this.middleCode.addCode("");
			if(!statement())return false;	//while成立部分
			this.middleCode.addCode("goto "+whileLine);
			this.middleCode.set(whileLine+1, "goto "+this.middleCode.getLine());
			break;
		default:
			return compoundStatement();
		}
		return true;
	}
	   
}
