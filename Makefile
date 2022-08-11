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

runmedser: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MedianFilterSerial $(INPUT) $(OUTPUT) $(WINDOW)
	
runmenser: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MeanFilterSerial $(INPUT) $(OUTPUT) $(WINDOW)

runmedpar: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MeanFilterParallel $(INPUT) $(OUTPUT) $(WINDOW)

runmenpar: $(CLASSES)
	$(JAVA) -cp $(BINDIR) MeanFilterParallel $(INPUT) $(OUTPUT) $(WINDOW)
	
clean:
	rm $(BINDIR)/*.class