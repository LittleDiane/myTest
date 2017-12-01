package bootSolrTest.web;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
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
        SolrDocument document = client.getById("mycollection", "1");
        System.out.println(document);
        return document.toString();
    }
}
