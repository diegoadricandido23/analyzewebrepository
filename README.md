#Analise Github repository

<p>This Api is responsible for receiving a github repository and scanning yours directories,
analyzing and returning information about the types of data found.</p>

<p>Below you see information about the endpoint:</p>

###EndPoints:

For to run local application, can use this command:<br/>
* mvn spring-boot:run

Check repository 'GET': localhost:8080/check/{user}/{repository}
* Returns a resume about files information </br>
    `
    [`</br>`
        {`</br>`
            "bytes": 894960.0,`</br>`
            "extensão": "JAVA",`</br>`
            "contagem": 8,`</br>`
            "linhas": 447`</br>`
        },`</br>`
        {`</br>`
            "bytes": 769000.0,`</br>`
            "extensão": "MARKDOWN",`</br>`
            "contagem": 1,`</br>`
            "linhas": 18`</br>`
        },`</br>`
        {`</br>`
            "bytes": 13000.0,`</br>`
            "extensão": "PROPERTIES",`</br>`
            "contagem": 1,`</br>`
            "linhas": 2`</br>`
        },`</br>`
        {`</br>`
            "bytes": 1680.0,`</br>`
            "extensão": "XML",`</br>`
            "contagem": 1,`</br>`
            "linhas": 58`</br>`
        },`</br>`
        {`</br>`
            "bytes": 395000.0,`</br>`
            "extensão": "JSON",`</br>`
            "contagem": 1,`</br>`
            "linhas": 33`</br>`
        }`</br>`
    ]`
  

### Heroku information

<p>You can test application using information below:</p>
* https://analyzewebrepository.herokuapp.com/check/{user}/{repository} <br/>

### DockerHub information

<p>You can get and using application with information below:</p>
* Repository > https://hub.docker.com/r/diegoadricandido23/analyzewebrepository</br>
* Command: docker pull diegoadricandido23/analyzewebrepository