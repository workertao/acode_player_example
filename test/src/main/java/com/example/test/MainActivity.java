package com.example.test;


import java.util.HashMap;
import java.util.Map;

public class MainActivity {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String father1 = "11112222333344445555";
        String father2 = "abcdefghrjklmnop1111abcdefghrjklmnop2222abcdefghrjklmnop3333abcdefghrjklmnop4444abcdefghrjklmnop5555";
        String sub = yourfather(father1, father2);
        System.out.print(args.length);
    }

    public static String yourfather(String father1, String father2) {
        //取出较长的字符串
        String max = (father1.length() > father2.length()) ? father1 : father2;
        //取出较短的字符串
        String min = max.equals(father1) ? father2 : father1;
        //临时存放最长字符串
        Map father=new HashMap<>();
        //初始相同的字符串长度
        int length=1;
        //遍历较短字符串
        for (int i = 0; i < min.length(); i++) {
            //遍历取出较短字符串中的字符串
            for (int m = 0, n = min.length() - i; n != min.length() + 1; m++, n++) {
                String sub = min.substring(m, n);
                System.out.println("取出的字符串："+sub);
                //判断取出的字符串在不在较长的字符串
                if (max.contains(sub)) {
                    System.out.println("被包含："+sub);
                    //当取出的字符串比初始长，存入map，并更新初始长度
                    if(sub.length()>length){
                        father.put(sub, sub);
                        length=sub.length();
                    }else if(sub.length()==length){
                        //相等的时候存入map
                        father.put(sub, sub);
                    }else{
                        //比初始短  移除
                        father.remove(sub);
                    }
                }
            }
        }
        return father.toString();
    }
}
