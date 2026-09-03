Feature: Training management

  Scenario: Create training successfully
    Given a registered trainee with username "trainee.training.1"
    And a registered trainer with username "trainer.training.1"
    When I create a training named "Morning Workout"
    Then the training should be created successfully