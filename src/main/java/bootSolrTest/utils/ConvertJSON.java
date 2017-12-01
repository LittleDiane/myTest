package bootSolrTest.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;

import bootSolrTest.bean.CDBean;

public class ConvertJSON {
    public static List<CDBean> jsonToList(String json){
        //把JSON文本parse成JavaBean集合
    	List<CDBean> list = JSON.parseArray(json,CDBean.class);
        //打印一下list的内容，调试时用
    	//list.stream().forEach(System.out::println);
        return list;
    }
}
