package com.company.mapper.mappings;

import com.company.repository.models.entity.ReaderEntity;
import com.company.dtos.ReaderDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReaderDtoToReaderEntity implements Converter<ReaderDto, ReaderEntity> {
    @Override
    public ReaderEntity convert(MappingContext<ReaderDto, ReaderEntity> context) {
        ReaderDto readerDto = context.getSource();
        ReaderEntity readerEntity = new ReaderEntity();

        Optional.ofNullable(readerDto.getId()).ifPresent(readerEntity::setId);
        Optional.ofNullable(readerDto.getName()).ifPresent(readerEntity::setName);
        Optional.ofNullable(readerDto.getSurname()).ifPresent(readerEntity::setSurname);
        Optional.ofNullable(readerDto.getBirthDate()).ifPresent(readerEntity::setBirthDate);
        Optional.ofNullable(readerDto.getNumberOfCurrentlyBorrowedBooks()).ifPresent(readerEntity::setNumberOfCurrentlyBorrowedBooks);
        Optional.ofNullable(readerDto.getNumberOfEveryBorrowedBooks()).ifPresent(readerEntity::setNumberOfEveryBorrowedBooks);

        return readerEntity;
    }
}
