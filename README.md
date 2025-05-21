CI/CD

We are using Maven for our CI pipeline. A step-by-step walkthrough of our proces is as follows.
On push or pull request to either the 'development' or 'main' branch we run a build job.
The build job, "Build and test with Maven", runs on Ubuntu and has the following steps:
  - Checkout
    The checkout action is performed (actions/checkout@v3)
  - Set up Java 21
    The setup action is performed (actions/setup-java@v3). It is done with Temurin.
  - Build with Maven
    The environment variables to access the database is gathered from our GitHub secrets. Afterwards the Maven commands clean and install are executed.

    CD pipeline:
    WIP :3

Klassediagram:    
![Class Diagram - EstimationTool](https://github.com/user-attachments/assets/08746197-c9ea-4ecb-9830-fb014215021f)
