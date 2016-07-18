package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 复制工具类
 * @author Michael
 *
 */
public class CopyUtil {
	/**
	 * 复制文本文件
	 * @param filePath	指定被复制的文件路径
	 * @param newPath	指定复制到那个路径
	 * @return	复制成功则返回操作成功，否则返回操作失败并附上失败原因
	 */
	public static String copyTextFile(String filePath, String newPath){
		File oldFile = new File(filePath);
		FileReader fileReader = null;
		FileWriter newFile = null;
		if(oldFile.exists()){
			if(oldFile.isFile()){
				try {
					fileReader = new FileReader(oldFile);
					char[] temp = new char[(int)oldFile.length()];
					while(fileReader.read(temp)!=-1){
						newFile = new FileWriter(new File(newPath));
						newFile.write(temp);
						newFile.flush();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "操作失败，发生了异常！";
				}finally{
					if(fileReader!=null){
						try {
							fileReader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(newFile!=null){
						try {
							newFile.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				return "操作失败，该抽象路径映射不不是一个文件，请确认！";
			}
		}else{
			return "操作失败，该抽象路径映射不存在，请确认！";
		}
		return "操作成功！";
	}
	/**
	 * 复制文件（可复制二进制文件）
	 * @param filePath	指定被复制的文件路径
	 * @param newPath	指定复制到那个路径
	 * @return		复制成功则返回操作成功，否则返回操作失败并附上失败原因
	 */
	public static String copyFile(String filePath, String newPath){
		File oldFile = new File(filePath);
		FileInputStream in = null;
		FileOutputStream newFile = null;
		if(oldFile.exists()){
			if(oldFile.isFile()){
				try {
					in = new FileInputStream(oldFile);
					byte[] temp = new byte[(int)oldFile.length()];
					while(in.read(temp)!=-1){
						newFile = new FileOutputStream(new File(newPath));
						newFile.write(temp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "操作失败，发生了异常！";
				}finally{
					if(in!=null){
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(newFile!=null){
						try {
							newFile.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				return "操作失败，该抽象路径映射不不是一个文件，请确认！";
			}
		}else{
			return "操作失败，该抽象路径映射不存在，请确认！";
		}
		return "操作成功！";
	}
}
