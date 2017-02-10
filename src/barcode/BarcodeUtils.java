package barcode;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.swing.JFrame;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code39Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;

/**
 * @description ���������İ�����
 * @ʹ�÷��� ��������
 * @����һ��getNumStringByBarcode() ɨ��������õ���Ӧ�������ַ���
 * @�������������Բ��ã�������ȡ����getBarcodeByNumString(String numStr) ͨ��һ�������ַ����õ�������(swing��ʾ)
 * @author 
 * @���ּ������뻹��ɨ��ǹ���� ˼·��ɨ������ļ���ȽϾ�����ʱ��̣ܶ�С��50���룩�� ���ֶ�������������100�������ϡ�
 * @��������createBarcodePictureByString(String numStr,String path)
 */
public class BarcodeUtils {

	static long timeMillis1;
	static long timeMillis2;

	// ����ʱ��ʱ�õ��ı�־λ��ÿ����һ��keyPressed��������flag�÷������Ҽ�¼��ǰϵͳ��ʱ�����ֵǰ��������õ����ʱ��
	static boolean IntervalFlag = false;

	static int count = 0;// ����һ��keyPressed()������1����һ�ν����ǲ���ʱ������count=2ʱ��ʼ�㣬������������ж��Ƿ��������������
	static StringBuilder sb = null;
	static String str = "";
	static long timeInterval = 0;
	static String str2 = "";
							
	static boolean scanningGunFlag = false; // �Ƿ��� ɨ��ǹ����
	static boolean kpressedFlag3 = false;// �Ƿ��м�������
	static boolean keyboardAndScanFlag4 = false;// �ж��ǲ��� ���м������� ����ɨ������

	/**
	 * 
	 * @param numStr �������ַ���
	 * @param path ͼƬ�洢·��������"f:/"
	 */
	public static void createBarcodePictureByString(String numStr,String path) {
		try {
			JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(),
					WidthCodedPainter.getInstance(),
					EAN13TextPainter.getInstance());
			//BufferedImage localBufferedImage = localJBarcode.createBarcode(numStr);
			//saveToGIF(localBufferedImage, "aaa.gif");
			
			 localJBarcode.setEncoder(Code39Encoder.getInstance());
			 localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
			 localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
			 localJBarcode.setShowCheckDigit(false); //xx str = "JBARCODE-39";
			 BufferedImage localBufferedImage = localJBarcode.createBarcode(numStr);
			 saveToPNG(localBufferedImage, numStr+".png",path);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		createBarcodePictureByString("123456","F://");
		getBarcodeByNumString("123456");
	}

	/**
	 * �˷����������Ǹ����������ַ�������һ��������(swing������ʾ����)
	 * 
	 * @param numStr
	 *            ���������ֶ�����ַ���
	 */
	@SuppressWarnings("static-access")
	public static void getBarcodeByNumString(String numStr) {
		JFrame frame = new JFrame("getBarcodeByNumString");
		BarcodeUtils u = new BarcodeUtils();
		Component contents = u.usingBarbecueAsSwingComponent(numStr);
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * ɨ��������ĵ������ַ���
	 * 
	 * @return �������Ӧ�������ַ���
	 */
	public static String getNumStringByBarcode() {

		JFrame frame = new JFrame("getNumStringByBarcode");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		frame.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				count++;
				if (IntervalFlag == false) {
					timeMillis1 = System.currentTimeMillis();
					// System.out.println(timeMillis1+":t1");
					IntervalFlag = true;
				} else {
					timeMillis2 = System.currentTimeMillis();
					// System.out.println(timeMillis2+":t2");
					IntervalFlag = false;
				}
				if (count > 1) {
					timeInterval = Math.abs(timeMillis2 - timeMillis1);
				}
				// System.out.print(timeInterval+"---");
				if (timeInterval < 50) {
					count++;
					if (sb == null)
						sb = new StringBuilder();
					if (e.getKeyCode() >= KeyEvent.VK_0
							&& e.getKeyCode() <= KeyEvent.VK_9) {
						// System.out.println(e.getKeyCode()+":"+e.getKeyChar());
						sb.append(e.getKeyChar());
						kpressedFlag3 = true;
					}
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						if (scanningGunFlag == true
								|| keyboardAndScanFlag4 == true) {
							str = str2 + sb.toString();
							str2 = "";
						} else {
							str = sb.toString();
						}
						sb = null;
						if (str.length() >= 8) {
							System.out.println("�����룺" + str);
							
						} else {
							str = "";
						}
						if (count >= 8) {
							scanningGunFlag = true;
							count = 0;
						}
					}
					// System.out.println("str-:"+str);
					if (kpressedFlag3 == true && scanningGunFlag == false) {
						// �����Ǽ�����������ɨ������
						keyboardAndScanFlag4 = true;
					}
				} else {
					if (e.getKeyCode() >= KeyEvent.VK_0
							&& e.getKeyCode() <= KeyEvent.VK_9
							&& scanningGunFlag == true) {
						// ����ɨ��
						str2 = "" + e.getKeyChar();
					}
					if (e.getKeyCode() >= KeyEvent.VK_0
							&& e.getKeyCode() <= KeyEvent.VK_9
							&& keyboardAndScanFlag4 == true) {
						// �Ȱ��¼�����ɨ��
						str2 = "" + e.getKeyChar();
						// System.out.println("s--��"+str2);
						scanningGunFlag = false;
					}
					sb = null;
				}

			}

		});

		frame.pack();
		frame.setVisible(true);
		return str;

	}

	/**
	 * @param num
	 * @return Component
	 */
	private static Component usingBarbecueAsSwingComponent(String num) {
		Barcode barcode = null;
		try {
			barcode = BarcodeFactory.createCode128B(num);

			barcode.setBarHeight(50);
			barcode.setBarWidth(1);
		} catch (BarcodeException e) {
		}
		return barcode;
	}

	//////һ�·���ΪcreateBarcodePictureByString������Ҫ�õ��ķ���--//
	@SuppressWarnings("unused")
	private static void saveToJPEG(BufferedImage paramBufferedImage,
			String paramString,String path) {
		saveToFile(paramBufferedImage, paramString, "jpeg",path);
	}

	private static void saveToPNG(BufferedImage paramBufferedImage,
			String paramString,String path) {
		saveToFile(paramBufferedImage, paramString, "png",path);
	}

	@SuppressWarnings("unused")
	private static void saveToGIF(BufferedImage paramBufferedImage,
			String paramString,String path) {
		saveToFile(paramBufferedImage, paramString, "gif",path);
	}

	private static void saveToFile(BufferedImage paramBufferedImage,
			String paramString1, String paramString2,String path) {
		try {
			FileOutputStream localFileOutputStream = new FileOutputStream("f:/"
					+ paramString1);
			ImageUtil.encodeAndWrite(paramBufferedImage, paramString2,
					localFileOutputStream, 96, 96);
			localFileOutputStream.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	//------/////
}

