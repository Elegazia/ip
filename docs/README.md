
# Nyeash User Guide

Nyeash is a simple command-line task manager that helps users keep track of todos, deadlines, and events.  
It allows you to add tasks, list them, mark them as done, unmark them, delete them, and search for tasks by keyword.  
All tasks are automatically saved to a file, so your list is still there the next time you open the program.

## Adding todos

Adds a todo task to the task list.

Example: `todo read book`

The task will be added to the list.

```text
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
````

## Adding deadlines

Adds a deadline task with a description and a deadline.

Example: `deadline return book /by Sunday`

The deadline task will be added to the list.

```text
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
```

## Adding events

Adds an event task with a description, start time, and end time.

Example: `event project meeting /from 2pm /to 4pm`

The event task will be added to the list.

```text
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
```

## Listing tasks

Shows all tasks currently stored in the task list.

Example: `list`

All tasks in the list will be displayed in numbered order.

```text
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: 2pm to: 4pm)
____________________________________________________________
```

## Marking a task as done

Marks a task as completed using its task number.

Example: `mark 1`

Task 1 will be marked as done.

```text
____________________________________________________________
Good job on finishing this! NYEASH is very proud of you!
  [T][X] read book
____________________________________________________________
```

## Unmarking a task

Marks a task as not done using its task number.

Example: `unmark 1`

Task 1 will be marked as not done.

```text
____________________________________________________________
OK, NYEASH unmarked it. Better finish it cause this is above my paygrade!
  [T][ ] read book
____________________________________________________________
```

## Deleting a task

Deletes a task from the list using its task number.

Example: `delete 2`

Task 2 will be removed from the list.

```text
____________________________________________________________
Noted. I've removed this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
```

## Finding tasks

Finds tasks whose descriptions contain a given keyword.

Example: `find book`

All matching tasks will be shown.

```text
____________________________________________________________
Here are the matching tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: Sunday)
____________________________________________________________
```

## Exiting the program

Closes the program.

Example: `bye`

Nyeash will say goodbye and exit.

```text
____________________________________________________________
Please bring me more food next time!
____________________________________________________________
```

## Saving data

Nyeash automatically saves your tasks after any change, such as adding, deleting, marking, or unmarking a task.
When the program starts again, it loads the saved tasks from the file.

## Command summary

| Action       | Format                                  | Example                                   |
| ------------ | --------------------------------------- | ----------------------------------------- |
| Add todo     | `todo DESCRIPTION`                      | `todo read book`                          |
| Add deadline | `deadline DESCRIPTION /by TIME`         | `deadline return book /by Sunday`         |
| Add event    | `event DESCRIPTION /from START /to END` | `event project meeting /from 2pm /to 4pm` |
| List tasks   | `list`                                  | `list`                                    |
| Mark task    | `mark INDEX`                            | `mark 1`                                  |
| Unmark task  | `unmark INDEX`                          | `unmark 1`                                |
| Delete task  | `delete INDEX`                          | `delete 2`                                |
| Find task    | `find KEYWORD`                          | `find book`                               |
| Exit         | `bye`                                   | `bye`                                     |

