package domymembeanunsudo;
import com.mashape.unirest.http.*;
import static java.lang.Thread.sleep;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class Domymembeanunsudo {

    public static void main(String[] args) throws Exception {
        Domymembeanunsudo obj = new Domymembeanunsudo("tim-solonin2@yandex.ru","Up760136");
        System.out.println("Sending GET request:");
        obj.getRequest();
        
        System.out.println("\nSending POST request:");
        obj.postRequest();
        System.out.println(checkCookie);
        
        System.out.println("\nCheking dashboard");
        //obj.getDashboard();
        obj.getNew();
        //System.out.println(obj.startingBarrier.get(1));
        obj.initSession();
        System.out.println("\nSession started");
        Instant now = Instant.now().plusSeconds(10*60);
        while (now.isAfter(Instant.now())) obj.solve();
        if (obj.костыль) obj.solve();
        obj.quit();
    }
    
    public String auth_token = new String();
    public ArrayList<String> Cookies = new ArrayList<>();
    public ArrayList<String> startingBarrier = new ArrayList<>();
    public String login;
    public String password;
    public static boolean checkCookie;
    public static Instant startingTime;
    public String temp2;
    public String redirect;
    public String id;
    public final String CFDUID = "dd03c37ddd861cefb827fc262fef885251421803491";
    public boolean костыль;
    
    public Domymembeanunsudo(String login, String password){
        this.login = login;
        this.password = password;
    }
    
    public void getRequest() throws Exception {
        
        HttpResponse<String> response = Unirest
                .get("https://membean.com/login")
                .header("Host", "membean.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Cookie", "__cfduid="+CFDUID)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Connection", "keep-alive")
                .asString();
        
        String toParse = response.getBody();
        {
        int index = toParse.indexOf("authenticity_token");
        String temp = toParse.substring(index+56,index+99);
        temp = temp.replace("+", "%2B"); //SUKA PIZDEC KAKOGO HUYA MI TRAHALIS NAD ETIM 4 DNYA
        auth_token = temp;
        }
        
        //response.getHeaders().values().forEach(header -> );
        Object[] headers = response.getHeaders().values().toArray();
        {
            ArrayList<String> temp = new <String>ArrayList();
            for (Object in : headers) temp.add(in.toString());
            StringBuilder tString = new StringBuilder();
            temp.stream().filter((in) -> (in.contains("_new_membean_session_id")||in.contains("__cfduid") )).forEach((in) -> {
                tString.append(in);
            });
            StringTokenizer token = new StringTokenizer(tString.toString(), ";/ =");
            while (token.hasMoreTokens()) {
                String currentToken = token.nextToken();
                //System.out.println(currentToken);
                if (currentToken.contains("__cfduid") || currentToken.contains("_new_membean_session_id"))
                    Cookies.add(token.nextToken());                 
            }
            //for (String m : Cookies) System.out.println(m);
//            Cookies.add(tString.substring(10, 53));
//            Cookies.add(tString.substring(tString.indexOf("_id")+4, tString.indexOf("_id")+4+286));

        }
    }
    
    public void postRequest() throws Exception {
        String urlParameters = 
            "utf8="+"%E2%9C%93"+
            "&authenticity_token="+auth_token+"="+
            "&login_session%5Blogin%5D="+login+
            "&login_session%5Bpassword%5D="+password+
            "&login_session%5Bremeber_me%5D="+0+
            "&button=";
        Map<String, String> headerMap = new TreeMap<>();
        headerMap.put("Host", "membean.com");
        headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headerMap.put("Accept-Language", "en-US,en;q=0.5");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Referer", "https://membean.com/login");
        headerMap.put("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(0));
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        //headerMap.put("Content-Length", "191");
        //System.out.println(urlParameters.length());
        HttpResponse response = Unirest
                .post("https://membean.com/sessions")
                .headers(headerMap)
                .body(urlParameters)
                .asString();
        checkCookie = response.getHeaders().containsKey("set-cookie");
        Object[] headers = response.getHeaders().values().toArray();
        {
            ArrayList<String> temp = new <String>ArrayList();
            for (Object in : headers) temp.add(in.toString());
            StringBuilder tString = new StringBuilder();
            temp.stream().filter((in) -> (in.contains("_new_membean_session_id"))).forEach((in) -> {
                tString.append(in);
            });
            StringTokenizer token = new StringTokenizer(tString.toString(), ";/ =");
            while (token.hasMoreTokens()) {
                String currentToken = token.nextToken();
                if (currentToken.contains("_new_membean_session_id")||currentToken.contains("auth_token"))
                    Cookies.add(token.nextToken());                 
            }
            //for (String in : Cookies) System.out.println(in);
        }
    }
    
    public void getDashboard() throws Exception {
        HttpResponse<String> response = Unirest
                .get("http://membean.com/dashboard")
                .header("Host", "membean.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Connection", "keep-alive")
                .asString();
        //System.out.println(response.getHeaders().get("date").get(0));
        
    }
    
    public void getNew() throws Exception {
        HttpResponse<String> response = Unirest
                .get("http://membean.com/training_sessions/new")
                .header("Host", "membean.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Connection", "keep-alive")
                .asString();
        String delim = "=\" ";
        StringBuilder tString = new StringBuilder();
        StringTokenizer token = new StringTokenizer(response.getBody(), delim);
        while (startingBarrier.size()!=4) {
            String currentToken = token.nextToken();
            if (currentToken.equalsIgnoreCase("barrier")) {
                for (int i=0;i<3;i++) token.nextToken();
                startingBarrier.add(token.nextToken());
            }
        }
        startingBarrier.set(3, startingBarrier.get(2).replace("+", "%2B")+"=");
        //System.out.println(response.getBody());
        //System.out.println(startingBarrier.get(1));
    }
    
    public void initSession() throws Exception {
        String urlParameters = 
                "barrier="+startingBarrier.get(3);
        Map<String, String> headerMap = new TreeMap<>();
        headerMap.put("Host", "membean.com");
        headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headerMap.put("Accept-Language", "en-US,en;q=0.5");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Referer", "http://membean.com/training_sessions/new");
        headerMap.put("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1));
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse response = Unirest
                .post("http://membean.com/training_sessions?t=15")
                .headers(headerMap)
                .body(urlParameters)
                .asString();
        System.out.println(response.getHeaders().get("location").get(0));
        redirect = response.getHeaders().get("location").get(0);
        id = redirect.substring(37, 45);
        System.out.println("MY ID: "+id);
        System.out.println("Location: "+redirect);
        startingTime = Instant.now().plusSeconds(300);
    }
    
    public void solve() throws Exception {
        ArrayList<String> tempBarrier = new ArrayList<>();
        HttpResponse<String> response = Unirest
            .get(redirect)//+temp2)
            .header("Host", "membean.com")
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0")
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Accept-Encoding", "gzip, deflate")
            .header("Referer", "http://membean.com/training_sessions/new")
            .header("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1)+"; answered-incorrectly=")
            .header("Connection", "keep-alive")
            .asString();
        
        String delim = "=\" ";
        StringBuilder tString = new StringBuilder();
        StringTokenizer token = new StringTokenizer(response.getBody(), delim);
        
        while (tempBarrier.size()!=2) {
            String currentToken = token.nextToken();
            if (currentToken.equalsIgnoreCase("barrier")) {
                for (int i=0;i<3;i++) token.nextToken();
                tempBarrier.add(token.nextToken());
            }
        }
        tempBarrier.set(1, tempBarrier.get(1).replace("+", "%2B")+"=");
        
        temp2="?xhr=_xhr";
        String urlParameters;
        double time = 20+Math.random()*20+Math.random()*0.999;
        
        if (костыль) { 
            System.out.println("finish_study!");
            urlParameters =
                "pass=true"+
                "&event=finish_study!"+
                "&id="+id+
                "&barrier="+tempBarrier.get(0)+
                "&it=0";
        sleep(4000+(int)(Math.random()*4000));
        костыль = false;
        }
        else if (response.getBody().contains("spell!") || response.getBody().contains("finish_restudy!")) { 
            System.out.println("new word");
            urlParameters =
                "event=spell!"+
                "&time-on-page=%7B%22time%22%3A"+time+"%7D"+
                "&id="+id+
                "&barrier="+tempBarrier.get(1)+
                "&it=0";  
        sleep((int)(1000*(time-1)));
            System.out.println("sleeping for: "+1000*((int)time-1));
        костыль = true;
        //never code like this
        if (response.getBody().contains("finish_restudy!")) костыль = false;
        }
        
        else {
            System.out.println("default ans");
            urlParameters = 
                "pass=true"+
                "&event=answer!"+
                "&id="+id+
                "&barrier="+tempBarrier.get(1)+
                "&it=0";
        sleep(4000+(int)(Math.random()*4000));
        }
        Map<String, String> headerMap = new TreeMap<>();
        headerMap.put("Host", "membean.com");
        headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0");
        headerMap.put("Accept", "application/json, text/html, text/javascript");
        headerMap.put("Accept-Language", "en-US,en;q=0.5");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("X-Requested-With", "XMLHttpRequest");
        headerMap.put("Referer", redirect);
        headerMap.put("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1)+"; answered-incorrectly=");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headerMap.put("Pragma", "no-cache");
        headerMap.put("Cache-Control", "no-cache");
        HttpResponse commit = Unirest
                .post(redirect.replace("user_state", "advance"))
                .headers(headerMap)
                .body(urlParameters)
                .asString();
    }    
    
    public void quit() throws Exception {
        ArrayList<String>tempBarrier = new ArrayList<>();
        HttpResponse<String> response = Unirest
            .get(redirect)
            .header("Host", "membean.com")
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0")
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Accept-Encoding", "gzip, deflate")
            .header("Referer", "http://membean.com/training_sessions/new")
            .header("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1)+"; answered-incorrectly=")
            .header("Connection", "keep-alive")
            .asString();
        
        String delim = "=\" ";
        StringBuilder tString = new StringBuilder();
        StringTokenizer token = new StringTokenizer(response.getBody(), delim);
   
        while (tempBarrier.size()!=2) {
            String currentToken = token.nextToken();
            if (currentToken.equalsIgnoreCase("barrier")) {
                for (int i=0;i<3;i++) token.nextToken();
                tempBarrier.add(token.nextToken());
            }
        }
        tempBarrier.set(0, tempBarrier.get(0).replace("+", "%2B")+"=");
        String urlParameters =
                "event=done!"+
                "&id="+id+
                "&barrier="+tempBarrier.get(0)+
                "&it=0";
        Map<String, String> headerMap = new TreeMap<>();
        headerMap.put("Host", "membean.com");
        headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0");
        headerMap.put("Accept", "application/json, text/html, text/javascript");
        headerMap.put("Accept-Language", "en-US,en;q=0.5");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("X-Requested-With", "XMLHttpRequest");
        headerMap.put("Referer", redirect);
        headerMap.put("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1)+"; answered-incorrectly=");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headerMap.put("Pragma", "no-cache");
        headerMap.put("Cache-Control", "no-cache");
        HttpResponse commit = Unirest
                .post(redirect.replace("user_state", "advance"))
                .headers(headerMap)
                .body(urlParameters)
                .asString();
        System.out.println(commit.getBody());
    }
}
