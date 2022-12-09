package com.guwen.service.Impl;

import com.guwen.mapper.ForgetMapper;
import com.guwen.mapper.ReviewMapper;
import com.guwen.mapper.StudyMapper;
import com.guwen.model.*;
import com.guwen.model.Dto.DetailDto;
import org.elasticsearch.client.RestHighLevelClient;
import com.guwen.model.Dto.StudyDto;
import com.guwen.model.Dto.WordDto;
import com.guwen.service.StudyService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudyServiceImpl implements StudyService {
    @Autowired
    private StudyMapper studyMapper;

    @Autowired
    private ForgetMapper forgetMapper;

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public HashMap<String,Object> InitialStudy(StudyDto study) {
        HashMap<String,Object> res=new HashMap<>();
        //背诵记录
        String startFromPage = studyMapper.startRecordPage(study);

        if(startFromPage==null){
            startFromPage ="0";
            //插入该书的记录
            study.setBookPage(0);
            studyMapper.insertRecord(study);
        }
        User user = reviewMapper.selectStudyWords(study.getUserId());

        int wordsNum = study.getStudyType() ==2?user.getReviewWordsNum():user.getNewWordsNum();
        int startPage=study.getStudyType() ==1?Integer.parseInt(startFromPage):0;
        int selectNum = wordsNum/2+1;//选择题数量
        int lineNum = wordsNum-selectNum;//连线题数量
        int singleNum=selectNum/2+1;//单选题数量
        //int multiNum = selectNum-singleNum;//多选题数量

        //选择的实词id集合
        List<Integer> wordIdList = new ArrayList<>();
        //找到选择题
        List<Object> selectList = selectQuestion(study,startPage,selectNum,singleNum,study.getStudyType(),wordIdList);
        res.put("selects",selectList);
       //找到连线题
        List<Object> lineList = lineQuestion(study,startPage,selectNum,lineNum,study.getStudyType(),wordIdList);
        res.put("lines",lineList);
        res.put("wordIdList",wordIdList);
        res.put("wordsNum",wordsNum);
        return res;
    }

    @Override
    public List<Object> everyStudyRecord(User user) {
        List<Object> studyRecord = new ArrayList<>();
        String year = new SimpleDateFormat("yyyy").format(new Date());
        DetailDto detailDto = new DetailDto();
        detailDto.setUserId(user.getUserId());
        detailDto.setYear(year);
        List<DetailDto> studyRecordList = studyMapper.everyStudyRecord(detailDto);
        HashMap<String,Object> res = new HashMap<>();
        List<String> month;
        List<String> days;
        for(DetailDto details:studyRecordList){
            month=new ArrayList<>();
            month.add(details.getMonth());
            days = new ArrayList<>();
            if(details.getDay().length()==1)
               days.add("0"+details.getDay());
            else days.add(details.getDay());
            res.put("months",month);
            res.put("days",days);
            res.put("things","★"+details.getWordNum());
            studyRecord.add(new HashMap<>(res));
        }

        return studyRecord;
    }


    //选择题
    public List<Object> selectQuestion(StudyDto study,Integer startPage,Integer selectNum,Integer singleNum,Integer studyType,List<Integer> wordIdList){
        List<Word> wordList= new ArrayList<>();
        if(studyType ==1)
            wordList = studyMapper.SelectRequestion(study.getBookId(),startPage,selectNum);
        else if(studyType ==2)
            wordList = reviewMapper.SelectRequestionFromReview(study.getUserId(),startPage,selectNum);
        else if(studyType ==3)
            wordList = forgetMapper.SelectRequestionFromForget(study.getUserId(),startPage,selectNum);
        List<Object> select_list=new ArrayList<>();
        for(Word word:wordList){
            wordIdList.add(word.getWordId());
            HashMap<String,Object> select =new HashMap<>();
            List<Explain> explainList = studyMapper.selectByWordId(word.getWordId());
            select.put("id",word.getWordId());
            select.put("score",2);
            select.put("name",word.getWordName());
            int explainCount =1;
            if(singleNum-->0){
                select.put("ismultiple",false);
            }else {
                select.put("ismultiple",true);
                explainCount = explainList.size()>3?3:explainList.size();
            }
            HashSet<Character> randomAnswer = getRandomSelectAnswer(explainCount);
            Iterator<Character> iterator = randomAnswer.iterator();
            StringBuilder answer = new StringBuilder("");
            for(Character c:randomAnswer){
                answer.append(c);
            }
            select.put("answer",answer);
            HashMap<String,Object> answerList = new HashMap<>();
            List<String> existExplain = new ArrayList<>();
            List<HashMap<String,Object>> option=new ArrayList<>();
            //遍历答案
            for(int i=0;i<explainCount;i++){
                Explain explain =  explainList.get(i);
                answerList.put("id",explain.getId());
                answerList.put("name",explain.getWordExplain());
                answerList.put("checked",false);
                answerList.put("letter",iterator.next());
                existExplain.add(explain.getWordExplain());
                option.add(new HashMap<>(answerList));
            }
            //制造混淆答案，随机查询4个答案，选择其中的4-explainCount条
            List<Explain> otherExplain = studyMapper.selectOtherExplain();

            List<Character> letters = new ArrayList<>(Arrays.asList('A','B','C','D'));

            //剩余未分配的选项
            Iterator it = letters.iterator();
            while(it.hasNext()){
                if (randomAnswer.contains(it.next())) {
                    it.remove(); //移除该对象
                }
            }
            //混淆答案
            int j=0;
            for(int i=0;i<4-explainCount;i++){
                while(j<4){
                    Explain explain =otherExplain.get(i);
                    if(!existExplain.contains(explain.getWordExplain())){
                        answerList.put("id",explain.getWordId());
                        answerList.put("name",explain.getWordExplain());
                        answerList.put("checked",false);
                        answerList.put("letter",letters.get(i));
                        option.add(new HashMap<>(answerList));
                        break;
                    }
                    j++;
                }
            }
            //对答案按照ABCD排序
            Collections.sort(option, new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    String name1 = o1.get("letter").toString();
                    String name2 = o2.get("letter").toString();
                    return name1.compareTo(name2);
                }
            });
            select.put("option",option);

            //答案解析
            HashMap<String,Object> reasonList = new HashMap<>();
            StringBuilder summary=new StringBuilder(word.getWordName()+"有以下含义：");
            // StringBuilder examples = new StringBuilder("");
            List<String> exampleList = new ArrayList<>();
            for(Explain explain:explainList){
                StringBuilder example =new StringBuilder("");
                example.append(explain.getWordType()+";");
                example.append(explain.getWordExplain()+";");
                example.append(explain.getWordInBook()+";");
                example.append(explain.getWordSentence()+"。");
                //example.append("\n");
                summary.append(explain.getWordExplain()+"、");
                exampleList.add(example.toString());
            }

            //exampleList.add(examples.toString());
            reasonList.put("examples",exampleList);
            summary.deleteCharAt(summary.length() -1 );
            reasonList.put("summary",summary.toString());
            select.put("reasons",reasonList);
            select_list.add(new HashMap<>(select));
        }
        return select_list;
    }

    //连线题
    public List<Object> lineQuestion(StudyDto study,Integer startPage,Integer selectNum,Integer lineNum,Integer studyType,List<Integer> wordIdList){
        List<Object> line_list=new ArrayList<>();
        List<Word> wordList= new ArrayList<>();
        if(studyType == 1)
        wordList=studyMapper.SelectRequestion(study.getBookId(),startPage+selectNum,lineNum*3);
        else if(studyType == 2)
        wordList=reviewMapper.SelectRequestionFromReview(study.getBookId(),startPage+selectNum,lineNum*3);
        else wordList=forgetMapper.SelectRequestionFromForget(study.getBookId(),startPage+selectNum,lineNum*3);
        HashMap<String,Object> record=new HashMap<>();
        for(int i=0;i<lineNum;i++){
            record=new HashMap<>();
            int index = i+1;
            record.put("title","第"+index+"题");
            //答案解析
            StringBuilder reason=new StringBuilder("");
            //实词列
            List<WordDto> firstList=new ArrayList<>();
            //释义列
            List<WordDto> secondList=new ArrayList<>();
            //例句列
            List<WordDto> thirdList=new ArrayList<>();
            //答案列
            List<List<int []>> answerList = new ArrayList<>();
            List<int []> firstAnswer = new ArrayList<>();
            List<int []> secondAnswer = new ArrayList<>();
            //释义答案位置
            List<Integer> explainAnswer = getRandomLineAnswer(6);
            //例句答案位置
            List<Integer> sentence_Answer = getRandomLineAnswer(6);
            int x=0;
            for(int j=0;j<3 && i*3+j<wordList.size();j++){
                Word word = wordList.get(i*3+j);
                record.put("id",word.getWordId());
                wordIdList.add(word.getWordId());
                List<Explain> explainList = studyMapper.selectByWordId(word.getWordId());
                reason.append(word.getWordName()+"有以下含义：");
                for(Explain explain:explainList){
                    reason.append(explain.getWordExplain()+"、");
                }
                String firstNodeId = i+"_0_"+j;
                firstList.add(new WordDto(word.getWordId(),word.getWordName(),firstNodeId));

                for(int k=0;k<2;k++){
                    //int explain=explain_iterator.next();
                    int explain=x;
                    String secondNodeId = i+"_1_"+explain;
                    firstAnswer.add(new int[]{j,explain});
                    secondList.add(new WordDto(explainList.get(k).getWordExplain(),secondNodeId));

                    //int sentence=sentence_iterator.next();
                    int sentence=x++;
                    String thirdNodeId = i+"_2_"+sentence;
                    secondAnswer.add(new int[]{explain,sentence});
                    thirdList.add(new WordDto(explainList.get(k).getWordSentence(),thirdNodeId));
                }
            }
            answerList.add(firstAnswer);
            answerList.add(secondAnswer);
            record.put("reason",reason);
            record.put("answer",answerList);
            record.put("firstList",firstList);
            //将释义答案打乱顺序
            Collections.shuffle(secondList);
            record.put("secondList",secondList);
            //将例句答案打乱顺序
            Collections.shuffle(thirdList);
            record.put("thirdList",thirdList);
            line_list.add(record);
        }

        return line_list;
    }
    //生成若干个不重复的随机数并转换成大写字母
    public HashSet<Character> getRandomSelectAnswer(int explainCount){
        HashSet<Character> set = new HashSet();
        Random r = new Random();
        while (set.size()<explainCount){
            int a = r.nextInt(4)+1;
            set.add((char)(a+64));
        }
        return set;
    }

    //生成若干个不重复的随机数
    public List<Integer> getRandomLineAnswer(int explainCount) {
        // HashSet<Integer> set = new HashSet();
        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        while (list.size()<explainCount){
            int a = r.nextInt(6);
            if(!list.contains(a)){
                list.add(a);
            }
        }
        return list;
    }

    // 3、 在2的基础上进行高亮查询
    public List<Map<String, Object>> SearchByWord(String keyword,String type,Integer pageIndex,Integer pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest("guwen");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 精确查询，添加查询条件
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(type, keyword);
        //searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.query(matchQueryBuilder);
        // 分页
        searchSourceBuilder.from(pageIndex);
        searchSourceBuilder.size(pageSize);
        // 高亮 =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(type);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 执行查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {
            // 使用新的字段值（高亮），覆盖旧的字段值
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
            // 高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField name = highlightFields.get(type);
            // 替换
            if (name != null){
                Text[] fragments = name.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                sourceAsMap.put(type,new_name.toString());
            }
            results.add(sourceAsMap);
        }
        return results;
    }

}
