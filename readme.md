# data-analysis-camel

A PoC to explore ability to process files on top of Apache Camel.

This project loads a file that contains a list of customers, sales and salesmen, then generate another file with the number of customers and salesman, the most expensive sale and the worst salesman.

By default, the input file should have the `.dat` extension and should be located at `HOMEPATH/data/in` directory. And the output file will be generated at `HOMEPATH/data/out` directory using the following pattern: `{flat_file_name}.done.dat`. All this options can be customized (we will discuss later).

The input file have this layout:

|          | Layout                                                        | Example                                    |
|----------|---------------------------------------------------------------|--------------------------------------------|
| Salesman | 001çCPFçNameçSalary                                           | 001ç1234567891234çJoãoç50000               |
| Customer | 002çCNPJçNameçBusiness Area                                   | 002ç2345675434544345çJose SilvaçRural      |
| Sale     | x003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name | 003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çJoão |

And the output have this layout:

| Layout                                                                    | Example  |
|---------------------------------------------------------------------------|----------|
| Number of customersçNumber of salesmençMost expensive saleçWorst salesman | 3ç2ç10ç5 |


## How to build and run

This project uses Gradle as dependency management tool. So, to create an executable jar you can just run `./gradlew build`, and the executable jar will be created at `build/libs` directory. To run the application you just need to do a double click at the executable jar, or just run within a console with the command `./data-analysis-camel-0.0.1-SNAPSHOT.jar`.

By default the application will listen for files under user home directory: `HOMEPATH` at windows or `HOME` at Mac OS or Linux. You can change this location passing the parameter `-Dapp.basedir=/some/directory`. All files to be consumed should be using UTF-8 as charset, and to change that you just need to use `-Dapp.charset=ISO-8859-1`. Also, only files with `.dat` extension will be consumed. To change that you can pass `-Dapp.allowed-file-extensions=txt,dat`.

## Docker

Docker is available. To create docker image you just need to execute `./gradlew bootBuildImage`. A docker image will be published over `otavio.com.br/data-analysis-camel` name. To run the image you just need to type `docker run otavio.com.br/data-analysis-camel` command.

By default, docker will try to process all files over `/home/cnb` directory. So you can create a volume to customize this location. In that case the following command will configure the application to keep the eyes, as example, on the directory `/opt/data-analysis-directory`:

```
docker run otavio.com.br/data-analysis-camel -v /opt/data-analysis-directory:/home/cnb
```

