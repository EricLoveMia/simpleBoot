package cn.eric.h2.util.excel;

/*import au.com.bytecode.opencsv.CSVReader;
import cn.cloudwalk.client.davinci.portal.api.file.FileService;
import cn.cloudwalk.client.davinci.portal.api.param.TemplateColumn;
import cn.cloudwalk.client.davinci.portal.api.person.PortalPersonService;
import cn.cloudwalk.client.davinci.portal.common.constant.BatchRespCodeConstant;
import cn.cloudwalk.client.davinci.portal.common.constant.PortalFileManagerConstant;
import cn.cloudwalk.client.davinci.portal.common.sync.PortalPersonSyncUtil;
import cn.cloudwalk.client.davinci.portal.person.batch.param.BatchImportUpdateParam;
import cn.cloudwalk.client.davinci.portal.person.batch.result.BatchConfigResult;
import cn.cloudwalk.client.davinci.portal.person.batch.result.BatchImportQueryResult;
import cn.cloudwalk.client.davinci.portal.person.batch.service.BatchConfigService;
import cn.cloudwalk.client.davinci.portal.person.batch.service.BatchDetailInsertBatchParam;
import cn.cloudwalk.client.davinci.portal.person.batch.service.BatchDetailService;
import cn.cloudwalk.client.davinci.portal.person.batch.service.BatchImportService;
import cn.cloudwalk.client.resource.dict.param.DictQueryParam;
import cn.cloudwalk.client.resource.dict.result.DictQueryResult;
import cn.cloudwalk.client.resource.dict.service.DictService;
import cn.cloudwalk.cloud.context.CloudwalkCallContext;
import cn.cloudwalk.cloud.context.CloudwalkCallContextBuilder;
import cn.cloudwalk.cloud.context.CloudwalkSessionContextHolder;
import cn.cloudwalk.cloud.context.CloudwalkSessionObject;
import cn.cloudwalk.cloud.exception.ServiceException;
import cn.cloudwalk.cloud.result.CloudwalkResult;
import cn.cloudwalk.cloud.utils.BeanCopyUtils;
import cn.cloudwalk.cloud.utils.CloudwalkDateUtils;
import cn.cloudwalk.data.davinci.portal.entity.batch.BatchImport;
import cn.cloudwalk.data.davinci.portal.entity.organization.Organization;
import cn.cloudwalk.data.davinci.portal.enums.ImportMode;
import cn.cloudwalk.data.davinci.portal.enums.ImportRecordStatus;
import cn.cloudwalk.data.davinci.portal.enums.ImportStatus;
import cn.cloudwalk.data.davinci.portal.enums.TemplateType;
import cn.cloudwalk.data.davinci.portal.exception.JobCommonException;
import cn.cloudwalk.data.davinci.portal.mapper.batch.BatchImportMapper;
import cn.cloudwalk.data.davinci.portal.mapper.organization.PortalOrganizationMapper;
import cn.cloudwalk.data.davinci.portal.utils.*;
import cn.cloudwalk.data.davinci.portal.utils.file.PathUtils;
import cn.cloudwalk.service.davinci.portal.common.constant.PersonBatchConstant;
import cn.cloudwalk.service.davinci.portal.service.person.batch.PortalBatchPersonHandler;
import cn.cloudwalk.service.davinci.portal.service.person.batch.ProcessContext;
import cn.cloudwalk.task.sdk.starter.job.AbstractJob;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;

import static cn.cloudwalk.client.davinci.portal.common.constant.BatchRespCodeConstant.*;*/

/**
 * 批量人员导入业务
 *
 * @author: yanghao@cloudwalk.cn
 * @date: 2019/3/25
 */
//@DisallowConcurrentExecution
public class BatchPersonImportTask
 //       extends AbstractJob
{

//    private static final Logger logger = LoggerFactory.getLogger(BatchPersonImportTask.class);
//    @Autowired
//    private BatchImportService batchImportService;
//
//    @Autowired
//    private BatchConfigService batchConfigService;
//
//    @Autowired
//    private PortalPersonService personService;
//
//    @Autowired
//    private MessageSource messageSource;
//
//    @Autowired
//    private BatchDetailService batchDetailService;
//
//    /**
//     * 批量导入的线程池
//     */
//    @Autowired
//    private ThreadPoolTaskExecutor taskExecutor;
//
//    /**
//     * 存储的根路径
//     */
//    @Value("${cloudwalk.storage.local.root-path}")
//    private String storageRootPath;
//
//    /**
//     * 每个批次的数量大小
//     */
//    @Value("${cloudwalk.person.import.batch.size: 1000}")
//    private Integer batchSize;
//
//    /**
//     * 图片文件的格式
//     */
//    @Value("${cloudwalk.person.import.batch.file.pattern:.+(\\.png|\\.jpg|\\.jpeg)$}")
//    private String filePattern;
//
//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private DictService dictService;
//
//    @Autowired
//    private CloudwalkSessionContextHolder sessionContextHolder;
//
//    /**
//     * 组织机构Mapper
//     */
//    @Resource
//    private PortalOrganizationMapper organizationMapper;
//
//    @Resource
//    private BatchImportMapper batchImportMapper;
//
//    @Resource
//    private PortalBatchPersonHandler portalBatchPersonHandler;
//
//    //@Resource
//    //private LargeExcelFileReadHandler largeExcelFileReadHandler;
//
//    /**
//     * 并发信号量宽带
//     */
//    private static final int BATCH_WIDTH = 3;
//    /**
//     * 人员导入并发信号量
//     */
//    private static final Semaphore BATCH_SEMAPHORE = new Semaphore(BATCH_WIDTH, true);
//
//    /**
//     * 处理源数据文件
//     */
//    private void process(ProcessContext processContext) throws Exception {
//        BatchConfigResult batchConfigResult = processContext.getBatchConfig();
//        //1.file导入
//        if (TemplateType.from(batchConfigResult.getType()) == TemplateType.FILE_NAME) {
//            //批量写入
//            readImageFile2DB(processContext.getUnzipFolder(), processContext);
//        }
//
//        //2.csv导入
//        else if (TemplateType.from(batchConfigResult.getType()) == TemplateType.CSV) {
//            List<File> csvFiles = processContext.getFileList();
//            //读取数据
//            for (File csv : csvFiles) {
//                //读取文件并发写入数据库
//                readCsvFile2DB(csv, processContext);
//            }
//        }
//
//        //3.excel导入
//        else if (TemplateType.from(batchConfigResult.getType()) == TemplateType.EXCEL) {
//            List<File> excelFiles = processContext.getFileList();
//            //读取数据
//            for (File excel : excelFiles) {
//                readExcelFile2DB(excel, processContext);
//            }
//        } else {
//            throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHCONFIG_TEMPLATE_TYPE_ERROR);
//        }
//        //等待所有信号量释放
//        while (true) {
//            if (BATCH_SEMAPHORE.availablePermits() == BATCH_WIDTH) {
//                ProcessContext.clearCache();
//                break;
//            } else {
//                Thread.sleep(1000);
//            }
//        }
//    }
//
//    private void readImageFile2DB(File folder, ProcessContext processContext) {
//        List<File> batchFileList = new ArrayList<>(batchSize);
//        // listFile 获取所有文件对象可能过大，此处先获取路径
//        String[] filePaths = folder.list();
//        if (filePaths == null || ArrayUtils.isEmpty(filePaths)) {
//            return;
//        }
//        File f;
//        for (int i = 0; i < filePaths.length; i++) {
//            if (StringUtil.isBlank(filePaths[i])) {
//                continue;
//            }
//            f = new File(folder.getPath() + File.separator + filePaths[i]);
//            if (f.exists() && f.isFile() && f.getName().toLowerCase().matches(filePattern)) {
//                batchFileList.add(f);
//                if (batchFileList.size() == batchSize) {
//                    //批量提交
//                    parallelFileBatch(batchFileList, processContext);
//                    batchFileList = new ArrayList<>(batchSize);
//                } else if (i == filePaths.length - 1) {
//                    //当前目录最后一次提交
//                    parallelFileBatch(batchFileList, processContext);
//                }
//            } else if (f.isDirectory()) {
//                readImageFile2DB(f, processContext);
//            }
//        }
//    }
//
//
//    /**
//     * 文件
//     *
//     * @param fileList       文件列表
//     * @param processContext 批量处理上下文
//     * @return 解析文件名称
//     */
//    private List<File> filterDuplicateCodeFile(List<File> fileList,
//                                               String batchListId,
//                                               ProcessContext processContext) {
//        Set<String> cacheSet = new HashSet<>(fileList.size());
//        List<BatchDetailInsertBatchParam> duplicateParamList = new LinkedList<>();
//
//        File file;
//        for (Iterator<File> iterator = fileList.iterator(); iterator.hasNext(); ) {
//            file = iterator.next();
//            String code = StringUtils.EMPTY;
//            String name = StringUtils.EMPTY;
//            String remark = getMessage(BATCHDETAIL_DUPLICATE_CODE);
//            int codeIdx = processContext.getCodeColumnIdx();
//            int nameIdx = processContext.getNameColumnIdx();
//            int index = 0;
//            StringTokenizer columnTokenizer = new StringTokenizer(FilenameUtils.getBaseName(file.getName()), "-");
//            while (columnTokenizer.hasMoreTokens()) {
//                String nextToken = columnTokenizer.nextToken();
//                if (index == nameIdx) {
//                    name = nextToken;
//                }
//                if (index == codeIdx) {
//                    code = nextToken;
//                }
//                index++;
//            }
//            //图片格式是否正确，是否可以进入写入操作
//            boolean containFlag;
//            if (index < processContext.getColumns().size()) {
//                remark = getMessage(BATCHIMPORT_IMAGE_FORMAT_ERROR);
//                containFlag = false;
//            } else {
//                //与当前导入数据中有冲突，则导入失败
//                containFlag = ProcessContext.cacheContains(code);
//                //如果缓存不存在，则校验当前批次是否有重复ID
//                if (!containFlag) {
//                    //如果有重复code, containFlag返回false
//                    containFlag = cacheSet.add(code);
//                }
//            }
//            // 处理格式错误数据
//            if (!containFlag) {
//                BatchDetailInsertBatchParam detail = new BatchDetailInsertBatchParam();
//                detail.setId(CloudwalkDateUtils.getUUID());
//                detail.setStatus(ImportRecordStatus.FAIL.value());
//                detail.setBatchId(processContext.getBatchImport().getId());
//                detail.setFileName(StringUtils.left(file.getName(), 128));
//                detail.setPersonCode(StringUtils.left(code, 32));
//                detail.setPersonName(StringUtils.left(name, 128));
//                detail.setCreateTime(System.currentTimeMillis());
//                detail.setCreateUserId(StringUtils.EMPTY);
//                if (StringUtils.isBlank(code)) {
//                    detail.setRemark(getMessage(BATCHDETAIL_PERSON_CODE_NOTNULL));
//                } else {
//                    detail.setRemark(remark);
//                }
//                duplicateParamList.add(detail);
//                processContext.getFailCount().incrementAndGet();
//                iterator.remove();
//            }
//        }
//        //添加当前批次到导入缓存
//        ProcessContext.addCacheSet(batchListId, cacheSet);
//        //重复数据标记为失败，写入明细
//        try {
//            batchDetailService.insertBatch(duplicateParamList);
//        } catch (ServiceException e) {
//            logger.error("--导入记录插入异常", e);
//        }
//
//        return fileList;
//    }
//
//    /**
//     * 基于code去除相同code人员导入
//     *
//     * @param batchRecordList 这一批次数据
//     * @param processContext  批量处理上下文
//     */
//    private List<List<String>> filterDuplicateCode(List<List<String>> batchRecordList,
//                                                   String batchListId,
//                                                   ProcessContext processContext) {
//        Set<String> cacheSet = new HashSet<>(batchRecordList.size());
//        //去重后的结构
//        List<List<String>> filterRecordList = new ArrayList<>(batchRecordList.size());
//        List<BatchDetailInsertBatchParam> duplicateParamList = new LinkedList<>();
//        synchronized (this) {
//            //基于code去重记录
//            for (List<String> record : batchRecordList) {
//                //如数据不完整则跳过这行
//                if ((processContext.getNameColumnIdx() > record.size() - 1) || (processContext.getPathColumnIdx() > record.size() - 1)) {
//                    continue;
//                }
//                String code;
//                String remark = getMessage(BATCHDETAIL_DUPLICATE_CODE);
//                int codeIdx = processContext.getCodeColumnIdx();
//                if (record.size() <= codeIdx) {
//                    code = StringUtils.EMPTY;
//                    remark = getMessage(BATCHIMPORT_FILE_FORMAT_ERROR);
//                } else {
//                    code = record.get(codeIdx);
//                }
//                //与当前导入数据中有冲突，则导入失败
//                boolean containFlag = ProcessContext.cacheContains(code);
//                //如果缓存不存在，则校验当前批次是否有重复ID
//                if (!containFlag) {
//                    //如果有重复code, containFlag返回true
//                    containFlag = !cacheSet.add(code);
//                }
//                if (containFlag) {
//                    BatchDetailInsertBatchParam detail = new BatchDetailInsertBatchParam();
//                    detail.setId(CloudwalkDateUtils.getUUID());
//                    detail.setStatus(ImportRecordStatus.FAIL.value());
//                    detail.setBatchId(processContext.getBatchImport().getId());
//                    detail.setPersonCode(StringUtils.left(code, 128));
//                    detail.setPersonName(StringUtils.left(record.get(processContext.getNameColumnIdx()), 32));
//                    File imageFile = new File(record.get(processContext.getPathColumnIdx()));
//                    detail.setFileName(StringUtils.left(imageFile.getName(), 128));
//                    detail.setCreateTime(System.currentTimeMillis());
//                    detail.setCreateUserId(StringUtils.EMPTY);
//                    if (StringUtils.isBlank(code)) {
//                        detail.setRemark(getMessage(BATCHDETAIL_PERSON_CODE_NOTNULL));
//                    } else {
//                        detail.setRemark(remark);
//                    }
//                    duplicateParamList.add(detail);
//                    processContext.getFailCount().incrementAndGet();
//                } else {
//                    filterRecordList.add(record);
//                }
//            }
//            //添加当前批次到导入缓存
//            ProcessContext.addCacheSet(batchListId, cacheSet);
//        }
//        //写入失败数据
//        try {
//            batchDetailService.insertBatch(duplicateParamList);
//        } catch (ServiceException e) {
//            logger.error("--导入记录插入异常", e);
//        }
//
//        return filterRecordList;
//    }
//
//    /**
//     * 并发处理
//     * 1.去重
//     * 2.分区
//     * 3.并发导入
//     *
//     * @param batchFileList
//     */
//    private void parallelFileBatch(final List<File> batchFileList,
//                                   final ProcessContext processContext) {
//
//        if (batchFileList.isEmpty()) {
//            return;
//        }
//        try {
//            //根据并发量阻塞主线程
//            BATCH_SEMAPHORE.acquire();
//            logger.info("人员批量导入当前并发数：{}", BATCH_WIDTH - BATCH_SEMAPHORE.availablePermits());
//        } catch (InterruptedException e) {
//            logger.error(StringUtils.EMPTY, e);
//        }
//        //每一批次的编号
//        final String batchListId = CloudwalkDateUtils.getUUID();
//        final List<File> fileList;
//        if (ImportMode.from(processContext.getBatchImport().getType()) == ImportMode.SERVER_PICTURE_GENERATION) {
//            //自动生成特殊处理
//            fileList = batchFileList;
//        } else {
//            //基于code去重
//            try {
//                fileList = filterDuplicateCodeFile(batchFileList, batchListId, processContext);
//            } catch (Exception e) {
//                logger.error(StringUtils.EMPTY, e);
//                BATCH_SEMAPHORE.release();
//                return;
//            }
//        }
//        logger.info("根据编号去重完毕");
//        taskExecutor.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    portalBatchPersonHandler.batchFileAdd(fileList, processContext);
//                    ProcessContext.removeBatchListId(batchListId);
//                } catch (Exception e) {
//                    logger.error("线程结果获取错误！", e);
//                } finally {
//                    BATCH_SEMAPHORE.release();
//                }
//            }
//        });
//    }
//
//    private void printMemoryDetail(String step) {
//        logger.info(step);
//        logger.info("TotalMemory：" + Runtime.getRuntime().totalMemory() / (1024 * 1024) + "M");
//        logger.info("FreeMemory：" + Runtime.getRuntime().freeMemory() / (1024 * 1024) + "M");
//        logger.info("MaxMemory：" + Runtime.getRuntime().maxMemory() / (1024 * 1024) + "M");
//        logger.info("UsedMemory：" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + "M");
//    }
//
//    /**
//     * 读取指定的CSV文件行内容到指定的列表中
//     */
//    private void readCsvFile2DB(File csvFile, ProcessContext processContext) throws JobCommonException {
//        FileInputStream fis = null;
//        InputStreamReader isr = null;
//        CSVReader reader = null;
//        try {
//            //获取配置文件
//            String csvConfigJson = batchImportService.getPersonBatchSetting(PersonBatchConstant.CSV_TEMPLATE_CONFIG, processContext.getCtx());
//            JSONObject csvConfig = JSON.parseObject(csvConfigJson);
//            String charset = csvConfig.getString("charset");
//            int skipRow = csvConfig.getInteger("skipRow");
//            char separateChar = csvConfig.getString("separateChar").charAt(0);
//            char quoteChar = csvConfig.getString("quoteChar").charAt(0);
//            fis = new FileInputStream(csvFile);
//            isr = new InputStreamReader(fis, charset);
//            reader = new CSVReader(isr, separateChar, quoteChar, skipRow);
//            int totalCount = 0;
//            boolean first = true;
//            List<List<String>> batchRecordList = new ArrayList<>(batchSize);
//            while (true) {
//                String[] data;
//                data = reader.readNext();
//                if (data != null) {
//                    if (first) {
//                        //第一行为标题行
//                        checkTitle(Arrays.asList(data), processContext);
//                        first = false;
//                    } else {
//                        totalCount++;
//                        //数据解析
//                        batchRecordList.add(Arrays.asList(data));
//                        if (batchRecordList.size() == batchSize) {
//                            parallelBatchAdd(batchRecordList, processContext, csvFile.getParent());
//                            batchRecordList = new ArrayList<>(batchSize);
//                        }
//                    }
//                } else {
//                    break;
//                }
//            }
//            if (totalCount == 0) {
//                //没有数据也直接返回
//                throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHIMPORT_DATA_EMPTY);
//            }
//            parallelBatchAdd(batchRecordList, processContext, csvFile.getParent());
//        } catch (IOException e) {
//            logger.error(StringUtils.EMPTY, e);
//            throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHCONFIG_PROCESS_EXCEPTION);
//        } finally {
//            IOUtils.closeQuietly(reader);
//            IOUtils.closeQuietly(isr);
//            IOUtils.closeQuietly(fis);
//        }
//    }
//
//    /**
//     * 读取Excel的方法 可以自动识别Excel2003或者Excel2007
//     *
//     * @param excelFile      excel文件
//     * @param processContext 上下文
//     */
//    private void readExcelFile2DB(File excelFile,
//                                  ProcessContext processContext) throws JobCommonException {
//        /*
//         * 获取配置文件
//         * sheetNumber    修改第几个Sheet，从0开始计算
//         * skipRowIndex   跳过行数,0~n,0|null表示不跳过
//         * skipColIndex   跳过列数，0~n,0|null表示不跳过
//         */
//        String excelConfigJson = batchImportService.getPersonBatchSetting(PersonBatchConstant.EXCEL_TEMPLATE_CONFIG, processContext.getCtx());
//        JSONObject excelConfig = JSON.parseObject(excelConfigJson);
//        int sheetNumber = excelConfig.getInteger("sheetNumber");
//        int skipRowIndex = excelConfig.getInteger("skipRowIndex");
//        int skipColIndex = excelConfig.getInteger("skipColIndex");
//
//        // 如果是2007版的
//        if (excelFile.getAbsolutePath().endsWith(".xlsx")) {
//            logger.info("检测到2007版excel文件： " + excelFile.getAbsolutePath());
//            readExcelFile2DB2007(excelConfig, excelFile, processContext);
//        } else {
//        // 如果是2003版的按照原来的方法处理 因为2003版的数据每个sheet不能超过65000多行 不会造成内存溢出
//            // 输入流对象
//            InputStream inputStream = null;
//            // 获取工作薄
//            Workbook workbook;
//            // 工作表
//            Sheet sheet;
//            try {
//                inputStream = new FileInputStream(excelFile);
//                // 从Excel里面获取数据放入工作薄里面
//                // 创建Excel2003/2007文件对象
//                workbook = WorkbookFactory.create(inputStream);
//                // 取出工作表
//                sheet = workbook.getSheetAt(sheetNumber);
//                int rowSize = ExcelUtils.getLastRowIndex(sheet);
//
//                int totalCount = 0;
//                boolean first = true;
//                List<List<String>> batchRecordList = new ArrayList<>(batchSize);
//                //处理过程中的缓存,String<batch_process_id, Set<code>>
//
//                // 解析Excel文件
//                // 开始循环遍历行，表头不处理，从1开始
//                Row row;
//                Cell cell;
//                for (int r = skipRowIndex; r < rowSize; r++) {
//                    List<String> rowData = new ArrayList<>(processContext.getColumns().size());
//                    // 获取行对象
//                    row = sheet.getRow(r);
//                    if (row == null) {
//                        batchRecordList.add(rowData);
//                        continue;
//                    }
//                    String cellStr;
//                    // 循环遍历单元格
//                    for (int c = skipColIndex; c < row.getLastCellNum(); c++) {
//                        // 获取单元格对象
//                        cell = row.getCell(c);
//                        if (cell == null) {
//                            // 为空值,也要保留空间
//                            cellStr = ExcelUtils.NA_EXCEL;
//                        } else {
//                            cellStr = ExcelUtils.getCellStringValue(cell);
//                        }
//                        rowData.add(cellStr);
//                    }
//
//                    if (first) {
//                        //第一行为标题行
//                        checkTitle(rowData, processContext);
//                        first = false;
//                    } else {
//                        batchRecordList.add(rowData);
//                        totalCount++;
//                        if (batchRecordList.size() == batchSize) {
//                            parallelBatchAdd(batchRecordList, processContext, excelFile.getParent());
//                            batchRecordList = new ArrayList<>(batchSize);
//                        }
//                    }
//                }
//                if (totalCount == 0) {
//                    //没有数据也直接返回
//                    throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHIMPORT_DATA_EMPTY);
//                }
//                parallelBatchAdd(batchRecordList, processContext, excelFile.getParent());
//            } catch (Exception e) {
//                logger.error(StringUtils.EMPTY, e);
//                throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHCONFIG_PROCESS_EXCEPTION);
//            } finally {
//                IOUtils.closeQuietly(inputStream);
//            }
//        }
//    }
//
//    /**
//     * @MethodName: readExcelFile2DB2007
//     * @Description: 针对2007 百万数据导入优化的功能  不会内存溢出 速度从几个小时提升到几分钟
//     * @Param: [excelConfig, excelFile, processContext]
//     * @Return: void
//     * @Author: YCKJ2725
//     * @Date: 2019/12/2 13:42
//    **/
//    private void readExcelFile2DB2007(JSONObject excelConfig, File excelFile, ProcessContext processContext) throws JobCommonException {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        try {
//            List<List<String>> batchRecordList = new ArrayList<>(batchSize);
//            // 根据文件路径获取首个sheet的数据 由于放在一个线程中会卡住 单独的线程不受影响
//            final File excelFileInside = excelFile;
//            Future<LargeExcelFileReadHandler> result = executor.submit(new Callable<LargeExcelFileReadHandler>() {
//                @Override
//                public LargeExcelFileReadHandler call() throws Exception {
//                    try {
//                        LargeExcelFileReadHandler largeExcelFileReadHandler = new LargeExcelFileReadHandler();
//                        largeExcelFileReadHandler.processOneSheet(excelFileInside.getAbsolutePath());
//                        return largeExcelFileReadHandler;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return new LargeExcelFileReadHandler();
//                    }
//                }
//            });
//            // 返回excel 文件中 位置+值（可以为空）
//            LinkedHashMap<String, String> map= result.get().getRowContents();
//            logger.info("读取文件完成 " + excelFileInside.getAbsolutePath());
//            // A1:2 A2:3
//            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//            // 没有数据也直接返回
//            if (!it.hasNext()) {
//                throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHIMPORT_DATA_EMPTY);
//            }
//            String prePos="";
//            // 第一行忽略
//            boolean first = true;
//            // 每行的元素数 初始化8个
//            List<String> rowData = new ArrayList<>(8);
//            while (it.hasNext()){
//                Map.Entry<String, String> entry=it.next();
//                String pos=entry.getKey();
//                // 换行判断
//                if(!pos.substring(1).equals(prePos)){
//                    prePos=pos.substring(1);
//                    if(!first){
//                        // 增加一行的数据
//                        batchRecordList.add(rowData);
//                        // 如果到了一定的阈值，就提交批量新增
//                        if (batchRecordList.size() == batchSize) {
//                            parallelBatchAdd(batchRecordList, processContext, excelFile.getParent());
//                            batchRecordList = new ArrayList<>(batchSize);
//                        }
//                        rowData = new ArrayList<>(8);
//                    }
//                    first = false;
//                }
//                rowData.add(entry.getValue());
//            }
//            // 增加最后一行的数据
//            batchRecordList.add(rowData);
//            // 提交剩余的数据
//            parallelBatchAdd(batchRecordList, processContext, excelFile.getParent());
//        } catch (Exception e) {
//            logger.error(StringUtils.EMPTY, e);
//            throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHCONFIG_PROCESS_EXCEPTION);
//        } finally {
//            executor.shutdown();
//        }
//
//    }
//
//    /**
//     * 并发提交excel,csv记录
//     *
//     * @param batchRecordList 批量提交数据
//     * @param processContext  上下文
//     * @param filePath        父文件路径
//     */
//    private void parallelBatchAdd(List<List<String>> batchRecordList,
//                                  final ProcessContext processContext,
//                                  String filePath) {
//        //转换绝对路径
//        convertAbsolutePath(batchRecordList, processContext, filePath);
//        try {
//            //根据并发量阻塞
//            BATCH_SEMAPHORE.acquire();
//            logger.info("人员批量导入当前并发数：{}", BATCH_WIDTH - BATCH_SEMAPHORE.availablePermits());
//        } catch (InterruptedException e) {
//            logger.error(StringUtils.EMPTY, e);
//        }
//        //每一批次的编号
//        final String batchListId = CloudwalkDateUtils.getUUID();
//        //基于code去重
//        final List<List<String>> uniqueBatchRecordList;
//        try {
//            uniqueBatchRecordList = filterDuplicateCode(batchRecordList, batchListId, processContext);
//        } catch (Exception e) {
//            logger.error(StringUtils.EMPTY, e);
//            BATCH_SEMAPHORE.release();
//            return;
//        }
//
//        taskExecutor.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //并发处理
//                    portalBatchPersonHandler.batchAdd(uniqueBatchRecordList, processContext);
////                    ProcessContext.removeBatchListId(batchListId);
//                } catch (Exception e) {
//                    logger.error(StringUtils.EMPTY, e);
//                } finally {
//                    BATCH_SEMAPHORE.release();
//                }
//            }
//        });
//    }
//
//    /**
//     * 校验模板，合并数据
//     *
//     * @param title          标题行
//     * @param processContext 上下文
//     * @throws JobCommonException 异常
//     */
//    private void checkTitle(List<String> title, ProcessContext processContext) throws JobCommonException {
//        List<TemplateColumn> templateColumnList = processContext.getColumns();
//        if (title.size() < templateColumnList.size()) {
//            throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHIMPORT_FILE_FORMAT_ERROR);
//        }
//        for (int i = 0; i < templateColumnList.size(); i++) {
//            //获取标题，如果标题与配置模板配置不一致，直接返回
//            if (!templateColumnList.get(i).getLabel().equals(title.get(i))) {
//                throw JobCommonException.throwEx(BatchRespCodeConstant.BATCHIMPORT_FILE_FORMAT_ERROR);
//            }
//        }
//    }
//
//    /**
//     * 配置绝对路径
//     *
//     * @param recordList     记录列表
//     * @param processContext 上下文
//     * @param parentPath     路径
//     */
//    private void convertAbsolutePath(List<List<String>> recordList, ProcessContext processContext, String parentPath) {
//        int pathIdx = processContext.getPathColumnIdx();
//        for (List<String> record : recordList) {
//            if (record.size() > pathIdx) {
//                String path = record.get(pathIdx);
//                if (StringUtils.isBlank(path)) {
//                    path = "NULL";
//                }
//                //配置绝对路径
//                record.set(pathIdx, parentPath + File.separatorChar + path);
//            }
//        }
//    }
//
//    /**
//     * 清理文件
//     */
//    private void cleanFiles(ProcessContext context) {
//        try {
//            //删除解压的目录
//            FileUtils.deleteDirectory(context.getUnzipFolder());
//            //删除原始ZIP文件
//            FileUtils.deleteQuietly(new File(storageRootPath, context.getBatchImport().getFilePath()));
//        } catch (Exception ex) {
//            //清理文件错误不用处理
//            logger.error(StringUtils.EMPTY, ex);
//        }
//    }
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) {
//        //张三-YCKJ100000-lvxj46-3-2-123456789.jpg
//        BatchImportQueryResult batchImportRecord = null;
//        String remark;
//        try {
//            // 在批量导入时，需要控制人员同步查询接口被禁用
//            // 默认一个 JOB 获取一个信号量
//            logger.info("开始执行扫描批量导入记录任务表job， 启动一个batch任务，消耗一个信号资源");
//            PortalPersonSyncUtil.PERSON_BATCH_SEMAPHORE.acquire();
//
//            //1. 扫描当前一天内Pending的任务
//            batchImportRecord = getBatchImportQueryResult();
//            if (batchImportRecord == null) {
//                logger.info("未查询到当前有需要执行的批量导入任务，或当前已经有正在执行的任务！");
//                return;
//            }
//            //2. 设置当前执行的上下文context, 并且初始化数据
//            ProcessContext processContext = new ProcessContext();
//            processContext.setCtx(getCloudwalkContext(batchImportRecord.getBusinessId()));
//            processContext.setBatchImport(batchImportRecord);
//            //3. 设置当前任务为进行状态
//            this.updateBatchImportRecord(ImportStatus.PROCESSING, StringUtil.EMPTY, batchImportRecord.getId(), 0L);
//            //4. 查询配置模板信息
//            this.setBatchConfig(processContext);
//
//            //5. 解压缩文件
//            long unzipStartTime = System.currentTimeMillis();
//            //5.1 如果是前端导入，先合并文件
//            if (ImportMode.FILE_FRONT_UPLOAD.value().equals(batchImportRecord.getType())) {
//                //合并
//                mergeFile(processContext);
//            }
//            this.unzipFile(processContext);
//            long unzipSpendTime = System.currentTimeMillis() - unzipStartTime;
//            // processContext.setUnzipFolder(new File("D:\\data\\file\\test\\69ecf574d6c4411c99f0bdf63162faf7"));
//
//            //6. 校验文件是否存在
//            checkFileExists(processContext);
//            //7. 获取需要导入的总数，直接统计图片的数量，用于展示进度
//            long calculateStartTime = System.currentTimeMillis();
//            long totalCount = this.getImageCount(processContext);
//            long calculateSpendTime = System.currentTimeMillis() - calculateStartTime;
//
//            //更新数据总数
//            updateBatchImportRecord(totalCount, batchImportRecord.getId());
//
//            //查询机构信息和字典列表信息
//            this.setOrganizationsAndDicts(processContext);
//
//            //6. 处理数据文件
//            process(processContext);
//
//            //7. 清理原始的导入压缩文件
//            long cleanStartTime = System.currentTimeMillis();
//            cleanFiles(processContext);
//            long cleanSpendTime = System.currentTimeMillis() - cleanStartTime;
//            long processSpendTime = System.currentTimeMillis() - unzipStartTime;
//            //生成日志记录
//            remark = String.format("导入完成，导入成功[%s]条，失败[%s]条，总耗时[%s]，解压耗时[%s], 统计总数耗时[%s]，图片复制耗时[%s]，数据插入耗时[%s], 清理文件耗时[%s]",
//                    processContext.getSuccessCount(),
//                    processContext.getFailCount(),
//                    convertTime(processSpendTime),
//                    convertTime(unzipSpendTime),
//                    convertTime(calculateSpendTime),
//                    convertTime(processContext.getImageCopyTime().get() / taskExecutor.getMaxPoolSize()),
//                    convertTime(processContext.getInsertTime().get() / taskExecutor.getMaxPoolSize()),
//                    convertTime(cleanSpendTime));
//
//            BatchImport batchImport = batchImportMapper.selectById(batchImportRecord.getId());
//            updateBatchImportRecord(ImportStatus.COMPLETE, remark, batchImportRecord.getId(), batchImport.getTotalCount());
//        } catch (Exception ex) {
//            if (ex instanceof JobCommonException) {
//                remark = getMessage(((JobCommonException) ex).getCode());
//            } else {
//                logger.error(ex.getMessage(), ex);
//                remark = ex.getMessage();
//            }
//            //更新数据导入结果
//            if (batchImportRecord != null) {
//                updateBatchImportRecord(ImportStatus.EXCEPTION, remark, batchImportRecord.getId(), null);
//            }
//        } finally {
//            /* 释放 */
//            logger.info("batch任务结束，释放一个信号资源");
//            PortalPersonSyncUtil.PERSON_BATCH_SEMAPHORE.release();
//        }
//    }
//
//    private void checkFileExists(ProcessContext processContext) throws JobCommonException {
//        List<File> fileList = null;
//        BatchConfigResult batchConfigResult = processContext.getBatchConfig();
//        if (TemplateType.from(batchConfigResult.getType()) == TemplateType.CSV) {
//            fileList = FileUtil.findExpecteFormatdFile(".+(\\.csv)$", processContext.getUnzipFolder(), null);
//            if (fileList.isEmpty()) {
//                throw new JobCommonException(BatchRespCodeConstant.BATCHCONFIG_CSV_FILE_NOTFOUND);
//            }
//        }
//        //excel
//        else if (TemplateType.from(batchConfigResult.getType()) == TemplateType.EXCEL) {
//            fileList = FileUtil.findExpecteFormatdFile(".+(\\.xls|\\.xlsx)$", processContext.getUnzipFolder(), null);
//            if (fileList.isEmpty()) {
//                throw new JobCommonException(BatchRespCodeConstant.BATCHCONFIG_EXCEL_FILE_NOTFOUND);
//            }
//        }
//        processContext.setFileList(fileList);
//    }
//
//    /**
//     * 合并文件
//     * 因大文件前端合并时间太长导致超时，故在此合并文件
//     *
//     * @param processContext 上下文
//     */
//    private void mergeFile(ProcessContext processContext) throws JobCommonException {
//        String part = "part";
//        String md5Path = processContext.getBatchImport().getFilePath();
//        //1. 找到所有切片文件并按切片顺序排序
//        long start = System.currentTimeMillis();
//        //2. 合并文件
//        String fileName = md5Path + ".zip";
//        String fullPath = PathUtils.joinPaths(fileService.storageRootPath(), FileService.MODEL_TEMP, fileName);
//        FileOutputStream fos = null;
//        FileChannel outChannel = null;
//        try {
//            //校验查询最大IDX
//            int maxIdx = 0;
//            int nextIdx = 0;
//            String tempPartListPath = PathUtils.joinPaths(fileService.storageRootPath(), FileService.MODEL_TEMP, md5Path);
//            boolean flag = true;
//            while (flag) {
//                File file = new File(tempPartListPath, nextIdx + part);
//                if (file.exists()) {
//                    maxIdx = nextIdx;
//                    nextIdx++;
//                } else {
//                    flag = false;
//                }
//            }
//            //这里仍然使用路径文件的原因是由于文件不能通过storageService全部都内存
//            final File zipFile = new File(fullPath);
//            fos = new FileOutputStream(zipFile);
//            outChannel = fos.getChannel();
//            logger.info("合并文件的最大的分片文件索引号：" + maxIdx);
//            for (int i = 0; i <= maxIdx; i++) {
//                CloudwalkResult<byte[]> result = fileService.get(PathUtils.paths(i + part, FileService.MODEL_TEMP, md5Path));
//                if (result != null && result.getData() != null && result.getData().length > 0) {
//                    outChannel.write(ByteBuffer.wrap(result.getData()));
//                }
//            }
//            //正常合并成功后，删除切片文件内容
//            CloudwalkResult<Boolean> deleteResult = fileService.delete(PathUtils.joinPaths(FileService.MODEL_TEMP, md5Path));
//            if (deleteResult == null || !deleteResult.getData()) {
//                logger.warn(String.format(getMessage(PortalFileManagerConstant.LOG_FILE_MANAGER_CHUNK_FILE_CLEAN_FAIL), md5Path));
//            }
//            logger.warn(String.format(getMessage(PortalFileManagerConstant.LOG_MERGE_SPEND_TIME), md5Path, System.currentTimeMillis() - start));
//
//            processContext.getBatchImport().setFilePath(PathUtils.paths(fileName, FileService.MODEL_TEMP));
//        } catch (Exception e) {
//            logger.error(StringUtils.EMPTY, e);
//            throw JobCommonException.throwEx(BATCHCONFIG_GET_CONFIG_FAILED, getMessage(BATCHCONFIG_GET_CONFIG_FAILED));
//        } finally {
//            IOUtils.closeQuietly(outChannel);
//            IOUtils.closeQuietly(fos);
//        }
//    }
//
//    /**
//     * 查询数据字典及机构信息
//     *
//     * @param processContext 上下文
//     * @throws JobCommonException
//     */
//    private void setOrganizationsAndDicts(ProcessContext processContext) throws JobCommonException {
//        //查询机构信息的信息
//        List<Organization> organizations = this.organizationMapper.query(new Organization());
//        if (!organizations.isEmpty()) {
//            processContext.setOrgMap(organizations);
//        } else {
//            throw JobCommonException.throwEx(BATCHCONFIG_QUERY_ORG_ERROR, getMessage(BATCHCONFIG_QUERY_ORG_ERROR));
//        }
//
//        try {
//            //查询数据字典信息
//            CloudwalkResult<List<DictQueryResult>> dicts = dictService.query(new DictQueryParam(), processContext.getCtx());
//            if (dicts != null && dicts.isSuccess()) {
//                processContext.setDictDatas(dicts.getData());
//            }
//        } catch (ServiceException e) {
//            throw JobCommonException.throwEx(e.getCode(), getMessage(e.getCode()));
//        }
//    }
//
//    /**
//     * 统计图片数量
//     *
//     * @return
//     */
//    private long getImageCount(ProcessContext processContext) throws JobCommonException {
//        long totalCount = 0L;
//        try {
//            List<File> imageFiles = FileUtil.findExpecteFormatdFile(filePattern, processContext.getUnzipFolder());
//            totalCount = imageFiles.size();
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            throw JobCommonException.throwEx(BATCHCONFIG_GET_TOTAL_ERROR, getMessage(BATCHCONFIG_GET_TOTAL_ERROR));
//        }
//        // 未发现人脸图片
//        if (totalCount == 0) {
//            throw JobCommonException.throwEx(BATCHCONFIG_FACE_IMAGE_NOTNULL, getMessage(BATCHCONFIG_FACE_IMAGE_NOTNULL));
//        }
//        return totalCount;
//    }
//
//    /**
//     * 设置配置模板信息
//     */
//    private void setBatchConfig(ProcessContext processContext) throws JobCommonException {
//        //获取导入信息
//        BatchImportQueryResult batchImportRecord = processContext.getBatchImport();
//        try {
//            if (ImportMode.from(batchImportRecord.getType()) != ImportMode.SERVER_PICTURE_GENERATION) {
//                CloudwalkResult<BatchConfigResult> config = batchConfigService.selectByBusinessId(batchImportRecord.getBusinessId());
//                processContext.setBatchConfig(config.getData());
//            } else {
//                // 当为后台文件生成的方式的时候,此种模式下不需要查询模板配置信息
//                BatchConfigResult configResult = new BatchConfigResult();
//                //图片格式解析
//                configResult.setType(1);
//                configResult.setConfigJson("[{\"label\":\"人员编号\",\"id\":\"code\"},{\"label\":\"机构编号\",\"id\":\"orgId\"},{\"label\":\"人员类型\",\"id\":\"type\"},{\"label\":\"证件类型\",\"id\":\"certType\"},{\"label\":\"证件编号\",\"id\":\"certNo\"},{\"label\":\"姓名\",\"id\":\"name\"},{\"label\":\"文件路径\",\"id\":\"path\"}]");
//                processContext.setBatchConfig(configResult);
//            }
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            throw JobCommonException.throwEx(BATCHCONFIG_GET_CONFIG_FAILED, getMessage(BATCHCONFIG_GET_CONFIG_FAILED));
//        }
//    }
//
//    /**
//     * 解压
//     *
//     * @param processContext
//     * @throws JobCommonException
//     */
//    private void unzipFile(ProcessContext processContext) throws JobCommonException {
//        //获取导入信息
//        BatchImportQueryResult batchImportRecord = processContext.getBatchImport();
//        File file = new File(storageRootPath, batchImportRecord.getFilePath());
//        if (!file.exists()) {
//            throw JobCommonException.throwEx(BATCHCONFIG_ZIP_FILE_EMPTY, getMessage(BATCHCONFIG_ZIP_FILE_EMPTY));
//        }
//        try {
//            File unzipFolder;
//            String encode = batchImportService.getPersonBatchSetting(PersonBatchConstant.FILE_ENCODE, processContext.getCtx());
//            //linux unzip解压方式
//            if (checkUnzip()) {
//                String unzipPath = PathUtils.joinPaths(fileService.storageRootPath(), FileService.MODEL_TEMP, ToolUtil.generateUUID());
//                unzipFolder = new File(unzipPath);
//                unzipFolder.mkdirs();
//                logger.info("解压前文件路径:{}，解压后文件路径:{}，编码方式:{}", file.getPath(), unzipFolder.getPath(), encode);
//                //封装解压命令
//                String commandLine = String.format("unzip -q -O %s %s -d %s", encode, file.getPath(), unzipFolder.getPath());
//                ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", commandLine);
//                //redirectErrorStream方法设置为ture的时候，会将getInputStream()，getErrorStream()两个流合并，自动会清空流
//                processBuilder.redirectErrorStream(true);
//                //执行unzip命令
//                Process process = processBuilder.start();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    logger.info(line);
//                }
//                //等待结果
//                int result = process.waitFor();
//
//                /*
//                 *  https://linux.die.net/man/1/unzip
//                 *  unzip 官网说明 进程退出码为1 2时，解压可能是成功的，此处视为成功，后续的导入过程会对文件内容处理
//                 *  风险：进程退出码为1,2，无法对文件完成性进行校验
//                 */
//                if (result == 1 || result == 2) {
//                    logger.warn("---unzip 进程退出码：{}", result);
//                }
//                if (result != 0 && result != 1 && result != 2) {
//                    logger.warn("---unzip 进程退出码：{}", result);
//                    throw JobCommonException.throwEx(BATCHCONFIG_UNZIP_FAILED, getMessage(BATCHCONFIG_UNZIP_FAILED));
//                }
//            } else {
//                //java解压方式
//                unzipFolder = ZipUtil.unzipFile(file, encode);
//            }
//            processContext.setUnzipFolder(unzipFolder);
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            throw JobCommonException.throwEx(BATCHCONFIG_UNZIP_FAILED, getMessage(BATCHCONFIG_UNZIP_FAILED));
//        }
//    }
//
//    /**
//     * 校验linux unzip是否可用
//     *
//     * @return true-可用，false-不可用
//     */
//    private boolean checkUnzip() {
//        try {
//            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "unzip -help"});
//            process.waitFor();
//            InputStream inputStream = process.getInputStream();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, length);
//            }
//            String outputResult = byteArrayOutputStream.toString("UTF-8");
//            try {
//                inputStream.close();
//                byteArrayOutputStream.close();
//            } catch (IOException ignored) {
//            }
//            if (outputResult.contains("-O")) {
//                logger.info("支持unzip解压，使用unzip进行解压");
//                return true;
//            } else {
//                logger.info("不支持unzip解压，请升级至最新unzip");
//            }
//        } catch (IOException | InterruptedException e) {
//            logger.error(e.getMessage());
//        }
//        return false;
//    }
//
//    /**
//     * 查询批量任务
//     *
//     * @return
//     */
//    private synchronized BatchImportQueryResult getBatchImportQueryResult() {
//        Long fromTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
//        List<BatchImport> query = this.batchImportMapper.selectImportTask(fromTime);
//        BatchImport pendingImport = null;
//        if (CollectionUtils.isNotEmpty(query)) {
//            for (BatchImport q : query) {
//                //获取第一个未进行的任务
//                if (ImportStatus.from(q.getStatus()) == ImportStatus.NONE && pendingImport == null) {
//                    pendingImport = q;
//                }
//                //如果存在正在导入的记录，全部置位失败
//                else if (ImportStatus.from(q.getStatus()) == ImportStatus.PROCESSING) {
//                    q.setStatus(ImportStatus.EXCEPTION.value());
//                    q.setRemark(getMessage(BatchRespCodeConstant.BATCHCONFIG_PROCESS_EXCEPTION));
//                    batchImportMapper.updateById(q);
//                }
//            }
//        }
//        if (pendingImport == null) {
//            return null;
//        } else {
//            return BeanCopyUtils.copyProperties(pendingImport, BatchImportQueryResult.class);
//        }
//    }
//
//    private static String convertTime(long executeTime) {
//        long ms = 1000;
//        long minute = 60 * ms;
//        long hour = 60 * minute;
//        long day = 24 * hour;
//        long days = executeTime / day;
//        long hours = (executeTime % day) / hour;
//        long minutes = (executeTime % hour) / minute;
//        long seconds = (executeTime % minute) / ms;
//        long milliSeconds = executeTime % ms;
//        StringBuilder builder = new StringBuilder();
//        if (days != 0) {
//            builder.append(days).append("天");
//        }
//        if (hours != 0) {
//            builder.append(hours).append("小时");
//        }
//        if (minutes != 0) {
//            builder.append(minutes).append("分");
//        }
//        if (seconds != 0) {
//            builder.append(seconds).append("秒");
//        }
//        if (builder.length() < 1) {
//            builder.append(milliSeconds).append("毫秒");
//        }
//        return builder.toString();
//    }
//
//    private void updateBatchImportRecord(ImportStatus status, String remark, String id, Long currentCount) {
//        try {
//            BatchImportUpdateParam bi = new BatchImportUpdateParam();
//            bi.setId(id);
//            bi.setStatus(status.value());
//            bi.setCurrentCount(currentCount);
//            if (status == ImportStatus.COMPLETE || status == ImportStatus.EXCEPTION) {
//                bi.setFinishTime(System.currentTimeMillis());
//            }
//            bi.setRemark(StringUtils.left(remark, 255));
//            batchImportService.updateById(bi);
//        } catch (Exception ex) {
//            logger.error(StringUtils.EMPTY, ex);
//        }
//    }
//
//    private void updateBatchImportRecord(Long totalCount, String id) {
//        try {
//            BatchImportUpdateParam bi = new BatchImportUpdateParam();
//            bi.setId(id);
//            bi.setTotalCount(totalCount);
//            batchImportService.updateById(bi);
//        } catch (Exception ex) {
//            logger.error(StringUtils.EMPTY, ex);
//        }
//    }
//
//    private String getMessage(String code) {
//        try {
//            return this.messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
//        } catch (Exception ex) {
//            logger.warn(StringUtils.EMPTY, ex);
//            return code;
//        }
//    }
//
//    private CloudwalkCallContext getCloudwalkContext(String businessId) {
//        CloudwalkSessionObject session = this.sessionContextHolder.getSession();
//        if (session == null) {
//            session = new CloudwalkSessionObject(new String[]{"0", businessId, "默认用户", "default"});
//            this.sessionContextHolder.putSession(session);
//        }
//        return CloudwalkCallContextBuilder.buildContext(this.sessionContextHolder);
//    }
}
