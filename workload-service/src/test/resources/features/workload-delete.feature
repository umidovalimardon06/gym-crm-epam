Feature: Trainer workload management

  Scenario: Delete training workload
    Given a trainer workload exists for username "trainer.workload.2" with 60 minutes for "2026-09-03"
    When I delete a workload for trainer "trainer.workload.2" with date "2026-09-03" and duration 60 minutes
    Then the monthly workload for trainer "trainer.workload.2" should be 0 minutes