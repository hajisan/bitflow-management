package com.example.estimationtool.interfaces;

import java.util.List;

// TODO Hvis vi bruger wrapper-klassen her, men ikke nede i metoderne, så bruger vi den jo egentlig ikke - Joakim :thinko:
public interface IRepository<T, Integer> { // Vi bruger wrapper-klasse til int, da vi ikke kan bruge den rå datatype

    T create(T t);

    List<T> readAll();

    T readById(int id);

    T update(T t);

    void deleteById(int id);
}


