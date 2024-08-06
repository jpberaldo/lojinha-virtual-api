package dev.servrest.modulos.utils;

public enum Token {

    VALIDO("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZ1bGFub0BxYS5jb20iLCJwYXNzd29yZCI6InRlc3RlIiwiaWF0IjoxNzIyOTg0NTIzLCJleHAiOjE3MjI5ODUxMjN9.xDahoaLuNwPcux3B3wDHNO0jsAtMN55XjBt-f6rPbSo"),
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
