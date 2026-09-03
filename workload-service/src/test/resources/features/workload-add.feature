Feature: Trainer workload management

  Scenario: Add training workload
    Given a trainer workload does not exist for username "trainer.workload.1"
    When I add a workload for trainer "trainer.workload.1" with first name "John", last name "Smith", date "2026-09-03" and duration 60 minutes
    Then the monthly workload for trainer "trainer.workload.1" should be 60 minutes