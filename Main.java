package com.company;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
/**
 * Example program to list links from a URL. 
 * title还无法正确显示。解决办法是用另一个queue在把url enqueue的时候同时记录link.text()。写file的时候从两个queue同时pop。
 * potential problem: 若是某个title或者text空缺，后面所有的title和text都对应错了
 * potential solution: if (title.isEmpty) title = " " // set title to be empty string
 */
public class Main {
    private static String path = "/home/wuyi/Code/cs242/data1/";
    private static int docNum = 0;
    private static HashMap<String, String> duplicateUrlChecker = new HashMap<String, String> ();
    private static final double CONVERT = 1024*1024*1024;

    public static void main(String[] args) throws IOException, InterruptedException {
       webCrawlerDemo();

    }
    public static void webCrawlerDemo() throws IOException, InterruptedException {
//        String fileName = path + "doc.txt";
        String hostUrl = "http://en.wikipedia.org";
        double dataSize = 0;

//        create_file(fileName);
//        Vector<String> links;

        Queue<String> q = new LinkedList<>();
        q.add(hostUrl);
        int count = 0;

//        PrintWriter writer = new PrintWriter(fileName);
//        writer.println(hostUrl);
        
        
//        while (!q.isEmpty() && dataSize < 8) {
        while (!q.isEmpty() && count < 5) {
             String url = q.remove();
             if (!duplicateUrlChecker.containsKey(url)) {
            	 duplicateUrlChecker.put(url, "bingo");
	             addUrlOnPage(url,q);
	             dataSize = saveLinkText(docNum, url, dataSize);
	             docNum++;
	             
	//             System.out.println(links.size());
	             
	//             if (!links.isEmpty()) {
	//                 for (String link : links) {
	//                     q.add(link);
	//                     writer.println(link);
	//                     }
	//                 writer.println();
	//                 }
	             count++;
             }
             
             System.out.println(count);
             System.out.println("url = " + url);
             System.out.println(q.size());
             System.out.println("queue head = " + q.peek());
             System.out.println(dataSize);
             System.out.println("**********\n");
        }
//        writer.close();
        
    }
    public static void create_file(String doc)throws IOException{
        File f = new File(doc);
        f.createNewFile();
    }

    /* param: the url
        return: a vector contain all links in this url
     */
    public static void addUrlOnPage(String url, Queue<String> q)throws IOException{

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
//        Elements media = doc.select("[src]");
//        Elements imports = doc.select("link[href]");

//        String text = doc.body().text();

        //create_file(fileName);
        //PrintWriter writer = new PrintWriter(fileName);
       // writer.println(text);
        //writer.close();

        for(Element link : links){
            String linkUrl = link.attr("abs:href");
            q.add(linkUrl);
//            String pathTemp = path + Integer.toString(docNum) + ".txt";
//            System.out.println(urls + "\t" + link.text());
//            MyThreads t = new MyThreads(pathTemp,urls,link.text());
//            t.run();
//            docNum++;
        }
//        return v;
    }
    
    public static double saveLinkText(int docNum, String url, double size) throws IOException {
    	String docName = path + Integer.toString(docNum) + ".txt";
    	Document doc = Jsoup.connect(url).get();
        String text = doc.body().text();
        String title = doc.title();
        System.out.println("title = " + title);

        try{
            create_file(docName);
        }
        catch(IOException e){
            System.out.println("create file failed");
        }
        try {

            PrintWriter writer = new PrintWriter(docName);
            writer.println(title + "\n");
            writer.println(text);
            writer.close();
            File file = new File(docName);
            size += file.length() / CONVERT;
            return size;
        }
        catch (FileNotFoundException e){
            System.out.println("open file fail");
        }
        return 0;
    }
/*
    public static void start(String urlTemp,String fileN)throws IOException{
        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = urlTemp;



        //        "http://www.ucr.edu";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        String text = doc.body().text();

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());

        try(BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
        }
        String everything = sb.toString();
    }

        print("\ntext: ");

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    */
}


