package com.pdv.lalapan.handler;

import java.time.LocalDateTime;

public record ErroResponse(String mensagem, LocalDateTime timestamp, int status, Object detalhes) {
}
