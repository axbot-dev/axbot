package com.github.axiangcoding.axbot.server.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {
    @Resource
    OpenAiService openAiService;

    final static String SYSTEM_PROMPT = "you are a ai assistant call 'AxBot', you should help user find information";
    final static String CHAT_MODEL = "gpt-3.5-turbo";

    public String singleChat(String ask) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage cm1 = new ChatMessage();
        cm1.setRole(ChatMessageRole.SYSTEM.value());
        cm1.setContent(SYSTEM_PROMPT);
        ChatMessage cm2 = new ChatMessage();
        cm2.setRole(ChatMessageRole.USER.value());
        cm2.setContent(ask);
        messages.add(cm1);
        messages.add(cm2);
        return chat(messages);
    }

    private String chat(List<ChatMessage> messages) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(CHAT_MODEL)
                .messages(messages)
                .topP(1.0)
                .temperature(0.8)
                .maxTokens(800).build();
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(request);
        return chatCompletion.getChoices().get(0).getMessage().getContent();
    }
}
