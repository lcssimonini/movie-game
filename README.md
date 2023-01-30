# movie-game

Api to play movie game

### Relatorio Sonarcloud

* [movie-game](https://sonarcloud.io/summary/overall?id=lcssimonini_movie-game)

### rodar a aplicacao localmente

Primeiro, rode o container do banco de dados utilizando o docker compose na raiz do projeto:

    docker-compose up

Agora, rode a aplicaçao, o makefile está na raiz do projeto:

    make start-app

### Como jogar

primeiramente, crie um usuário:


    curl --location --request POST 'http://localhost:8080/auth/register' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "username": "simonini",
    "password": "simo123",
    "passwordConfirmation": "simo123"
    }'

O endpoint irá retornar seu jwtToken:

    {
        "token": "<your jwt token>"
    }

Também poderá autenticar pelo endpoint:

    curl --location --request POST 'http://localhost:8080/auth/authenticate' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "username": "simonini",
    "password": "simo123"
    }'

Agora, podemos criar uma partida:

    curl --location --request POST 'http://localhost:8080/game/new-game' \
    --header 'Authorization: Bearer <your jwt token>'

Resposta de criaçao da sua partida:

    {
        "id": 1,
        "username": "simonini",
        "startedAt": "2023-01-30T00:00:00",
        "score": null,
        "totalTurns": 0
    }

Agora voce poderá criar rodadas:

    curl --location --request POST 'http://localhost:8080/game/1/new-turn' \
    --header 'Authorization: Bearer <your jwt token>'

Resposta para criacao de uma rodada:

    {
        "gameTurnId": 1,
        "movieTitle1": "Cei care platesc cu viata",
        "movieTitle2": "Gaúcho de Passo Fundo",
        "scored": false
    }

Agora voce pode jogar seu palpite:

    curl --location --request POST 'http://localhost:8080/game/102/play-turn/256' \
    --header 'Authorization: Bearer <your jwt token>' \
    --header 'Content-Type: application/json' \
    --data-raw '{"playOption": "MOVIE_1"}'

A resposta do palpite:

    {
        "gameTurnId": 1,
        "movieTitle1": "Cei care platesc cu viata",
        "movieTitle2": "Gaúcho de Passo Fundo",
        "scored": true
    }

Finalizar uma partida:

    curl --location --request POST 'http://localhost:8080/game/102/finish-game' \
    --header 'Authorization: Bearer <your jwt token>' \
    --data-raw '

Resposta da partida finalizada:

    {
        "id": 1,
        "username": "simonini",
        "startedAt": "2023-01-30T00:00:00",
        "score": 5,
        "totalTurns": 5
    }

Endpoint para recuperar o ranking do jogo:

    [{
    "username": "simonini",
    "score": 4,
    "position": 1
    }, {
    "username": "ze-goiaba",
    "score": 3,
    "position": 2
    }, {
    "username": "cebolinha",
    "score": 2,
    "position": 3
    }]