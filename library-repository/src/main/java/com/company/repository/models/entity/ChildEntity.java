package com.company.repository.models.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity(name = "Child")
public class ChildEntity extends ReaderEntity {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ParentEntity parent;

    @Override
    public String toString() {
        return super.toString() +
                "\t" + "type : " + "***Child***" + "\n" +
                "\t" + "type : " + "address: " + parent.getAddress() + "\n" +
                "\t" + "Parent name: : " + parent.getName() + " surname: " + parent.getSurname() + "phone number: " + parent.getPhoneNumber() + "\n";
    }
}
