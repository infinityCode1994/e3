package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;


public class TestSolrCloud {
    @Test
    public void testAddDocument()throws Exception{
        //创建一个集群连接，应该使用cloudsolrserver创建
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.130:2181," +
                "192.168.25.130:2182,192.168.25.130:2183");
        //zkHost：zookeeper的地址列表
        //设置一个defaultcollection
        cloudSolrServer.setDefaultCollection("collection1");
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域
        document.addField("id","solrcloud01");
        document.addField("item_title","测试01");
        document.addField("item_price",1000);
        //把文档写入索引库
        cloudSolrServer.add(document);
        //提交
        cloudSolrServer.commit();
    }
    @Test
    public void queryIndex()throws Exception{
        //创建一个集群连接，应该使用cloudsolrserver创建
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.130:2181," +
                "192.168.25.130:2182,192.168.25.130:2183");
        //zkHost：zookeeper的地址列表
        //设置一个defaultcollection
        cloudSolrServer.setDefaultCollection("collection1");
        //创建一个solrquery对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.set("q","*:*");
        //执行查询，queryresponse对象
        QueryResponse queryResponse = cloudSolrServer.query(solrQuery);
        //读取文档列表，取查询条件的总记录数
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
        //遍历文档列表，取域的内容
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
        }
    }
}
