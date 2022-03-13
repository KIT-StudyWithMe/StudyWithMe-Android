#!/bin/bash
echo "Coverage: $(grep -o '<tfoot><tr><td>Total<\/td><td class="bar">.*%<\/td><td class="bar">' app/manual-build-artifacts/reports/coverage/androidTest/debug/index.html | grep -o "[[:digit:]]*%")"
echo "Missed: $(grep -o '<tfoot><tr><td>Total<\/td><td class="bar">.*%<\/td><td class="bar">' app/manual-build-artifacts/reports/coverage/androidTest/debug/index.html | grep -o "[[:digit:]]*,[[:digit:]]* ")"
echo "Total:$(grep -o '<tfoot><tr><td>Total<\/td><td class="bar">.*%<\/td><td class="bar">' app/manual-build-artifacts/reports/coverage/androidTest/debug/index.html | grep -o " [[:digit:]]*,[[:digit:]]*")"
