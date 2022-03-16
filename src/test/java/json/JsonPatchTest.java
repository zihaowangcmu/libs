package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.diff.JsonDiff;
import com.tracelink.dnp.utils.ResourceUtils;
import io.vertx.core.json.Json;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JsonPatchTest {

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test
    public void DiffTest() throws IOException {
        String json1 = ResourceUtils.loadResource("data/dnp-user-1.json");
        String json2 = ResourceUtils.loadResource("data/dnp-user-2.json");
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node1 = mapper.readTree(json1);
        JsonNode node2 = mapper.readTree(json2);

        JsonNode patchNode = JsonDiff.asJson(node1, node2);

        System.out.println(Json.encodePrettily(patchNode));
    }

    @Test
    public void JsonPatchTest() {}
}
