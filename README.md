# hibersap-generator
Simple tool generating ready-to-use [Hibersap](https://hibersap.org/)-annotated Java classes by reading SAP ERP ECC/S4 Function Module

## What is Hibersap
[Hibersap](https://hibersap.org/) helps developers of Java applications to call business logic in SAP backends. It defines a set of Java annotations to map SAP function modules to Java classes as well as a small, clean API to execute these function modules and handle transaction and security aspects.

Hibersap's programming model is quite similar to those of modern O/R mappers, significantly speeding up the development of SAP interfaces and making it much more fun to write the integration code.

## Requirements
* direct access to target SAP ERP ECC/S4 instance (by VPN or LAN)
* SAP JCO3 libraries: [download](https://support.sap.com/en/product/connectors/jco.html) and extract to `libs/sapjco3` so that you have `libs/sapjco3/sapjco3.jar`

## SAP ECC/S4 connection
Create a file with SAP ERP ECC/S4 destination settings like `ECQ.jcoDestination`

     jco.client.ashost=HOST
     jco.client.sysnr=00
     jco.client.client=100
     jco.client.user=USERNAME
     jco.client.passwd=PASSWORD
     jco.client.lang=en

## How to use
As an example to generate Hibersap Java classes for SAP function module `ZFUNCTION1` from destination `ECQ` saved in `ECQ.jcoDestination` file:

### Mac/Linux

    ./gradlew run --args="-sapDestination ECQ -sapFunctionModule ZFUNCTION1"    

### Windows

    gradle.bat run --args="-sapDestination ECQ -sapFunctionModule ZFUNCTION1"    

Inside folder `generated-sources` you'll get the generated Java classes.
