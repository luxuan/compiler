package inter;

import java.util.ArrayList;

public class MiddleCode {
	private final static String tname="t"; 
	private int index=0;
	private int line=0;
	private ArrayList<String> codes=new ArrayList<String>();
	
	public void addCode(String code){ this.codes.add(code);line++; }
	public String getTempName(){ return tname+(index++);}
	public int getLine(){ return line;}
	public String set(int index , String code){ return codes.set(index, code); }
	
	public String toString(){
		System.out.println("middle code:");
		StringBuilder sb=new StringBuilder();
		int i=0;
		for(String code:codes){
			sb.append((i++)+"\t"+code+"\n");
		}
		return sb.toString();
	}

}
