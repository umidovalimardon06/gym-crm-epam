Feature: Password management

  Scenario: Change password successfully
    Given a user "alimardon" exists with password "password123"
    When I change password from "password123" to "newpassword123"
    Then the response status should be 200
    And the user should be able to login with "newpassword123"