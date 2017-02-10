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

	//U_DAY_READ建表语句
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
		//拼接分区sql
		sql.append("partition by range (ORG_NO) \n");
		sql.append("subpartition by range (DATA_DATE) \n");
		sql.append("(\n");
		//首先遍历供电所
		List<String> orgNos = ReadFile.readFileByLines("D://ORG_NO.txt");
		//获取开始日期和结束日期
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
			//将这个供电所从20150101-20171231之内按每天再分区
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
			//一个供电所的分区结束
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
				+"  is '测量点日冻结电能示值';\n"
				+"-- Add comments to the columns \n"
				+"comment on column E_MP_DAY_READ.id\n"
				+"  is '测量点编号';\n"
				+"comment on column E_MP_DAY_READ.data_date\n"
				+"  is '数据日期';\n"
				+"comment on column E_MP_DAY_READ.org_no\n"
				+"  is '地市供电单位编号';\n"
				+"comment on column E_MP_DAY_READ.col_time\n"
				+"  is '终端抄表时间';\n"
				+"comment on column E_MP_DAY_READ.pap_r\n"
				+" is '测量点日冻结电能示值';\n"
				+"comment on column E_MP_DAY_READ.pap_r1\n"
				+"  is '正向有功费率1电能示值';\n"
				+"comment on column E_MP_DAY_READ.pap_r2\n"
				+"  is '正向有功费率2电能示值';\n"
				+"comment on column E_MP_DAY_READ.pap_r4\n"
				+"  is '正向有功费率4电能示值';\n"
				+"comment on column E_MP_DAY_READ.pap_r3\n"
				+"  is '正向有功费率3电能示值';\n"
				+"comment on column E_MP_DAY_READ.prp_r\n"
				+"  is '正向无功总电能示值';\n"
				+"comment on column E_MP_DAY_READ.prp_r1\n"
				+"  is '正向无功费率1示值';\n"
				+"comment on column E_MP_DAY_READ.prp_r2\n"
				+"  is '正向无功费率2示值';\n"
				+"comment on column E_MP_DAY_READ.prp_r3\n"
				+"  is '正向无功费率3示值';\n"
				+"comment on column E_MP_DAY_READ.prp_r4\n"
				+"  is '正向无功费率4示值';\n"
				+"comment on column E_MP_DAY_READ.rap_r\n"
				+"  is '反向有功总能量示值';\n"
				+"comment on column E_MP_DAY_READ.rap_r1\n"
				+"  is '反向有功费率1示值';\n"
				+"comment on column E_MP_DAY_READ.rap_r2\n"
				+"  is '反向有功费率2示值';\n"
				+"comment on column E_MP_DAY_READ.rap_r3\n"
				+"  is '反向有功费率3示值';\n"
				+"comment on column E_MP_DAY_READ.rap_r4\n"
				+"  is '反向有功费率4示值';\n"
				+"comment on column E_MP_DAY_READ.rrp_r\n"
				+"  is '反向无功总能示值';\n"
				+"comment on column E_MP_DAY_READ.rrp_r1\n"
				+"  is '反向无功费率1示值';\n"
				+"comment on column E_MP_DAY_READ.rrp_r2\n"
				+"  is '反向无功费率2示值';\n"
				+"comment on column E_MP_DAY_READ.rrp_r3\n"
				+"  is '反向无功费率3示值';\n"
				+"comment on column E_MP_DAY_READ.rrp_r4\n"
				+"  is '反向无功费率4示值';\n"
				+"comment on column E_MP_DAY_READ.rrp_r5\n"
				+"  is '反向无功费率5示值';");
		//-------------------------
		writeFile(sql,filePath);
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
	
	public static void writeFile(StringBuffer content,String filePath){
		//生成文件
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
