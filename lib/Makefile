CURL = curl
WGET = wget
DEX2JAR = dex2jar
TAR = tar
MKDIR = mkdir

download: google-collections-1.0-rc2.jar commons-logging-1.2.jar commons-lang3-3.4.jar android-4.1.1.4.jar httpcore-4.4.4.jar httpclient-4.5.1.jar json-20151123.jar transdroid-2.5.5.jar

google-collections-1.0-rc2.jar: _com_google_collections_google-collections_1.0-rc2
commons-logging-1.2.jar: _commons-logging_commons-logging_1.2
commons-lang3-3.4.jar: _org_apache_commons_commons-lang3_3.4
android-4.1.1.4.jar: _com_google_android_android_4.1.1.4
httpcore-4.4.4.jar: _org_apache_httpcomponents_httpcore_4.4.4
httpclient-4.5.1.jar: _org_apache_httpcomponents_httpclient_4.5.1
json-20151123.jar: _org_json_json_20151123

_%:
	eval $(WGET) $$(echo https://repo1.maven.org/maven2$@ | tr '_' '/' | sed -ne 's|^\(.*\)\/\([^\/]*\)\/\([^\/]*\)$$|\1\/\2\/\3\/\2-\3.jar -O \2-\3.jar|p') 
	@touch $@

transdroid-2.5.5.apk:
	$(WGET) https://github.com/erickok/transdroid/releases/download/v2.5.5/$@
transdroid-2.5.5.jar: transdroid-2.5.5.apk
	@$(DEX2JAR) -o $@ $< &&	7z d $@ org/transdroid/daemon/util/HttpHelper*

clean:
	find . \( -name '*.jar' -o -name '_*' \) -exec rm -f {} \;
	rm -f transdroid-2.5.5.apk