package com.example.springrestdemo.authentication;

import com.example.springrestdemo.exception.error.CharacterInvalidException;
import com.example.springrestdemo.exception.error.LengthInvalidException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwtRequestDeserializer extends StdDeserializer<JwtRequest> {

    public JwtRequestDeserializer() {
        this(null);
    }

    public JwtRequestDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JwtRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        var username = node.get("username").asText();
        var password = node.get("password").asText();

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
        Matcher matcherUsername = pattern.matcher(username);

        if (username.length() > 25 || username.length() < 2 || password.length() > 64 || password.length() < 5) {
            throw new LengthInvalidException();
        }
        if (!matcherUsername.find()) {
            throw new CharacterInvalidException();
        }
        return new JwtRequest(username, password);
    }
}
