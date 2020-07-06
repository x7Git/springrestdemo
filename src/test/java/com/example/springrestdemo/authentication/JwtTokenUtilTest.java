package com.example.springrestdemo.authentication;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class JwtTokenUtilTest {
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImV4cCI6MTU5MjE1MTg3OSwiaWF0IjoxNTkyMTMzODc5fQ._pdSAQ1to8m181swsja4tF7bB-zteJzxx3gQMaJx5jbRzVcHo7hWrLgQlixh_yOyiLZ-Z7JcFCvINVkAbUGr6Q";

    @Mock
    private UserDetails mockUserDetails;

    @InjectMocks
    private JwtTokenUtil classUnderTest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(classUnderTest, "secret", "javainuse");
    }

    @Test
    void getUsernameFromToken_foundUsername_ok() {
        //Arrange
        String expected = "username";
        when(mockUserDetails.getUsername()).thenReturn(expected);
        String token = classUnderTest.generateToken(mockUserDetails);
        //Act
        String result = classUnderTest.getUsernameFromToken(token);
        //Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void validateToken_tokenIsValid_ok() {
        //Arrange
        when(mockUserDetails.getUsername()).thenReturn("username");
        String token = classUnderTest.generateToken(mockUserDetails);
        //Act
        boolean result = classUnderTest.validateToken(token, mockUserDetails);
        //Assert
        assertThat(result).isEqualTo(true);
    }

    @Test
    void validateToken_isExpired_throwsExpiredJwtException() {
        assertThrows(ExpiredJwtException.class, () -> classUnderTest.validateToken(EXPIRED_TOKEN, mockUserDetails));
    }
}