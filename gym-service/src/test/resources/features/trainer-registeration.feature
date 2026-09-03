Feature: Trainer registration

  Scenario: Register a trainer successfully
    When I register a trainer with first name "John", last name "Smith" and specialization "CARDIO"
    Then the trainer registration response status should be 201

  Scenario: Update trainer profile successfully
      Given a registered trainer with username "trainer.update.1"
      When I update trainer "trainer.update.1" with first name "Updated", last name "Trainer" and specialization "STRENGTH"
      Then the trainer update response status should be 200