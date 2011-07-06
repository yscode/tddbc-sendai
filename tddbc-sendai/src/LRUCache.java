import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LRUCache {

	private static final int DEFAULT_CACHE_SIZE = 2;

	private int cacheSize;

	private Map<String, String> map = new HashMap<String, String>();

	private List<String> keyList = new ArrayList<String>();

	public LRUCache(int cacheSize) {
		if (cacheSize <= 0) {
			throw new IllegalArgumentException();
		}
		this.cacheSize = cacheSize;
	}

	public LRUCache() {
		this(DEFAULT_CACHE_SIZE);
	}

	public String get(String key) {
		// キャッシュアウトしていたり、そもそも格納されていなければ null 出返す
		if (keyList.contains(key) == false) {
			return null;
		}

		storeKey(key);

		return map.get(key);
	}

	public void put(String key, String value) {
		storeKey(key);

		map.put(key, value);
	}

	public int size() {
		return Math.min(map.size(), this.cacheSize);
	}

	public int cacheSize() {
		return this.cacheSize;
	}

	public List<String> getKeyList() {
		return new ArrayList<String>(keyList);
	}

	private void storeKey(String key) {
		// キーリストに入っていたらいったん追い出して末尾に追加する
		keyList.remove(key);
		keyList.add(key);

		// キャッシュ容量を超えたら先頭から取り除く
		if (keyList.size() > this.cacheSize) {
			keyList.remove(0);
		}
	}
}
