package tool;

import inter.Expr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import lexer.Word;


public class Tool {

	public static void addError(int line,String context){
		System.out.println("´íÎóËùÔÚÐÐ:"+line+"\t"+context);
	}
	
	public static  String reader(String file)
	{
		StringBuilder sb=new StringBuilder();
		BufferedReader br=null;
		try{
			br=new BufferedReader(new FileReader(file));
			String line;
			while((line=br.readLine())!=null){
				sb.append(line+"\n");
			}
			br.close();
			return sb.toString();
		}catch(IOException ex){
			System.out.println("Tool.IOException :"+ex.toString());
			return null;
		}
	}
}






