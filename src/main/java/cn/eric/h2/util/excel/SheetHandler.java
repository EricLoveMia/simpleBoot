package cn.eric.h2.util.excel;

/**
 * @ClassName SheetHandler
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2019/11/27
 * @Version V1.0
 **/
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedHashMap;

public class SheetHandler  extends DefaultHandler {

    private SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;
    private String cellPosition;
    private LinkedHashMap<String, String> rowContents=new LinkedHashMap<String, String>();

    public LinkedHashMap<String, String> getRowContents() {
        return rowContents;
    }

    public void setRowContents(LinkedHashMap<String, String> rowContents) {
        this.rowContents = rowContents;
    }

    public SheetHandler(SharedStringsTable sst) {
        this.sst = sst;
    }

    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        if(name.equals("c")) {
            //   System.out.print(attributes.getValue("r") + " - ");
            cellPosition=attributes.getValue("r");
            String cellType = attributes.getValue("t");
            if(cellType == null){
                cellType = "";
            }
            if(cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
        }
        // 清楚缓存内容
        lastContents = "";
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if("B2".equals(cellPosition)){
            System.out.println(cellPosition);
        }
        if(nextIsString) {
            int idx = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            nextIsString = false;
        }

        if(name.equals("v")) {
//            System.out.println("lastContents:"+cellPosition+";"+lastContents);
            // 数据读取结束后，将单元格坐标,内容存入map中 不保存第一行数据
            if(!(cellPosition.length()==2)||(cellPosition.length()==2&&!"1".equals(cellPosition.substring(1)))){
                rowContents.put(cellPosition, lastContents);
            }
        }
        if(name.equals("c")){
            // 数据读取结束后，将单元格坐标,内容存入map中 这里是为了解决空数据不存储的问题
            // 不保存第一行数据
            if(!(cellPosition.length()==2)||(cellPosition.length()==2&&!"1".equals(cellPosition.substring(1)))){
                if(rowContents.get(cellPosition) == null) {
                    rowContents.put(cellPosition, lastContents);
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

       lastContents += new String(ch, start, length);

    }
}
