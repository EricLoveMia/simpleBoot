package cn.eric.h2.util;

import cn.eric.h2.cache.CacheEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: FileUtil
 * @Description: 文件操作类
 * @company lsj
 * @date 2019/8/13 9:49
 **/
@Component
public class FileUtil {

    public static final String basePath = "cache/";

    public static final String fileReadIndexPath = "file_read/";

    private static final String line = "\t\n";

    private static final String[] preArray = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    static Map<String, PathEntity> pathMaps = new ConcurrentHashMap<>(256);

    static Map<String, IndexEntity> indexMap = new ConcurrentHashMap<>(102400);

    static {
        for (int i = 0; i < preArray.length; i++) {
            pathMaps.put(preArray[i], new PathEntity(preArray[i] + "-main.log"));
        }
        // 载入所有文件生成index
        File file = new File(basePath);
        File[] tempList = file.listFiles();
        int number;
        BufferedReader reader = null;
        try {
            for (File log : tempList) {
                number = 1;
                if (log.isFile()) {
                    //
                    try {
                        reader = new BufferedReader(new FileReader(basePath + log.getName()));
                        String contentLine;
                        while ((contentLine = reader.readLine()) != null) {
                            // System.out.println(contentLine);
                            String[] split = contentLine.split(":");
                            indexMap.put(split[0], new IndexEntity(split[0], log.getName(), number++));
                            pathMaps.get(split[0].substring(0,1)).getCount().incrementAndGet();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private FileUtil() {

    }

    /** 新加数据 */
    public static void writeToFile(Map<String, Object> map,String keyPre) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileWriter fileWriter = new FileWriter(basePath + pathMaps.get(keyPre).path);
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                stringBuffer.append(next.getKey() + ":" + JSONObject.toJSONString(next.getValue())).append(line);
                // TODO
                indexMap.put(next.getKey(), new IndexEntity(next.getKey(), pathMaps.get(keyPre).path, pathMaps.get(keyPre).count.incrementAndGet()));
            }
            fileWriter.write(stringBuffer.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 追加数据 */
    public static void addToFile(Map<String, CacheEntity> map, String keyPre) {

        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileWriter fileWriter = new FileWriter(basePath + pathMaps.get(keyPre).path,true);
            Set<Map.Entry<String, CacheEntity>> entries = map.entrySet();
            Iterator<Map.Entry<String, CacheEntity>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, CacheEntity> next = iterator.next();
                stringBuffer.append(next.getKey() + ":" + JSONObject.toJSONString(next.getValue())).append(line);
                indexMap.put(next.getKey(), new IndexEntity(next.getKey(), pathMaps.get(keyPre).path, pathMaps.get(keyPre).count.incrementAndGet()));
            }
            fileWriter.write(stringBuffer.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void addToFile(String key, Object value) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileWriter fileWriter = new FileWriter(basePath + pathMaps.get(key.substring(0, 1)).path, true);
            stringBuffer.append(key + ":" + JSONObject.toJSONString(value)).append(line);
            // 加入索引
            indexMap.put(key, new IndexEntity(key, pathMaps.get(key.substring(0, 1)).path, pathMaps.get(key.substring(0, 1)).count.incrementAndGet()));

            fileWriter.write(stringBuffer.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String path) {
        StringBuffer stringBuffer = new StringBuffer();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(basePath + path);
            char[] buf = new char[1024];
            int num;
            while ((num = fileReader.read(buf)) != -1) {
                stringBuffer.append(new String(buf, 0, num));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }

    public synchronized static String getByFile(String key) {
        String string = null;
        BufferedReader reader = null;
        IndexEntity indexEntity = indexMap.get(key);
        if (indexEntity != null) {
            String filePre = indexEntity.getFilePre();
            long line = indexEntity.getLine();
            try {
                reader = new BufferedReader(new FileReader(basePath + filePre));
                for (long i = 0; i < line - 1; i++) {
                    reader.readLine();
                }
                string = reader.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return string;
    }

    public synchronized static boolean deleteByKey(String key) {
        // 删除键值
        // StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader;
        IndexEntity indexEntity = indexMap.get(key);

        OutputStreamWriter osw;

        BufferedWriter writer;
        if (indexEntity != null) {
            try {
                // 拿到文件
                String filePre = indexEntity.getFilePre();
                long line = indexEntity.getLine();
                FileOutputStream fos=new FileOutputStream(new File(basePath + filePre + "temp"));
                System.out.println(basePath + filePre);
                reader = new BufferedReader(new FileReader(basePath + filePre));
                osw= new OutputStreamWriter(fos, "UTF-8");
                writer = new BufferedWriter(osw);
                String contentLine;
                while((contentLine = reader.readLine()) != null){
                    if(--line == 0) {
                        // 那一行删除
                        writer.write("--:--\t\n");
                        continue;
                    }else {
                        System.out.println(contentLine);
                        writer.write(contentLine + "\t\n");
                    }
                }
                //刷新流

                writer.flush();
                //关闭资源
                writer.close();
                osw.close();
                fos.close();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 删除原有的文件
                delFile(new File(basePath + filePre));
                // 重命名临时文件
                File oldName = new File(basePath + filePre + "temp");
                File newName = new File(basePath + filePre);
                if(oldName.renameTo(newName)) {
                    System.out.println("已重命名");
                } else {
                    System.out.println("Error");
                }

                // 索引删除
                indexMap.remove(key);

                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

    // 文件内容的总行数
    public static long getTotalLines(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        long lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
        }
        reader.close();
        in.close();
        return lines;
    }

    // 读取文件指定行
    public static String readLineNumber(File sourceFile, int lineNumber) throws IOException {
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        String s = null;
        int line = 1;
        if (lineNumber < 0 || lineNumber > getTotalLines(sourceFile)) {
            System.out.println("传入行数" + lineNumber + "有误，不在范围之内");
        } else {
            reader.setLineNumber(lineNumber);
            int i = reader.getLineNumber();
            while (reader.readLine() != null) {
                line++;
                if (i == line) {
                    s = reader.readLine();
                    break;
                }
            }
        }
        reader.close();
        in.close();
        return s;
    }

    // 读取文件指定行内容
    public static String readLineNumberString(File sourceFile, int lineNumber) throws IOException {
        String s = null;
        if (lineNumber < 0 || lineNumber > getTotalLines(sourceFile)) {
            System.out.println("传入行数" + lineNumber + "有误，不在范围之内");
        } else {
            InputStreamReader inputReader = null;
            BufferedReader bufferReader = null;
            // OutputStream outputStream = null;
            try {
                InputStream inputStream = new FileInputStream(sourceFile);
                inputReader = new InputStreamReader(inputStream, "UTF-8");
                bufferReader = new BufferedReader(inputReader);

                // 读取一行
                String lineContent = null;
                StringBuilder strBuffer = new StringBuilder();
                int currentReadLine = 0;
                while ((lineContent = bufferReader.readLine()) != null) {
                    currentReadLine++;
                    if (currentReadLine == lineNumber) {
                        strBuffer.append(lineContent);
                        break;
                    }
                }
                s = strBuffer.toString();
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            } finally {
                bufferReader.close();
                inputReader.close();
                // outputStream.close();
                //IOCloseUtil.closeAll(outputStream, bufferReader, inputReader);
            }
        }
        return s;
    }


    // 读取文件指定行到结尾
    public static String readLineNumberStringToEnd(File sourceFile, long lineNumber,long endLine) throws IOException {
        String s = null;
        if (lineNumber < 0 || lineNumber > endLine) {
            System.out.println("传入行数" + lineNumber + "有误，不在范围之内");
        } else {
            InputStreamReader inputReader = null;
            BufferedReader bufferReader = null;
            // OutputStream outputStream = null;
            try {
                InputStream inputStream = new FileInputStream(sourceFile);
                inputReader = new InputStreamReader(inputStream, "UTF-8");
                bufferReader = new BufferedReader(inputReader);

                // 读取一行
                String lineContent = null;
                StringBuilder strBuffer = new StringBuilder();
                int currentReadLine = 0;
                while ((lineContent = bufferReader.readLine()) != null) {
                    if(currentReadLine < lineNumber){
                        currentReadLine++;
                        continue;
                    }
                    currentReadLine++;
                    strBuffer.append(lineContent);
                    if (currentReadLine >= endLine) {
                        break;
                    }
                }
                s = strBuffer.toString();
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            } finally {
                bufferReader.close();
                inputReader.close();
                // outputStream.close();
                //IOCloseUtil.closeAll(outputStream, bufferReader, inputReader);
            }
        }
        return s;
    }

    public static void main(String[] args) {
//        Map map = new HashMap(32);
//        for (int i = 0; i < 10; i++) {
//            map.put("r" + i, "rv" + i);
//        }
//        FileUtil.addToFile(map,"r");
//        FileUtil.addToFile(map);

//        String s = FileUtil.readFromFile("main.log");
////        System.out.println(s);
//        String[] split = s.split("\t\n");
//        List<String> list = new ArrayList<>(Arrays.asList(split));
//        System.out.println(list.size());
//        for (String s1 : list) {
//            System.out.println(s1);
//        }

//        String k1 = FileUtil.getByFile("h2");
//        System.out.println(k1);
//
//        boolean d3 = FileUtil.deleteByKey("r2");
//        System.out.println(d3);

//
//        k1 = FileUtil.getByFile("h30");
//        System.out.println(k1);

        File sourceFile = new File("D:\\data\\file\\logs\\2020-06-04.txt");
        try {
            long lineNum = getTotalLines(sourceFile);
            // 获取文件的内容的总行数
            System.out.println("总行数=" + lineNum);
            // 读取指定的行
            long lineNumber = 0;
            // String content = readLineNumberString(sourceFile, lineNumber);
            String content = readLineNumberStringToEnd(sourceFile, lineNumber,lineNum);
            System.out.println("第" + lineNumber + "行的内容是= \n" + content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class PathEntity {
        private String path;
        private AtomicLong count;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public AtomicLong getCount() {
            return count;
        }

        public void setCount(AtomicLong count) {
            this.count = count;
        }

        public PathEntity(String path) {
            this.path = path;
            count = new AtomicLong(0);

        }
    }


    private static class IndexEntity {

        private String key;
        private String filePre;

        private long line;

        public IndexEntity(String key, String filePre, long line) {
            this.key = key;
            this.filePre = filePre;
            this.line = line;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getFilePre() {
            return filePre;
        }

        public void setFilePre(String filePre) {
            this.filePre = filePre;
        }

        public long getLine() {
            return line;
        }

        public void setLine(long line) {
            this.line = line;
        }
    }

}
