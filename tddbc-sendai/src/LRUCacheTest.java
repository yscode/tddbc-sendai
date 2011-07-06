
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LRUCacheTest {

    private LRUCache lru;

    @Before
    public void 前準備() {
    }

    @Test
    public void なにも足さずにgetするとnullガかえる() {
        lru = new LRUCache();
        assertThat(lru.get("_"), nullValue());
        assertThat(lru.get("a"), nullValue());
    }

    @Test
    public void 文字列をputしてgetすると値が取得できる() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        assertThat(lru.get("a"), is("dataA"));
    }

    @Test
    public void 文字列_をputしてgetすると値が取得できる() {
        lru = new LRUCache();
        lru.put("_", "data_");
        assertThat(lru.get("_"), is("data_"));
    }

    @Test
    public void 文字列aをputしてbでgetするとmullが取得できる() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        assertThat(lru.get("b"), nullValue());
    }

    @Test
    public void 何も要素を足さずにsizeを取得すると0が返される() {
        lru = new LRUCache();
        assertThat(lru.size(), is(0));
    }

    @Test
    public void まず１つ要素を足すとsizeに１が返る() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        assertThat(lru.size(), is(1));
    }

    @Test
    public void 要素を３つ足して最初のを取得するとnullが返る() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.put("c", "dataC");
        assertThat(lru.get("a"), nullValue());
    }

    @Test
    public void デフォルトコンストラクタで生成するとキャッシュサイズ2が返る() {
        lru = new LRUCache();
        assertThat(lru.cacheSize(), is(2));
    }

    @Test
    public void コンストラクタ指定したサイズ3がキャッシュサイズが返る() {
        lru = new LRUCache(3);
        assertThat(lru.cacheSize(), is(3));
    }

    @Test
    public void 要素を３つ足しても要素数がキャッシュサイズを越えない() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.put("c", "dataC");
        assertThat(lru.size(), is(2));
    }

    // 防御的プログラミング
    @Test(expected = IllegalArgumentException.class)
    public void キャッシュサイズに０を渡すと例外が発生する() {
        lru = new LRUCache(0);
    }

    @Test
    public void 文字列を２回putしてgetすると後から追加した値が取得できる() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("a", "dataA2");
        assertThat(lru.get("a"), is("dataA2"));
    }

    @Test
    public void 最初にキーリストを取得すると空のリストが返る() {
        lru = new LRUCache();
        assertThat(lru.getKeyList(), notNullValue());
        assertThat(lru.getKeyList().size(), is(0));
    }

    @Test
    public void 要素を１つ足すとキーリストに値が追加される() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        assertThat(lru.getKeyList(), notNullValue());
        assertThat(lru.getKeyList().size(), is(1));
        assertThat(lru.getKeyList().get(0), is("a"));
    }

    @Test
    public void 要素を2つ足すとキーリストに2つ値が追加される() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");

        assertThat(lru.getKeyList(), notNullValue());
        assertThat(lru.getKeyList().size(), is(2));
        assertThat(lru.getKeyList().get(0), is("a"));
        assertThat(lru.getKeyList().get(1), is("b"));

    }

    @Test
    public void 要素を3つ足すとキーリストにキーリストから最後の二つが返される() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.put("c", "dataC");
        assertThat(lru.getKeyList(), is(Arrays.asList("b", "c")));
    }

    @Test
    public void 最初に値をgetしてもキーリストは空のまま() {
        lru = new LRUCache();
        lru.get("a");
        assertThat(lru.getKeyList(), is(Collections.<String> emptyList()));
    }

    @Test
    public void 要素を2つ追加して最初の値をgetするとキーリストの順番が変わる() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.get("a");
        assertThat(lru.getKeyList(), is(Arrays.asList("b", "a")));
    }

    @Test
    public void 要素を2つ追加して最初の値をgetしてさらに要素を追加したらキーリストの順番が変わる() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.get("a");
        lru.put("c", "dataC");
        assertThat(lru.getKeyList(), is(Arrays.asList("a", "c")));
    }

    @Test
    public void 同じキーを複数回putしてもキーリストのサイズは１() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("a", "dataA");
        lru.put("a", "dataA");
        assertThat(lru.getKeyList(), is(Arrays.asList("a")));

    }

    @Test
    public void キーをputした順番でキーリストに入る() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.put("a", "dataA");
        assertThat(lru.getKeyList(), is(Arrays.asList("b", "a")));

    }

    @Test
    public void キャッシュサイズを越えたときキーをputした順番でキーリストに入る() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.put("b", "dataBB");
        assertThat(lru.getKeyList(), is(Arrays.asList("a", "b")));
    }

    @Test
    public void 要素を3つ足した後最初に追加したキーでgetするとnullになるはず() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.put("c", "dataC");
        assertThat(lru.get("a"), nullValue());
    }

    // TODO キーリストと値のマップの同期が取れていない
    @Ignore
    @Test
    public void 要素を3つ足した後最初に追加したキーでgetしてその後キーリストを取得する() {
        lru = new LRUCache();
        lru.put("a", "dataA");
        lru.put("b", "dataB");
        lru.put("c", "dataC");
        lru.get("a");
        assertThat(lru.getKeyList(), is(Arrays.asList("b", "c")));
    }

}