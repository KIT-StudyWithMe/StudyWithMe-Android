#!/bin/bash
#./gradlew clean :app:connectedDebugAndroidTest createDebugCoverageReport
rm -r app/manual-build-artifacts/*
cp -r app/build/reports app/manual-build-artifacts
$(zip -r -q app/manual-build-artifacts.zip app/manual-build-artifacts/*)
exec ./printCoverage.sh
