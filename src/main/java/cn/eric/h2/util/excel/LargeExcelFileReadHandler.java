package cn.eric.h2.util.excel;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @ClassName LargeExcelFileReadUtil
 * @Description: 2007 excel 解析
 * @Author YCKJ2725
 * @Date 2019/11/27
 * @Version V1.0
 **/
//@Component
//@Scope("prototype")
public class LargeExcelFileReadHandler {

    private LinkedHashMap<String, String> rowContents = new LinkedHashMap<String, String>();
    private SheetHandler sheetHandler;

    public LinkedHashMap<String, String> getRowContents() {
        return rowContents;
    }
    public void setRowContents(LinkedHashMap<String, String> rowContents) {
        this.rowContents = rowContents;
    }

    public SheetHandler getSheetHandler() {
        return sheetHandler;
    }
    public void setSheetHandler(SheetHandler sheetHandler) {
        this.sheetHandler = sheetHandler;
    }
    //处理一个sheet
    public void processOneSheet(String filename) throws Exception {
        InputStream sheet2=null;
        OPCPackage pkg =null;
        try {
            pkg = OPCPackage.open(filename);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            sheet2 = r.getSheet("rId1");
            InputSource sheetSource = new InputSource(sheet2);
            parser.parse(sheetSource);
            setRowContents(sheetHandler.getRowContents());
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(pkg!=null){
                pkg.close();
            }
            if(sheet2!=null){
                sheet2.close();
            }
        }
    }

    //处理多个sheet
    public void processAllSheets(String filename) throws Exception {
        OPCPackage pkg =null;
        InputStream sheet=null;
        try{
            pkg=OPCPackage.open(filename);
            XSSFReader r = new XSSFReader( pkg );
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            Iterator<InputStream> sheets = r.getSheetsData();
            while(sheets.hasNext()) {
                System.out.println("Processing new sheet:\n");
                sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            if(pkg!=null){
                pkg.close();
            }
            if(sheet!=null){
                sheet.close();
            }
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "com.sun.org.apache.xerces.internal.parsers.SAXParser"
                );
        setSheetHandler(new SheetHandler(sst));
        ContentHandler handler = (ContentHandler) sheetHandler;
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    //测试
    public void test()throws Exception {
        Long time= System.currentTimeMillis();
        LargeExcelFileReadHandler example = new LargeExcelFileReadHandler();

        //example.processOneSheet("D:\\data\\file\\test\\69ecf574d6c4411c99f0bdf63162faf7\\10w\\3w1-1.xlsx");
        example.processOneSheet("D:\\data\\file\\smalltest-1.xlsx");
        Long endtime= System.currentTimeMillis();
        LinkedHashMap<String, String> map=example.getRowContents();
        Iterator<Entry<String, String>> it= map.entrySet().iterator();
        int count=0;
        String prePos="";
        while (it.hasNext()){
            Entry<String, String> entry=(Entry<String, String>)it.next();
            String pos=entry.getKey();
            if(!pos.substring(1).equals(prePos)){
                prePos=pos.substring(1);
                count++;
            }
            //System.out.println(pos+";"+entry.getValue());
        }
        System.out.println("解析数据"+count+"条;耗时"+(endtime-time)/1000+"秒");
    }

    //测试
    public void test2()throws Exception {
        Long time= System.currentTimeMillis();
        LargeExcelFileReadHandler example = new LargeExcelFileReadHandler();
        List<List<String>> batchRecordList = new ArrayList<>(1024);
        File file = new File("D:\\data\\file\\test\\69ecf574d6c4411c99f0bdf63162faf7\\10w\\3w1-1.xlsx");
        example.processOneSheet(file.getAbsolutePath());
        //example.processOneSheet("D:\\data\\file\\test\\69ecf574d6c4411c99f0bdf63162faf7\\10w\\3w1-1.xlsx");
        Long endtime= System.currentTimeMillis();
        LinkedHashMap<String, String> map=example.getRowContents();
        Iterator<Entry<String, String>> it= map.entrySet().iterator();

        String prePos="";
        boolean first = true;
        List<String> rowData = new ArrayList<>(6);
        while (it.hasNext()){
            Entry<String, String> entry=(Entry<String, String>)it.next();
            String pos=entry.getKey();
            if(!pos.substring(1).equals(prePos)){
                prePos=pos.substring(1);
                if(!first){
                    batchRecordList.add(rowData);
                    rowData = new ArrayList<>(6);
                }
                first = false;
            }
            rowData.add(entry.getValue());
            //System.out.println(pos+";"+entry.getValue());
        }

        // System.out.println(batchRecordList.size());
        System.out.println("解析数据"+batchRecordList.size()+"条;耗时"+(endtime-time)/1000+"秒");
    }

    public static void main(String[] args) {

    }
}
