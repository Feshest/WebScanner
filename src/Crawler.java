import java.net.*;
import java.util.*;
import java.io.*;

public class Crawler {

    public static final String URL_PREFIX="https://"; //константа, содержащая префикс ссылки
    public static void main(String[] args) {
        new Crawler(new String[]{"https://www.nytimes.com/","1"});
    }
    Crawler(String[] args){
        int max_depth = Integer.parseInt(args[1]); //максимальная глубина
        LinkedList<URLDepthPair> checked = new LinkedList<URLDepthPair>(); //список с просмотренными ссылками
        LinkedList<URLDepthPair> unchecked = new LinkedList<URLDepthPair>(); //список с непросмотренными ссылками
        URLDepthPair first = new URLDepthPair(args[0], 0); //создаем объект класса, с глубиной 0
        unchecked.add(first); //добавляем в список непросмотренных ссылок
        int j=0;
        while (unchecked.size() != 0) {   //цикл выполнятеся пока лист unchecked не пуст
            URLDepthPair depthPair = unchecked.pop(); //извлекается элемент из стека, представленного списком unchecked
            System.out.println("Проверяется ссылка:" + '\t' + depthPair.getURL() + '\n'+
                    "Ссылок проверено:" + '\t' + ++j);
            checked.add(depthPair); //добавляем в список проверенных ссылок
            int url_depth = depthPair.getDepth();
            LinkedList<String> linksList = new LinkedList<String>();
            try{
                linksList = getAllLinks(depthPair);
            } catch (IOException e){ }
            if (url_depth < max_depth) {
                for (int i=0;i<linksList.size();i++) {
                    String newURL = linksList.get(i);
                    if (!(unchecked.contains(new URLDepthPair(newURL, 0)) | checked.contains(new URLDepthPair(newURL, 0)))){
                        unchecked.add(new URLDepthPair(newURL, newURL.split("/").length-3));
                    }
                }
            }
        }

        int i=0;
        for (URLDepthPair linksList : checked) System.out.println(++i + ": " + linksList.getURL() + " [" + linksList.getDepth() + "]");
    }

    public static LinkedList<String> getAllLinks(URLDepthPair myDepthPair) throws IOException {
        LinkedList<String> URLs = new LinkedList<String>();
        Socket sock = new Socket(myDepthPair.getWebHost(), 80);
        sock.setSoTimeout(3000); //время ожидания сокета
        PrintWriter myWriter = new PrintWriter(sock.getOutputStream(), true);
        myWriter.println("GET " + myDepthPair.getDocPath() + " HTTP/1.1");
        myWriter.println("Host: " + myDepthPair.getWebHost());
        myWriter.println("Connection: close");
        myWriter.println();
        BufferedReader BuffReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        String line;
        while (true) {
            line=BuffReader.readLine();
            if (line==null){ break; }
            for (String reta : line.split("href=\"")) {
                if (line.contains("a href=\"") & (reta.startsWith(URL_PREFIX))){
                    try {
                        String Link = (reta.substring(0, reta.indexOf("\"")));
                        URLs.add(Link);
                    }catch (IndexOutOfBoundsException e){
                        System.err.println(e);
                        break;
                    }
                }
            }
        }
        sock.close();
        BuffReader.close();
        return URLs;

    }

}

