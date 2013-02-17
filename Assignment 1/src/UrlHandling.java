
public class UrlHandling {
	private String[] blocked_urls = new String[20];
	
	// no need for constructor
	public UrlHandling(){
		this.blocked_urls[0] = "77.238.178.122"; // www.yahoo.com
		this.blocked_urls[1] = "91.198.174.225"; // www.wikipedia.org
	}
	
	public void addUrl(String ip) {
		for(int i = 0; i < blocked_urls.length; i++){
			if(blocked_urls[i].equals("")){
				blocked_urls[i] = ip;
				break;
			}
		}
	}
	
	public String[] getBlockedUrls(){
		return this.blocked_urls;
	}
}
