package com.epam.workload.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "trainer_workload")
@CompoundIndex(
        name = "trainer_name_idx",
        def = "{'firstname': 1, 'lastname': 1}"
)
public class TrainerWorkload {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String firstname;
    private String lastname;
    private boolean active;
    private List<YearSummary> years = new ArrayList<>();

    public TrainerWorkload() {
    }

    public TrainerWorkload(String username, String firstname, String lastname, boolean active) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.active = active;
    }

    public void addDuration(int year, int month, int minutes) {
        YearSummary yearSummary = years.stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElseGet(() -> {
                    YearSummary newYear = new YearSummary(year);
                    years.add(newYear);
                    return newYear;
                });

        MonthSummary monthSummary = yearSummary.getMonths().stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseGet(() -> {
                    MonthSummary newMonth = new MonthSummary(month, 0);
                    yearSummary.getMonths().add(newMonth);
                    return newMonth;
                });

        monthSummary.setTrainingSummaryDuration(
                monthSummary.getTrainingSummaryDuration() + minutes
        );
    }

    public void subtractDuration(int year, int month, int minutes) {
        addDuration(year, month, -minutes);
    }

    public int getDuration(int year, int month) {
        return years.stream()
                .filter(y -> y.getYear() == year)
                .flatMap(y -> y.getMonths().stream())
                .filter(m -> m.getMonth() == month)
                .mapToInt(MonthSummary::getTrainingSummaryDuration)
                .findFirst()
                .orElse(0);
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirstName() { return firstname; }
    public void setFirstName(String firstName) { this.firstname = firstName; }
    public String getLastName() { return lastname; }
    public void setLastName(String lastName) { this.lastname = lastName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public List<YearSummary> getYears() {return years;}
    public void setYears(List<YearSummary> years) {this.years = years;}
}