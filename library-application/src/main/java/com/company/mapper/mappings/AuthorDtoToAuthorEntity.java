package com.company.mapper.mappings;

import com.company.repository.models.entity.AuthorEntity;
import com.company.dtos.AuthorDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorDtoToAuthorEntity implements Converter<AuthorDto, AuthorEntity> {

    @Override
    public AuthorEntity convert(MappingContext<AuthorDto, AuthorEntity> context) {
        AuthorDto authorDto = context.getSource();
        AuthorEntity authorEntity = new AuthorEntity();

        Optional.ofNullable(authorDto.getId()).ifPresent(authorEntity::setId);
        Optional.ofNullable(authorDto.getName()).ifPresent(authorEntity::setName);
        Optional.ofNullable(authorDto.getSurname()).ifPresent(authorEntity::setSurname);

        return authorEntity;
    }
}
