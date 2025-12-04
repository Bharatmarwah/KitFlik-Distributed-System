package in.bm.RecommendService.Controller;

import in.bm.RecommendService.Service.ChatService;
import in.bm.RecommendService.Service.ClassifierService;
import in.bm.RecommendService.Service.RagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ClassifierService classifierService;

    @Autowired
    private RagService ragService;

    @Autowired
    private ChatService chatService;


    @PostMapping("/recommend")
    public ResponseEntity<?> call(@RequestBody String message ,@RequestHeader("Username")String username) {
        log.info("Username is: {}",username);
        String classifiedMessage = classifierService.classify(message);
        if (classifiedMessage.equals("MOVIE_QUERY")){
            return ResponseEntity.ok(ragService.recommend(message,username));
        }
        else{
            return ResponseEntity.ok(chatService.normalChat(message,username));
        }
    }
}