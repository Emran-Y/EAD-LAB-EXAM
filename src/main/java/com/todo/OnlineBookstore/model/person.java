package com.todo.OnlineBookstore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
public class person {
    private int id;
    private String name;
    private int age;
    private Date birDate;
}
