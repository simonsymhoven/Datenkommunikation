# Datenkommunikation
Dient zur Organisation vom Praktikum in Datenkommunikation im WS2019/20.
Das Repo ist verfügbar über

```
git clone https://github.com/simonsymhoven/Datenkommunikation.git 
```

## Getting started
- Eclipse runterladen
- Git Repo importieren
- [e(fx)eclipse](https://o7planning.org/de/10619/die-installation-von-efxclipse-in-die-eclipse) installieren, falls nicht bereits vorhanden, aktuelle Version ist die *3.3.0*
- Eventuell die im Pojekt verwendete JRE System Library auf 1.8 stellen

## Aufgabenstellung

Siehe [Aufgabenstellung.pdf](Aufgabenstellung_Studienarbeit_Datenkommunikation_WS_19_20.pdf) für Details zur Aufgabestellung.

## Authoren
**Marc** <br>
**Magdalena** <br>
**Simon** (@ottnorml) <br>
**Simon** (@simonsymhoven) <br> 

## Deployment
Client und Server werden über die main-Methode der [ClientFxGUI.java](DakoChatApplication_Graddle/src/main/java/edu/hm/dako/chat/client/ClientFxGUI.java) bzw. [ChatServerGUI.java](DakoChatApplication_Graddle/src/main/java/edu/hm/dako/chat/server/ChatServerGUI.java) Klasse gestartet.

## Continuous Integration mit [Travis-CI](https://travis-ci.com)
In der [.travis.yml](.travis.yml) Datei ist die Config für die CI. Jeder commit und jeder PullRequest wird automatisch deployt und gecheckt. Das Ergebnis sollte GitHub im jewieligen Commit/PullRequest anzeigen, ansonsten siehe hier: [Travis](https://travis-ci.com/simonsymhoven/Datenkommunikation)

## Projektplan
Im Repo befindet sich eine Excel, in der ein Projektplan enthalten ist und ein zugehöriges Gantt-Diagramm. Die rote vertiakle, gestrichelte Linie zeigt das aktuelle Datum.

## Projektboard
Über das interne Projektboard von Github werden alle anstehenden Aufgaben verwaltet und getrackt. Diese können jemandem zugeordent werden, mit Labels versehen werden, Pull-Requests zugeordnet werden usw und diese automatisch als Issues angelegt werden, die dann über den Issue Reiter kommentiert werden können um so den Fortschirtt und Probleme bei der Bearbeitung festzuhalten.

## SourceCode
Der Source-Code zu dem Projekt liegt im Ordner DakoChattApplication.
Im master-Branch befindet sich immer eine auffähige Version der Chatt-Application. Entwickelt jemand an der Application, wird ein neuer Branch erzeugt, der nach einem Review in den master-Branch gemerged wird. Auf dem master-Branch wird nciht aktiv gearbeitet. Siehe [Git_basiscs.pdf](Git_basics.pdf) für Details zum *Git-Flow*.

## Teilaufgabe 1
Das Repo enthält einen Ordner **Aufgabe 1**, in dem alle erzeugten Diagramme zur Dokumentation gesammelt werden. Dort liegt außerdem eine .ppt-Vorlage, in der nachher die Charts einfügen werden können.

## Teilaufgabe 2
-

## Teilaufgabe 3
-

