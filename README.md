## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


Folder structure is seperated into the model, Data access, Service and Presentation layers. 
    Model: contains Data, variables, constructors, getters and setters. Aswell as related model objects if needed.
    Data Access: Database connections and SQL executions, uses values from the model layer
    Service: Contains primary manager and manager classes for the individual entity classes, this is where the primary methods and validation/verification checks occur. Using the methods used by the Data Access layer. 
    Presentation: Contains main menu class that will only use the main manager class, to create majority of the menus for the system before being called at the DataApp(main) file. 


    Presentation(Receives input sends it to Service) -> Service (Checks validation, puts input to create a object using model, Model is given to DAO) -> DAO(writes object to database using sql)


    Few key notes: 
        DAO classes could be centralised to one single classes but for the sake of understanding OOP and this file structure we won't do that initially. 
