package test;

import java.security.MessageDigest;

public class jiami {
	public static void main(String[] args) {
		String str = jiami("w");
		System.out.println(str);
	}
	
	public static String jiami(String password){
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' }; // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(password.getBytes());
			byte[] tmp = digest.digest();
			char str[] = new char[16 * 2];
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
            for (int i = 0; i < 16; i++)
            { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
                // ת���� 16 �����ַ���ת��
                byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��, 
                // >>> Ϊ�߼����ƣ�������λһ������
                str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
            }
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
