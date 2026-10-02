Implementação de um bate-papo utilizando o protocolo MQTT na linguagem Java.
## 1. Pré-requisitos e Instalação

Para compilar e rodar a aplicação, é necessário ter o **Java Development Kit (JDK)** e o broker **Eclipse Mosquitto** instalados. Certifique-se de que a biblioteca Paho (arquivo `.jar`) está localizada dentro da pasta `lib/` do projeto.

**Linux**
sudo pacman -S jdk-openjdk mosquitto
ou
sudo apt update
sudo apt install default-jdk mosquitto

**MacOS**
brew install java mosquitto

**Windows**
winget install Microsoft.OpenJDK.17

## 2. Iniciando o Broker 

Antes de abrir o aplicativo de chat, o servidor MQTT precisa estar ativo para rotear as mensagens. Abra um terminal exclusivamente para ele.

Linux e MacOS:

mosquitto -v

Windows:

mosquitto.exe -v

## 3. Compilando o Projeto
Linuxo e MacOS:

javac -cp "lib/*" -d bin src/chatmqtt/*.java

Windows:

javac -cp "lib\*" -d bin src\chatmqtt\*.java

## 4. Executando 
Linux e MacOs:

java -cp "bin:lib/*" chatmqtt.chat

Windows: 
java -cp "bin;lib\*" chatmqtt.chat

# Para fazer
## Tópico GROUPS

Serve para os líderes do grupos avisarem para os assinantes o [Nome do Grupo : Líder<userId>]
Enviar periodicamente a mensagem para informar aos usuários que iniciaram a sessão posteriormente à criação do grupo.
Todos os usuários se inscrevem no tópico GROUPS e podem acessar os logs de cadastros desse tópico

## Solicitações de conversas

A listagem de histórico de solicitações e listagem de confirmação/rejeição de solicitações podem ser registradas juntas em formato de log.
Criar a funcionalidade de rejeitar solicitação.
