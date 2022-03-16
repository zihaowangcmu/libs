import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BinaryTest {

    @Test
    public void zwangTest() throws JsonProcessingException {
        String qs = "{\"indexScopes\":[\"application-manager:manifest#\"],\"predicates\":[{\"$type\":\"com.tracelink.wv.common.model.query.predicates.And\",\"operator\":\"AND\",\"clauses\":[{\"$type\":\"com.tracelink.wv.common.model.query.predicates.True\",\"operator\":\"TRUE\",\"clauses\":[{\"$type\":\"com.tracelink.wv.common.model.query.terms.Equals\",\"lowerBound\":[102,101,100,53,48,52,55,50,45,102,100,54,97,45,52,57,52,54,45,97,51,102,52,45,98,50,102,50,100,53,97,99,50,97,102,53],\"upperBound\":[102,101,100,53,48,52,55,50,45,102,100,54,97,45,52,57,52,54,45,97,51,102,52,45,98,50,102,50,100,53,97,99,50,97,102,53],\"origSchemaId\":\"application-manager:manifest\",\"schemaId\":\"application-manager:manifest\",\"indexName\":\"application-manager:manifest_index-applicationToId\"}],\"isRoot\":false},{\"$type\":\"com.tracelink.wv.common.model.query.terms.Equals\",\"lowerBound\":[48,48,48,48,48,48,48,48,45,48,48,48,48,45,48,48,48,48,45,48,48,48,48,45,48,48,48,48,48,48,48,48,48,48,48,48],\"upperBound\":[48,48,48,48,48,48,48,48,45,48,48,48,48,45,48,48,48,48,45,48,48,48,48,45,48,48,48,48,48,48,48,48,48,48,48,48],\"schemaId\":\"application-manager:manifest\",\"indexName\":\"application-manager:manifest_indexOwnerId\"}],\"isRoot\":false}],\"maximumResults\":1000,\"keyFormat\":\"PRIMARY_KEY\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(qs);
        JsonNode upperBound = jsonNode.get("predicates").get(0).get("clauses").get(0).get("clauses").get(0).get("upperBound");
        List<Integer> list = objectMapper.convertValue(upperBound, ArrayList.class);
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = list.get(i).byteValue();
        }
        String s = new String(bytes);
        System.out.println("zwang: UB: " + s);
    }
}
