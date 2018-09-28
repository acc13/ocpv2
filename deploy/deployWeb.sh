#!/bin/bash

display_usage() { 
	echo -e "\nUsage:\n__g5_token5ba897ea44608 --env <environment/stack name> [--clean] \n" 
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


#CONSTANTS
DEPLOY_FOLDER="${0%/*}"
HOSTED_ZONE=yetanotherwhatever.io
BUCKET_NAME=$ENV.$HOSTED_ZONE

if [[ $CLEAN = "true" ]]
then
	aws s3 rm s3://$BUCKET_NAME --recursive
fi

#copy web content first
aws s3 sync $DEPLOY_FOLDER/../web s3://$BUCKET_NAME --content-type "text/html" --acl public-read

echo
echo Deployment ended.