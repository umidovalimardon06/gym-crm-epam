package com.epam.workload.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TrainerWorkload {
    private String username;
    private String firstname;
    private String lastname;
    private boolean active;
    private final Map<Integer, Map<Integer,Integer>> years = new ConcurrentHashMap<>();

    public TrainerWorkload() {
    }

    public TrainerWorkload(String username, String firstname, String lastname, boolean active) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.active = active;
    }

    public void addDuration(int year, int month, int minutes) {
        years.computeIfAbsent(year, y -> new ConcurrentHashMap<>())
                .merge(month, minutes, Integer::sum);
    }

    public void subtractDuration(int year, int month, int minutes) {
        years.computeIfAbsent(year, y -> new ConcurrentHashMap<>())
                .merge(month, -minutes, Integer::sum);
    }

    public int getDuration(int year, int month) {
        return years.getOrDefault(year, Map.of()).getOrDefault(month, 0);
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstname; }
    public void setFirstName(String firstName) { this.firstname = firstName; }
    public String getLastName() { return lastname; }
    public void setLastName(String lastName) { this.lastname = lastName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Map<Integer, Map<Integer, Integer>> getYears() { return years; }
}