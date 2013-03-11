package lexer;

public class Lexer {
	private int line;
	private char peek;
	private int index;
	private int length;
	private String text;
	public Lexer(String text) {
		this.text=text;
		line=1;
		index=0;
		length=text.length();
	}

	private void readch() { 
		if(index<length)peek = text.charAt(index++); 
		else peek=0;
	}
	private void backch(){
		if(index>=0)index--;
	}
	  
	public int getLine(){ return line; }

	public Word scan() {
		for(readch();;readch()){
			if( peek == ' ' || peek == '\t' );
			else if( peek == '\n' ) line = line + 1;
			else if( peek == '{'){	//过滤注释
				do{ 
					readch(); 
				}while(peek !='}');
			}else break;
		}
		//符号集
	    switch( peek ) {
	        case ':':
			readch();
			if( peek == '=' ) return Word.ASSIGNOP_WORD;  
			else{
				backch();
				return Word.ASSIGN_WORD;
			}
			case '<':
				  readch();
			if( peek == '=' ) return Word.LE_WORD;   
			else if(peek=='>') return Word.NE_WORD;
			else {
				backch();
				return Word.LT_WORD;
			}
			case '>':
				readch();
			if( peek == '=' ) return Word.GE_WORD;  
			else {
				backch();
				return Word.GT_WORD;
			}
			case '=':
				return Word.EQ_WORD;
			case '+':
				return Word.PLUS_WORD;
			case '-':
				return Word.MINUS_WORD;
			case '*':
				return Word.MULTI_WORD;
			case '/':
				return Word.CDIV_WORD;
	
			case '(':
				return Word.LQ_WORD;
			case ')':
				return Word.RQ_WORD;
			case '[':
				return Word.LS_WORD;
			case ']':
				return Word.RS_WORD;
			case ',':
				return Word.COMMA_WORD;
			case ';':
				return Word.SEMIC_WORD;
			case '.':
				return Word.DOT_WORD;
		}
		//数字，多读了一个字符：backch();
		if( Character.isDigit(peek) ) {
			StringBuffer b=new StringBuffer();
			do {
				b.append(peek); readch();
			} while( Character.isDigit(peek) );
			if( peek == '.' ) {
				readch();
				if(Character.isDigit(peek)){
					b.append(Word.DOT_WORD.getLexme());
					while(Character.isDigit(peek)) {
						b.append(peek);
						readch();
					}
				}else backch();//回溯DOT后的字符
			}
			backch();
			return new Word(b.toString() , Word.NUM);
		}

		//普通字符串，多读了一个字符：backch()
		if( Character.isLetter(peek) ) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek); readch();
			} while( Character.isLetterOrDigit(peek) );
			backch();
			String s = b.toString();
			Word w = Word.find(s);
			if( w == null ) w = new Word(s, Word.ID);
				return w;
		}
		//其它字符
		Word w=new Word(""+peek,Word.ERROR);
		peek=' ';
		return w;
	}
}
