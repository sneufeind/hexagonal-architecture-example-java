# language: en

Feature: A user wants to get todos done.

  Scenario: A user has todos that are still undone.
    Given a list with the following todos:
      | Read some stuff |
      | Get the idea |
      | Write some tests |
      | Make up docs |
    When the todo "Read some stuff" is done
    Then the list contains 3 todos