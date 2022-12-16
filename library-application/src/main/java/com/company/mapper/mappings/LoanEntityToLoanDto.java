package com.company.mapper.mappings;

import com.company.dtos.BookDto;
import com.company.dtos.LoanDto;
import com.company.dtos.ReaderDto;
import com.company.repository.models.entity.LoanEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanEntityToLoanDto implements Converter<LoanEntity, LoanDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public LoanDto convert(MappingContext<LoanEntity, LoanDto> context) {
        LoanEntity loanEntity = context.getSource();
        LoanDto loanDto = new LoanDto();

        Optional.ofNullable(loanEntity.getBook()).ifPresent(book -> {
            modelMapper.addConverter(new BookEntityToBookDto());
            loanDto.setBook(modelMapper.map(book, BookDto.class));
        });
        Optional.ofNullable(loanEntity.getReader()).ifPresent(reader -> {
            modelMapper.addConverter(new ReaderEntityToReaderDto());
            modelMapper.addConverter(new ParentEntityToParentDto());
            modelMapper.addConverter(new ChildEntityToChildDto());
            loanDto.setReader(modelMapper.map(reader, ReaderDto.class));
        });
        Optional.ofNullable(loanEntity.getBorrowDate()).ifPresent(loanDto::setBorrowDate);
        Optional.ofNullable(loanEntity.getReturnedDate()).ifPresent(loanDto::setReturnedDate);

        return loanDto;
    }
}
