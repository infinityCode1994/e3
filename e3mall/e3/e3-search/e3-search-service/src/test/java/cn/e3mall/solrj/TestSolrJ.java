package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {
    @Test
    public void addDocument()throws Exception{
        //创建一个solrservier对象，参数是solr服务url
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        //创建一个文档对象solrinpputdocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档对象中添加域。文档中必须包含一个id域，所有的域名称必须在schema。xml中定义
        document.addField("id","doc02");
        document.addField("item_title","中文测试");
        document.addField("item_price",1000);
        //把文档写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }
    @Test
    public void deleteDocument()throws Exception{
        //创建一个solrservier对象，参数是solr服务url
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        //删除文档
        solrServer.deleteByQuery("id:doc01");
        //提交
        solrServer.commit();
    }
    @Test
    public void queryIndex()throws Exception{
        //创建一个solrservier对象，参数是solr服务url
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        //创建一个solrquery对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.set("q","*:*");
        //执行查询，queryresponse对象
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //读取文档列表，取查询条件的总记录数
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
        //遍历文档列表，取域的内容
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
        }
    }
    @Test
    public void queryIndexcomplex()throws Exception{
        //创建一个solrservier对象，参数是solr服务url
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.130:8080/solr/collection1");
        //创建一个solrquery对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.set("q","手机");
        solrQuery.setStart(0);
        solrQuery.setRows(20);
        solrQuery.set("df","item_title");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");
        //执行查询，queryresponse对象
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //读取文档列表，取查询条件的总记录数
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
        //遍历文档列表，取域的内容
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title;
            if (list != null && list.size()>0){
                title = list.get(0);
            }else {
                title = (String) solrDocument.get("item_title");
            }
            System.out.println(title);
        }
    }
}
