Main.class: Main.java Solver.class
	javac -g Main.java

Solver.class: Solver.java
	javac -g Solver.java

run: Main.class
	java Main

clean:
	rm *.class

debug: Main.class
	jdb Main