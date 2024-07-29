package com.softserve.itacademy.exception;

import com.softserve.itacademy.tools.AppLogger;

import java.util.logging.Logger;

public class DuplicateEventException extends Exception {

    public DuplicateEventException(String message) {
        super(message);
    }
}
