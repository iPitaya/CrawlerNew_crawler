package cn.b2b.crawler.resultdata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StoreFile {
	public  String filename = "data/urllog/";
	FileWriter fw;
	BufferedWriter bw;
	
	public StoreFile(){
		try {
			long time1 = new Date().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fname  = sdf.format(new Date());
			System.out.println(fname);
			filename = filename + fname;
			
			fw = new FileWriter(filename,true);
			bw=new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public  void  writedata(String data){

		try {
			data = data + "\n";
			bw.write(data);
			bw.flush();
			fw.flush();
			//System.out.println(data + "  dd");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void fileFinish(){
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
