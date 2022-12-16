package com.company.mapper.mappings;

import com.company.dtos.LoanDto;
import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.LoanEntity;
import com.company.repository.models.entity.ReaderEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanDtoToLoanEntity implements Converter<LoanDto, LoanEntity> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public LoanEntity convert(MappingContext<LoanDto, LoanEntity> context) {
        LoanDto loanDto = context.getSource();
        LoanEntity loanEntity = new LoanEntity();

        Optional.ofNullable(loanDto.getBook()).ifPresent(book -> {
            modelMapper.addConverter(new BookDtoToBookEntity());
            loanEntity.setBook(modelMapper.map(book, BookEntity.class));
        });
        Optional.ofNullable(loanDto.getReader()).ifPresent(reader -> {
            modelMapper.addConverter(new ReaderDtoToReaderEntity());
            modelMapper.addConverter(new ParentDtoToParentEntity());
            modelMapper.addConverter(new ChildDtoToChildEntity());
            loanEntity.setReader(modelMapper.map(reader, ReaderEntity.class));
        });
        Optional.ofNullable(loanDto.getBorrowDate()).ifPresent(loanEntity::setBorrowDate);
        Optional.ofNullable(loanDto.getReturnedDate()).ifPresent(loanEntity::setReturnedDate);

        return loanEntity;
    }
}
