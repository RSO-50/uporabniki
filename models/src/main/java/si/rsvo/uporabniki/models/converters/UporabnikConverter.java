package si.rsvo.uporabniki.models.converters;

import si.rsvo.uporabniki.lib.Uporabnik;
import si.rsvo.uporabniki.models.entities.UporabnikEntity;

public class UporabnikConverter {

    public static Uporabnik toDto(UporabnikEntity entity) {

        Uporabnik dto = new Uporabnik();
        dto.setId(entity.getId());
        dto.setIme(entity.getIme());
        dto.setPriimek(entity.getPriimek());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());

        return dto;

    }

    public static UporabnikEntity toEntity(Uporabnik dto) {

        UporabnikEntity entity = new UporabnikEntity();
        entity.setId(dto.getId());
        entity.setIme(dto.getIme());
        entity.setPriimek(dto.getPriimek());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());

        return entity;

    }

}
