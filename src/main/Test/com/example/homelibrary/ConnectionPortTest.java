package com.example.homelibrary;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPortTest {

    @Test
    void should_ThrowNullPointerException_When_RequestString_Invalid(){
        // given
        String requestString = "1";

        // when
        Executable executable = () -> ConnectionPort.makeRequest(requestString);

        // then
        assertThrows(NullPointerException.class, executable);
    }




}