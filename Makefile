.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
JAVA=/usr/bin/java
JAVAC=/usr/bin/javac

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<
	
CLASSES2=MedianFilterSerial.class \
	 MeanFilterSerial.class \
	 MedianFilterParallel.class \
	 MeanFilterParallel.class
         
CLASSES=$(CLASSES2:%.class=$(BINDIR)/%.class)
	
default: $(CLASSES)	

clean:
	rm $(BINDIR)/*.class