BUILD_DIR = ./build
SRC_DIR = ./src

$(BUILD_DIR)/Main.class: $(SRC_DIR)/Main.java $(BUILD_DIR)/Solver.class $(BUILD_DIR)/Checker.class
	javac -g -d $(BUILD_DIR) $(SRC_DIR)/Main.java

$(BUILD_DIR)/Solver.class: $(SRC_DIR)/Solver.java $(BUILD_DIR)/Checker.class
	javac -g -d $(BUILD_DIR) $(SRC_DIR)/Solver.java

$(BUILD_DIR)/Checker.class: $(SRC_DIR)/Checker.java
	javac -g -d $(BUILD_DIR) $(SRC_DIR)/Checker.java

run: $(BUILD_DIR)/Main.class
	java -cp $(BUILD_DIR) src.Main

clean:
	rm $(BUILD_DIR)/src/*.class

debug: $(BUILD_DIR)/Main.class
	jdb -classpath $(BUILD_DIR) src.Main