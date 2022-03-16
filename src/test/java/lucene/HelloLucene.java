package lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// http://www.lucenetutorial.com
public class HelloLucene {

    private StandardAnalyzer analyzer;
    private Directory index;
    private IndexWriterConfig config;

    @Before
    public void before() throws IOException {
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        analyzer = new StandardAnalyzer();

        // 1. create the index
        index = new RAMDirectory();

        config = new IndexWriterConfig(analyzer);
    }

    @After
    public void after() throws IOException {
//        w.close();
    }

    @Test
    public void test() throws IOException, ParseException {

        IndexWriter w = new IndexWriter(index, config);
        addDoc(w, "Lucene in Action", "193398817");
        addDoc(w, "Lucene for Dummies", "55320055Z");
        addDoc(w, "Managing Gigabytes", "55063554A");
        addDoc(w, "The Art of Computer Science", "9900333X");
        addDoc(w, "The stress of computer Science", "9900123X");
        addBytesRefDoc(w, "Zihao");
        w.close();

        // 2. query
        // Default field
//        String querystr = "lucene";
        // TextField supports keyWord match. Note "for"/"in" are not keywords
//        String querystr = "title:Computer";
//        String querystr = "title:Computer";
//        String querystr = "title:Com*";

        // StringField does not support keyWord match
//        String querystr = "isbn:19339";
        // StringField supports exact match
        String querystr = "isbn:55063554A";
        // StringField supports wildcard
//        String querystr = "isbn:19*17";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = new QueryParser("title", analyzer).parse(querystr);

        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }

    private static void addBytesRefDoc(IndexWriter w, String author) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("author", new BytesRef(author), Field.Store.YES));
        w.addDocument(doc);
    }

    @Test
    public void luceneSyntaxTest() throws ParseException {
        String querystr = "index-username:232#ase+-";
        querystr = querystr.replaceAll("\\+|-|&&|\\|\\||!|\\(|\\)|\\{|}|\\[|]|\\^|\"|~|\\*|\\?|:|#|\\|/",
                "_");
        Query q = new QueryParser("title", analyzer).parse(querystr);
    }


}