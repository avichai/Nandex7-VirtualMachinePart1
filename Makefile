
JAVAC=javac
JAVACFLAGS=

SRCS=*.java
EXEC=VMtranslator

TAR=tar
TARFLAGS=cvf
TARNAME=project7.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~ project7.tar

