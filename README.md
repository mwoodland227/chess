# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


## Sequence Diagram URL

```sh
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAHZM9qBACu2AMQALADMABwATACcIDD+yPYAFmA6CD6GAEoo9kiqFnJIEGiYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAEQDlGjAALYo43XjMOMANCu46gDu0ByLy2srKLPASAj7KwC+mMK1MJWs7FyUDRNTUDPzF4fjm6o7UD2SxW63Gx1O52B42ubE43FgD1uogaUCyOTAlAAFJlsrlKJkAI5pXIAShuNVE9yqsnkShU6ga9hQYAAqoNMe9PigyTTFMo1KoqUYdHUAGJITgwNmUXkwHSWGCcuZiHSo4AAaylgxgWyQYASisGXJgwAQao4CpQAA90RpeXSBfdERSVA1pVBeeSRConVVbi8YAozShgBaOhr0ABRK0qbAEQpeu5lB4lcwNQJOYIjCbzdTAJmLFaRqDeeqG6bKk3B0MK+Tq9DQsycTD2-nqX3Vb0oBpoHwIBCJykPVv01R1EBqjHujmDXk87QO9sPYx1BQcDhamXaQc+4cLttjichjEKHz6zHAM8JOct-ejoUrtcb0-6z1I3cPWHPMs49H4tR9lgX7wh2-plm8RrKssDQHKCl76h0ED1mg0ErFciaUB2qYYA04ROE42aTJBXwwDBIIrPBCSIchqEHNc6AcKYXi+AE0DsEysSinAkbSHACgwAAMhA2RFNhzDOtQAYtO03R9AY6gFGg2ZKvM6x-ACHDXGBQrAQGowqSgan6P8uwwk8IESVQyIwAgwkSpiQkiYSxJgGS76GHutIHoyzLTgZ85efey4ijA4qSu6sryuWHzKpgqohpqegbgZvTqbst6BY6lnWRF27uUKYENAAkmgIDQKi4BwKiHDmEgpqqNGsbxkU2kpsgaYwBmACMhG5qo+YLDBxalg0PgzFe0BIAAXigez0c2+WeXyo4NEecgoC+CQXleN4jllVQroG66BjteUuh5VS6WWjkSpkqiATOFaqSaV7UegZJXQVNR6QZ6yUW9aBad9CJVGJuH4cpxGGS9CFIeg82MZ43h+P4XgoOgsTxEkaMY45vhYGJX2SWWjTSJGAmRh0kY9L08mqIpIz-XDLXAzp5kBkzyGYJ92WujZwn4w5Atns5aiuTuF0yHeAo+WAm3bbDyEBct+3Cg0YXPqd8hygqnPoHFaqahKVCmkgG560Ue1Lrz3YwL2-YSx2V1uuN+qTTNHCNSgcaKdz7MIpZAbdb1AoDYW4zDdAo2uwk7uzY2DEZSr1t+udDSbW+51ClbY5GCg3AnleCtUczyuLoKwUNJkswQDQJ2vmdXZO-7DR4yLAEIEB-ugcDrxA8TWHtThMB4QRYzXE2iPMSj1WxNgEqagJ6IwAA4sqGiE4HJMr5TNP2MqjOvczGEg48cIc0fXM86nXYNMguRr7mDnoo-aii6SjtLeXsvyxbZcHg+EKGt67Xm0DrGGJcubxQ1DAY2ptzaX31jnHut87Z9gQDAGAn9PwtylDHOOnsYze2an7c+Acb61HTE4HqYxxh9TDkNEsUdFT4KgNNeOCMk7lxQVZPmGdG68MljnO+L916Yn-kFA6QCJTm37KvZUnpkHJgodZJeuQAA8r9eQVGwZdXBaiwCv1UHdQCpDvw8KkqMfe8wuqXHWFY5U4Q7EwBcK4roJ9B6lDAODMejQHG5hsuiS47jE5IxYv4DgbhIhOBQE4WIkZghwG4gANngJOQwr8sGbwoVJNonQ94HxjgDbM1iUAADllT9yTHoshDQLbrFKRU+YZiLIqL5mtDEr9MRVWPCgV+79xaLWpNLBkMAmRyyLn-LhADK6hRkSAyKutEFFGgUbNAJtkAIMVkgkZFcbY9nQZg3RZ9vwu0ogQr2PsEw8y3lQmhOZQ4FkYSNFh5y2EewTs2JR+yFkCKHMMzKoyOl9OVJiUpu1dmAPTsdLR25vltNtmgFAWxYXa0xLqfUYzKxFWkG5LOyiTnwgaD09a-SO5dzIRY8CpScUNOVE06GriXBVMwgSsGI8IZjFKbAtAAAzCAnCwko0sPnWyWxMZIESGAEV-YIDioAFIQAlPI+YsQUigHVMUIe4kckk2aCyWSvRSmH22UpMY2AEDABFVAOAEBbJQDpfMHFLLT7OwgQDR1KBaUwAtVaygtr7UtNZQihoAArJVaAumKolGSokYs8VNy-t5MZzJf7LIkarQ6wD+HayihbA2CUeUbLNu64+8LOyCIOXIrBi0cG1LwW89hhCmq+xubqu5Ic8xPKLEwssY1G0fM4eW9y6ctbAE-gC5OudxldJpbi6Zki1ZzMlKi4A4C50FpgdyjgEA1BoAAOTMGtOiBdWUfn2wHLWmppyVXlIgGAUUvhOCXOajATE0AYD01mGcPAH1u4EsKp1JwgRO39W7RHXtjJKxoHvTAflY05qT1PSnCt1lV0TqloC3OfgtCdNBeC7QGalxSLdNgXDGSFFwshQBtOioyPrQ4Ohq9hKAzRsjcqExncg3kIrXpblEp+Uus8R1UeJTKwCYFaYUJ08AheCtRKqVcmFSIBDLAYA2ALWEHyIULJ2qiaUKaGTCmVMabGA8XWm9mIYM+rwMqDEUBBRItmrNP9lKfkgG4HgcRGHhEwA86p90qhvNKJIzIfOzJDCmgwYFnzuzVqeagEY4L1HQvSHCxiKsGCjGxaw-F1T8siMV1S+lyLcic3juY26lTeBOMUvMTR4mfdzNtS8T47ME9E5AA
```