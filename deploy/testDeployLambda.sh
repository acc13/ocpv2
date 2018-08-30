#!/bin/bash


rm ../tmp/1.zip
zip -D -j ../tmp/1.zip ../lambdas/Ocpv2InviteCandidate.js

#copy web content first
aws lambda update-function-code --function-name Ocpv2InviteCandidate --zip-file fileb://../tmp/1.zip

echo
echo Deployment ended