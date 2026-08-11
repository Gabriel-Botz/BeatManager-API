package br.com.gabriel.beatmanager.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String mensagem,
        LocalDateTime timestamp
) {
    public ErrorResponse(int status, String mensagem) {
        this(status, mensagem, LocalDateTime.now());
    }
}
