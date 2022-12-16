package com.company.mapper.mappings;

import com.company.dtos.AuthorDto;
import com.company.repository.models.entity.BookEntity;
import com.company.dtos.BookDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookEntityToBookDto implements Converter<BookEntity, BookDto> {

    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    public BookDto convert(MappingContext<BookEntity, BookDto> context) {
        BookEntity bookEntity = context.getSource();
        BookDto bookDto = new BookDto();

        Optional.ofNullable(bookEntity.getId()).ifPresent(bookDto::setId);
        Optional.ofNullable(bookEntity.getTitle()).ifPresent(bookDto::setTitle);
        if(Optional.ofNullable(bookEntity.getAuthor()).isPresent()){
            modelMapper.addConverter(new AuthorEntityToAuthorDto());
            bookDto.setAuthor(modelMapper.map(bookEntity.getAuthor(), AuthorDto.class));
        }
        Optional.ofNullable(bookEntity.getQuantity()).ifPresent(bookDto::setQuantity);
        Optional.ofNullable(bookEntity.getQuantityAvailable()).ifPresent(bookDto::setQuantityAvailable);

        return bookDto;
    }
}
