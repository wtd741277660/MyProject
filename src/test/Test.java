package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().maxMemory());
		System.out.println(Runtime.getRuntime().totalMemory());
		
	}
	
	
	//设计自定义异常类
	public void testException() throws MyException{
		Scanner in = new Scanner(System.in);
		String string = in.next();
		if (string.equals("abc")) {
			throw new MyException("输入的值为abc");
		}else {
			System.out.println("不抛异常");
		}
	}
	
	class MyException extends Exception{
		public MyException(String msg){
			super(msg);
		}
	}
	
	
	//将字符串日期转成Calendar
	public static Calendar getCalendar(String string){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = null;
		try {
			calendar = Calendar.getInstance();
			Date d = sdf.parse(string);
			calendar.setTime(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar;
	}
	
	//字母在字符串中出现的最多次数1
	public static void maxTimesInString(){
		String string = "adfdbksdhfsadfaa";
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int i = 0;i < string.length();i++){
			String a = string.charAt(i) + "";
			if (map.get(a) != null) {
				map.put(a, map.get(a)+1);
			}else {
				map.put(a, 1);
			}
		}
		Integer max = 0;
		for(String key:map.keySet()){
			if (map.get(key) > max) {
				max = map.get(key);
			}
		}
		System.out.println("出现最多的次数是：" + max);
		System.out.print("出现最多的字母有：");
		for(String key:map.keySet()){
			if (map.get(key) == max) {
				System.out.print(key + " ");
			}
		}
	}
	
	//字母在字符串中出现的最多次数2
	private static void getDittogram() {
		String input = "aavlasdjflajeeeeewjjowejjojasjfesdvoeawjje";
        char[] chars = input.toCharArray();
        ArrayList lists = new ArrayList();
        TreeSet set = new TreeSet();
        for (int i = 0; i < chars.length; i++){
            lists.add(String.valueOf(chars[i]));
            set.add(String.valueOf(chars[i]));
        }
        Collections.sort(lists);
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < lists.size(); i++){
            sb.append(lists.get(i));
        }
        input = sb.toString();
        int max = 0; 
        String maxString = "";
        ArrayList maxlist =  new ArrayList();
        Iterator its = set.iterator();
        while(its.hasNext()){
            String os = (String) its.next();
            int begin = input.indexOf(os);
            int end = input.lastIndexOf(os);
            int value = end - begin + 1;
            if(value > max){
                max = value; 
                maxString = os; 
                maxlist.add(os);
            }else if(value == max){
                maxlist.add(os);
            }
        }
        int index = 0;
        for (int i = 0; i< maxlist.size(); i++){
            if(maxlist.get(i).equals(maxString)){
                index = i; 
                break;
            }
        }
        for(int i = index; i < maxlist.size(); i++){
            System.out.println("Max Character: = " + maxlist.get(i) );
        }
        System.out.println("Repeat Count = " + max);
    }

}
