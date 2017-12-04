package bootSolrTest.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SolrController {
	
	@Autowired
    private SolrClient client;

    @RequestMapping("/")
    @ResponseBody
    public String testSolr() throws IOException, SolrServerException {
    	//mycollection为core名,"1"是id
    	SolrDocument document = client.getById("mycollection", "1");
    	//application.properties spring.data.solr.host=http://127.0.0.1:8983/solr/mycollection
    	//SolrDocument document = client.getById("1");
        System.out.println(document);
        return document.toString();
    }
    
    @RequestMapping("/queryDocs")
    @ResponseBody
    public String queryDocs() {
        SolrQuery params = new SolrQuery();
        System.out.println("======================query===================");
        params.set("q", "*:*");
        params.set("start", 0);
        params.set("rows", 5);
        params.set("sort", "id desc");

        try {
            QueryResponse rsp = client.query("mycollection", params);
            SolrDocumentList docs = rsp.getResults();
            System.out.println("查询内容:" + params);
            System.out.println("文档数量：" + docs.getNumFound());
            System.out.println("查询花费时间:" + rsp.getQTime());

            System.out.println("------query data:------");
            for (SolrDocument doc : docs) {
                // 多值查询
              //  @SuppressWarnings("unchecked")//告诉编译器忽略 unchecked 警告信息，如使用List，ArrayList等未进行参数化产生的警告信息。
                String name = (String) doc.getFieldValue("name");
                String number = (String) doc.getFieldValue("number");
                System.out.println("name:" + name + "\t number:" + number);
            }
            System.out.println("-----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
		return "queryDocs success!";

    }

    
    @RequestMapping("/addDocs")
    @ResponseBody
    public String addSolr(){
    	System.out.println("======================add doc ===================");
    	// 单条数据写入
    	SolrInputDocument document = new SolrInputDocument();
    	document.addField("id","6");
    	document.addField("number","111");
    	document.addField("name","Diane");
    	document.addField("updateTime",new Date());
    	
    	try {
			UpdateResponse rspDoc = client.add("mycollection", document);
			System.out.println(("UpdateResponse result:" + rspDoc.getStatus() + " Qtime:" + rspDoc.getQTime()));
			UpdateResponse rspcommit = client.commit("mycollection");// client.commit();
            System.out.println(
                    "commit doc to index" + " result:" + rspcommit.getStatus() + " Qtime:" + rspcommit.getQTime());
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	 // 多条数据通过实体类写入
    	/*List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id","222");
        doc.addField("number","222");
        doc.addField("name","Diane2");
        doc.addField("updateTime",new Date());
        docList.add(doc);
        
        SolrInputDocument doc2 = new SolrInputDocument();
        doc2.addField("id","333");
        doc2.addField("number","333");
        doc2.addField("name","Diane3");
        doc2.addField("updateTime",new Date());
        docList.add(doc2);

        try {
            UpdateResponse rspDocs = client.add(docList);
            System.out.println("UpdateResponse result:" + rspDocs.getStatus() + " Qtime:" + rspDocs.getQTime());
            UpdateResponse rspcommit = client.commit();
            System.out.println(
                    "commit doc to index" + " result:" + rspcommit.getStatus() + " Qtime:" + rspcommit.getQTime());
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return document.toString();	
    }
    
    @RequestMapping("deleteDocs")
    @ResponseBody
    public String deleteDocs(){
    	try {
			UpdateResponse rspDoc = client.deleteById("mycollection","6");
			System.out.println(("UpdateResponse result:" + rspDoc.getStatus() + " Qtime:" + rspDoc.getQTime()));
			UpdateResponse rspcommit = client.commit("mycollection");
			System.out.println(
                    "commit doc to index" + " result:" + rspcommit.getStatus() + " Qtime:" + rspcommit.getQTime());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return "deleteDocs!";
    }
    
    @RequestMapping("updateDocs")
    @ResponseBody
    public String updateDocs(){
    	try {
    		SolrInputDocument document = new SolrInputDocument();
    		document.addField("id", "1");
    		document.addField("name", "PENG YU CHANG");
    		document.addField("number","111");
    		document.addField("updateTime",new Date());
			client.add("mycollection", document);
			client.commit("mycollection");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		return "updateDocs!";
    }
}
