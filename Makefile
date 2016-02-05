JAVA = java
JAVAC = javac
JAVA_ARGS = -cp .:./lib/*
JAR = jar
7Z = 7z
JFLEX = jflex

EMPTY = classes usedclasses
.PHONY: %.jar $(EMPTY) lib
.SECONDARY: Lexer.java

$(EMPTY):
	rm -Rf $@ && mkdir -p $@
build: Main.class
jar: torrent-automove-optimized.jar
download: lib

Main.class: Lexer.class lib
%.class: %.java lib
	$(JAVAC) $(JAVA_ARGS) $<
%.java: %.jflex
	$(JFLEX) $<
lib:
	$(MAKE) -C $@
test: Main.class
	$(JAVA) $(JAVA_ARGS) Main
extract: build classes
	cd classes && for jar in $$(echo ../lib/*.jar); do $(JAR) xf $$jar; done
	cp *.class classes/
	cp -Rf org android classes/
torrent-automove.jar: extract
	$(JAR) cfm torrent-automove.jar Manifest -C classes .
torrent-automove-optimized.jar: extract usedclasses
	cd classes && java -verbose:class Main 2>&1 | sed -n -e 's/\./\//g' -e 's/\[Loaded \(.*\) from file.*/\1.class/gp' | while read line; do cp --parents $$line ../usedclasses; done
	$(JAR) cfm torrent-automove-optimized.jar Manifest -C usedclasses/ .

clean:
	$(MAKE) -C lib clean
	rm -Rf classes/ usedclasses/ Lexer.java
	find . -name '*.class' -exec rm -f {} \;