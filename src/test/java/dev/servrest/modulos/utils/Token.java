package dev.servrest.modulos.utils;

public enum Token {

    VALIDO("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNzIyMDI0NzI1LCJleHAiOjE3MjIwMjUzMjV9.YuWFW5n3LUz1fYdIm-L7LDfWIUJNchos2CJpSsuPCJs"),
    INVALIDO("xxxxxxxxxxx"),
    EM_BRANCO(" ");

    private String token;

    Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
