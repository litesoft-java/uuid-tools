#!/bin/bash

if [ ! -f "support/uuid-codec-support-sources-1.0.0.jar" ]; then
      echo "ERROR: 'support/uuid-codec-support-sources-1.0.0.jar' does not exist"
      exit 1
fi

retval=0

if [ ! -d "target/generated-sources/java" ]; then
  if [ ! -d "target" ]; then
    mkdir target
    retval=$?
  fi
  if [ $retval -ne 0 ]; then
      echo "ERROR: creating 'target' of $retval"
      exit $retval
  fi
  if [ ! -d "target/generated-sources" ]; then
    mkdir target/generated-sources
    retval=$?
  fi
  if [ $retval -ne 0 ]; then
      echo "ERROR: creating 'target/generated-sources' of $retval"
      exit $retval
  fi
  mkdir target/generated-sources/java
  retval=$?
fi
if [ $retval -ne 0 ]; then
    echo "ERROR: creating 'target/generated-sources/java' of $retval"
    exit $retval
fi

# shellcheck disable=SC2164
cd target/generated-sources/java

jar -xf ../../../support/uuid-codec-support-sources-1.0.0.jar
retval=$?

cd ../../..

if [ $retval -ne 0 ]; then
  echo "ERROR: copying uuid-codec-support source files of $retval"
else
  echo "uuid-codec-support source files copied to generated-sources"
fi
exit $retval
