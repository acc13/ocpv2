#!/bin/bash

PROJ_ROOT="${0%/*}"
LAMBDAS=$PROJ_ROOT/../lambdas
BUCKET=deployocp

#copy web content first
ZIP=$LAMBDAS/InviteCandidate/build/distributions/InviteCandidate-1.0.zip
aws s3 cp $ZIP s3://$BUCKET/lambas/InviteCandidate-1.0.zip

echo
echo Deployment ended
