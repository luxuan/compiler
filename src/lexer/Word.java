package lexer;

public class Word {
	private char tag;
	private String lexeme;
	public Word(String lexeme, char tag) { this.tag=tag; this.lexeme = lexeme; }
	public char getTag(){ return this.tag; }
	public String getLexme() { return lexeme; }

	
	public final static char
		PROGRAM=1,	FUNCTION=2,	PROCEDURE=3,
		BEGIN=4,	END=5,		LQ=6,		RQ=7,	
		LS=24,	RS=25,	COMMA=26,	SEMIC=27,	ASSIGN=28,	DOT=29,	//[],;:	
		VAR=8,		ARRAY=9,	OF=10,	
		IF=14,		THEN=15,	ELSE=16,	WHILE=17,	DO=18,
		NOT=19,		ASSIGNOP=23,RELOP=20,	ADDOP=21,	MULOP=22,
		TYPE=11,	NUM=12,		ID=13,
		ERROR=(char)0;
	
	//符号集
	public final static Word 
		ASSIGNOP_WORD = new Word(":=",Word.ASSIGNOP),
		ASSIGN_WORD = new Word(":",Word.ASSIGN),
		LE_WORD = new Word("<=",Word.RELOP),
		NE_WORD = new Word("<>",Word.RELOP),
		LT_WORD = new Word("<",Word.RELOP),
		
		GE_WORD = new Word(">=",Word.RELOP),  
		GT_WORD = new Word(">",Word.RELOP),
		EQ_WORD = new Word("=",Word.RELOP),
		 
		PLUS_WORD = new Word("+",Word.ADDOP),
		MINUS_WORD = new Word("-",Word.ADDOP),
		MULTI_WORD = new Word("*",Word.MULOP),
		CDIV_WORD = new Word("/",Word.MULOP),
		 
		LQ_WORD = new Word("(",Word.LQ),
		RQ_WORD = new Word(")",Word.RQ),
		LS_WORD = new Word("[",Word.LS),
		RS_WORD = new Word("]",Word.RS),
		
		COMMA_WORD = new Word(",",Word.COMMA),
		SEMIC_WORD = new Word(";",Word.SEMIC),
		DOT_WORD = new Word(".",Word.DOT);

	//关键字
	public final static Word 
		PROGRAM_WORD = new Word( "program",	PROGRAM),
		FUNCTION_WORD = new Word( "function",	FUNCTION),
		PROCEDURE_WORD = new Word( "procedure",	PROCEDURE),
		BEGIN_WORD = new Word( "begin",		BEGIN),
		END_WORD = new Word( "end",		END),
		VAR_WORD = new Word( "var",		VAR),
		ARRAY_WORD = new Word( "array",		ARRAY),
		OF_WORD = new Word( "of",			OF),
		IF_WORD = new Word( "if",  	  	IF),
		THEN_WORD = new Word( "then",  	  	THEN),
		ELSE_WORD = new Word( "else", 		ELSE),
		WHILE_WORD = new Word( "while", 		WHILE),
		DO_WORD = new Word( "do",    		DO),
			
		NOT_WORD = new Word( "not",	NOT),
			
		INTEGER_WORD = new Word( "integer",	TYPE),
		REAL_WORD = new Word( "real",		TYPE),
			
		OR_WORD = new Word( "or",		ADDOP),
		
		DIV_WORD = new Word( "div",	MULOP),
		MOD_WORD = new Word( "mod",	MULOP),
		AND_WORD = new Word( "and",	MULOP);

	
	//记录语言关键字，数字在Lexer中直接匹配
	private final static Word[] keywords = {	
		PROGRAM_WORD,	FUNCTION_WORD,		PROCEDURE_WORD,
		BEGIN_WORD,		END_WORD,			VAR_WORD,
		ARRAY_WORD,		OF_WORD,			IF_WORD,	THEN_WORD,
		ELSE_WORD,		WHILE_WORD,			DO_WORD,
		NOT_WORD,		INTEGER_WORD,		REAL_WORD,
		OR_WORD,		DIV_WORD,			MOD_WORD,		AND_WORD
	};
	private final static Word[] identifiers={
		ASSIGNOP_WORD,	ASSIGN_WORD,	LE_WORD,	NE_WORD,	LT_WORD,
		GE_WORD,	GT_WORD,	EQ_WORD,	PLUS_WORD,	MINUS_WORD,
		MULTI_WORD,	CDIV_WORD,	LQ_WORD,	RQ_WORD,	LS_WORD,
		RS_WORD,	COMMA_WORD,	SEMIC_WORD,	DOT_WORD
	};

	public static Word find(String s){
		for(Word w:keywords){
			if(w.lexeme.equals(s))return w;
		}
		return null;
	}
	
	public static String getLexme(char tag){
		StringBuilder rs=new StringBuilder();
		for(Word w:keywords){
			if(w.tag==tag)rs.append(w.lexeme+',');
		}
		for(Word w:identifiers){
			if(w.tag==tag)rs.append(w.lexeme+',');
		}
		if(rs.length()>0){ rs.deleteCharAt(rs.length()-1); }
		return rs.toString();

	}
	
}
