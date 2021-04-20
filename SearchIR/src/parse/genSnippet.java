package parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class genSnippet {

	public static void main(String[] args) throws FileNotFoundException {
		//src폴더의 data package에 input.txt를 만들고 진행했습니다/
		
		File file=new File("/Users/jodaehyeon/Desktop/SimpleIR/SearchIR/src/data/"+args[3]);
		ArrayList input=new ArrayList<String>();
		String str=args[5];
		String[] strline=str.split(" ");
		
		Scanner scan=new Scanner(file);
		ArrayList list=new ArrayList<String>();
		ArrayList count=new ArrayList<Integer>();
		while(scan.hasNextLine()) {
			list.add(scan.nextLine());
		}
		
		for(int i=0;i<list.size();i++) {
			int c=0;
			for(int j=0;j<strline.length;j++) {
				if(list.get(i).toString().contains(strline[j])) {
					c++;
				}
			}
			count.add(i, c);
		}
		
		int temp=(int)count.get(0);
		for(int k=0;k<count.size()-1;k++) {
	
			if(temp<=(int)count.get(k+1)) {
				temp=(int)count.get(k+1);
			}
		}
		System.out.println(list.get(temp-1));
	}

}
