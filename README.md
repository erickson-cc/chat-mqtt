Implementação de um bate-papo utilizando o protocolo MQTT na linguagem Java.
# Compilar
javac -cp "lib/*" -d bin src/chatmqtt/*.java
# Rodar
java -cp "bin:lib/*" chatmqtt.chat


# Para fazer
## Tópico GROUPS

Serve para os líderes do grupos avisarem para os assinantes o [Nome do Grupo : Líder<userId>]
Enviar periodicamente a mensagem para informar aos usuários que iniciaram a sessão posteriormente à criação do grupo.
Todos os usuários se inscrevem no tópico GROUPS e podem acessar os logs de cadastros desse tópico

## Solicitações de conversas

A listagem de histórico de solicitações e listagem de confirmação/rejeição de solicitações podem ser registradas juntas em formato de log.
Criar a funcionalidade de rejeitar solicitação.
