package in.bm.RecommendService.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ClassifierService {

    private final ChatClient chatClient;

    public ClassifierService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String classify(String message) {
        String prompt = """
                Classify the message into ONE category:
                
                MOVIE_QUERY  
                → Use ONLY if the user asks about movies, films, cast, directors, genres, plots, summaries, ratings, similar movies, or recommendations.
                
                CHAT  
                → Use for EVERYTHING else:
                  - Kitflik app issues (gateway, backend, login)
                  - booking/showtimes/payments
                  - tech/coding questions
                  - greetings, casual chat
                  - any non-movie topic
                
                Rules:
                - Kitflik system messages → CHAT
                - Booking/ticket/showtimes → CHAT
                - If unsure → CHAT
                Return ONLY: MOVIE_QUERY or CHAT
                Message: "%s"
                """.formatted(message);

        return chatClient.prompt(prompt).call().content().trim();
    }

}
