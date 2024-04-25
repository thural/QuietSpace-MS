package com.jellybrains.quietspace_backend_ms.chatservice.controller;

import com.jellybrains.quietspace_backend_ms.chatservice.dto.request.ChatRequest;
import com.jellybrains.quietspace_backend_ms.chatservice.dto.response.ChatResponse;
import com.jellybrains.quietspace_backend_ms.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatController {

    public static final String CHAT_PATH = "/api/v1/chats";

    private final ChatService chatService;

    @RequestMapping(value = CHAT_PATH + "/{chatId}", method = RequestMethod.GET)
    ChatResponse getSingleChatById(@PathVariable("chatId") UUID chatId) {

        return chatService.getChatById(chatId);
    }

    @RequestMapping(value = CHAT_PATH + "/member/{userId}", method = RequestMethod.GET)
    List<ChatResponse> getChatsByMemberId(@PathVariable("userId") UUID userId) {

        return chatService.getChatsByUserId(userId);
    }

    @RequestMapping(value = CHAT_PATH, method = RequestMethod.POST)
    ResponseEntity<?> createChat(@RequestBody ChatRequest chat) {

        ChatResponse createdChatResponse = chatService.createChat(chat);
        return new ResponseEntity<>(createdChatResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = CHAT_PATH + "/{chatId}/members/add/{userId}", method = RequestMethod.PATCH)
    ResponseEntity<?> addMemberWithId(@PathVariable("chatId") UUID chatId,
                                      @PathVariable("userId") UUID userId) {

        chatService.addMemberWithId(userId, chatId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = CHAT_PATH + "/{chatId}/members/remove/{userId}", method = RequestMethod.PATCH)
    ResponseEntity<?> removeMemberWithId(@PathVariable("chatId") UUID chatId,
                                         @PathVariable("userId") UUID userId) {

        chatService.removeMemberWithId(userId, chatId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = CHAT_PATH + "/{chatId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteChatWithId(@PathVariable("chatId") UUID chatId) {

        chatService.deleteChatById(chatId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
