package domymembeanunsudo;
import com.mashape.unirest.http.*;
import static java.lang.Thread.sleep;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class Domymembeanunsudo {
    
    //Chance to miss on a word
    public double CHANCE = 0.15;
    
    //Auth token required for login
    public String auth_token = new String();
    
    //Where auth cookies are stored
    public ArrayList<String> Cookies = new ArrayList<>();
    
    //Token for initiating a session
    public ArrayList<String> startingBarrier = new ArrayList<>();
    
    //Redirect on session initiation
    public String redirect;
    
    //ID of your session
    public String id;
    
    //"d859fb6490f0273c949d9ea2f519437a81422231629";MISHA//"dd03c37ddd861cefb827fc262fef885251421803491";TIMA//Sanya
    public final String CFDUID = "d8304d6f18dcdd5910a12af7af3d282bb1422237872";
    
    //Because .contains("finish_study") doesn't work for some reason, and because you can name vars in any language :D
    public boolean костыль;
    
    
    public static void main(String[] args) throws Exception {
        //"tim-solonin2@yandex.ru","Up760136"
        //
        //"mikhail.yakushin@mynbps.org","436398733"
        Domymembeanunsudo obj = new Domymembeanunsudo("Aleksandr.Agapitov@mynbps.org","Alex1997",15);
        
        System.out.println("Sending GET request:");
        obj.getRequest();
        
        System.out.println("\nSending POST request:");
        obj.postRequest();
        
        System.out.println("\nObtaining barrier for session");
        obj.getNew();
        
        System.out.println("\nSession started");
        obj.initSession();
        
        System.out.println(Instant.now()); //What time is it? Membean time!
        
        while (obj.end.isAfter(Instant.now())) obj.solve();
        
        if (obj.костыль) obj.solve(); //This is required in case loop ends on finish_study! ,in this case bot has to run extra loop since you can't end session from finish_study!
        
        System.out.println("ABORT MISSION");
        obj.quit();
    }
 
    //Default constructor
    public final String login;
    public final String password;
    public Instant end;
    
    public Domymembeanunsudo(String login, String password, int minutes){
        this.login = login;
        this.password = password;
        end = Instant.now().plusSeconds(minutes*60);
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
            //Parsing for auth token
            int index = toParse.indexOf("authenticity_token");
            String temp = toParse.substring(index+56,index+99);
            temp = temp.replace("+", "%2B"); //This took me 4
            auth_token = temp;
        }
        
        Object[] headers = response.getHeaders().values().toArray();
        {
            ArrayList<String> temp = new <String>ArrayList();
            for (Object in : headers) temp.add(in.toString());
            StringBuilder tString = new StringBuilder();
            
            //Lambda expression wat?
            temp.stream().filter((in) -> (in.contains("_new_membean_session_id") || in.contains("__cfduid") )).forEach((in) -> {
                tString.append(in);
            });
            
            StringTokenizer token = new StringTokenizer(tString.toString(), ";/ =");
            
            while (token.hasMoreTokens()) {
                String currentToken = token.nextToken();
                if (currentToken.contains("__cfduid") || currentToken.contains("_new_membean_session_id"))
                    Cookies.add(token.nextToken());                 
            }
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

        HttpResponse response = Unirest
                .post("https://membean.com/sessions")
                .headers(headerMap)
                .body(urlParameters)
                .asString();
        
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
                if (currentToken.contains("_new_membean_session_id") || currentToken.contains("auth_token"))
                    Cookies.add(token.nextToken());                 
            }
        }
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
        //Parsing for barrier
        {
            String delim = "=\" ";
            StringBuilder tString = new StringBuilder();
            StringTokenizer token = new StringTokenizer(response.getBody(), delim);
            while (startingBarrier.size()!=10) {
                String currentToken = token.nextToken();
                if (currentToken.equalsIgnoreCase("barrier")) {
                    for (int i=0;i<3;i++) token.nextToken();
                    startingBarrier.add(token.nextToken());
                }
            }
        }
        //0 - ?, 1 - 5min, 2 - 10min, etc...
        startingBarrier.set(6, startingBarrier.get(4).replace("+", "%2B")+"=");
        System.out.println(startingBarrier.get(4));
    }
    
    public void initSession() throws Exception {
        String urlParameters = 
                "barrier="+startingBarrier.get(4);
        Map<String, String> headerMap = new TreeMap<>();
        {
            headerMap.put("Host", "membean.com");
            headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0");
            headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            headerMap.put("Accept-Language", "en-US,en;q=0.5");
            headerMap.put("Accept-Encoding", "gzip, deflate");
            headerMap.put("Referer", "http://membean.com/training_sessions/new");
            headerMap.put("Cookie", "__cfduid="+CFDUID+"; _new_membean_session_id="+Cookies.get(2)+"; auth_token="+Cookies.get(1));
            headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        }
        HttpResponse response = Unirest
                .post("http://membean.com/training_sessions?t=20")
                .headers(headerMap)
                .body(urlParameters)
                .asString();
     
        redirect = response.getHeaders().get("location").get(0);
        id = redirect.substring(37, 45);
    }
    
    public void solve() throws Exception {
        ArrayList<String> tempBarrier = new ArrayList<>();
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
        tempBarrier.set(1, tempBarrier.get(1).replace("+", "%2B")+"=");     
        
        String urlParameters;
        
        //Time to sleep for when styding new words
        int time = (int)((30+Math.random()*40+Math.random())*1000);
        
        //This is required for finish_study field, no idea why it's not obtained by the body
        if (костыль) { 
            System.out.println("finish_study!");
            urlParameters =
                "pass=true"+
                "&event=finish_study!"+
                "&id="+id+
                "&barrier="+tempBarrier.get(0)+
                "&it=0";
            sleep((int)(6000+(Math.random()*6000)));
            костыль = false;
        }
        
        else if (response.getBody().contains("spell!")) { 
            System.out.println("new word");
            urlParameters =
                "event=spell!"+
                "&time-on-page=%7B%22time%22%3A"+(double)time/1000+"%7D"+
                "&id="+id+
                "&barrier="+tempBarrier.get(1)+
                "&it=0";  
            sleep(time);
            System.out.println("sleeping for: "+time);
            //Never code like this
            костыль = true;
        }
        
        else if (response.getBody().contains("finish_restudy!")) {
            System.out.println("restudy");
            urlParameters =
                "event=finish_restudy!"+
                "&time-on-page=%7B%22time%22%3A"+time+"%7D"+
                "&id="+id+
                "&barrier="+tempBarrier.get(1)+
                "&it=0";  
            sleep((int)(1000*time)); 
            System.out.println("sleeping for: "+(int)(1000*time));
        }
        
        else {
            //Chance to miss
            String pass;
            if (Math.random()<CHANCE) pass="false";
            else pass="true";
            
            System.out.println("default ans");
            urlParameters = 
                "pass="+pass+
                "&event=answer!"+
                "&id="+id+
                "&barrier="+tempBarrier.get(1)+
                "&it=0";
            sleep((int)(6000+(Math.random()*6000)));
        }
        
        Map<String, String> headerMap = new TreeMap<>();
        {
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
        }
        //Push the answer
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
   
        while (tempBarrier.size()!=1) {
            String currentToken = token.nextToken();
            if (currentToken.equalsIgnoreCase("barrier")) {
                for (int i=0;i<3;i++) token.nextToken();
                tempBarrier.add(token.nextToken());
            }
        }
        
        String event;
        if (response.getBody().contains("close!")) event="close!";
        else event="done!";
        tempBarrier.set(0, tempBarrier.get(0).replace("+", "%2B")+"=");
        String urlParameters =
                "event="+event+
                "&id="+id+
                "&barrier="+tempBarrier.get(0)+
                "&it=0";
        
        Map<String, String> headerMap = new TreeMap<>();
        {
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
        }
        
        HttpResponse commit = Unirest
                .post(redirect.replace("user_state", "advance"))
                .headers(headerMap)
                .body(urlParameters)
                .asString();
        
        System.out.println(commit.getBody());
    }
}
