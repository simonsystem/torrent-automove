WGET = wget

V=v2.5.5
OriginalHttpHelper.java:
	$(WGET) https://github.com/erickok/transdroid/raw/$(V)/app/src/main/java/org/transdroid/daemon/util/HttpHelper.java -O $@ &&	sed -i 's|class HttpHelper|class OriginalHttpHelper|g' $@
