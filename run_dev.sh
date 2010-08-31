# Run this in target directory
mvn -f ../pom.xml compile && java -Xms32m -Xmx128m -cp libs/*:classes/ -Dlog4j.configuration=META-INF/spring/log4j.properties com.admob.rocksteady.Main
