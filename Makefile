JAVA = java
JAVAC = javac
JAVA_ARGS = -cp .:./lib/classes:./lib/*

build: Main.class

Main.class: download

%.class: %.java
	$(JAVAC) $(JAVA_ARGS) $<

download: lib
	@test -f $</.done || $(MAKE) -C $<

test: Main.class
	$(JAVA) $(JAVA_ARGS) Main


