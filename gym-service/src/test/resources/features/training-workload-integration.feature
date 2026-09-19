Feature: Training workload integration

  Scenario: Creating a training updates trainer workload
    When I register a trainer with first name "Alimardon", last name "Umidov" and specialization "STRENGTH"
    Then the trainer registration response status should be 201
    When I register a trainee with first name "Jasur", last name "Karimov"
    Then the trainee registration response status should be 201
    When I create a 60 minute training for the registered trainer and trainee
    Then the training creation response status should be 201
    And the trainer workload should contain 60 minutes