package inter;

import lexer.Word;

public class Expr {
	public static MiddleCode middleCode=null;
	private String signOrOp=null;
	private String s1=null;
	private String s2=null;
	
	public Expr(String s1) { this.s1=s1; }
	public String getSignOrOp(){ return this.signOrOp; }
	public String getS1(){ return this.s1; }
	public String getS2(){ return this.s2; }
	
	/**
	 * 合并语句，并把合并后的结果修改在主调Expr中
	 */
	public void merge(){
		this.s1=getTemp(this);
		this.s2=null;
		this.signOrOp=null;
	}
	public void merge(String op){
		this.s1=getTemp(this);
		this.s2=null;
		this.signOrOp=op;
	}
	public void merge(String op,Expr expr){
		this.s1=getTemp(this);
		this.s2=getTemp(expr);
		this.signOrOp=op;
	}
	
	/**
	 * 检测expr的语句类型，以确定是否使用中间变量，当使用中间变量时将把此中间代码插入到middleCode中
	 * @return 中间变量名称
	 */
	private String getTemp(Expr expr){
		if(expr.getSignOrOp()!=null){
			String ts;
			if(expr.getS2()==null){	//含负号时
				ts=expr.getSignOrOp()+expr.getS1();
			}else{	//含两个操作数的运算，care: 保持与toString的结构一样
				ts=expr.getS1()+" "+expr.getSignOrOp()+" "+expr.getS2();
			}
			String tempName=middleCode.getTempName();
			middleCode.addCode(tempName+Word.ASSIGNOP_WORD.getLexme()+ts);
			//System.out.println("getTemp "+ts);
			return tempName;
		}
		return expr.getS1();
	}
	
	public String toString(){
		if(signOrOp==null) return s1;
		if(s2==null) return signOrOp+s1;
		return s1+" "+signOrOp+" "+s2;
	}

}
