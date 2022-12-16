package com.company.repository.models.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Parent")
public class ParentEntity extends ReaderEntity {

    private String address;
    private String phoneNumber;

    @Override
    public String toString() {
        return super.toString() +
                "\t" + "type : " + "***Parent***" + "\n" +
                "\t" + "address : " + address + "\n" +
                "\t" + "phone number : " + phoneNumber + "\n";
    }
}
