import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class LRUCacheTest {

	public static class 引数なしのコンストラクタで生成した場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
		}

		@Test
		public void キャッシュ容量は2() {
			assertThat(lru.cacheSize(), is(2));
		}
	}

	public static class コンストラクタでキャッシュ容量を指定した場合 {
		private LRUCache lru;
		private static final int CAPACITY = 3;

		@Before
		public void 前準備() {
			lru = new LRUCache(CAPACITY);
		}

		@Test
		public void キャッシュ容量はコンストラクタで指定したもの() {
			assertThat(lru.cacheSize(), is(CAPACITY));
		}
	}

	// 防御的プログラミング
	public static class コンストラクタでキャッシュ容量に不正な値を指定した場合 {

		@Test(expected = IllegalArgumentException.class)
		public void キャッシュ容量に0を指定すると例外が発生する() {
			new LRUCache(0);
		}

		@Test(expected = IllegalArgumentException.class)
		public void キャッシュ容量に負の値を指定すると例外が発生する() {
			new LRUCache(-1);
		}
	}

	public static class 何も要素を追加していない場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
		}

		@Test
		public void 要素数を取得すると0() {
			assertThat(lru.size(), is(0));
		}

		@Test
		public void getするとnull() {
			assertThat(lru.get("_"), nullValue());
			assertThat(lru.get("a"), nullValue());
		}

		@Test
		public void キーリストを取得すると空のリスト() {
			assertThat(lru.getKeyList().size(), is(0));
		}
	}

	public static class 要素を1つ追加した場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
		}

		@Test
		public void 要素数は1() {
			assertThat(lru.size(), is(1));
		}

		@Test
		public void getすると値が取得できる() {
			assertThat(lru.get("a"), is("dataA"));
		}

		@Test
		public void 格納したのと違うキーでgetするとnull() {
			assertThat(lru.get("b"), nullValue());
		}

		@Test
		public void キーリストにキーが格納される() {
			assertThat(lru.getKeyList().get(0), is("a"));
		}
	}

	public static class 要素としてaでないものを1つ追加した場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("_", "data_");
		}

		@Test
		public void getすると値が取得できる() {
			assertThat(lru.get("_"), is("data_"));
		}
	}

	public static class 別々のキーで要素を追加した場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
			lru.put("b", "dataB");
		}

		@Test
		public void キーリストにキーが格納される() {
			assertThat(lru.getKeyList(), is(Arrays.asList("a", "b")));
		}
	}

	public static class 同じキーで続けて値を上書きした場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
			lru.put("a", "dataA2");
		}

		@Test
		public void getすると最後に追加した値が取得できる() {
			assertThat(lru.get("a"), is("dataA2"));
		}

		@Test
		public void 同じキーを複数回putしてもキーリストのサイズは１() {
			assertThat(lru.getKeyList().size(), is(1));
			assertThat(lru.getKeyList().get(0), is("a"));
		}
	}

	public static class 容量内で同じキーの値を上書きした場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
			lru.put("b", "dataB");
			lru.put("a", "dataA2");
		}

		@Test
		public void キーリストの順番が入れ替わる() {
			assertThat(lru.getKeyList(), is(Arrays.asList("b", "a")));
		}

		@Test
		public void getすると最後に格納した値が取得される() {
			assertThat(lru.get("a"), is("dataA2"));
		}
	}

	public static class 容量内で古いほうのキーでgetした場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
			lru.put("b", "dataB");
			lru.get("a");
		}

		@Test
		public void キーリストの順番が変わる() {
			assertThat(lru.getKeyList(), is(Arrays.asList("b", "a")));
		}
	}

	public static class 何も要素を追加せずにgetした場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.get("a");
		}

		@Test
		public void 要素数は0() {
			assertThat(lru.size(), is(0));
		}

		@Test
		public void getしてもキーリストは空のまま() {
			assertThat(lru.getKeyList(), is(Collections.<String> emptyList()));
		}
	}

	public static class キャッシュ容量を超えた場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
			lru.put("b", "dataB");
			lru.put("c", "dataC");
		}

		@Test
		public void キャッシュアウトしたキーでgetすると消えている() {
			assertThat(lru.get("a"), nullValue());
		}

		@Test
		public void 要素数は容量を超えない() {
			assertThat(lru.size(), is(2));
		}

		@Test
		public void キーリストにはキャッシュアウトしたものは含まれていない() {
			assertThat(lru.getKeyList(), is(Arrays.asList("b", "c")));
		}
	}

	public static class キーリストが入れ替わった後にキャッシュ容量を超えた場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
			lru.put("b", "dataB");
			lru.get("a");
			lru.put("c", "dataC");
		}

		@Test
		public void 順番が入れ替わった後のキーリストの末尾にキーが追加される() {
			assertThat(lru.getKeyList(), is(Arrays.asList("a", "c")));
		}
	}

	public static class キャッシュ容量を超えた後にキャッシュアウトしたキーでgetした場合 {
		private LRUCache lru;

		@Before
		public void 前準備() {
			lru = new LRUCache();
			lru.put("a", "dataA");
			lru.put("b", "dataB");
			lru.put("c", "dataC");
			lru.get("a");
		}

		@Test
		public void キャッシュアウトしたキーに対するgetはキーリストに影響を与えない() {
			assertThat(lru.getKeyList(), is(Arrays.asList("b", "c")));
		}
	}
}