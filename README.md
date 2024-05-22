# StickyNotesApp

## Project info
This application is a REST API designed to enable managers to create and manage tasks for employees who are part of the manager's team. 
Managers have the ability to add, delete, approve (through task status), and reassign tasks to other team members. Employees can confirm the completion of tasks by changing their status.

### Roles and Responsibilities
- Admin:
  - Can create manager accounts.
- Manager:
  - Can create employee accounts.
  - Can create, manage, and review tasks.
  - Can view tasks they have created.
- Employee:
  - Can view tasks assigned to them.
  - Can change the status of tasks to indicate progress or completion.

### Task management:
Tasks consist of, among others, the following attributes:
  - Title
  - Project name
  - Content
  - Assigned user
  - Creation date
  - Priority (Low/Normal/High)
  - Status (New, In Progress, Done, Accepted, Not Accepted)
   
### User Account and Authentication
New users are assigned a default password which is the name of the team they are assigned to (e.g., if the team name is ‘Spring Dev Team’, the new employee's initial password will be ‘Spring Dev Team’).
Upon first login, employees are required to change their password via a mandatory password change form.

### REST API functionalities include
- Creating and editing accounts: Endpoints for admins to edit user accounts, create manager accounts and for managers to create employee accounts.
- Managing tasks: Endpoints for managers to create, update, delete, and reassign tasks.
- Updating task status: Endpoints for employees to update the status of tasks assigned to them.
- Viewing tasks: Endpoints for managers to view tasks they have created and for employees to view tasks assigned to them.

## Technologies
![Static Badge](https://img.shields.io/badge/SPRING-green?style=for-the-badge&logo=spring&logoColor=white)
![Static Badge](https://img.shields.io/badge/SPRING%20BOOT-green?style=for-the-badge&logo=springboot&logoColor=white)
![Static Badge](https://img.shields.io/badge/spring%20security-green?style=for-the-badge&logo=springsecurity&logoColor=white)
![Static Badge](https://img.shields.io/badge/junit-green?style=for-the-badge&logo=junit5&logoColor=white&color=%2325A162)
