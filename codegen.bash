set ff=UNIX
set -e
cat | java -cp /ulib/java/antlr-4.9-complete.jar:./bin Main -O2 -S