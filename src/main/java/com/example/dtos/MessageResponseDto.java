package com.example.dtos;

public class MessageResponseDto {

    private boolean success;
    private String message;

    public MessageResponseDto(String message) {
        this.success = true;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}