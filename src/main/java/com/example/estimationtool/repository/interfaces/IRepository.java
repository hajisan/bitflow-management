package com.example.estimationtool.repository.interfaces;

import java.util.List;

public interface IRepository<T, Integer> { // Vi bruger wrapper-klasse til int, da vi ikke kan bruge den rå datatype

    T create(T t);

    List<T> readAll();

    T readById(Integer id);

    T update(T t);

    void deleteById(Integer id);
}