#!/bin/bash

DEPLOY_FOLDER="${0%/*}"

bucketname=test.yetanotherwhatever.io

# idiomatic parameter and option handling in sh
while test $# -gt 0
do
    case "$1" in
        --clean) clean="true"
            ;;
        --*) echo "argument $1"
            ;;
    esac
    shift
done

if [[ $clean = "true" ]]
then
	aws s3 rm s3://$bucketname --recursive
fi

#copy web content first
aws s3 sync $DEPLOY_FOLDER/../web s3://$bucketname --content-type "text/html" --acl public-read --exclude "*problems/outputs*" --exclude "*/combos.html"

echo
echo Deployment ended.
