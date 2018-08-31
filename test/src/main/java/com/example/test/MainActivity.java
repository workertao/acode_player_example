package com.example.test;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MainActivity {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        String father1 = "11112222333344445555";
//        String father2 = "abcdefghrjklmnop1111abcdefghrjklmnop2222abcdefghrjklmnop3333abcdefghrjklmnop4444abcdefghrjklmnop5555";
//        String sub = yourfather(father1, father2);
//        System.out.print(args.length);
//        float a = 100.235f;
//        int b = -35;
//        System.out.print(a + b);
//        atrim();
//        a();
        int[] a = new int[]{20, 10, 10};
        setA(a);
        int[] b = a;
        a = null;
        System.out.print(b[0] + " " + b[1] + " " + b[2]);
//        String a = new String("15501196165");
//        setA(a);
//        String b = a;
//        a = null;
//        System.out.print(a);
//        System.out.print(b);
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("张三");
//        strings.add("李四");
//        strings.add("王五");
//        setA(strings);
//        System.out.print(strings.toString());
//        int a = 100;
//        setA(a);
//        System.out.print(a);
        landa();
    }

    public static void setA(String a) {
        a = "98k";
    }

    public static void setA(int[] a) {
        a[0] = 10000;
    }

    public static void setA(int a) {
        a = 50;
    }

    public static void setA(ArrayList<String> strings) {
        strings.remove(0);
    }

    public static String yourfather(String father1, String father2) {
        //取出较长的字符串
        String max = (father1.length() > father2.length()) ? father1 : father2;
        //取出较短的字符串
        String min = max.equals(father1) ? father2 : father1;
        //临时存放最长字符串
        Map father = new HashMap<>();
        //初始相同的字符串长度
        int length = 1;
        //遍历较短字符串
        for (int i = 0; i < min.length(); i++) {
            //遍历取出较短字符串中的字符串
            for (int m = 0, n = min.length() - i; n != min.length() + 1; m++, n++) {
                String sub = min.substring(m, n);
                System.out.println("取出的字符串：" + sub);
                //判断取出的字符串在不在较长的字符串
                if (max.contains(sub)) {
                    System.out.println("被包含：" + sub);
                    //当取出的字符串比初始长，存入map，并更新初始长度
                    if (sub.length() > length) {
                        father.put(sub, sub);
                        length = sub.length();
                    } else if (sub.length() == length) {
                        //相等的时候存入map
                        father.put(sub, sub);
                    } else {
                        //比初始短  移除
                        father.remove(sub);
                    }
                }
            }
        }
        return father.toString();
    }

    //说明：斐波那契数列的定义：它的第一项和第二项均为1，以后各项都为前两项之和
    private static void a() {
        int a1 = 1;
        int a2 = 1;
        for (int i = 0; i < 15; i++) {
            a1 = a1 + a2;
            a2 = a2 + a1;
            System.out.print(a1 + "    ");
            System.out.print(a2 + "    ");
        }
    }

    private static void atrim() {
//        str.trim();
//        String a = str.replace(" ","");
//        String a1 = a .replace("\n","");
//        System.out.print(a1);
    }

    public static  void landa() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setId(i);
            user.setName(i + " text");
            users.add(user);
        }
        System.out.println("data："+users.toString());
        System.out.println("find：53");
    }
}
