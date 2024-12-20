package com.goosegame.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ItemDoesNotBelongToUserException extends RuntimeException{
    public ItemDoesNotBelongToUserException(String message) {
        super(message);
    }
}
