#!/bin/bash

TEMP_ZIP=../tmp/1.zip
LDIR=../lambdas

#clean up old junk
rm $TEMP_ZIP

#lambda expects entrypoint to reside in index.js
ln $LDIR/Ocpv2InviteCandidate.js $LDIR/index.js
zip -D -j $TEMP_ZIP $LDIR/index.js

#copy web content first
aws lambda update-function-code --function-name Ocpv2InviteCandidate --zip-file fileb://$TEMP_ZIP

#clean up
rm ../tmp/1.zip

echo
echo Deployment ended