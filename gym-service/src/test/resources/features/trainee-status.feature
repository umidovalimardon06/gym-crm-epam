Feature: Trainee status management

  Scenario: Deactivate trainee successfully
    Given a registered trainee with username "trainee.status.1" and password "password123"
    When I deactivate the trainee "trainee.status.1" with password "password123"
    Then the trainee status response should be 200

  Scenario: Activate trainee successfully
    Given an inactive trainee with username "trainee.status.2" and password "password123"
    When I activate the trainee "trainee.status.2" with password "password123"
    Then the trainee status response should be 200