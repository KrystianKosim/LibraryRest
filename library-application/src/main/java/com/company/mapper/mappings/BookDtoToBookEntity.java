package com.company.mapper.mappings;

import com.company.dtos.BookDto;
import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookDtoToBookEntity implements Converter<BookDto, BookEntity> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public BookEntity convert(MappingContext<BookDto, BookEntity> context) {
        BookDto bookDto = context.getSource();
        BookEntity bookEntity = new BookEntity();

        Optional.ofNullable(bookDto.getId()).ifPresent(bookEntity::setId);
        Optional.ofNullable(bookDto.getTitle()).ifPresent(bookEntity::setTitle);
        Optional.ofNullable(bookDto.getAuthor()).ifPresent(author -> {
            modelMapper.addConverter(new AuthorDtoToAuthorEntity());
            bookEntity.setAuthor(modelMapper.map(author, AuthorEntity.class));
        });
        Optional.ofNullable(bookDto.getQuantity()).ifPresent(bookEntity::setQuantity);
        Optional.ofNullable(bookDto.getQuantityAvailable()).ifPresent(bookEntity::setQuantityAvailable);

        return bookEntity;
    }
}
