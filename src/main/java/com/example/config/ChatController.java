/*package com.example.spa.controller;

import com.example.spa.entity.ChatEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private Firestore firestore;

    @PostMapping("/send")
    public void sendMessage(@RequestBody ChatEntity chatMessage) {
        CollectionReference chats = firestore.collection("chats").document(chatMessage.getTeamId()).collection("messages");
        chatMessage.setTimestamp(System.currentTimeMillis());
        chats.add(chatMessage);
    }

    // 특정 팀의 채팅 메시지를 조회하는 엔드포인트
    @GetMapping("/messages/{teamId}")
    public ResponseEntity<List<ChatEntity>> getMessages(@PathVariable String teamId) {
        CollectionReference chats = firestore.collection("chats").document(teamId).collection("messages");
        ApiFuture<QuerySnapshot> querySnapshot = chats.get();


        //Firestore에서 데이터를 가져오는 비동기 작업 처리
        chats.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    chatMessages.add(document.toObject(ChatEntity.class));
                }
            }
        });
        return chatMessages;
    }
}*/
        /*try {
            List<ChatEntity> chatMessages = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                ChatEntity chatMessage = document.toObject(ChatEntity.class);
                chatMessages.add(chatMessage);
            }
            return ResponseEntity.ok(chatMessages);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(500).build();
        }
    }*/


