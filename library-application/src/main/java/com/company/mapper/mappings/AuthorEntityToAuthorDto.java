package com.company.mapper.mappings;

import com.company.repository.models.entity.AuthorEntity;
import com.company.dtos.AuthorDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorEntityToAuthorDto implements Converter<AuthorEntity, AuthorDto> {

    @Override
    public AuthorDto convert(MappingContext<AuthorEntity, AuthorDto> context) {
        AuthorEntity authorEntity = context.getSource();
        AuthorDto authorDto = new AuthorDto();

        Optional.ofNullable(authorEntity.getId()).ifPresent(authorDto::setId);
        Optional.ofNullable(authorEntity.getName()).ifPresent(authorDto::setName);
        Optional.ofNullable(authorEntity.getSurname()).ifPresent(authorDto::setSurname);

        return authorDto;
    }
}
