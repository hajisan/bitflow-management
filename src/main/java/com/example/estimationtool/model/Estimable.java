package com.example.estimationtool.model;

import java.time.LocalDate;

// TODO SKAL MÅSKE SLET IKKE BRUGES

public interface Estimable {
    // Et interface der implementeres af alle projektdelene, så de lettere kan kaldes af TimeCalculateren,
    // hvilket fører til mere DRY kode

    public int getEstimatedTime();
        
    public int getTimeSpent();
    
    public void setEstimatedTime(int estimatedTime);
    
    public void setTimeSpent(int timeSpent);

    LocalDate getDeadline();
}
