JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=MeanFilterSerial.class MeanFilterParallel.class\
MedianFilterSerial.class MedianFilterParallel.class
		
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class
	
run:
	@java -cp bin MeanFilterSerial $(INPUT) $(OUTPUT) $(WINDOW)
	 
docs:
	javadoc -d doc src/*.java