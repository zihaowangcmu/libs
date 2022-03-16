package json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.filter.ValueNode;
import com.tracelink.dnp.utils.ResourceUtils;
import com.tracelink.dnp.utils.parser.Json;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.jayway.jsonpath.JsonPath.using;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for JsonPath
 * https://github.com/json-path/JsonPath
 */
public class JsonPathTest {

    private String DNP_USER_SCHEMA;
    private String DNP_USER;

    @Before
    public void before() {
        this.DNP_USER_SCHEMA = ResourceUtils.loadResource("/schema/wv.dnp-user-schema.v1.json");
        this.DNP_USER = ResourceUtils.loadResource("/data/dnp-user-1.json");
    }

    /**
     * Return Path for query
     */
    @Test
    public void pathTest() {
        Configuration conf = Configuration.builder()
                .options(Option.AS_PATH_LIST).build();
        DocumentContext documentContext = using(conf).parse(DNP_USER_SCHEMA);
        List<String> pathList;

        // All paths that contains field luceneStore
        pathList = documentContext.read("$..luceneStore");
        System.out.println(Json.encode(pathList));
        assertEquals(pathList.size(), 4);
        System.out.println(Json.encode(pathList));
        assertTrue(pathList.containsAll(List.of(
                "$['definitions']['data']['properties']['username']['luceneStore']",
                "$['definitions']['data']['properties']['address']['properties']['country']['luceneStore']",
                "$['definitions']['data']['properties']['phones']['items']['properties']['phoneNumberType']['luceneStore']",
                "$['definitions']['data']['properties']['phones']['items']['properties']['phoneNumberValue']['luceneStore']")));

        // All fields whose luceneStore is true
        pathList = documentContext.read("$..[?(@.luceneStore==true)]");
        System.out.println(Json.encode(pathList));
        assertEquals(pathList.size(), 3);
        assertTrue(pathList.containsAll(List.of(
                "$['definitions']['data']['properties']['username']",
                "$['definitions']['data']['properties']['phones']['items']['properties']['phoneNumberType']",
                "$['definitions']['data']['properties']['address']['properties']['country']")));

        // All fields having snippet
        pathList = documentContext.read("$..[?(@.snippet)]");
        System.out.println(Json.encode(pathList));
        assertEquals(pathList.size(), 1);
        assertTrue(pathList.containsAll(List.of(
                "$['definitions']['data']['properties']['college']")));
    }

    /**
     * Return Value for read
     */
    @Test
    public void valueTest() {
        Configuration readCtxConf = Configuration.builder()
                .options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        ReadContext dataCtx = JsonPath.parse(DNP_USER, readCtxConf);

        DocumentContext dataDocCtx = JsonPath.parse(DNP_USER);

        Configuration conf = Configuration.builder()
                .options(Option.AS_PATH_LIST).build();
        DocumentContext schemaDocCtx = using(conf).parse(DNP_USER_SCHEMA);
        List<String> pathList;

        String username = dataCtx.read("$['data']['username']");
        System.out.println(Json.encode(username));

        List<ValueNode.JsonNode> phones = dataCtx.read("$['data']['phones']");
        System.out.println(Json.encode(phones));

        pathList = schemaDocCtx.read("$..phoneNumberType");
        System.out.println(Json.encode(pathList));

        List<String> phoneNumberType = dataCtx.read("$['data']['phones'][*]['phoneNumberType']");
        System.out.println(Json.encode(phoneNumberType));

        // This will set the whole array(phoneNumberType) to every item
        phoneNumberType = List.of("1", "2", "3");
        String newJson = dataDocCtx.set("$['data']['phones'][*]['phoneNumberType']", phoneNumberType).jsonString();
        System.out.println(newJson);

        dataDocCtx.set("$['data']['phones'][0]['phoneNumberType']", "t1");
        dataDocCtx.set("$['data']['phones'][1]['phoneNumberType']", "t2");
        dataDocCtx.set("$['data']['phones'][2]['phoneNumberType']", "t3");
        newJson = dataDocCtx.jsonString();
        System.out.println(newJson);
    }
}
