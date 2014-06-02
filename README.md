mta-metaxmlgenerator
====================

A meta.xml generator for Multi Theft Auto: San Andreas (fully automatic)

To run:
cmd.exe -> java -jar Generator.jar [options]

Options:
Usage: generator.jar [options] <Eingabeordner>
  Options:
    -author
       Der Author der Ressource
       Default: Unknow
    -cachescripts
       Script-Dateien Cachen
       Default: false
    -clientfolder
       Ordner mit den Client-Dateien, z.B. 'client/'
       Default: client
    -defaultdownload
       Standartwert fuer die Download-Dateien
       Default: true
    -description
       Die Beschreibung des Gamemodes
       Default: A nice little gamemode
    -filevalue
       Downloaddateien Argument, z.B. 'customfile'
       Default: file
    -log
       Operationen Loggen
       Default: true
    -minclientversion
       Die minimale Clientversion
       Default: 1.3.5
    -minserverversion
       Die minimale Serverversion
       Default: 1.3.5
    -name
       Der Name der Ressource
       Default: Some resource
    -nomainfolder
       Dateien im Hauptverzeichnis nicht hinzufuegen
       Default: false
    -oop
       OOP Aktivieren?
       Default: true
    -serverfolder
       Ordner mit den Server-Dateien, z.B. 'server/'
       Default: server
    -syncmap
       Sychronisation der Elementdatas fuer Map-Objekte aktivieren
       Default: false
    -type
       Der Typ der Ressource
       Default: script
    -version
       Die Version der Ressource
       Default: 1.0.0
