package com.jellybrains.quietspace.chat_service.mapper.custom;

import com.jellybrains.quietspace.chat_service.entity.Chat;
import com.jellybrains.quietspace.chat_service.service.MessageService;
import com.jellybrains.quietspace.common_service.model.request.ChatRequest;
import com.jellybrains.quietspace.common_service.model.response.ChatResponse;
import com.jellybrains.quietspace.common_service.model.response.MessageResponse;
import com.jellybrains.quietspace.common_service.model.response.UserResponse;
import com.jellybrains.quietspace.common_service.webclient.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final MessageService messageService;
    private final UserClient userClient;

    public Chat chatRequestToEntity(ChatRequest chatRequest) {
        return Chat.builder()
                .memberIds(chatRequest.getUserIds())
                .build();
    }

    public ChatResponse chatEntityToResponse(Chat chat) {
        return ChatResponse.builder()
                .id(chat.getId())
                .userIds(chat.getMemberIds())
                .members(getChatMembers(chat))
                .recentMessage(getLastMessage(chat))
                .createDate(chat.getCreateDate())
                .updateDate(chat.getUpdateDate())
                .build();
    }

    private MessageResponse getLastMessage(Chat chat) {
        return messageService.getLastMessageByChat(chat).block();
    }


    private List<UserResponse> getChatMembers(Chat chat) {
        return userClient.getUsersFromIdList(chat.getMemberIds()).join();
    }
}
