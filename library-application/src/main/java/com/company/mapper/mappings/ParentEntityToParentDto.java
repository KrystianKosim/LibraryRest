package com.company.mapper.mappings;

import com.company.repository.models.entity.ParentEntity;
import com.company.dtos.ParentDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParentEntityToParentDto implements Converter<ParentEntity, ParentDto> {
    @Override
    public ParentDto convert(MappingContext<ParentEntity, ParentDto> context) {
        ParentEntity parentEntity = context.getSource();
        ParentDto parentDto = new ParentDto();


        Optional.ofNullable(parentEntity.getId()).ifPresent(parentDto::setId);
        Optional.ofNullable(parentEntity.getName()).ifPresent(parentDto::setName);
        Optional.ofNullable(parentEntity.getSurname()).ifPresent(parentDto::setSurname);
        Optional.ofNullable(parentEntity.getBirthDate()).ifPresent(parentDto::setBirthDate);
        Optional.ofNullable(parentEntity.getNumberOfCurrentlyBorrowedBooks()).ifPresent(parentDto::setNumberOfCurrentlyBorrowedBooks);
        Optional.ofNullable(parentEntity.getNumberOfEveryBorrowedBooks()).ifPresent(parentDto::setNumberOfEveryBorrowedBooks);
        Optional.ofNullable(parentEntity.getAddress()).ifPresent(parentDto::setAddress);
        Optional.ofNullable(parentEntity.getPhoneNumber()).ifPresent(parentDto::setPhoneNumber);

        return parentDto;
    }
}
