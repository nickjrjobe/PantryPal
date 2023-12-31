# Makefile

# Dependencies
JAVAC = javac 
JAVA = java
ABSOLUTE_PROJECT_DIR = $(shell pwd)
JUNIT = $(ABSOLUTE_PROJECT_DIR)/lib/junit-4.13.2.jar
OPENJFX = $(ABSOLUTE_PROJECT_DIR)/lib/javafx-sdk-17.0.9/lib/
HAMCREST = $(ABSOLUTE_PROJECT_DIR)/lib/hamcrest-core-1.3.jar
JSON = $(ABSOLUTE_PROJECT_DIR)/lib/json-20231013.jar
CHECKSTYLE = $(ABSOLUTE_PROJECT_DIR)/lib/checkstyle-10.12.4-all.jar
GOOGLE_FORMAT = $(ABSOLUTE_PROJECT_DIR)/lib/google-java-format-1.18.1-all-deps.jar
MONGODB_SYNC = $(ABSOLUTE_PROJECT_DIR)/lib/mongodb-driver-sync-4.11.0.jar
MONGODB_CORE = $(ABSOLUTE_PROJECT_DIR)/lib/mongodb-driver-core-4.11.0.jar
BSON_CODEC = $(ABSOLUTE_PROJECT_DIR)/lib/bson-record-codec-4.11.0.jar
BSON = $(ABSOLUTE_PROJECT_DIR)/lib/bson-4.11.0.jar

# Compile flags
JFLAGS = --module-path $(OPENJFX) --add-modules javafx.controls,javafx.fxml
# Classpath for JUNIT
JTESTCP = $(JUNIT):$(HAMCREST):$(JCP)
# Classpath for JSON
JCP = $(JSON):$(MONGODB):$(MONGODB_SYNC):$(MONGODB_CORE):$(BSON_CODEC):$(BSON)

# Source directory and testing directories
SRC_DIR = ./src
TEST_DIR = ./test

# Output directory
OUT_DIR = out

# Package name
PROGRAM = PantryPal
SERVER_PROGRAM = server
UTIL_PROGRAM = utils
# Executable name
REALPROGRAM = PantryPal

# Define the main class
MAIN_CLASS = $(PROGRAM).$(REALPROGRAM)
SERVER_CLASS = $(SERVER_PROGRAM).PantryPalServer

# Find all .java files in the source directory
SRCS = $(wildcard $(SRC_DIR)/$(PROGRAM)/*.java)
UTILSRCS = $(wildcard $(SRC_DIR)/$(UTIL_PROGRAM)/*.java)
TESTSRCS = $(wildcard $(TEST_DIR)/$(PROGRAM)/*.java)
SERVERSRCS = $(wildcard $(SRC_DIR)/$(SERVER_PROGRAM)/*.java)
TESTSERVERSRCS = $(wildcard $(TEST_DIR)/$(SERVER_PROGRAM)/*.java)

# Generate .class file names from .java file names
CLASSES = $(patsubst $(SRC_DIR)/$(PROGRAM)/%.java, $(OUT_DIR)/$(PROGRAM)/%.class, $(SRCS))

UTILCLASSES = $(patsubst $(SRC_DIR)/$(UTIL_PROGRAM)/%.java, $(OUT_DIR)/$(PROGRAM)/%.class, $(UTILSRCS))
TESTCLASSES = $(patsubst $(TEST_DIR)/$(PROGRAM)/%.java, $(OUT_DIR)/$(PROGRAM)/%.class, $(TESTSRCS))
SERVERCLASSES = $(patsubst $(SRC_DIR)/$(SERVER_PROGRAM)/%.java, $(OUT_DIR)/$(SERVER_PROGRAM)/%.class, $(SERVERSRCS))
SERVERTESTCLASSES = $(patsubst $(TEST_DIR)/$(SERVER_PROGRAM)/%.java, $(OUT_DIR)/$(SERVER_PROGRAM)/%.class, $(TESTSERVERSRCS))

# Convert Testclass names to executable path names (/ to .)
RUNNABLE_TEST_CLASSES = $(patsubst $(TEST_DIR)/$(PROGRAM)/%.java, $(PROGRAM).%, $(TESTSRCS))
RUNNABLE_SERVER_TEST_CLASSES = $(patsubst $(TEST_DIR)/$(SERVER_PROGRAM)/%.java, $(SERVER_PROGRAM).%, $(TESTSERVERSRCS))
$(info runnable server tests $(RUNNABLE_SERVER_TEST_CLASSES))

# Filter test class executable names by ones which are actually tests
RUNNABLE_TESTS = $(filter %Test,$(RUNNABLE_TEST_CLASSES))
RUNNABLE_SERVER_TESTS = $(filter %Test,$(RUNNABLE_SERVER_TEST_CLASSES))

all: $(CLASSES) $(TESTCLASSES) $(SERVERCLASSES) $(SERVERTESTCLASSES)

$(UTILCLASSES): build_util_classes
build_util_classes: $(UTILSRCS)
	@mkdir -p $(@D)
	$(JAVAC) $(JFLAGS) -cp $(OUT_DIR):$(JCP):. -d $(OUT_DIR) $^

$(SERVERCLASSES): build_util_classes build_server_classes
build_server_classes: $(SERVERSRCS)
	@mkdir -p $(@D)
	$(JAVAC) $(JFLAGS) -cp "$(OUT_DIR):$(JCP):." -d $(OUT_DIR) $^

$(TESTCLASSES): build_util_classes build_test_classes
build_test_classes: $(SRCS) $(TESTSRCS)
	@mkdir -p $(@D)
	$(JAVAC) $(JFLAGS) -cp $(OUT_DIR):$(JTESTCP) -d $(OUT_DIR) $^
$(SERVERTESTCLASSES): build_util_classes build_servertest_classes
build_servertest_classes: $(SERVERSRCS) $(TESTSERVERSRCS)
	@mkdir -p $(@D)
	$(JAVAC) $(JFLAGS) -cp $(OUT_DIR):$(JTESTCP) -d $(OUT_DIR) $^

$(CLASSES): build_util_classes build_classes
build_classes: $(SRCS)
	@mkdir -p $(@D)
	$(JAVAC) $(JFLAGS) -cp $(OUT_DIR):$(JCP):. -d $(OUT_DIR) $^

# Run the Java application
run: $(CLASSES) 
	$(JAVA) $(JFLAGS) -cp $(OUT_DIR):$(JCP) $(MAIN_CLASS)
test: all
	$(JAVA) $(JFLAGS) -cp $(JTESTCP):$(OUT_DIR) org.junit.runner.JUnitCore $(RUNNABLE_TESTS)

servertest: all
	$(JAVA) $(JFLAGS) -cp $(JTESTCP):$(OUT_DIR) org.junit.runner.JUnitCore $(RUNNABLE_SERVER_TESTS)
# TODO not currently linting testcode
lint:
	$(JAVA) -jar $(CHECKSTYLE) -c google_checks.xml $(SRCS) $(SERVERSRCS)

format:
	$(JAVA) -jar $(GOOGLE_FORMAT) --replace $(SRCS) $(TESTSRCS) $(SERVERSRCS) $(TESTSERVERSRCS) 
makeserver: $(SERVERCLASSES)
#TODO we can avoid including OPENJFX
server: $(SERVERCLASSES)
	$(JAVA) $(JFLAGS) -cp $(OUT_DIR):$(JCP) $(SERVER_CLASS)

clean:
	rm -rf $(OUT_DIR)
