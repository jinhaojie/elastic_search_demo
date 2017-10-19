import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jhj on 17-10-16.
 */
public class JestTest {

    private String indexName = "test1";
    private String typeName = "user";


    private JestUtil jestUtil = new JestUtil();

    private JestClient client = JestUtil.getJestClient();


    @Test
    public void testCreateIndex() {

        try {
            jestUtil.createIndex(client, indexName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateIndexMapping() {
        @Language("json")
        String source = "{\n" +
                "  \"user\":{\n" +
                "    \"properties\":{\n" +
                "      \"id\":{\n" +
                "        \"type\":\"integer\"\n" +
                "      },\n" +
                "      \"name\":{\n" +
                "        \"type\":\"string\",\n" +
                "        \"index\":\"not_analyzed\"\n" +
                "      },\n" +
                "      \"birth\":{\n" +
                "        \"type\":\"date\",\n" +
                "        \"format\":\"strict_date_optional_time||epoch_millis\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";


        try {
            boolean rst = jestUtil.createIndexMapping(client, indexName, typeName, source);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testGetIndexMapping() {
        try {
            System.out.println(jestUtil.getIndexMapping(client, indexName, typeName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果数据存在，则更具id更新，如果不存在，则插入
     */
    @Test
    public void testIndex() {
        try {
            UserDO userDO = new UserDO();
            userDO.setBirth(new Date());
            userDO.setId(1);
            userDO.setName("靳昊杰sadf");
            boolean rst = jestUtil.index(client, indexName, typeName, Arrays.<Object>asList(userDO));
            System.out.println(rst);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文档
     */
    @Test
    public void testGet() {
        try {
            JestResult jestResult = jestUtil.get(client, indexName, typeName, "1");
            System.out.println(jestResult);

            JsonObject jsonObject = jestResult.getJsonObject();
            List<UserDO> userDOS = jestResult.getSourceAsObjectList(UserDO.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 删除索引
     */
    @Test
    public void testDeleteIndex() {
        try {
            String indexName = "customer";
            jestUtil.delete(client, indexName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void f() {

        try {
            String indexName = "news";
            String typeName = "user";

            UserDO u = new UserDO();
            u.setName("jinhaoje");
            u.setId(3);
            u.setBirth(new Date());
            List<Object> users = new ArrayList<Object>();
            users.add(u);
            JsonObject user = new JsonObject();
            user.addProperty("jin", "jinhaojie111");
            String source = "{\"" + typeName + "\":{\"properties\":{"
                    + "\"id\":{\"type\":\"integer\"}"
                    + ",\"name\":{\"type\":\"string\",\"index\":\"not_analyzed\"}"
                    + ",\"birth\":{\"type\":\"date\",\"format\":\"strict_date_optional_time||epoch_millis\"}"
                    + "}}}";

            boolean rst = jestUtil.createIndexMapping(client, indexName, typeName, source);

            boolean indexrst=  jestUtil.index(client, indexName, typeName, users);

            String[] name = new String[]{"jinhaojie" };
            String from = "2016-09-01T00:00:00";
            String to = "2018-10-21T00:00:00";
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "jinhaoje");
//                    .must(QueryBuilders.termsQuery("name", name))
//                    .must(QueryBuilders.rangeQuery("birth").gte(from).lte(to));
            searchSourceBuilder.query(queryBuilder);
            String query = searchSourceBuilder.toString();
            System.out.println(query);
            SearchResult result = jestUtil.search(client, indexName, typeName, query);

            List<SearchResult.Hit<UserDO, Void>> hits = result.getHits(UserDO.class);
            for (SearchResult.Hit<UserDO, Void> hit : hits) {
                UserDO uu = hit.source;
                System.out.println(uu.getName());
            }

            client.shutdownClient();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
