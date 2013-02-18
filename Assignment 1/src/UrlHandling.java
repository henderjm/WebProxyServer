import java.util.ArrayList;


public class UrlHandling {
	private ArrayList<String> blocked_urls = new ArrayList<String>();
	
	// no need for constructor
	public UrlHandling(){
//		this.blocked_urls[0] = "77.238.178.122"; // www.yahoo.com
//		this.blocked_urls[1] = "91.198.174.225"; // www.wikipedia.org
	}
	
	public void addUrl(String ip) {
		if(!blocked_urls.contains(ip)) {
			blocked_urls.add(ip);
		}
	}
	public void removeUrl(String ip) {
		blocked_urls.remove(ip);
	}
	public boolean isBlocked(String ip){
		if(blocked_urls.contains(ip))
			return true;
		else
			return false;
	}
	
	public String[] getBlockedUrls(){
		return null;
	}
}
