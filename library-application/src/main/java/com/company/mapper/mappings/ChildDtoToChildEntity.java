package com.company.mapper.mappings;

import com.company.repository.models.entity.ChildEntity;
import com.company.repository.models.entity.ParentEntity;
import com.company.dtos.ChildDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChildDtoToChildEntity implements Converter<ChildDto, ChildEntity> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public ChildEntity convert(MappingContext<ChildDto, ChildEntity> context) {
        ChildDto childDto = context.getSource();
        ChildEntity childEntity = new ChildEntity();

        Optional.ofNullable(childDto.getId()).ifPresent(childEntity::setId);
        Optional.ofNullable(childDto.getName()).ifPresent(childEntity::setName);
        Optional.ofNullable(childDto.getSurname()).ifPresent(childEntity::setSurname);
        Optional.ofNullable(childDto.getBirthDate()).ifPresent(childEntity::setBirthDate);
        Optional.ofNullable(childDto.getNumberOfCurrentlyBorrowedBooks()).ifPresent(childEntity::setNumberOfCurrentlyBorrowedBooks);
        Optional.ofNullable(childDto.getNumberOfEveryBorrowedBooks()).ifPresent(childEntity::setNumberOfEveryBorrowedBooks);
        Optional.ofNullable(childDto.getParent()).ifPresent(parent -> {
            modelMapper.addConverter(new ParentDtoToParentEntity());
            childEntity.setParent(modelMapper.map(parent,ParentEntity.class));
        });

        return childEntity;
    }
}
