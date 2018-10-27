#!/bin/bash

display_usage() { 
	echo -e "\nUsage:\n$0 --env <environment/stack name>\n" 
    exit 1
} 

# idiomatic parameter and option handling in sh
while test $# -gt 0
do
    case "$1" in
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
echo $0 started

DEPLOY_FOLDER="${0%/*}"

./validateCFTemplate.sh --env $ENV
./deployStack.sh --env $ENV
./deployWeb.sh --env $ENV
./deployLambda.sh --env $ENV

echo $0 finished
echo
