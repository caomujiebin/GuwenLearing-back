package com.guwen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class GuwenLearingBackApplicationTests {

    @Test
    void contextLoads() {
        /*List<Integer> s = getRandomAnswer(6);
        System.out.println(s);*/
        Integer[] arr = new Integer[] {1,2,3,4,5,6,7,8,9};
        List list = Arrays.asList(arr);
        Collections.shuffle(list);
        System.out.println(list);
    }

    public List<Integer> getRandomAnswer(int explainCount) {
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
}
