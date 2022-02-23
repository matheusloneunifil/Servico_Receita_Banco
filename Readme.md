# Projeto SpringBoot - CSV para Receita

Desenvolvido por Matheus Lone Arruda 

## Tecnologias: 
Spring Boot (JDK 8)\
Gradle\
Appache\
Common CSV

### Passo a passo para rodar o programa:

-Instalar uma IDE Java(IntelliJ é o recomendado)\
-Fazer o build do projeto na IDE\
-Abrir o código do projeto na pasta 'src' - 'servico'\
-Rodar o projeto na IDE\
-Abrir o Postman, e em um Workspace criar um novo request POST : "localhost:9877/upload-arquivo"\
-Fazer o upload do arquivo CSV teste no BODY do request no Postman(ou de um arquivo CSV teste)\
-Rodar o Request\
-Resultados com o arquivo atualizado serão mostrados no Postman\
-Podes baixar um arquivo de dados dummy no navegador usando "http://localhost:9877/download-file", será baixado um arquivo com dados aleatorios apenas para mostrar a formatação de como ficaria.\
-Se integrado com o Front end, o arquivo CSV será baixado na forma mostrada no Postman.
