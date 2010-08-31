Rocksteady is an effort to utilize complex event process engine to analyze user defined metric. End goal is to derive conclusion based on metric driven events instead of simple alerts.

This project is written by an ops guy who want to learn Java and solve his metric problem. In documentation, there will probably be things that a real programmar snorts at, criticism is welcome. It will probably cover some obvious things in detail because I don't want my fellow ops to be as confused as I was when first enter the wonderful world of Java programming.

Rocksteady is an application written in Java using Spring framework, build with Maven, and should run wherever you can install Java.

Requirements:
Java 1.5+
Maven2 (It's like apt-get + ./configure + make for Java)
Rabbitmq + shovel plugin(optional)
Mysql (Track historic data)

Optional:
Graphite (Orbitz open sourced graphing backend)
GLP, Graphite Local Proxy. Useful to collect metric, install on each server and create a listener to collect metric which it forwards to configured rabbitmq exchange.

Quick Install:
Install rabbitmq, mysql server
Load setup.sql with `mysql rocksteady < setup.sql`

Installation Guide,
http://code.google.com/p/rocksteady/wiki/InstallNotes

More documentation can be found on 
http://code.google.com/p/rocksteady/

