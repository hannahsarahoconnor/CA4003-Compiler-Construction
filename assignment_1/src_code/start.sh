#!/usr/bin/env sh

# Starting script to compile & run my javacc files

echo "Implementing the parser and lexical analyzer..." 
javacc HannahParser.jj

echo "\n"

echo "Compiling the Java programs..."
echo "_____________________________________________________________"
javac *.java

echo "\n"

echo "Running tests..."
echo "_____________________________________________________________"
echo "Test 1: case senstivity check 1"
java HannahParser ../tests/case_senstive_1.ccal
echo "\n"
echo "Test 2: case senstivity check 2"
java HannahParser ../tests/case_senstive_2.ccal
echo "\n"
echo "Test 3: case senstivity check 3"
java HannahParser ../tests/case_senstive_3.ccal
echo "\n"
echo "Test 4: running a simple program"
java HannahParser ../tests/simple_program.ccal
echo "\n"
echo "Test 5: comments check"
java HannahParser ../tests/comments_1.ccal
echo "\n"
echo "Test 6: function check"
java HannahParser ../tests/functions.ccal
echo "\n"
echo "Test 7: scope check"
java HannahParser ../tests/scopes.ccal

echo "\n"
echo "\n"

echo "Instructions on running HannahParser..."
echo "_____________________________________________________________"
echo "To pass standard input to the parser, run:"
echo "java HannahParser"
echo "To pass a file to the parser, run:"
echo "java HannahParser <FILENAME>.ccl"
echo "           OR                    "
echo "java HannahParser < <FILENAME>.ccl"