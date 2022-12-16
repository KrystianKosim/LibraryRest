package com.company.mapper.mappings;

import com.company.repository.models.entity.ReaderEntity;
import com.company.dtos.ReaderDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReaderEntityToReaderDto implements Converter<ReaderEntity, ReaderDto> {

    @Override
    public ReaderDto convert(MappingContext<ReaderEntity, ReaderDto> context) {
        ReaderEntity readerEntity = context.getSource();
        ReaderDto readerDto = new ReaderDto();

        Optional.ofNullable(readerEntity.getId()).ifPresent(readerDto::setId);
        Optional.ofNullable(readerEntity.getName()).ifPresent(readerDto::setName);
        Optional.ofNullable(readerEntity.getSurname()).ifPresent(readerDto::setSurname);
        Optional.ofNullable(readerEntity.getBirthDate()).ifPresent(readerDto::setBirthDate);
        Optional.ofNullable(readerEntity.getNumberOfCurrentlyBorrowedBooks()).ifPresent(readerDto::setNumberOfCurrentlyBorrowedBooks);
        Optional.ofNullable(readerEntity.getNumberOfEveryBorrowedBooks()).ifPresent(readerDto::setNumberOfEveryBorrowedBooks);

        return readerDto;
    }
}
