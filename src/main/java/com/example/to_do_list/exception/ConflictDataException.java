package com.example.to_do_list.exception;

public class ConflictDataException extends RuntimeException {
    public ConflictDataException(String message) {
        super(message);
    }
}
