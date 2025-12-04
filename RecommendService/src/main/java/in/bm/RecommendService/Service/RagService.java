package in.bm.RecommendService.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RagService {

    @Autowired
    PineconeVectorStore vectorStore;

    private final ChatClient chatClient;
    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

    public RagService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    public String recommend(String message, String username) {

        List<Document> documents = vectorStore.similaritySearch(message);
        if (documents.isEmpty()) {
            log.warn("Movies not present in the database");
        }

        String retrievedContext = documents.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n\n" + b);

        return chatClient
                .prompt()
                .system("""
                         You are Kitflik Movie Recommendation AI 🎬.
                         Always recognize and refer to the user by this username.
                         Always display the username with the first letter capital and the rest lowercase.
                         Example: "bharat", "BHARAT", "bHaRaT" → "Bharat".
                         Use ONLY the movie data provided. Never invent movies or details.
                         Use emojis appropriately.
                         Always follow strict line-by-line formatting.
                        
                                        --- MOVIE DATA ---
                                        %s
                                        ------------------
                        
                                        TASK RULES:
                                        1. If the user's movie exists, show ONLY:
                                           Title: <movie>
                                           Genre: <genre>
                                           IMDb: <rating>
                                           About: <short summary>
                        
                                        2. If NO exact movie match:
                                           Output:
                                           No exact match found. Here are similar movies:
                        
                                           1)
                                           Title: <movie>
                                           Genre: <genre>
                                           IMDb: <rating>
                                           Reason: <why it's similar>
                        
                                           2)
                                           Title: <movie>
                                           Genre: <genre>
                                           IMDb: <rating>
                                           Reason: <why it's similar>
                        
                                        STRICT RULES:
                                        - No paragraphs EVER.
                                        - Each field MUST be on a new line.
                                        - No invented movies or cast.
                                        - Apply user conditions strictly (genre, rating, language).
                                        - If nothing matches EXACTLY:
                                          "Movie not found in database"
                                        - Never mention RAG, vectors, Pinecone, or databases.
                        
                        """.formatted(retrievedContext, username))
                .user(message)
                .call()
                .content();
    }
}