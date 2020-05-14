# language: en

Feature: A user wants to get an overview over all his todos.

  Scenario: A user has still todos that are still undone.
    Given a list with the following todos:
      | Read some stuff |
      | Get the idea |
      | Write some tests |
      | Make up docs |
    When the user asks for his todos
    Then the list contains 4 todos
