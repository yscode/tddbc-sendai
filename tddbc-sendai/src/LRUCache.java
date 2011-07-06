import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LRUCache {
	private int cacheSize;
	private Map<String, String> map = new HashMap<String, String>();
	private List<String> keyList = new ArrayList<String>();

	public LRUCache(int cachesize) {
		if (cachesize <= 0) {
			throw new IllegalArgumentException();
		}
		this.cacheSize = cachesize;
	}

	public LRUCache() {
		this.cacheSize = 2;
	}

	public String get(String key) {
		// キャッシュアウトしていたり、そもそも格納されていなければ null 出返す
		if (keyList.contains(key) == false) {
			return null;
		}

		boolean containsKey = false;
		if (keyList.contains(key)) {
			keyList.remove(key);
			containsKey = true;
		}
		if (map.containsKey(key)) {
			keyList.add(key);
		}

		if (containsKey) {
			return map.get(key);
		} else {
			return null;
		}
	}

	public void put(String key, String value) {

		if (keyList.size() < this.cacheSize) {
			if (keyList.contains(key)) {
				keyList.remove(key);
			}
			keyList.add(key);
		} else {
			if (keyList.contains(key)) {
				keyList.remove(key);
			} else {
				keyList.remove(0);
			}
			keyList.add(key);
		}
		map.put(key, value);
	}

	public int size() {
		return Math.min(map.size(), this.cacheSize);
	}

	public int cacheSize() {
		return this.cacheSize;
	}

	public List<String> getKeyList() {
		return keyList;
	}

}
