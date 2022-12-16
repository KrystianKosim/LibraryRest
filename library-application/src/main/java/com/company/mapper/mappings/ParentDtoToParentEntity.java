package com.company.mapper.mappings;

import com.company.repository.models.entity.ParentEntity;
import com.company.dtos.ParentDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParentDtoToParentEntity implements Converter<ParentDto, ParentEntity> {

    @Override
    public ParentEntity convert(MappingContext<ParentDto, ParentEntity> context) {
        ParentDto parentDto = context.getSource();
        ParentEntity parentEntity = new ParentEntity();

        Optional.ofNullable(parentDto.getId()).ifPresent(parentEntity::setId);
        Optional.ofNullable(parentDto.getName()).ifPresent(parentEntity::setName);
        Optional.ofNullable(parentDto.getSurname()).ifPresent(parentEntity::setSurname);
        Optional.ofNullable(parentDto.getBirthDate()).ifPresent(parentEntity::setBirthDate);
        Optional.ofNullable(parentDto.getNumberOfCurrentlyBorrowedBooks()).ifPresent(parentEntity::setNumberOfCurrentlyBorrowedBooks);
        Optional.ofNullable(parentDto.getNumberOfEveryBorrowedBooks()).ifPresent(parentEntity::setNumberOfEveryBorrowedBooks);
        Optional.ofNullable(parentDto.getAddress()).ifPresent(parentEntity::setAddress);
        Optional.ofNullable(parentDto.getPhoneNumber()).ifPresent(parentEntity::setPhoneNumber);

        return parentEntity;
    }
}
