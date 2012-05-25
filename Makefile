#
# Catacomb-Snatch
#

# Usage
# Run "make" or "make jar" to create a redistributable jar file
# Run "make clean" or "make clean-class" to clean up files created by make

distdir = dist
distjar = $(distdir)/Om-Nom-Zom.jar

all: jar

class:
	find src/ -name '*.java' -exec javac -classpath res/ '{}' '+'

jar: class | $(distdir)
	jar cfe $(distjar) game.Game -C src/ . -C res/ .

$(distdir):
	mkdir -p $(distdir)

clean:
	find src/ -name '*.class' -exec rm '{}' '+'

.PHONY: all class jar clean
