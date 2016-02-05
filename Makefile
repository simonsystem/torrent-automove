JAVA = java
JAVAC = javac
JAVA_ARGS = -cp .:./lib/*
JAR = jar
7Z = 7z
JFLEX = jflex

build: Main.class Lexer.class lib HttpHelper
.PHONY: lib HttpHelper
.SECONDARY: Lexer.java %.jar

jar: torrent-automove.jar
download: lib
%.class: %.java
	$(JAVAC) $(JAVA_ARGS) $<
%.java: %.jflex
	$(JFLEX) $<

HttpHelper: lib
	$(MAKE) -C org/transdroid/daemon/util/ V=$$(echo lib/transdroid-*.jar | sed -ne 's|^.*-\(.*\)\.jar$$|v\1|p')
lib:
	$(MAKE) -C $@

test: Main.class
	$(JAVA) $(JAVA_ARGS) Main
test-jar: torrent-automove.jar
	$(JAVA) -jar $<

classes: build
	mkdir classes; cd classes && for jar in $$(echo ../lib/*.jar); do $(JAR) xf $$jar; done
	mkdir classes; cp *.class classes/
	mkdir classes; cp -Rf org android classes/
usedclasses: classes
	mkdir usedclasses; cd classes && java -verbose:class Main 2>&1 | sed -n -e 's/\./\//g' -e 's/\[Loaded \(.*\) from file.*/\1.class/gp' | while read line; do cp --parents $$line ../usedclasses; done
torrent-automove.jar: usedclasses
	$(JAR) cfm torrent-automove.jar Manifest -C usedclasses/ .

clean:
	$(MAKE) -C lib clean
	rm -Rf classes/ usedclasses/ Lexer.java
	find . -name '*.class' -exec rm -f {} \;