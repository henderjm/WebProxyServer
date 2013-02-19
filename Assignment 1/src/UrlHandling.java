import java.util.ArrayList;


public class UrlHandling {
	private ArrayList<String> blocked_urls = new ArrayList<String>();
	
	// no need for constructor
	public UrlHandling(){
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
