package com.guwen.service.Impl;

import com.guwen.mapper.StudyMapper;
import com.guwen.model.Content;
import com.alibaba.fastjson.JSON;
import com.guwen.service.ContentService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private StudyMapper studyMapper;

    // 1、解析数据放入 es 索引中
    public Boolean parseContent() throws IOException {
        /*// 获取内容
        List<Explain> contents = HtmlParseUtil.parseJD(keyword);*/
        List<Content> contents = studyMapper.selectAllWord();
        // 内容放入 es 中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m"); // 可更具实际业务是指
        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("guwen")
                            .source(JSON.toJSONString(contents.get(i)), XContentType.JSON)
            );
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        restHighLevelClient.close();
        return !bulk.hasFailures();
    }




}