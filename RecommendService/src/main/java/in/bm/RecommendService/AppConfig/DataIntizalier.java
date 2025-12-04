package in.bm.RecommendService.AppConfig;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class DataIntizalier {

    @Autowired
    private PineconeVectorStore vectorStore;

    @PostConstruct
    public void dataInit() {
        log.info("DataInitialization service called");
        TextReader textReader = new TextReader(new ClassPathResource("movies.txt"));
        TokenTextSplitter splitter =
                new TokenTextSplitter(200, 50, 10, 2000, true);

        List<Document> document = splitter.split(textReader.get());
        vectorStore.add(document);
    }

}
