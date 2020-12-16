# data-analysis-camel

A PoC to explore ability to process files on top of Apache Camel.

This project loads a file that contains a list of customers, sales and salesmen, then generate another file with the number of customers and salesman, the most expensive sale and the worst salesman.

By default, the input file should have the `.dat` extension and should be located at `HOMEPATH/data/in` directory. And the output file will be generated at `HOMEPATH/data/out` directory using the following pattern: `{flat_file_name}.done.dat`. All this options can be customized (we will discuss later).

The input file have this layout:

|          | Layout                                                        | Example                                    |
|----------|---------------------------------------------------------------|--------------------------------------------|
| Salesman | 001ÁCPFÁNameÁSalary                                           | 001Á1234567891234ÁJo„oÁ50000               |
| Customer | 002ÁCNPJÁNameÁBusiness Area                                   | 002Á2345675434544345ÁJose SilvaÁRural      |
| Sale     | x003ÁSale IDÁ[Item ID-Item Quantity-Item Price]ÁSalesman name | 003Á10Á[1-10-100,2-30-2.50,3-40-3.10]ÁJo„o |

And the output have this layout:

| Layout                                                                    | Example  |
|---------------------------------------------------------------------------|----------|
| Number of customersÁNumber of salesmenÁMost expensive saleÁWorst salesman | 3Á2Á10Á5 |


## How to build and run

This project uses Gradle as dependency management tool. So, to create an executable jar you can just run `./gradlew build`, and the executable jar will be created at `build/libs` directory. To run the application you just need to do a double click at the executable jar, or just run within a console with the command `./data-analysis-camel-0.0.1-SNAPSHOT.jar`.

Docker is available. To create docker image you just need to execute `./gradlew bootBuildImage`. A docker image will be published over `data-analysis-camel` name. To run the image you just need to type `docker run data-analysis-camel` command.

## Configuration

TODO