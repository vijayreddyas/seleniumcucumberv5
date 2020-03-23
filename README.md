# BDD Based JAVA FRAMEWORK FOR UI AUTOMATION

_**# Goal of the framework**_
Framework can be used to automate the UI scenarios using Selenium with BDD approach, in this case we are using Cucumber

##### **_`Capabilities of the framework`_**
*   Page Object Model Design Pattern
*   Cross Browser Support
*   Parallel execution of features and also scenario outlines.
*   In-built Selenium Grid support
*   Can be integrated with any CI tool, in this case we tried using Jenkins.
*   Scenarios can be written in a plain English language called Gherkin.
*   Flows can be easily developed using available utility functions.
*   Separate log file generation for every scenario
*   Wonderful html report using the third party plugin called Cluecumber. 

#### **_`TechStack`_**
*   Java 1.8
*   TestNG
*   Cucumber Version 5.5.0
*   Selenium 3.14
*   Reporting : Cluecumber third party report
*   Editor: Intellij

### **_`Usage`_**
**_#Using maven_**
*   Executing the entire feature files
    
    mvn clean install -DthreadCount=<no. of threads>
    
    Usage:
    mvn clean install -DthreadCount=3
    
*   Cucumber tags can be created for marking bunch of cases under a specific category and a specific
    cases can be executed by using the below command
    
    **_`Single Tag`_**
    
    mvn clean install -Dcucumber.filter.tags="@tag name"
    
    Usage:
    mvn clean install -Dcucumber.filter.tags=@Login
    
    ###### **_`Multiple Tags`_**
    
    mvn clean install -Dcucumber.filter.tags="@tag1 or @tag2"
        
    Usage:
    mvn clean install -Dcucumber.filter.tags="@Login and @ContactUs"
    
    These tags should be defined on top of features or it can be defined on scenario as well.
    
## **_`Roadmap`_**
*   Implementation of Docker containerization for execution of the scripts
*   Integration of Excel, Yaml, Json files for capturing the test data during run time.
*   Integration of random test data generation using JFairy Library
    


