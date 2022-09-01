Feature: Health check functionality

  Scenario: I want to do health check
    When I want to @Get health
    Then I received OK response
