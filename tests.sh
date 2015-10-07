echo "------------------------------------- Running Tests -------------------------------------------"
cd classes
export CLASSPATH=""
for file in `ls ../lib/`; do export CLASSPATH=$CLASSPATH:../lib/$file; done
java -cp $CLASSPATH:. org.junit.runner.JUnitCore CtdsScannerTest
java -cp $CLASSPATH:. org.junit.runner.JUnitCore CtdsParserTest
java -cp $CLASSPATH:. org.junit.runner.JUnitCore CtdsSemanticTest