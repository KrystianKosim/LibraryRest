package com.company.mapper.mappings;

import com.company.dtos.ParentDto;
import com.company.repository.models.entity.ChildEntity;
import com.company.dtos.ChildDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChildEntityToChildDto implements Converter<ChildEntity, ChildDto> {

    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    public ChildDto convert(MappingContext<ChildEntity, ChildDto> context) {
        ChildEntity childEntity = context.getSource();
        ChildDto childDto = new ChildDto();


        Optional.ofNullable(childEntity.getId()).ifPresent(childDto::setId);
        Optional.ofNullable(childEntity.getName()).ifPresent(childDto::setName);
        Optional.ofNullable(childEntity.getSurname()).ifPresent(childDto::setSurname);
        Optional.ofNullable(childEntity.getBirthDate()).ifPresent(childDto::setBirthDate);
        Optional.ofNullable(childEntity.getNumberOfCurrentlyBorrowedBooks()).ifPresent(childDto::setNumberOfCurrentlyBorrowedBooks);
        Optional.ofNullable(childEntity.getNumberOfEveryBorrowedBooks()).ifPresent(childDto::setNumberOfEveryBorrowedBooks);
        Optional.ofNullable(childEntity.getParent()).ifPresent(parent -> {
            modelMapper.addConverter(new ParentEntityToParentDto());
            childDto.setParent(modelMapper.map(parent, ParentDto.class));
        });

        return childDto;
    }
}
