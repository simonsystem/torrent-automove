WGET = wget
DEX2JAR = dex2jar
TAR = tar
MKDIR = mkdir
download: transdroid themoviedbapi thetvdbapi json httpclient httpcore commons-logging commons-lang3 android google-collections
httpcore: httpcore-4.4.4.jar
httpclient: httpclient-4.5.1.jar
json: json-20151123.jar
thetvdbapi: thetvdbapi-1.8.jar
themoviedbapi: themoviedbapi-4.1.jar
android: android-4.1.1.4.jar
commons-logging: commons-logging-1.2.jar
commons-lang3: commons-lang3-3.4.jar
transdroid: transdroid-2.5.5.jar
google-collections: google-collections-1.0-rc2.jar

google-collections-1.0-rc2.jar:
	$(WGET) https://repo1.maven.org/maven2/com/google/collections/google-collections/1.0-rc2/$@
commons-logging-1.2.jar:
	$(WGET) https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/$@
commons-lang3-3.4.jar:
	$(WGET) https://repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.4/$@
android-4.1.1.4.jar:
	$(WGET) https://repo1.maven.org/maven2/com/google/android/android/4.1.1.4/$@
httpcore-4.4.4.jar:
	$(WGET) https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.4/$@
httpclient-4.5.1.jar:
	$(WGET) https://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.5.1/$@
json-20151123.jar:
	$(WGET) https://repo1.maven.org/maven2/org/json/json/20151123/$@
thetvdbapi-1.8.jar:
	$(WGET) https://repo1.maven.org/maven2/com/omertron/thetvdbapi/1.8/$@
themoviedbapi-4.1.jar:
	$(WGET) https://repo1.maven.org/maven2/com/omertron/themoviedbapi/4.1/$@

transdroid-2.5.5.apk:
	$(WGET) https://github.com/erickok/transdroid/releases/download/v2.5.5/$@
transdroid-2.5.5.jar: transdroid-2.5.5.apk
	@$(DEX2JAR) -o $@ $< &&	7z d $@ org/transdroid/daemon/util/HttpHelper*

clean:
	find . -name '*.jar' -exec rm -f {} \;
	rm -f transdroid-2.5.5.apk