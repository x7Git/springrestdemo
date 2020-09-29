package com.example.springrestdemo.authentication;

import com.example.springrestdemo.service.CustomerDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.ServletException;
import java.io.IOException;

import static com.example.springrestdemo.TestValues.JWT_TOKEN;
import static com.example.springrestdemo.TestValues.JWT_TOKEN_BEARER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class JwtRequestFilterTest {

    @Mock
    private CustomerDetailsService mockCustomerDetailsService;
    @Mock
    private JwtTokenUtil mockJwtTokenUtil;
    @InjectMocks
    private JwtRequestFilter classUnderTest;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    @Test
    void getJwtToken_HeaderIsNull_returnEmptyString() {
        //Act
        String s = classUnderTest.getJwtToken(request);
        //Assert
        assertThat(s).isNotNull();
        assertThat(s).isEmpty();
    }

    @Test
    void getJwtToken_HeaderDoesntStartWithBearer_returnEmptyString() {
        //Arrange
        request.addHeader("Authorization", JWT_TOKEN);
        //Act
        String resultToken = classUnderTest.getJwtToken(request);
        //Assert
        assertThat(resultToken).isEmpty();
        assertThat(resultToken).isNotNull();
    }

    @Test
    void getJwtToken_JwtTokenWithBearer_jwtTokenRemovedBearer() {
        //Arrange
        request.addHeader("Authorization", JWT_TOKEN_BEARER);
        //Act
        String resultToken = classUnderTest.getJwtToken(request);
        //Assert
        assertThat(resultToken).isEqualTo(JWT_TOKEN);
    }

    @Test
    void getUsername_TokenOk_ok() {
        //Arrange
        when(mockJwtTokenUtil.getUsernameFromToken(JWT_TOKEN)).thenReturn("username");
        //Act
        String username = classUnderTest.getUserName(JWT_TOKEN);
        //Assert
        assertThat(username).isEqualTo("username");
    }

    @Test
    void getUsername_TokenExpired_usernameIsEmpty() {
        //Arrange
        when(mockJwtTokenUtil.getUsernameFromToken(JWT_TOKEN)).thenThrow(ExpiredJwtException.class);
        //Act
        String username = classUnderTest.getUserName(JWT_TOKEN);
        //Assert
        assertThat(username).isEmpty();
    }

    @Test
    void getUsername_UnableToGetToken_usernameIsEmpty() {
        //Arrange
        when(mockJwtTokenUtil.getUsernameFromToken(JWT_TOKEN)).thenThrow(IllegalArgumentException.class);
        //Act
        String username = classUnderTest.getUserName(JWT_TOKEN);
        //Assert
        assertThat(username).isEmpty();
    }

    @Test
    void doFilterInternal_authorized_ok() throws ServletException, IOException {
        //Arrange
        when(mockJwtTokenUtil.getUsernameFromToken(JWT_TOKEN)).thenReturn("username");
        when(mockCustomerDetailsService.loadUserByUsername("username")).thenReturn(mock(UserDetails.class));
        request.setRequestURI("/customer");
        request.addHeader("Authorization", JWT_TOKEN_BEARER);
        //Act
        classUnderTest.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain());
        //Assert
        verify(mockCustomerDetailsService).loadUserByUsername("username");
    }

    @Test
    void authenticateUser_validateToken_ok() {
        //Arrange
        when(mockJwtTokenUtil.validateToken(anyString(), any())).thenReturn(true);
        //Act
        classUnderTest.authenticateUser(mock(UserDetails.class), "Token", request);
    }
}