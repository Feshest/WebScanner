import java.net.*;
import java.util.Objects;


public class URLDepthPair {

    private int depth;
    private String URL;

    public URLDepthPair(String URL, int depth) {
        this.depth = depth;
        this.URL = URL;
    }

    public String getURLS(){    //getter возвращающий ссылку
        return URL;
    }

    public String getURL(){
        return URL;
    }


    public int getDepth() { return depth; } //getter возвращающий глубину ссылки

    public String toString() {
        return ("|    "+getDepth() + "    |"+" "+getURLS());

    }

    public String getDocPath() {
        try {
            return new URL(URL).getPath(); //Возвращает путь к url адресу
        } catch (MalformedURLException e) {
            return null;
        }
    }
    public String getWebHost() {      //возвращает имя хоста URL в формате IPv6
        try {
            return new URL(URL).getHost();
        } catch (MalformedURLException e) {
            return null;
        }
    }
    @Override
    public boolean equals(Object obj) {   //переопределение метода equals
        if (obj instanceof URLDepthPair) {
            URLDepthPair o = (URLDepthPair)obj;
            return this.URL.equals(o.getURLS());
        }
        return false;
    }

    @Override                                          //переопределение метода hashCode
    public int hashCode() {
        return Objects.hash();
    }
}