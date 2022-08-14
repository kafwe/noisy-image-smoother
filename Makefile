.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
JAVA=/usr/bin/java
JAVAC=/usr/bin/javac
JUNIT=lib/junit-platform-console-standalone-1.9.0.jar

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<
	
CLASSES2=MedianFilterSerial.class \
	 MeanFilterSerial.class \
	 MedianFilterParallel.class \
	 MeanFilterParallel.class \
         
CLASSES=$(CLASSES2:%.class=$(BINDIR)/%.class)
	
default: $(CLASSES)	

compiletest:
	$(JAVAC) -d $(BINDIR) -sourcepath tests -cp .:$(JUNIT) tests/FilterTests.java

runtest:
	$(JAVA) -jar $(JUNIT) \
	--cp $(BINDIR)/ -c FilterTests

clean:
	rm $(BINDIR)/*.class