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
 * @description 针对条形码的帮助类
 * @使用方法 类名调用
 * @方法一：getNumStringByBarcode() 扫描条形码得到对应的数字字符串
 * @方法二：（可以不用，方法三取代）getBarcodeByNumString(String numStr) 通过一串数字字符串得到条形码(swing显示)
 * @author 
 * @区分键盘输入还是扫描枪输入 思路：扫描输入的间隔比较均匀且时间很短（小于50毫秒）， 而手动输入间隔至少在100毫秒以上。
 * @方法三：createBarcodePictureByString(String numStr,String path)
 */
public class BarcodeUtils {

	static long timeMillis1;
	static long timeMillis2;

	// 求间隔时间时用到的标志位，每进来一次keyPressed方法，把flag置反，并且记录当前系统的时间毫秒值前后相减，得到间隔时间
	static boolean IntervalFlag = false;

	static int count = 0;// 进入一次keyPressed()方法加1，第一次进入是不算时间间隔，count=2时开始算，另外可以用来判断是否是连续多次输入
	static StringBuilder sb = null;
	static String str = "";
	static long timeInterval = 0;
	static String str2 = "";
							
	static boolean scanningGunFlag = false; // 是否是 扫描枪输入
	static boolean kpressedFlag3 = false;// 是否有键盘输入
	static boolean keyboardAndScanFlag4 = false;// 判断是不是 先有键盘输入 再有扫描输入

	/**
	 * 
	 * @param numStr 条形码字符串
	 * @param path 图片存储路径，例如"f:/"
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
	 * 此方法的作用是更具条形码字符串生成一个条形码(swing界面显示出来)
	 * 
	 * @param numStr
	 *            条形码数字对象的字符串
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
	 * 扫描条形码的到数字字符串
	 * 
	 * @return 条形码对应的数字字符串
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
							System.out.println("条形码：" + str);
							
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
						// 首先是键盘输入再有扫描输入
						keyboardAndScanFlag4 = true;
					}
				} else {
					if (e.getKeyCode() >= KeyEvent.VK_0
							&& e.getKeyCode() <= KeyEvent.VK_9
							&& scanningGunFlag == true) {
						// 连续扫描
						str2 = "" + e.getKeyChar();
					}
					if (e.getKeyCode() >= KeyEvent.VK_0
							&& e.getKeyCode() <= KeyEvent.VK_9
							&& keyboardAndScanFlag4 == true) {
						// 先按下键盘再扫描
						str2 = "" + e.getKeyChar();
						// System.out.println("s--："+str2);
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

	//////一下方法为createBarcodePictureByString（）需要用到的方法--//
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

