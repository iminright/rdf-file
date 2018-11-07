/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.alipay.rdf.file.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.alipay.rdf.file.interfaces.FileFactory;
import com.alipay.rdf.file.interfaces.FileStorage;
import com.alipay.rdf.file.model.StorageConfig;
import com.alipay.rdf.file.sftp.SftpTestUtil;
import com.alipay.rdf.file.sftp.TemporaryFolderUtil;
import com.alipay.rdf.file.util.RdfFileUtil;

/**
 *
 * @author haofan.whf
 * @version $Id: MkdirTest.java, v 0.1 2018年11月07日 下午4:55 haofan.whf Exp $
 */
public class MkdirTest {

    TemporaryFolderUtil temporaryFolderUtil = new TemporaryFolderUtil();

    StorageConfig storageConfig;

    FileStorage fileStorage;

    String ROOT_PATH = SftpTestUtil.combineHomeDir("testcase");

    String remoteRenameDir = "renamedir/hahah";

    @Before
    public void setup(){
        storageConfig = SftpTestUtil.getStorageConfig();
        fileStorage = FileFactory.createStorage(storageConfig);
        try {
            temporaryFolderUtil.create();
        } catch (IOException e) {
            throw new RuntimeException("获取临时目录异常", e);
        }
    }

    @Test
    public void test1() throws Exception{

        //ExecutorService executorService = Executors.newFixedThreadPool(100);
        //List<Future> futures = new ArrayList<Future>(100);
        //for (int i = 0; i < 30; i++) {
        //    futures.add(executorService.submit(new UploadTask()));
        //}
        //
        //for(int i = 0; i < futures.size(); i++){
        //    futures.get(i).get();
        //}

        UploadTask uploadTask = new UploadTask();
        uploadTask.call();

    }

    class UploadTask implements Callable{

        @Override
        public Object call() throws Exception {
            String tempFileName = UUID.randomUUID().toString();
            String localTmpFileName = RdfFileUtil.combinePath("/Users/iminright-ali/haha"
                    , tempFileName);

            File localTempFile = new File(localTmpFileName);
            FileOutputStream fos = new FileOutputStream(localTempFile);
            String testStr = "aaa";
            byte[] testStrByte = testStr.getBytes(Charset.forName("UTF-8"));
            fos.write(testStrByte);
            fos.flush();

            fileStorage.upload(localTmpFileName, buildPath(remoteRenameDir
                    + "/" + tempFileName), false);
            return null;
        }
    }

    private String buildPath(String relativePath){
        return RdfFileUtil.combinePath(ROOT_PATH, relativePath);
    }

}