package aa;

import test.Test;

public class eee {

	public static void main(String[] args) {
		String s = "abcdef";
		System.out.println("JVM MAX Memory:");
		System.out.println("maxMemory" + Runtime.getRuntime().maxMemory()/1024/1024 + "M");
		System.out.println("totalMemory" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");
		int count = 0;
		while(true){
			count++;
			System.out.println(count);
			try {
				s = s + s;
			} catch (Error e) {
				System.out.println("error totalMemory" + Runtime.getRuntime().totalMemory()/1024/1024 + "M");
				break;
			} 
		}
	}
}
