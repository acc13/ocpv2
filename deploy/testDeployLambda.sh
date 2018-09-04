#!/bin/bash

PROJ_ROOT="${0%/*}"
LAMBDAS=$PROJ_ROOT/../lambdas
ZIP=/tmp/testdeploylambda.zip
LN=/tmp/index.js

#lambda expects entrypoint to reside in index.js
ln $LAMBDAS/Ocpv2InviteCandidate.js $LN
zip -D -j $ZIP $LN

#copy web content first
aws lambda update-function-code --function-name Ocpv2InviteCandidate --zip-file fileb://$ZIP

#clean up
rm $LN
rm $ZIP

echo
echo Deployment ended
