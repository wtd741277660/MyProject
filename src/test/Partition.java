package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Partition {

	private static final String TABLESPACE = "test";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		U_DAY_READ();
	}

	//U_DAY_READ�������
	public static void U_DAY_READ(){
		String filePath = "D://U_DAY_READ.sql";
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();	
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sql = new StringBuffer();
		sql.append("create table E_MP_DAY_READ\n"
				+"(\n"
				+"id            NUMBER(16) not null,\n"
				+"data_date     DATE not null,\n"
				+"org_no        VARCHAR2(16) not null,\n"
				+"col_time      DATE,\n"
				+"pap_r         NUMBER(11,4),\n"
				+"pap_r1        NUMBER(11,4),\n"
				+"pap_r2        NUMBER(11,4),\n"
				+"pap_r3        NUMBER(11,4),\n"
				+"pap_r4        NUMBER(11,4),\n"
				+"prp_r         NUMBER(11,4),\n"
				+"prp_r1        NUMBER(11,4),\n"
				+"prp_r2        NUMBER(11,4),\n"
				+"prp_r3        NUMBER(11,4),\n"
				+"prp_r4        NUMBER(11,4),\n"
				+"rap_r         NUMBER(11,4),\n"
				+"rap_r1        NUMBER(11,4),\n"
				+"rap_r2        NUMBER(11,4),\n"
				+"rap_r3        NUMBER(11,4),\n"
				+"rap_r4        NUMBER(11,4),\n"
				+"rrp_r         NUMBER(11,4),\n"
				+"rrp_r1        NUMBER(11,4),\n"
				+"rrp_r2        NUMBER(11,4),\n"
				+"rrp_r3        NUMBER(11,4),\n"
				+"rrp_r4        NUMBER(11,4)\n"
				+")\n");
		//ƴ�ӷ���sql
		sql.append("partition by range (ORG_NO) \n");
		sql.append("subpartition by range (DATA_DATE) \n");
		sql.append("(\n");
		//���ȱ���������
		List<String> orgNos = ReadFile.readFileByLines("D://ORG_NO.txt");
		//��ȡ��ʼ���ںͽ�������
		Calendar start = getCalendar("2017-01-01");
		Calendar end = getCalendar("2017-12-31");
		Calendar temp = (Calendar) start.clone();
		//---------------------
		writeFile(sql,filePath);
		sql = new StringBuffer();
		for(int i = 0;i < orgNos.size();i++){
			System.out.println(orgNos.get(i) + "---" + (i+1) + "/" + orgNos.size());
			int num = Integer.parseInt(orgNos.get(i)) + 1;
			sql.append(" partition DATA" + orgNos.get(i) + " values less than ('" + num + "')\n"
				    +"tablespace " + TABLESPACE + "\n"
				    +"pctfree 10\n"
				    +"initrans 1\n"
				    +"maxtrans 255\n");
			sql.append("(");
			//�������������20150101-20171231֮�ڰ�ÿ���ٷ���
			int count = 0;
			for(;(temp.before(end) || temp.equals(end));temp.add(Calendar.DAY_OF_YEAR, 1)){
				Calendar tomorrow = (Calendar) temp.clone();
				tomorrow.add(Calendar.DAY_OF_YEAR, 1);
				String s = sdf1.format(tomorrow.getTime());
				sql.append("subpartition DATA" + orgNos.get(i) + "_PART" + sdf.format(temp.getTime()) + 
						" values less than (TO_DATE('" + s + "', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN')) " +
						"tablespace " + TABLESPACE);
				if (temp.equals(end)) {
					sql.append("\n");
				}else{
					sql.append(",\n");
				}
				//--------------------
//				if (count < 100) {
//					count++;
//				}else{
//					writeFile(sql,filePath);
//					sql = new StringBuffer();
//					count = 0;
//				}
			}
			temp = (Calendar) start.clone();
			//һ���������ķ�������
			if (i < orgNos.size() - 1) {
				sql.append("),\n");
			}else{
				sql.append(")\n");
			}
			//-------------------------
			writeFile(sql,filePath);
			sql = new StringBuffer();
		}
		sql.append(");\n");
		sql.append("-- Add comments to the table \n"
				+"comment on table E_MP_DAY_READ\n"
				+"  is '�������ն������ʾֵ';\n"
				+"-- Add comments to the columns \n"
				+"comment on column E_MP_DAY_READ.id\n"
				+"  is '��������';\n"
				+"comment on column E_MP_DAY_READ.data_date\n"
				+"  is '��������';\n"
				+"comment on column E_MP_DAY_READ.org_no\n"
				+"  is '���й��絥λ���';\n"
				+"comment on column E_MP_DAY_READ.col_time\n"
				+"  is '�ն˳���ʱ��';\n"
				+"comment on column E_MP_DAY_READ.pap_r\n"
				+" is '�������ն������ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.pap_r1\n"
				+"  is '�����й�����1����ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.pap_r2\n"
				+"  is '�����й�����2����ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.pap_r4\n"
				+"  is '�����й�����4����ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.pap_r3\n"
				+"  is '�����й�����3����ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.prp_r\n"
				+"  is '�����޹��ܵ���ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.prp_r1\n"
				+"  is '�����޹�����1ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.prp_r2\n"
				+"  is '�����޹�����2ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.prp_r3\n"
				+"  is '�����޹�����3ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.prp_r4\n"
				+"  is '�����޹�����4ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rap_r\n"
				+"  is '�����й�������ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rap_r1\n"
				+"  is '�����й�����1ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rap_r2\n"
				+"  is '�����й�����2ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rap_r3\n"
				+"  is '�����й�����3ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rap_r4\n"
				+"  is '�����й�����4ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rrp_r\n"
				+"  is '�����޹�����ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rrp_r1\n"
				+"  is '�����޹�����1ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rrp_r2\n"
				+"  is '�����޹�����2ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rrp_r3\n"
				+"  is '�����޹�����3ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rrp_r4\n"
				+"  is '�����޹�����4ʾֵ';\n"
				+"comment on column E_MP_DAY_READ.rrp_r5\n"
				+"  is '�����޹�����5ʾֵ';");
		//-------------------------
		writeFile(sql,filePath);
	}
	
	//���ַ�������ת��Calendar
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
	
	public static void writeFile(StringBuffer content,String filePath){
		//�����ļ�
		try {
			writeByOutputStreamWrite(filePath,content.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeByOutputStreamWrite(String filePath,String content) throws IOException {
		OutputStreamWriter os = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath,true);
			os = new OutputStreamWriter(fos, "UTF-8");
			os.write(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (os != null) {
				os.close();
				os = null;
			}
			if (fos != null) {
				fos.close();
				fos = null;
			}
		}
	}
}
