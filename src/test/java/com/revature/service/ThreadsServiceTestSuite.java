package com.revature.service;

import com.revature.get_comments.Comment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ThreadsServiceTestSuite {

    ThreadsService sut;

    @BeforeEach
    public void caseSetUp() {
        sut = new ThreadsService();
    }

    @AfterEach
    public void caseCleanUp() {
        sut = null;
    }

    @Test
    public void isValidParent_returnsTrue_whenGivenValidParent() {
        //Arrange
        Comment mockParent = Comment.builder()
                .id("valid")
                .parent("valid")
                .ancestors(new ArrayList<>(Arrays.asList("subforum")))
                .description("valid")
                .owner("user")
                .build();

        //Act
        boolean result = sut.isValidParent(mockParent);

        //Assert
        assertTrue(result);
    }

}
