package test;
/**
 * dom4j解析xml
 */
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.dom4j.Document;

public class Dom4jTest {

	public static void main(String[] args) throws DocumentException {
		//System.out.println(getElement("e:/b.xml"));
		//System.out.println(addElement("e:/b.xml","sex","men"));
		System.out.println(addElement("e:/b.xml","age","20"));
	}
	/**
	 * 创建解析xml文档中得到name节点中的值的方法
	 * @param url 传入xml文档路径
	 * @return	元素节点得到节点内的值并返回
	 * @throws  DocumentException
	 */
	public static String getElement(String url) throws DocumentException{
		SAXReader reader = new SAXReader();		//创建解析器
		Document document = reader.read(url);		//得到ducument文档对象
		Element el = document.getRootElement();			//得到文档根节点
		Element name = el.element("name");			//通过根节点得到包含的子节点
		return name.getText();						//通过元素节点得到节点内的值并返回
	}
	/**
	 * 通过dom4j操作xml创建节点
	 * @param url	操作的文件路径
	 * @param node 元素节点名称
	 * @param val	元素节点值
	 * @return 如果创建成功则返回true 在执行过程中发生了异常则创建失败返回false
	 */
	public static String addElement(String url, String node, String val){
		SAXReader reader = new SAXReader();		//创建解析器
		XMLWriter xmlWriter = null;			//创建回写输出流
		try {
			Document document = reader.read(url);		//得到ducument文档对象
			Element root = document.getRootElement();		//得到文档根节点
			Element node1 = root.addElement(node);
			node1.setText(val);
			OutputFormat format = OutputFormat.createPrettyPrint();		//调用内置的输出格式
			xmlWriter = new XMLWriter(new FileOutputStream(new File(url)), format);
			xmlWriter.write(document);		//调用回写流写入文档
			return "添加成功";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "添加失败";
		}finally{
			if(xmlWriter!=null){
				try {
					xmlWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
}
