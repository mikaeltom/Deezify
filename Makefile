.PHONY: all compile run clean

all: clean compile run

compile:
	mvn compile

run:
	mvn exec:java
test:
	mvn test
clean:
	mvn clean
