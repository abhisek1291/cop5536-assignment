JFLAGS = -g
JC = javac -cp . Event.java RedBlackTree.java bbst.java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Event.java \
	RedBlackTree.java \
	bbst.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class