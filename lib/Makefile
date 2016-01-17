WGET = wget
DEX2JAR = dex2jar
TAR = tar
MKDIR = mkdir
download: classes themoviedbapi thetvdbapi json httpclient httpcore commons-logging android
	touch .done
httpcore: httpcore-4.4.4.jar
httpclient: httpclient-4.5.1.jar
json: json-20151123.jar
thetvdbapi: thetvdbapi-1.8.jar
themoviedbapi: themoviedbapi-4.1.jar
android: android-4.1.1.4.jar
commons-logging: commons-logging-1.2.jar



commons-logging-1.2.jar:
	$(WGET) https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/$@
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

libcore-net-master.tar.gz:
	$(WGET) -O $@ https://android.googlesource.com/platform/libcore/+archive/master/luni/src/main/java/libcore/net.tar.gz
libcore-net-master: libcore-net-master.tar.gz
	$(MKDIR) -p $@/libcore/net && $(TAR) xfz $< -C $@/libcore/net
android-net-master.tar.gz:
	$(WGET) -O $@ https://android.googlesource.com/platform/frameworks/base/+archive/master/core/java/android/net.tar.gz
android-net-master: android-net-master.tar.gz
	$(MKDIR) -p $@/android/net && $(TAR) xfz $< -C $@/android/net
transdroid-2.5.5.apk:
	$(WGET) https://github.com/erickok/transdroid/releases/download/v2.5.5/$@
transdroid-2.5.5.jax: transdroid-2.5.5.apk
	@$(DEX2JAR) -o $@ $<
transdroid-2.5.5: transdroid-2.5.5.jax
	@mkdir tmp01
	cd tmp01 && jar xf ../$<
	@rename HttpHelper TransdroidHttpHelper tmp01/org/transdroid/daemon/util/HttpHelper.class
	@mv tmp01 transdroid-2.5.5
classes: transdroid-2.5.5
	@mkdir tmp02
	for i in transdroid-2.5.5; do cp -r $$i/* tmp02; done
	@mv tmp02 classes
