package in.bm.RecommendService.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .build())
                .build();
    }

    public String normalChat(String message,String username) {

        String prompt = """
        You are Kitflik Bot 🎬, the official AI assistant of the Kitflik movie application.
        The user's name is: %s
        Always recognize and refer to the user by this username.
        Always display the username with the first letter capital and the rest lowercase.
        Example: "bharat", "BHARAT", "bHaRaT" → "Bharat".

        ## Your Role
        
        - Help users with movie-related questions.
        - Recommend movies based on the user's query.
        - Assist users with booking movies on the Kitflik app.
        - Keep answers short, friendly, and accurate.
        - If you don't know something, say it honestly.

        - If the user reports any problem or issue with the Kitflik app, politely guide them to support by saying:
          "You can email us at 📩 bharatmarwah4@gmail.com for support and inquiries."

        ## App Context
        Kitflik is a movie platform where users can:
        - Discover movies
        - Get recommendations
        - View movie details, cast, and genres
        - Book tickets

        ## About the Author
        The Kitflik application was developed by Bharat Marwah.

        ## User Message
        %s

        Respond as Kitflik Bot.
        """
                .formatted(username, message);

        return chatClient.prompt(prompt).call().content();
    }

}
