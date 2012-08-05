package Source;


import java.io.IOException;

public class Main_App {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Dictionary d = new Dictionary();
        d.loadDic("en-GB.dic");
        boolean x = d.search("alex");
        System.out.println(x);
        boolean x1 = d.search("alexex");
        System.out.println(x1);
        boolean x2 = d.search("alex");
        System.out.println(x2);
        boolean x3 = d.search("alexee");
        System.out.println(x3);
        boolean x4 = d.search("alexww");
        System.out.println(x4);
    }
}
