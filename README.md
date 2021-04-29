# OlhaOGás

FIXME: my new application.

## Instalação

Download: https://github.com/joaoapel/olhaogas

## Uso

Esta biblioteca foi criada com o intuito de utilizar frequências sonoras
para detectar a quantidade de gás no seu botijão.

Para tanto é só gravar a batida no meio do butijão com um pedaço de madeira.

Para rodar o projeto:

    $ clojure -M -m joaoapel.olhaogas

Para rodar os testes (por enquanto apenas testando a função principal):

    $ clojure -M:test:runner

Para fazer a build do uberjar:

    $ clojure -M:uberjar

Rode o uberjar:

    $ java -jar olhaogas.jar ./caminho-para-arquivo.wav


## Examplos
O arquivo utilizado está na raiz do projeto.

    $ java -jar olhaogas.jar ./usado.wav

## License

Copyright © 2021 Joao

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
