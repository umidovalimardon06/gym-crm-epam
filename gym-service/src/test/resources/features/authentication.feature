Feature: User authentication

    Scenario: Authenticate trainee with valid credentials
        Given a registered trainee with username "alimardon.umidov" and password "password123"
        When I login as "alimardon.umidov" with password "password123"
        Then the response status should be 200
        And an authentication token should be returned
