#!/bin/bash

display_usage() { 
	echo -e "\nUsage:\n$0 --env <environment/stack name> [--clean] \n" 
    exit 1
	} 

# idiomatic parameter and option handling in sh
while test $# -gt 0
do
    case "$1" in
        --clean) 
        	CLEAN="true"
            ;;
        --env)
        	shift
        	ENV=$1
        	;;
        --*) 
            echo "Unexpected argument $1"
            display_usage
            ;;
    esac
    shift
done

# VALIDATION
if [[ -z $ENV ]]
then
	display_usage
fi

echo
echo $0 starting


#CONSTANTS
DEPLOY_FOLDER="${0%/*}"
HOSTED_ZONE=yetanotherwhatever.io
BUCKET_NAME=$ENV.$HOSTED_ZONE

if [[ $CLEAN = "true" ]]
then
	aws s3 rm s3://$BUCKET_NAME --recursive --exclude "tp/*"
fi

#copy web content first
#html, css, js files should be content-type text/html
aws s3 sync $DEPLOY_FOLDER/../web s3://$BUCKET_NAME --content-type "text/html" --acl public-read --exclude "_tests_*" --exclude "inputs*"
#whereas the input files, we want the link to work as a file download
aws s3 sync $DEPLOY_FOLDER/../web s3://$BUCKET_NAME --content-type "application/txt" --content-disposition "attachment" --acl public-read --include "*inputs/*" --exclude "_tests_*"

echo $0 finished
echo