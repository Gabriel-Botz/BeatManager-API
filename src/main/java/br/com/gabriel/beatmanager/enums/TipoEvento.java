package br.com.gabriel.beatmanager.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo do evento de música eletrônica")
public enum TipoEvento {

    RAVE,
    FESTIVAL,
    SHOW,
    CLUBNIGHT,
    POOLPARTY,
    AFTER
}
