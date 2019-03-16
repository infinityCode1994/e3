package cn.e3mall.fast;

import cn.e3mall.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDfsTest {
    @Test
    public void testUpload()throws Exception{
        //创建一个配置文件。文件名任意。内容就是tracker服务器地址
        //使用全局对象加载配置文件
        ClientGlobal.init("E:/e3/manager-web/src/main/resources/conf/client.conf");
        //创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient获得一个TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建一个StrorageServer的引用，可以是null
        StorageServer storageServer = null;
        //创建一个storageClient，参数需要TrackerSserver和StorageServer
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        //使用storageclient上传文件
        String[] strings = storageClient.upload_file("C:/Users/jjj/Desktop/f5fe1218bf3098984ec744bf993ee2fd.jpg", "jpg", null);
        for (String string : strings) {
            System.out.println(string);

        }
    }
    @Test
    public void testFastDfsClient()throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient("E:/e3/manager-web/src/main/resources/conf/client.conf");
        String string = fastDFSClient.uploadFile("C:/Users/jjj/Desktop/a283677a1dfb4deee151421c054baead.jpg");
        System.out.println(string);
    }
}
