package duang;

import java.util.TreeMap;

public class Test {

    public static void main(String[] args) {
        TreeMap<String,String> treeMap = new TreeMap<>();
        treeMap.put("/a", "1");
        treeMap.put("/a/b", "4");
        treeMap.put("/a/a", "2");
        treeMap.put("/a/a/b", "3");
        treeMap.put("/b", "5");
        treeMap.put("/b/a", "6");
        treeMap.put("/b/a/a", "7");
        treeMap.put("/b/b", "8");

        System.out.println(treeMap);
    }

}
