# language: en

Feature: A user wants to add new todos

  Scenario: A user adds a new todo.
    Given an empty list
    When user adds a new todo
    Then this todo will be added to the list
    And this todo will be unchecked

  Scenario: A user tries to add a todo twice.
    Given an empty list
    When user adds a new todo
    And tries to add this todo again
    Then this todo will be added twice

  Scenario: A user adds different todos
    Given a list with the following todos:
      | Read some stuff |
      | Get the idea |
    When user adds a new todo
    Then the list contains 3 todos

