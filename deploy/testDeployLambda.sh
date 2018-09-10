#!/bin/bash

PROJ_ROOT="${0%/*}"
LAMBDAS=$PROJ_ROOT/../lambdas

#copy web content first
ZIP=$LAMBDAS/InviteCandidate/build/distributions/InviteCandidate-1.0.zip
aws lambda update-function-code --function-name Ocpv2InviteCandidate --zip-file fileb://$ZIP

echo
echo Deployment ended
