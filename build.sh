#!/bin/sh
mvn -U -up clean cobertura:cobertura javadoc:jar source:jar assembly:single
