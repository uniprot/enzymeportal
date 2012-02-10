#!/bin/bash

case $1 in
	dev|test|prod);;
	*) echo 'runtime environment (enzdev|ezprel) required as $1' && exit 1;;
esac
