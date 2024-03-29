import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyCookieHandler extends CookieHandler{

	@Override
	public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
		// the cookies will be included in request
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> l = retrieveCookies(uri, requestHeaders);
        map.put("Cookie",l);
        return Collections.unmodifiableMap(map);
	}

	private List<String> retrieveCookies(URI uri, Map<String, List<String>> requestHeaders) {
		
		return null;
	}

	@Override
	public void put(URI arg0, Map<String, List<String>> arg1)
			throws IOException {
		
	}

}
