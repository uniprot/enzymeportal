# Creates a variable, if it does not exist yet, with the current time and date.

if [ -z $NOW ]
then
    NOW=$(date +%Y%m%d_%H%M)
fi

