package com.example.springrestdemo.authentication;

import com.example.springrestdemo.exception.error.CharacterInvalidException;
import com.example.springrestdemo.exception.error.LengthInvalidException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class JwtRequestDeserializerTest {

    @Mock
    private JsonParser mockJsonParser;
    @Mock
    private JsonNode mockJsonNode;

    @InjectMocks
    private JwtRequestDeserializer classUnderTest;

    @BeforeEach
    public void setUp() throws Exception {
        when(mockJsonParser.getCodec()).thenReturn(mock(ObjectCodec.class));
        when(mockJsonParser.getCodec().readTree(any())).thenReturn(mockJsonNode);

        when(mockJsonNode.get(anyString())).thenReturn(mockJsonNode);
    }

    @Test
    void deserialize_StringValid_ok() throws IOException {
        //Arrange
        when(mockJsonNode.get(anyString()).asText()).thenReturn("usernamePassword");
        //Act
        JwtRequest result = classUnderTest.deserialize(mockJsonParser, mock(DeserializationContext.class));
        //Assert
        assertThat(result.getUsername()).isEqualTo("usernamePassword");
        assertThat(result.getPassword()).isEqualTo("usernamePassword");
    }

    @Test
    void deserialize_Strings2Long_throwLengthInvalidException() {
        //Arrange
        when(mockJsonNode.get(anyString()).asText()).thenReturn("AsqOoRTGjdwGJQayOPKiKmoJmRxtNxnKjDCGYsEVFSmlgXfsJvdMaDVnQXMlRDbamwZlgUORKECLWJMlbyKAMJwAVDetuHcjheoSwGfMdbBRbHTYnafXrvnUkIBPOAlUTVwgCXTpLXTmOZqwLGuRmQDZWiwsrYEDySYDpvqdxXASDTjXSDFWXLlgLlEiwYjsJCXSggKLrWaoFRrPMfWnpFnpbUOwWreASmyOAeTPxmxkCellEIqkbtNfoaNHehnIKmlFAKgCpXfaZsKgIXSBiIRxvjeyRyNannODscutwwmffOCjWPNfywWIrliQOpfGaxlicPebORMgVoAfeHDkaGZymngfIUjffcgXNtbicoECdsMoAZKbXCsoabKMFykEHxlvLPuGcFgaGOAFCOFGlmbQPgggsdGsfHZjvtgnEAk");
        //Act
        assertThrows(LengthInvalidException.class, () -> classUnderTest.deserialize(mockJsonParser, mock(DeserializationContext.class)));
    }

    @Test
    void deserialize_InvalidCharacters_throwLengthInvalidException() {
        //Arrange
        when(mockJsonNode.get(anyString()).asText()).thenReturn("<$%§$&/)=?");
        //Act
        assertThrows(CharacterInvalidException.class, () -> classUnderTest.deserialize(mockJsonParser, mock(DeserializationContext.class)));
    }
}