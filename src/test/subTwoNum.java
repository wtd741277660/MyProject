package test;

import java.util.Scanner;

//键盘输入两个数字，并相减
public class subTwoNum {
	
	public static void main(String[] args) throws Exception {
		Double a = getDouble();
		Double b = getDouble();
		System.out.println(a - b);
	}
	
	private static Double getDouble(){
		Scanner scanner = null;
		Double d = null;
		String inPutStr = null;
		while (true) {
			scanner = new Scanner(System.in);
			inPutStr = scanner.next();
			if (isNum(inPutStr)) {
				d = Double.valueOf(inPutStr);
				break;
			}
		}
		return d;
	}
	
	private static boolean isNum(String inPutStr){
		try {
			Double.valueOf(inPutStr);
		} catch (Exception e) {
			System.err.println("请输入合法数字！");
			return false;
		}
		return true;
	}
	
}
